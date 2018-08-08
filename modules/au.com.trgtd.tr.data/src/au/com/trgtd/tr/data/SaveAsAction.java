/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.data;

import au.com.trgtd.tr.appl.Constants;
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
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.util.UtilsFile;
import java.util.logging.Level;

/**
 * SaveAs action.
 *
 * @author Jeremy Moore
 */
public final class SaveAsAction extends CallableSystemAction { // implements Observer {
    
    private static final Logger LOG = Logger.getLogger("tr.data");
    
    /** Constructs a new instance. */
    public SaveAsAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }
    
    @Override
    protected String iconResource() {
        return Resource.DataSaveAs;
    }

    private void enableDisable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = (Data)DataLookup.instance().lookup(Data.class);
                setEnabled(data != null);
            }
        });
    }
    
    /** Save the current data-store as another file. */
    @Override
    public void performAction() {
        DataStore ds = (DataStore)DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found.");
            return;
        }
        
        saveData(ds);
        JFileChooser chooser = new JFileChooser();
        String[] extns = XStreamDataStore.FILE_EXTENSIONS;
        FileFilter filter = new FileFilterImpl(org.openide.util.NbBundle.getMessage(SaveAsAction.class, "tr.xstream.datafiles"), extns, true);
        chooser.setFileFilter(filter);
        chooser.setSelectedFile(new File(ds.getPath()));
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showDialog(p, org.openide.util.NbBundle.getMessage(SaveAsAction.class, "save.as"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            String extn = UtilsFile.getExtension(path);
            if (!Utils.in(extn, extns) ) {
                path = UtilsFile.setExtension(path, "trx");
            }
            File file = new File(path);
            if (file.exists()) {
                String t = org.openide.util.NbBundle.getMessage(SaveAsAction.class, "save.as");
                String m = org.openide.util.NbBundle.getMessage(SaveAsAction.class, "confirm.replace.file");
                int r = JOptionPane.showConfirmDialog(p, m, t, JOptionPane.YES_NO_OPTION);
                if (r != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            LOG.log(Level.INFO, "Save As: {0}", path);
            
            ds.setPath(path);
            ds.setChanged(true);
            saveData(ds);
            WindowManager.getDefault().getMainWindow().setTitle(Constants.TITLE  + " " + path);
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
        return NbBundle.getMessage(SaveAction.class, "CTL_SaveAsAction");
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
