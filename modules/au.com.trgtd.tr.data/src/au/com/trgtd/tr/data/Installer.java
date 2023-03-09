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
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import org.openide.windows.WindowManager;

/**
 * Module installer.
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {

    private static final Logger LOG = Logger.getLogger("tr.data"); // No I18N

    /** Creates a new instance of Installer. */
    public Installer() {
//        System.setProperty("netbeans.tab.close.button.enabled","false");
//        System.setProperty("nb.tabs.suppressCloseButton","true");
    }

    /**
     * On every startup register the data-store then listen for the main window
     * to be opened and attempt to load the data-store and set the window title
     * to include the data file path.
     */
    @Override
    public void restored() {
        loadDatastore();
    }

    private void loadDatastore() {

        DataStore ds = XStreamDataStore.instance();

        DataStoreLookup.instance().setDataStore(ds);

        try {
            ds.load();
            ds.startDaemon();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Data store could not load data. {0}", ex.getMessage()); // No I18N
        }

        final String path = (ds == null || !ds.isLoaded()) ? "" : ds.getPath();

        EventQueue.invokeLater(() -> {
            WindowManager.getDefault().getMainWindow().addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    super.windowOpened(e);

                    ((Frame) e.getSource()).setTitle(Constants.TITLE + " " + path); // No I18N

                    if (path.equals("")) { // No I18N
                        WindowUtils.openOverviewWindow();
                    } else {
                        WindowUtils.openInitialWindow();
                    }
                }
            });
        });
    }

    @Override
    public boolean closing() {
        WindowUtils.closeWindows();
        return super.closing();
    }

    @Override
    public void close() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds != null) {
            ds.stopDaemon();
            try {
                ds.store();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Data store could not store data. {0}", ex.getMessage()); // No I18N
            }
        }
        super.close();
    }

}
