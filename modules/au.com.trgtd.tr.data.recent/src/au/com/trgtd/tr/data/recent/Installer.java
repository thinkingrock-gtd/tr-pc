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

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import java.util.List;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.actions.SystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Module installer that detects each change of data file and stores the path of
 * the ten most recently used data files in preferences.
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {
    
    private static final Logger LOG = Logger.getLogger("tr.data.files");
    
    private static final int N_MOST_RECENT = 10;
    
    private Lookup.Result result;
    private RecentDataFilesAction action;
    
    /**
     * Starts listening for change of data file and populates the Open Recent
     * sub-menu of the File menu.
     */
    @Override
    public void restored() {
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            storeDataFilePath();
            refreshRecentFilesMenu();
        });
    }
    
    /**
     * Store latest data file path in preferences.
     */
    private synchronized void storeDataFilePath() {
        LOG.info("Change of data file.");
        
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.info("Datastore was not found.");
            return;
        }
        
        if (!ds.isLoaded()) {
            LOG.info("No data file loaded.");
            return;
        }
        
        String path = ds.getPath();
        if (path == null || path.trim().length() == 0) {
            LOG.info("Data file path was null or an empty string.");
            return;
        }
        
        List<String> paths = Prefs.getPaths();
        
        if (paths.contains(path)) {
            paths.remove(path);
        }
        
        paths.add(0, path);
        
        Prefs.setPaths(paths, N_MOST_RECENT);
    }
    
    private synchronized void refreshRecentFilesMenu() {
        if (action == null) {
            action = SystemAction.get(RecentDataFilesAction.class);
        }
        action.refreshMenu();
    }
    
}
