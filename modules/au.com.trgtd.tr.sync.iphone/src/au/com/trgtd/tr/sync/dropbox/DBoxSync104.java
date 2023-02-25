/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.sync.dropbox;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.sync.iphone.FileIDException;
import au.com.trgtd.tr.sync.iphone.SyncMsg104;
import au.com.trgtd.tr.sync.iphone.SyncMsg104.MsgActionUpdate;
import au.com.trgtd.tr.sync.iphone.SyncMsg104.MsgHandshake;
import au.com.trgtd.tr.sync.iphone.SyncMsg104.MsgProjectUpdate;
import au.com.trgtd.tr.sync.iphone.SyncMsg104.MsgReferenceUpdate;
import au.com.trgtd.tr.sync.iphone.SyncMsg104.MsgThought;
import au.com.trgtd.tr.sync.iphone.SyncPrefs;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.util.UtilsFile;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.action.Action;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * Sync using DropBox.
 * 
 * @author Jeremy Moore
 */
public class DBoxSync104 extends Thread {

    private final static Logger LOG = Logger.getLogger(DBoxSync104.class.getName());
    
    public static final String VERSION = "V1.04";
    
    private File dboxFileRecv;
    private File dboxFileSend;
    private File tempFileSend;
    private Data data;
    private BufferedReader reader;
    private PrintWriter writer;
    private ProgressHandle progressHandle;
    private SyncHandler104 handler;
    private MsgThought[] newThoughtMsgs;
    private MsgActionUpdate[] updActionMsgs;
    private MsgProjectUpdate[] updProjectMsgs;
    private MsgReferenceUpdate[] updReferenceMsgs;    
    
    private List<Context> contexts;
    private List<Topic> topics;
    private Map<Integer, Action> actions;
    private List<Value> times;
    private List<Value> energies;
    private List<Value> priorities;
    
    private List<Information> references;
    private List<Project> projects;    
    
    private int todo = 0;
    private int done = 0;

    public DBoxSync104(File file) {
        if (null == file) {
            throw new IllegalArgumentException("DropBox sync file can not be null.");
        }
        this.dboxFileRecv = file;
    }

    @Override
    public void run() {
        if (null == dboxFileRecv) {
            LOG.log(Level.WARNING, "DropBox sync up file is null.");
            return;
        }
        if (!dboxFileRecv.isFile()) {
            LOG.log(Level.WARNING, "DropBox sync up file does not exist: {0}", dboxFileRecv.getPath());
            return;
        }
        try {
            LOG.log(Level.INFO, "Starting DropBox sync ...");
            sync();
            LOG.log(Level.INFO, "Finished DropBox sync.");
        } catch (Exception ex) {
            LOG.log(Level.INFO, "DropBox sync exception: {0}", ex.getMessage());

            Exceptions.printStackTrace(ex);

            notifyUser(ex.getMessage());
            quit();
        } finally {
            cleanup();
        }
    }

    public void sync() throws Exception {

        progressHandle = ProgressHandleFactory.createHandle("iPhone Dropbox Sync");
        progressHandle.start();
        progressHandle.switchToIndeterminate();

//      dboxFileRecv = getDBoxFileRecv();
        tempFileSend = getTempOutFile();
        dboxFileSend = getDBoxSendFile();

        reader = getReader(dboxFileRecv);
        writer = getWriter(tempFileSend);
        handler = new SyncHandler104(reader, writer, null);

        data = handler.getData();
        if (null == data) {
            LOG.severe("TR data not found.");
            throw new Exception("TR data not found.");
        }

        MsgHandshake handshake = getRecvHandshake();

        contexts = handler.getContexts();
        topics = handler.getTopics();
        actions = handler.getActionMap();
        times = handler.getTimeValues();
        energies = handler.getEnergyValues();
        priorities = handler.getPriorityValues();
        
        projects = handler.getProjects();
        references = handler.getReferences();
        
        done = 0;
        todo = 0;
        todo += handshake.nbrNewThoughts;
        todo += handshake.nbrUpdActions * 2;   // double for updating
        todo += handshake.nbrUpdProjects * 2;
        todo += handshake.nbrUpdReferences * 2;
        
        todo += contexts.size();
        todo += topics.size();
        todo += actions.size();
        todo += times.size();
        todo += energies.size();
        todo += priorities.size();
        todo += projects.size();
        todo += references.size();

        progressHandle.switchToDeterminate(todo);

        LOG.log(Level.INFO, "Starting sync up ...");
        syncUp(handshake);
        LOG.log(Level.INFO, "Finished sync up.");

        LOG.log(Level.INFO, "Starting adding new thoughts ...");
        addNewThoughts();
        LOG.log(Level.INFO, "Finish adding new thoughts.");

        LOG.log(Level.INFO, "Starting update actions ...");
        updateActions();
        LOG.log(Level.INFO, "Finish update actions.");

        LOG.log(Level.INFO, "Starting update references ...");
        updateReferences();
        LOG.log(Level.INFO, "Finish update references.");
        
        LOG.log(Level.INFO, "Starting update projects ...");        
        updateProjects();
        LOG.log(Level.INFO, "Finish update projects.");
        
        LOG.log(Level.INFO, "Starting sync down ...");
        syncDown();
        LOG.log(Level.INFO, "Finished sync down.");
    }

    private BufferedReader getReader(File file) throws Exception {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), Constants.FILE_ENCODING));
    }

    private void cleanup() {
        if (reader != null) {
            try {
                reader.close();
            } catch (Exception ex) {
            }
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
        if (tempFileSend != null) {
            try {
                tempFileSend.delete();
            } catch (Exception ex) {
            }
        }
        if (progressHandle != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            progressHandle.finish();
        }
    }

    private MsgHandshake getRecvHandshake() throws Exception {

        // parse handshake
        MsgHandshake hs = SyncMsg104.getHandshakeMsg(reader.readLine());
        if (hs == null) {
            throw new Exception("Handshake message not found.");
        }

        LOG.log(Level.INFO, "Recv: {0}", hs.toString());

        // compare fileIDs
        String fileID = getFileID();
        boolean wrongFileID = hs.fileID.length() > 0 && !Utils.equal(hs.fileID, fileID);
        if (wrongFileID) {
            final Component parent = null;
            final String title = NbBundle.getMessage(getClass(), "wrong_sync_file_id_title");
            final Object messg = NbBundle.getMessage(getClass(), "wrong_sync_file_id_messg", fileID, hs.fileID);
            final int dtype = JOptionPane.YES_NO_OPTION;
            final int mtype = JOptionPane.QUESTION_MESSAGE;
            final Icon icon = null;
            final Object[] options = null;
            final Object option = null;
            int rslt = JOptionPane.showOptionDialog(parent, messg, title, dtype, mtype, icon, options, option);
            if (rslt == JOptionPane.NO_OPTION) {
                throw new FileIDException("Wrong File ID, sync cancelled by user.");
            } else {
                hs.nbrUpdActions = 0;
                hs.nbrNewThoughts = 0;
            }
        }

        return hs;
    }

    private void notifyUser(String msg) {
        msg = "Dropbox sync did not finish.\n\n" + (msg == null ? "" : msg);
        NotifyDescriptor nd = new NotifyDescriptor.Message(msg, NotifyDescriptor.WARNING_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }

//    private Date getTodayPlus30Days() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(DateUtils.getEnd(new Date()));
//        cal.add(Calendar.DAY_OF_YEAR, 30);
//        return cal.getTime();
//    }

//    // Determines whether or not an action should be sent.
//    private boolean isToBeSent(Action action, Date endDate) {
//        if (action.isDone()) {
//            return false;
//        }
//        if (action.isStateASAP()) {
//            return true;
//        }
//        if (action.isStateDelegated()) {
//            return true;
//        }
//        Date actionDate = action.getActionDate();
//        if (actionDate == null || !actionDate.before(endDate)) {
//            return false;
//        }
//        return true;
//    }

    // Adds the received new thoughts.
    private void addNewThoughts() throws Exception {

        for (MsgThought newThoughtMsg : newThoughtMsgs) {

            Thought thought = new Thought(data.getNextID());
            thought.setDescription(newThoughtMsg.title);
            thought.setNotes(newThoughtMsg.notes);
            thought.setTopic(handler.getTopic(newThoughtMsg.topic));
            data.getThoughtManager().add(thought);

            updateProgress(++done);

            LOG.log(Level.INFO, "Added new thought: {0}", newThoughtMsg.title);
        }
    }

    // Updates actions as per received action updates.
    // Adds actions that should now be sent (to the list of actions to send).
    // Removes actions that should now not be sent (from the list of actions to send).
    private void updateActions() throws Exception {

//        Date todayPlus30Days = getTodayPlus30Days();

        for (MsgActionUpdate msg : updActionMsgs) {

            updateProgress(++done);

            // update the action
            Action action = Services.instance.getAction(msg.id);
            if (action == null) {
                LOG.log(Level.WARNING, "Action to update not found. Action id: {0}", msg.id);
                continue;
            }

            if (msg.done) {
                action.setDone(true);
                LOG.log(Level.INFO, "Updated action with id: {0} Done set to True", msg.id);
            }
            if (!Utils.equal(action.getActionDate(), msg.date)) {
                handler.postponeAction(action, msg.date, msg.hh, msg.mm);
                LOG.log(Level.INFO, "Updated action with id: {0} Postponed action date", msg.id);
            }
            action.setNotes(msg.notes.trim());

//            // see if the action should now be sent
//            if (isToBeSent(action, todayPlus30Days)) {
//                // yes, make sure action is in list
//                if (!actions.containsKey(action.getID())) {
//                    actions.put(action.getID(), action);
//                    LOG.log(Level.INFO, "Updated action will now not be sent. Action id: {0}", msg.id);
//                }
//            } else {
//                // no, make sure action is not in list
//                if (actions.containsKey(action.getID())) {
//                    actions.remove(action.getID());
//                    done++;
//                    LOG.log(Level.INFO, "Updated action will now be sent. Action id: {0}", msg.id);
//                }
//            }
        }
    }
    
    // Updates references as per received reference updates.
    private void updateReferences() throws Exception {
        for (MsgReferenceUpdate msg : updReferenceMsgs) {
            updateProgress(++done);
            Information reference = handler.getReferencesMap().get(msg.id);
            if (null == reference) {
                LOG.log(Level.WARNING, "Reference to update not found. Reference id: {0}", msg.id);
                continue;                
            }
            reference.setNotes(msg.notes.trim());
        }
    }
    
    // Updates projects as per received project updates.
    private void updateProjects() throws Exception {
        for (MsgProjectUpdate msg : updProjectMsgs) {
            updateProgress(++done);
            Project project = handler.getProjectsMap().get(msg.id);
            if (null == project) {
                LOG.log(Level.WARNING, "Project to update not found. Project id: {0}", msg.id);
                continue;                
            }
            project.setBrainstorming(msg.brainstorm.trim());
            project.setNotes(msg.notes.trim());
            project.setOrganising(msg.organise.trim());
            project.setPurpose(msg.purpose.trim());
            project.setVision(msg.vision.trim());
            project.setDueDate(msg.due);
        }
    }

    // Reads in the new thought and action updates from the dropbox sync up file.
    private void syncUp(MsgHandshake hsMsg) throws Exception {

        newThoughtMsgs = new MsgThought[hsMsg.nbrNewThoughts];
        updActionMsgs = new MsgActionUpdate[hsMsg.nbrUpdActions];
        updProjectMsgs = new MsgProjectUpdate[hsMsg.nbrUpdProjects];
        updReferenceMsgs = new MsgReferenceUpdate[hsMsg.nbrUpdReferences];    
        
        // read in new thoughts from iPhone
        for (int i = 0; i < hsMsg.nbrNewThoughts; i++) {
            SyncMsg104 msg = SyncMsg104.getNextMsg(reader);
            if (msg instanceof MsgThought msgThought) {
                newThoughtMsgs[i] = msgThought;
                LOG.log(Level.INFO, "Received: {0}", msg.toString());
            } else {
                throw new Exception("Expected new thought message not found.");
            }
            updateProgress(++done);
        }

        // read in action updates from iPhone
        for (int i = 0; i < hsMsg.nbrUpdActions; i++) {
            SyncMsg104 msg = SyncMsg104.getNextMsg(reader);
            if (msg instanceof MsgActionUpdate msgActionUpdate) {
                updActionMsgs[i] = msgActionUpdate;
                LOG.log(Level.INFO, "Received: {0}", msg.toString());
            } else {
                throw new Exception("Expected update action message not found.");
            }
            updateProgress(++done);
        }

        // read in project updates from iPhone
        for (int i = 0; i < hsMsg.nbrUpdProjects; i++) {
            SyncMsg104 msg = SyncMsg104.getNextMsg(reader);
            if (msg instanceof MsgProjectUpdate msgProjectUpdate) {
                updProjectMsgs[i] = msgProjectUpdate;
                LOG.log(Level.INFO, "Received: {0}", msg.toString());
            } else {
                throw new Exception("Expected update project message not found.");
            }
            updateProgress(++done);
        }
        
        // read in reference updates from iPhone
        for (int i = 0; i < hsMsg.nbrUpdReferences; i++) {
            SyncMsg104 msg = SyncMsg104.getNextMsg(reader);
            if (msg instanceof MsgReferenceUpdate msgReferenceUpdate) {
                updReferenceMsgs[i] = msgReferenceUpdate;
                LOG.log(Level.INFO, "Received: {0}", msg.toString());
            } else {
                throw new Exception("Expected update reference message not found.");
            }
            updateProgress(++done);
        }
        
        
        reader.close();
    }

    private void syncDown() throws Exception {
        write(getSendHandshake());
        for (Context context : contexts) {
            write(handler.getMsg(context));
            updateProgress(++done);
        }
        for (Topic topic : topics) {
            write(handler.getMsg(topic));
            updateProgress(++done);
        }
        for (Value time : times) {
            write(handler.getTimeMsg(time));
            updateProgress(++done);
        }
        for (Value energy : energies) {
            write(handler.getEnergyMsg(energy));
            updateProgress(++done);
        }
        for (Value priority : priorities) {
            write(handler.getPriorityMsg(priority));
            updateProgress(++done);
        }
        for (Project project : projects) {
            write(handler.getProjectMsg(project));
            updateProgress(++done);
        }        
        
        actions = handler.getActionMap(); // read again for updated actions
        for (Action action : actions.values()) {
            write(handler.getMsg(action));
            updateProgress(++done);
        }        
        for (Information reference : references) {
            write(handler.getReferenceMsg(reference));
            updateProgress(++done);
        }
        writer.close();
        UtilsFile.copyFile(tempFileSend, dboxFileSend);
    }

    private String getSendHandshake() {
        return (VERSION + "|HELLO"
                + "|Contexts|" + contexts.size()
                + "|Topics|" + topics.size()
                + "|Actions|" + actions.size()
                + "|Times|" + times.size()
                + "|Energies|" + energies.size()
                + "|Priorities|" + priorities.size()
                + "|References|" + references.size()                
                + "|Projects|" + projects.size()
                + "|FileID|" + getFileID()
                + "|");
    }

    private File getTempOutFile() throws Exception {
        return new File(UtilsFile.getTempDir(), Constants.FILE_SYNC_DOWN + new Date().getTime());
    }

    private File getDBoxSendFile() throws Exception {

        File file = new File(SyncPrefs.getDropBoxPath(), Constants.FILE_SYNC_DOWN);

        if (!file.exists()) {
            return file;
        }

        if (file.delete()) {
            LOG.log(Level.INFO, "Deleted previous dropbox send file: {0}", file.getPath());
            return file;
        } else {
            throw new Exception("File could not be deleted: " + file.getPath());
        }
    }

    private PrintWriter getWriter(File file) throws Exception {
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), Constants.FILE_ENCODING));
    }

    // Writes a single sync message to file.
    private void write(String msg) {
        LOG.log(Level.INFO, msg);
        writer.println(msg + Constants.ETX);
    }

    // Creates a sync send dropbox file containing the quit code.
    private void quit() {
        try {
            LOG.log(Level.INFO, "Quit sync.");
            writer = getWriter(getDBoxSendFile());
            writer.write("Q");
            writer.close();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Could not create dropbox file. {0}", ex.getMessage());
        }
    }

    // Gets the TR data file ID.
    private String getFileID() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found.");
            return "";
        }
        File datafile = new File(ds.getPath());
        if (!datafile.exists()) {
            LOG.severe("Data file was not found.");
            return "";
        }
        return datafile.getName();
    }

    private void updateProgress(int done) {
        progressHandle.progress(done < todo ? done : todo);
    }
}
