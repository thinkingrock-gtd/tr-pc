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
package au.com.trgtd.tr.sync.device.dbx;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.sync.device.exception.DataException;
import au.com.trgtd.tr.sync.device.prefs.SyncPrefsDbx;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import au.com.trgtd.tr.sync.device.v100.message.process.Processor;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgAction;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgContext;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnergy;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgPriority;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgProject;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgReference;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgTime;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgTopic;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.util.UtilsFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.openide.util.NbBundle;

/**
 * Android DropBox sync thread.
 *
 * @author Jeremy Moore
 */
final class DbxSyncThread extends Thread {

    static final Logger LOG = Logger.getLogger(DbxSyncThread.class.getName());
//  static final double DBX_SYNC_VERSION = 1.0;
    static final double DBX_SYNC_VERSION = 2.1;

    private final File fileSyncUp;
    private boolean cancel;
    

    DbxSyncThread(File fileSyncUp) {
        super("TR DropBox Sync");
        this.fileSyncUp = fileSyncUp;
        this.cancel = false;
    }

    void cancel() {
        cancel = true;
    }

    @Override
    public void run() {
        if (cancel) {
            return;
        }

        // delay to make sure sync-up file is fully written.
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//        }
        
        // make sure data is saved
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds != null) {
            try {
                ds.store();
            } catch (Exception ex) {
            }
        }

        // get sync-up file
        LOG.log(Level.FINE, "Processing Android DropBox sync up file.");
        if (!fileSyncUp.isFile()) {
            LOG.log(Level.WARNING, "Sync failed sync up file does not exist.");
            return;
        }

        if (cancel) {
            return;
        }

        DataMgr dataMgr;
        try {
            dataMgr = new DataMgr();
        } catch (DataException ex) {
            LOG.log(Level.SEVERE, "Could not create sync data manager. ", ex);
            return;
        }

        if (cancel) {
            return;
        }

        DbxParser dbxParser = new DbxParser();
        try {
            dbxParser.parse(fileSyncUp, dataMgr);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Could not parse sync up file.", ex);
            return;
        }

        if (cancel) {
            return;
        }

        String token = getToken(fileSyncUp);
        
        // delete sync-up file
        if (!fileSyncUp.delete()) {
            LOG.log(Level.WARNING, "Could not delete sync up file.");
        }

        // check Data ID
        final String thisDataID = dataMgr.getDataID();
        final String syncDataID = dbxParser.getDataID();
        final boolean isSameDataID = Utils.equal(thisDataID, syncDataID);
        if (!isSameDataID) {
            final Component parent = null;
            final Class clazz = Processor.class;
            final String title = NbBundle.getMessage(clazz, "wrong_sync_file_id_title");
            final Object messg = NbBundle.getMessage(clazz, "wrong_sync_file_id_messg", thisDataID, syncDataID);
            final int dtype = JOptionPane.YES_NO_OPTION;
            final int mtype = JOptionPane.QUESTION_MESSAGE;
            final Icon icon = null;
            final Object[] options = null;
            final Object option = null;
            int rslt = JOptionPane.showOptionDialog(parent, messg, title, dtype, mtype, icon, options, option);
            if (rslt == JOptionPane.NO_OPTION) {
                LOG.log(Level.INFO, "Wrong data ID and sync cancelled.");
                return;
            } 
        }
        
        // create sync-down file
        LOG.log(Level.FINE, "Creating Android DropBox sync down file.");
        JsonNode rootNode;
        try {
            rootNode = createSyncDownJSON(dataMgr);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Failed to create JSON data to send.", ex);
            return;
        }
        
        // put sync-down file on DropBox
        File fileSyncDown = SyncPrefsDbx.getCurrent().getSyncDownFile(token);
        if (fileSyncDown.isFile()) {
            LOG.log(Level.FINE, "Deleting old sync down file: {0}", fileSyncDown.getPath());
            if (!fileSyncDown.delete()) {
                LOG.log(Level.WARNING, "Could not delete old sync down file: {0}", fileSyncDown.getPath());                
                return;
            }
        }
        
        try {
            ObjectMapper om = new ObjectMapper();
            om.writeValue(fileSyncDown, rootNode);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Problem writing data to file: {0}", fileSyncDown.getPath());                
            return;
        }
        
        // updata data only if same data ID (assume Android synced)
        if (isSameDataID) {
            dataMgr.commitDataChanges();            
        } else {
            LOG.log(Level.INFO, "DropBox sync data not updated (due to wrong data ID).");
        }
    }

    private String getToken(File syncUpFile) {
        if (syncUpFile == null) {
            return "";
        }
        String filename = UtilsFile.removeExtension(syncUpFile.getName());
        if (!filename.startsWith("syncup")) {
            return "";
        }
        return filename.substring("syncup".length());
    }
    
    private JsonNode createSyncDownJSON(DataMgr dataMgr) throws IOException {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        
        ArrayNode topicsNode = factory.arrayNode();
        for (SendMsgTopic msg : dataMgr.getSendTopics()) {
            topicsNode.add(msg.toJsonNode(factory));
        }
        ArrayNode contextsNode = factory.arrayNode();
        for (SendMsgContext msg : dataMgr.getSendContexts()) {
            contextsNode.add(msg.toJsonNode(factory));
        }
        ArrayNode timesNode = factory.arrayNode();
        for (SendMsgTime msg : dataMgr.getSendTimes()) {
            timesNode.add(msg.toJsonNode(factory));
        }
        ArrayNode energiesNode = factory.arrayNode();
        for (SendMsgEnergy msg : dataMgr.getSendEnergies()) {
            energiesNode.add(msg.toJsonNode(factory));
        }
        ArrayNode prioritiesNode = factory.arrayNode();
        for (SendMsgPriority msg : dataMgr.getSendPriorities()) {
            prioritiesNode.add(msg.toJsonNode(factory));
        }
        ArrayNode actionsNode = factory.arrayNode();
        for (SendMsgAction msg : dataMgr.getSendActions()) {
            if (!msg.isDone()) {
                actionsNode.add(msg.toJsonNode(factory));                
            }
        }
        ArrayNode projectsNode = factory.arrayNode();
        for (SendMsgProject msg : dataMgr.getSendProjects()) {
            projectsNode.add(msg.toJsonNode(factory));
        }
        ArrayNode referencesNode = factory.arrayNode();
        for (SendMsgReference msg : dataMgr.getSendReferences()) {
            referencesNode.add(msg.toJsonNode(factory));
        }
        
        ObjectNode node = factory.objectNode();
        node.set(Fields.Common.VERSION, factory.numberNode(DBX_SYNC_VERSION));
        node.set(Fields.Common.DATA_ID, factory.textNode(dataMgr.getDataID()));
        node.set(Fields.Send.Summary.TOPICS, topicsNode);
        node.set(Fields.Send.Summary.CONTEXTS, contextsNode);
        node.set(Fields.Send.Summary.TIMES, timesNode);
        node.set(Fields.Send.Summary.ENERGIES, energiesNode);
        node.set(Fields.Send.Summary.PRIORITIES, prioritiesNode);
        node.set(Fields.Send.Summary.ACTIONS, actionsNode);
        node.set(Fields.Send.Summary.PROJECTS, projectsNode);
        node.set(Fields.Send.Summary.REFERENCES, referencesNode);
        return node;
    }

}
