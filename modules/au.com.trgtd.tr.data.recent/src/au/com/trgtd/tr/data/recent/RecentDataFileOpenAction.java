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
import java.util.logging.Level;

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
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
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
            LOG.log(Level.SEVERE, "Datastore store exception: {0}", ex.getMessage());
        }

        // save review actions screens and force reload
        DAOProvider screensDAOProvider = DAOProviderLookup.instance().lookup(DAOProvider.class);
        if (screensDAOProvider != null) {
            try {
                LOG.info("Saving actions screens.");                
                if (screensDAOProvider.isInitialised()) {
                    screensDAOProvider.provide().persist();
                    screensDAOProvider.provide().reset();
                    screensDAOProvider.reset();
                }
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Review Actions screens could not be saved. {0}", ex.getMessage());
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
