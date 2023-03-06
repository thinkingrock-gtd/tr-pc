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
package au.com.trgtd.tr.archive;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.datastore.xstream.XStreamWrapper;
import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.util.UtilsFile;
import java.awt.Frame;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.awt.StatusDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.RecurrenceRemover;
import tr.model.future.Future;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.util.Manager;

/**
 * Archive action.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public final class ArchiveAction extends CallableSystemAction implements LookupListener {

    private static final Logger LOG = Logger.getLogger("tr.archive"); // No I18N
    private static final DateFormat DATESTAMP = new SimpleDateFormat("yyyyMMdd"); // No I18N
    private static final DateFormat TIMESTAMP = new SimpleDateFormat("HHmmssSSS"); // No I18N
    private Date archiveDate;
    private Vector<Project> archiveProjects;
    private Vector<Action> archiveActions;
    private Vector<Action> archiveSingleActions;

    /** Constructs a new instance. */
    public ArchiveAction() {
        super();
        setIcon(Icons.Archive);
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener(this);
        r.allInstances();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ArchiveAction"); // No I18N
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.archive"); // No I18N
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    /** Save the current data store as another file. */
    @Override
    public void performAction() {

        // get saved preferences
        Date date = ArchivePrefs.getDate();
        String path = ArchivePrefs.getPath();
        boolean doneProjectsOnly = ArchivePrefs.isDoneProjectsOnly();

        // dialog to get archive preferences
        Frame frame = WindowManager.getDefault().getMainWindow();

        ArchiveDialog dialog = new ArchiveDialog(frame, true, date, path, doneProjectsOnly);
        dialog.setVisible(true);
        if (!dialog.archive) {
            LOG.fine("User did not select archive"); // No I18N
            return;
        }

        StatusDisplayer.getDefault().setStatusText(NbBundle.getMessage(ArchiveAction.class, "archiving"));

        archiveDate = DateUtils.getEnd(dialog.getArchiveDate());
        String archivePath = dialog.getArchivePath();
        doneProjectsOnly = dialog.isDoneProjectsOnly();

        DataStore datastore = DataStoreLookup.instance().lookup(DataStore.class);
        if (datastore == null) {
            LOG.severe("Data store could not be obtained."); // No I18N
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }

        File dataFile = new File(datastore.getPath());
        if (!dataFile.isFile()) {
            LOG.severe("Data file path error."); // No I18N
            StatusDisplayer.getDefault().setStatusText(""); // No I18N
            return;
        }

        File archiveFolder = (archivePath == null || archivePath.equals("")) // No I18N
                ? dataFile.getParentFile()
                : new File(archivePath);

        if (!archiveFolder.isDirectory()) {
            LOG.severe("Archive directory error."); // No I18N
            StatusDisplayer.getDefault().setStatusText(""); // No I18N
            return;
        }

        String filename = UtilsFile.removeExtension(dataFile.getName());
        String extension = UtilsFile.getExtension(dataFile.getName());

        Date currentDate = Calendar.getInstance().getTime();
        String currentDateStamp = DATESTAMP.format(currentDate);
        String currentTimeStamp = TIMESTAMP.format(currentDate);

        // make backupFile copy of dataFile to archive folder
        File backupFile = new File(archiveFolder, filename + "-" + currentDateStamp + "-" + currentTimeStamp + ".backup." + extension); // No I18N
        try {
            UtilsFile.copyFile(dataFile, backupFile);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error creating archive backup of datafile. {0}", ex.getMessage()); // No I18N
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }

        // Make archive copy of data file to archive folder
        String archiveDateStamp = DATESTAMP.format(archiveDate);
        File archiveFile = new File(archiveFolder, filename + "-" + archiveDateStamp + "-" + currentTimeStamp + ".archive." + extension); // No I18N

        try {
            UtilsFile.copyFile(dataFile, archiveFile);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error creating archive copy of datafile. {0}", ex.getMessage()); // No I18N
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }

        archiveSingleActions = new Vector<>();
        archiveActions = new Vector<>();
        archiveProjects = new Vector<>();

        Data archiveData = null;
        try {
            archiveData = XStreamWrapper.instance().load(archiveFile);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error loading data from archive file. {0}", ex.getMessage()); // No I18N
            StatusDisplayer.getDefault().setStatusText("");
            return;
        }

        // Remove from archive all single singleActions that are not done or
        // have done date after the archive date
        for (Action action : archiveData.getRootActions().getChildren(Action.class)) {
            if (action.isDone() && action.getDoneDate().before(archiveDate)) {
                archiveSingleActions.add(action);
            } else {
                LOG.log(Level.FINE, "Removing from archive - action: {0}", action.getDescription()); // No I18N
                action.removeFromParent();
            }
        }

        if (doneProjectsOnly) {            
            // Remove from archive all top level projects which are not done or 
            // have done date after archive date
            for (Project project : archiveData.getRootProjects().getChildren(Project.class)) {
                if (project.isDone() && project.getDoneDate().before(archiveDate)) {
                    archiveProjects.add(project);
                } else {
                    LOG.log(Level.FINE, "Removing from archive - project: {0}", project.getDescription()); // No I18N
                    project.removeFromParent();
                }
            }
        } else {
            /* @since 1.2 */
            for (Project project : archiveData.getRootProjects().getChildren(Project.class)) {
                keepArchived(project);
            }
        }

        // remove from archive all unprocessed thoughts
        Manager<Thought> archiveThoughtManager = archiveData.getThoughtManager();
        for (Thought thought : archiveThoughtManager.list()) {
            if (!thought.isProcessed()) {
                archiveThoughtManager.remove(thought);
            }
        }

        // Remove from archive all future projects
        for (Iterator<Project> i = archiveData.getRootFutures().iterator(Project.class); i.hasNext();) {
            i.next().removeFromParent();
        }

        // Remove from archive all template projects
        for (Iterator<Project> i = archiveData.getRootTemplates().iterator(Project.class); i.hasNext();) {
            i.next().removeFromParent();
        }

        // remove from archive all future items
        archiveData.getFutureManager().removeAll();

        // remove from archive all information items
        archiveData.getInformationManager().removeAll();

        // remove from archive actions all recurrence definitions
        RecurrenceRemover.removeAll(archiveData);

        // save archive
        try {
            XStreamWrapper.instance().store(archiveData, archiveFile);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error storing archive. {0}", ex.getMessage()); // No I18N
            StatusDisplayer.getDefault().setStatusText(""); // No I18N
            return;
        }

        // get actual data file
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data could not be obtained."); // No I18N
            StatusDisplayer.getDefault().setStatusText(""); // No I18N
            return;
        }

//      // Remove from data file all archived single singleActions
//        Project singleActions = data.getRootActions();
//        for (Action archiveAction : archiveActions) {
//            LOG.log(Level.FINE, "Removing from data - action: {0}", archiveAction.getDescription()); // No I18N
//            singleActions.remove(archiveAction);
//        }

        // Remove from data file all archived single actions
        Project singleActions = data.getRootActions();
        for (Action a : archiveSingleActions) {
            LOG.log(Level.FINE, "Removing from data - single action: {0}", a.getDescription());
            singleActions.remove(a);
        }        

        // create a map of all data file projects
        Map<Integer, Project> dataProjectsMap = createProjectsMap(data);        

        // Remove from data file all archived actions
        for (Action archiveAction : archiveActions) {
            LOG.log(Level.FINE, "Removing from data file - archived action: {0}", archiveAction.getDescription());
            
            Project dataParent = dataProjectsMap.get(archiveAction.getParent().getID());
            if (dataParent != null) {
                if (!dataParent.remove(archiveAction)) {
                    LOG.severe("Archived action could not be removed from data file.");                                                    
                }
            } else {
                // should never happen.
                LOG.severe("Could not find parent project in data file for archived action.");                                
            }
        }

        // Remove from data file all archived projects
        for (Project archiveProject : archiveProjects) {
            LOG.log(Level.FINE, "Removing from data file - archived project: {0}", archiveProject.getDescription());
            Project dataParent = dataProjectsMap.get(archiveProject.getParent().getID());
            if (dataParent != null) {
                if (!dataParent.remove(archiveProject)) {
                    LOG.severe("Archived project could not be removed from data file.");                                                    
                }
            } else {
                // should never happen.
                LOG.severe("Could not find parent project in data file for archived project.");                                
            }
        }

        // Remove from data file all orphaned processed thoughts
        HashSet<Integer> usedThoughts = getUsedThoughts(data);
        Vector<Thought> orphanedThoughts = new Vector<>();
        Manager<Thought> thoughtManager = data.getThoughtManager();
        for (Thought thought : thoughtManager.list()) {
            if (thought.isProcessed() && !usedThoughts.contains(thought.getID())) {
                orphanedThoughts.add(thought);
            }
        }
        for (Thought thought : orphanedThoughts) {
            thoughtManager.remove(thought);
        }
        LOG.log(Level.INFO, "Removed from data file: {0} processed thoughts no longer used.", orphanedThoughts.size());


        saveData(datastore);

        // Save archive preferences
        ArchivePrefs.setDate(archiveDate);
        ArchivePrefs.setPath(archivePath);
        ArchivePrefs.setDoneProjectsOnly(doneProjectsOnly);

        StatusDisplayer.getDefault().setStatusText(""); // No I18N

        String t = Constants.TITLE + " " + NbBundle.getMessage(getClass(), "CTL_ArchiveAction"); // No I18N

        String COMPLETED = NbBundle.getMessage(getClass(), "archive.completed"); 
        String ARCHIVE_DATE = NbBundle.getMessage(getClass(), "archive.date"); 
        String BACKUP_FILE = NbBundle.getMessage(getClass(), "backup.file"); 
        String ARCHIVE_FILE = NbBundle.getMessage(getClass(), "archive.file");
        String SINGLE_ACTIONS = NbBundle.getMessage(getClass(), "single.actions");
        String PROJECTS = NbBundle.getMessage(getClass(), "projects"); 
        String PROJECT_ACTIONS = NbBundle.getMessage(getClass(), "project.actions");

        String m = COMPLETED + ". \n\n" 
                + ARCHIVE_DATE + ": " + archiveDate + "\n\n" 
                + BACKUP_FILE + ": " + backupFile.getPath() + "\n" 
                + ARCHIVE_FILE + ": " + archiveFile.getPath() + "\n\n"; 
//                + archiveSingleActions.size() + "  " + SINGLE_ACTIONS + "\n" 
//                + archiveProjects.size() + "  " + PROJECTS + "\n"
//                + archiveActions.size() + "  " + PROJECT_ACTIONS + "\n\n";
        JOptionPane.showMessageDialog(frame, m, t, JOptionPane.INFORMATION_MESSAGE);
    }

    /* Remove all projects and actions that are not to be archived and
     * return true if any are kept.
     */
    private boolean keepArchived(Project project) {

        if (project == null) {
            return false;
        }

        if (project.isDone() && project.getDoneDate().before(archiveDate)) {
            archiveProjects.add(project);
            return true;
        }

        boolean isAnyKept = false;

        for (Project subproject : project.getChildren(Project.class)) {
            if (keepArchived(subproject)) {
                isAnyKept = true;
            }
        }

        for (Action action : project.getChildren(Action.class)) {
            if (action.isDone() && action.getDoneDate().before(archiveDate)) {
                archiveActions.add(action);
                isAnyKept = true;
            } else {
                action.removeFromParent();
            }
        }

        if (isAnyKept) {
            // we need to keep this project since it has archived children
            // but we do not add to the archived projects list as we do not 
            // want to remove it from the data file.
        } else {
            // no archived children so remove
            project.removeFromParent();
        }
        
        return isAnyKept;
    }
    
    private Map<Integer, Project> createProjectsMap(Data data) {        
        Map<Integer, Project> map = new HashMap<>();
        mapProjects(data.getRootProjects(), map);
        return map;
    }

    private void mapProjects(Project project, Map<Integer, Project> map) {
        map.put(project.getID(), project);
        for (Project subproject : project.getChildren(Project.class)) {
            mapProjects(subproject, map);
        }
    }
    
    private void saveData(DataStore ds) {
        try {
            ds.store();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Could not save data. {0}", ex.getMessage()); // No I18N
        }
    }

    /**
     * Implement LookupListener to handle data change.
     * @param lookupEvent Is not used.
     */
    public void resultChanged(LookupEvent lookupEvent) {
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }



    private HashSet<Integer> getUsedThoughts(Data data) {
        HashSet<Integer> set = new HashSet<>();
        getUsedThoughts(set, data.getFutureManager());
        getUsedThoughts(set, data.getRootActions());
        getUsedThoughts(set, data.getRootProjects());
        getUsedThoughts(set, data.getRootFutures());
        getUsedThoughts(set, data.getRootTemplates());
        return set;
    }

    private void getUsedThoughts(HashSet<Integer> set, Manager<Future> manager) {
        for (Future future : manager.list()) {
            if (future.getThought() != null) {
                set.add(future.getThought().getID());
            }
        }
    }

    private void getUsedThoughts(HashSet<Integer> set, Project project) {
        if (project.getThought() != null) {
            set.add(project.getThought().getID());
        }
        for (Item item : project.getChildren()) {
            if (item instanceof Action action) {
                getUsedThoughts(set, action);
            } else if (item instanceof Project prj) {
                getUsedThoughts(set, prj);
            }
        }
    }

    private void getUsedThoughts(HashSet<Integer> set, Action action) {
        if (action.getThought() != null) {
            set.add(action.getThought().getID());
        }
    }

}
