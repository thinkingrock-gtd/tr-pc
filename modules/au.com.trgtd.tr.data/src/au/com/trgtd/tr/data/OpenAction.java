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
package au.com.trgtd.tr.data;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.datastore.xstream.XStreamDataStore;
import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import au.com.trgtd.tr.resource.Resource;
import java.awt.Component;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import au.com.trgtd.tr.task.activation.ActivatorTaskScheduler;
import au.com.trgtd.tr.task.recurrence.RecurrenceTaskScheduler;
import java.util.logging.Level;

/**
 * Action to open a TR datafile.
 *
 * @author Jeremy Moore
 */
public final class OpenAction extends CallableSystemAction {

    private static final Logger LOG = Logger.getLogger("tr.data");

    public OpenAction() {
    }

    @Override
    protected String iconResource() {
        return Resource.DataOpen;
    }    
    
    /** Perform the action to show a file dialog and open a data file. */
    @Override
    public void performAction() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        String[] extns = XStreamDataStore.FILE_EXTENSIONS;
        FileFilter filter = new FileFilterImpl(org.openide.util.NbBundle.getMessage(OpenAction.class, "tr.xstream.datafiles"), extns, true);
        chooser.setFileFilter(filter);
        File defaultFile = new File(ds.getPath());
        if (defaultFile.exists()) {
            chooser.setSelectedFile(defaultFile);
        }
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showOpenDialog(p);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // save current data
        try {
            ds.store();
        } catch (Exception ex) {
        }

        // close windows here so that window settings are saved. 
        WindowUtils.closeWindows();

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

        // load data from selected file
        try {
            String path = chooser.getSelectedFile().getPath();
            ds.setPath(path);
            ds.load();
            WindowManager.getDefault().getMainWindow().setTitle(Constants.TITLE + " " + path);
            WindowUtils.closeWindows();
            WindowUtils.openInitialWindow();
            ActivatorTaskScheduler.instance().runTask(0, 1, 0);
            RecurrenceTaskScheduler.instance().runTask(0, 2, 0);
        } catch (Exception ex) {
            String t = org.openide.util.NbBundle.getMessage(OpenAction.class, "file.open.error.title");
            String m = org.openide.util.NbBundle.getMessage(OpenAction.class, "file.open.error.message") + "\n" + ex.getMessage();
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(OpenAction.class, "CTL_OpenAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
