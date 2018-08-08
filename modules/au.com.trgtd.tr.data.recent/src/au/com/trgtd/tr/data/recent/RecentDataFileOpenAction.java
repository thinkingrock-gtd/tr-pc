package au.com.trgtd.tr.data.recent;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.data.DAOProvider;
import au.com.trgtd.tr.data.DAOProviderLookup;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.util.HelpCtx;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.task.activation.ActivatorTaskScheduler;
import au.com.trgtd.tr.task.recurrence.RecurrenceTaskScheduler;
import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;

/**
 * Action to open a recent data file.
 *
 * @author Jeremy Moore
 */
public final class RecentDataFileOpenAction extends AbstractAction {

    private static final Logger LOG = Logger.getLogger("tr.data.files");
    private final String path;

    /**
     * Constructs a new instance for the given data file path.
     * @param path The data file path.
     */
    public RecentDataFileOpenAction(String path) {
        super(path);
        this.path = path;
    }

    /**
     * Perform the action to open the recent data file.
     */
    public void actionPerformed(ActionEvent actionEvent) {

        DataStore ds = (DataStore) DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found.");
            return;
        }

        File datafile = new File(path);
        if (!datafile.exists()) {
            String t = org.openide.util.NbBundle.getMessage(RecentDataFileOpenAction.class, "datafile.not.found.title");
            String m = org.openide.util.NbBundle.getMessage(RecentDataFileOpenAction.class, "datafile.not.found.message") + "\n\n";
            Component p = WindowManager.getDefault().getMainWindow();
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // close windows here so that window settings are saved. 
        WindowUtils.closeWindows();

        // save current data
        try {
            ds.store();
        } catch (Exception ex) {
            LOG.severe("Datastore store exception: " + ex.getMessage());
        }

        // save review actions screens and force reload
        DAOProvider screensDAOProvider = (DAOProvider)DAOProviderLookup.instance().lookup(DAOProvider.class);
        if (screensDAOProvider != null) {
            try {
                LOG.info("Saving actions screens.");                
                if (screensDAOProvider.isInitialised()) {
                    screensDAOProvider.provide().persist();
                    screensDAOProvider.provide().reset();
                    screensDAOProvider.reset();
                }
            } catch (Exception ex) {
                LOG.severe("Review Actions screens could not be saved. " + ex.getMessage());                
            }
        }        

        // open new data
        try {
            ds.setPath(path);
            ds.load();
            WindowManager.getDefault().getMainWindow().setTitle(Constants.TITLE + " " + path);
            WindowUtils.closeWindows();
            WindowUtils.openInitialWindow();
            ActivatorTaskScheduler.instance().runTask(0, 1, 0);
            RecurrenceTaskScheduler.instance().runTask(0, 2, 0);
        } catch (Exception ex) {
            String e = ex.getMessage();
            String t = org.openide.util.NbBundle.getMessage(RecentDataFileOpenAction.class, "file.open.error.title");
            String m = org.openide.util.NbBundle.getMessage(RecentDataFileOpenAction.class, "file.open.error.message") + " " + path + "\n\n" + ((e == null) ? "" : e);
            Component p = WindowManager.getDefault().getMainWindow();
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
        }
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected boolean asynchronous() {
        return false;
    }
}
