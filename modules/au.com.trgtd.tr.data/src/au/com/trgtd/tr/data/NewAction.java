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
import tr.model.Data;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.util.UtilsFile;
import java.util.logging.Level;

/**
 * Action to create a new data file.
 *
 * @author Jeremy Moore
 */
public final class NewAction extends CallableSystemAction {

    private static final Logger LOG = Logger.getLogger("tr.data"); // No I18N

    public NewAction() {
    }
    
    @Override
    protected String iconResource() {
        return Resource.DataNew;
    }    

    /** Perform the action to show a file dialog and create a data file. */
    @Override
    public void performAction() {

        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found."); // No I18N
            return;
        }
        
        JFileChooser chooser = new JFileChooser();
        String[] extns = XStreamDataStore.FILE_EXTENSIONS;
        FileFilter filter = new FileFilterImpl(NbBundle.getMessage(NewAction.class, "tr.xstream.datafiles"), extns, true);
        chooser.setFileFilter(filter);

        File defaultFile = new File(ds.getPath());
        if (defaultFile.exists()) {
            chooser.setSelectedFile(defaultFile);
        }

        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showDialog(p, NbBundle.getMessage(NewAction.class, "new.file"));
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        // close windows here so that window settings are saved. 
        WindowUtils.closeWindows();

        // save current data
        try {
            ds.store();
        } catch (Exception ex) {
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
        
        // create new data
        String path = chooser.getSelectedFile().getPath();
        String extn = UtilsFile.getExtension(path);
        if (!Utils.in(extn, extns)) {
            path = UtilsFile.setExtension(path, "trx");
        }
        File newfile = new File(path);
        if (newfile.exists()) {
            String t = org.openide.util.NbBundle.getMessage(NewAction.class, "new.file");
            String m = org.openide.util.NbBundle.getMessage(NewAction.class, "confirm.replace.file");
            int r = JOptionPane.showConfirmDialog(p, m, t, JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                newfile.delete();
            } else {
                return;
            }
        }
        LOG.log(Level.INFO, "New file: {0}", path);
        try {
            newfile.createNewFile();
            ds.setPath(path);
            ds.setData(new Data());
            ds.setChanged(true);
            ds.store();
            WindowManager.getDefault().getMainWindow().setTitle(Constants.TITLE + " " + path);
            WindowUtils.closeWindows();
            WindowUtils.openInitialWindow();
        } catch (Exception ex) {
            String t = org.openide.util.NbBundle.getMessage(NewAction.class, "new.file.error.title");
            String m = org.openide.util.NbBundle.getMessage(NewAction.class, "new.file.error.message") + "\n" + ex.getMessage();
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(NewAction.class, "CTL_NewAction");
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
