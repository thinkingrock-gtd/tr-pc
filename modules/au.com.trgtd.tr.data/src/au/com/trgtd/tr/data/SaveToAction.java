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

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.datastore.xstream.XStreamDataStore;
import au.com.trgtd.tr.resource.Resource;
import java.awt.Component;
import java.awt.EventQueue;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.util.UtilsFile;
import java.util.logging.Level;

/**
 * Backup action.
 *
 * @author Jeremy Moore
 */
public final class SaveToAction extends CallableSystemAction {

    private static final Logger LOG = Logger.getLogger("tr.data");

    /** Constructs a new instance. */
    public SaveToAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
    }
    
    @Override
    protected String iconResource() {
        return Resource.DataSaveTo;
    }

    private void enableDisable() {
        EventQueue.invokeLater(() -> {
            Data data = DataLookup.instance().lookup(Data.class);
            setEnabled(data != null);
        });
    }
    
    /** Save the current data store to another file. */
    @Override
    public void performAction() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) return;
        
        saveData(ds);
        
        JFileChooser chooser = new JFileChooser();
        String[] extns = XStreamDataStore.FILE_EXTENSIONS;
        FileFilter filter = new FileFilterImpl(NbBundle.getMessage(SaveToAction.class, "tr.xstream.datafiles"), extns, true);
        chooser.setFileFilter(filter);
        
//      chooser.setSelectedFile(new File(ds.getPath()));
        String lastPath = SaveToPrefs.getSaveToPath();
        lastPath = lastPath.equals(SaveToPrefs.DEF_SAVE_TO_PATH) ? ds.getPath() : lastPath;
        chooser.setSelectedFile(new File(lastPath));
        
        Component p = WindowManager.getDefault().getMainWindow();
        
        int returnVal = chooser.showDialog(p, NbBundle.getMessage(SaveToAction.class, "save.to"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String pathTo = chooser.getSelectedFile().getPath();
            String extn = UtilsFile.getExtension(pathTo);
            if (!Utils.in(extn, extns) ) {
                pathTo = UtilsFile.setExtension(pathTo, "trx");
            }
            File file = new File(pathTo);
            if (file.exists()) {
                String t = NbBundle.getMessage(SaveToAction.class, "save.to");
                String m = NbBundle.getMessage(SaveToAction.class, "confirm.replace.file");
                int r = JOptionPane.showConfirmDialog(p, m, t, JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) return;
            }
            
            LOG.log(Level.INFO, "Backup to: {0}", pathTo);
            
            String path = ds.getPath();
            
            ds.setPath(pathTo);
            ds.setChanged(true);
            saveData(ds);
            
            SaveToPrefs.setSaveToPath(pathTo);
            
            ds.setPath(path);
        }
    }
    
    private void saveData(DataStore ds) {
        try {
            ds.store();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(SaveToAction.class, "CTL_SaveToAction");
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
