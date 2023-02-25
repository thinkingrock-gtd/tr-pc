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
package au.com.trgtd.tr.sync.device.prefs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileFilter;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Application logic for a snapshot of the Android DropBox synchronization user
 * preferences.
 *
 * @author Jeremy Moore
 */
public final class SyncPrefsDbx {

    private static final Logger LOG = Logger.getLogger(SyncPrefsDbx.class.getName());
    
    private static final Object SOURCE = new Object();
    private static final PropertyChangeSupport PCS = new PropertyChangeSupport(SOURCE);
    
    public static void addListener(PropertyChangeListener listener) {
        PCS.addPropertyChangeListener(listener);
    }
    
    public static void removeListener(PropertyChangeListener listener) {
        PCS.removePropertyChangeListener(listener);
    }
    
    static {
        SyncPrefs.addListener((PreferenceChangeEvent evt) -> {
            if (evt.getKey().equals(SyncPrefs.KEY_SYNC_ANDROID)
                    || evt.getKey().equals(SyncPrefs.KEY_SYNC_DBX)
                    || evt.getKey().equals(SyncPrefs.KEY_DBX_PATH)) {
                PCS.firePropertyChange("changed", false, true);
            }            
        });        
    }
    
    /**
     * Gets the current DropBox sync user preferences.
     * @return The current DropBox sync user preferences.
     */
    public static SyncPrefsDbx getCurrent() {
        return new SyncPrefsDbx(SyncPrefs.isSyncAndroid() && SyncPrefs.isSyncDbx(), SyncPrefs.getDbxPath());
    }

    private final boolean sync; // sync using DropBox
    private final String path;  // root path 

    /**
     * Constructor.
     * @param sync Whether or not to sync using DropBox.
     * @param path The root file system path for DropBox.
     */
    private SyncPrefsDbx(boolean sync, String path) {
        this.sync = sync;
        this.path = path;
    }

    /**
     * Determines whether synchronization using DropBox should be done.
     * @return true if the user preferences are set to do DropBox sync.
     */
    public boolean on() {
        return sync;
    }

    /**
     * Determines whether synchronization using DropBox should not be done.
     * @return true if not on.
     */
    public boolean off() {
        return !on();
    }

    /**
     * Gets the DropBox root directory path.
     * @return The path which could be null or empty.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the directory for DropBox sync files.
     * @return The directory for sync files or null if not found.
     */
    public File getSyncDir() {
        String rootPath = getPath();
        if (rootPath == null || rootPath.length() == 0) {
            LOG.warning("DropBox path user preference is not entered.");
            return null;
        }
        File rootDir = new File(rootPath);
        if (!rootDir.isDirectory()) {
            LOG.warning("DropBox path user preference is not a directory.");
            return null;
        }        
        // The DropBox applications dir is normally named "Apps".
        File appsDir = new File(rootDir, "Apps");
        if (appsDir.isDirectory()) {
            File syncDir = new File(appsDir, "tr.sync");
            if (syncDir.isDirectory()) {
                return syncDir;
            }
        }        
        // The DropBox applications dir is sometimes translated.
        for (File dir : rootDir.listFiles(directoriesOnlyFilter)) {
            File syncDir = new File(dir, "tr.sync");
            if (syncDir.isDirectory()) {
                return syncDir;
            }
        }
        LOG.warning("DropBox sync directory was not found.");
        return null;
    }
    
    private final FileFilter directoriesOnlyFilter = File::isDirectory;
    
    /**
     * Gets the sync down file within the DropBox path.
     * @return The sync down file or null if the DropBox path is not valid.
     */
    public File getSyncDownFile(String token) {
        File dir = getSyncDir();
        return dir == null ? null : new File(dir, "syncdown" + token + ".json");
    }

    @Override
    public String toString() {
        return "SyncPrefsDbx{" + "sync=" + sync + ", path=" + path + "}";
    }

    @Override
    public boolean equals(Object object) {        
        if (object == null) {
            return false;
        }        
        if (getClass() != object.getClass()) {
            return false;
        }
        return this.sync == ((SyncPrefsDbx)object).sync;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.sync ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.path);
        return hash;
    }
    
}
