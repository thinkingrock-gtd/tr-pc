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
package au.com.trgtd.tr.sync.device.dbx;

import au.com.trgtd.tr.sync.device.prefs.SyncPrefsDbx;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DbxController {

    private static final Logger LOG = Logger.getLogger(DbxController.class.getName());
    
    private static DbxController instance;

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static DbxController instance() {
        if (instance == null) {
            instance = new DbxController();
        }
        return instance;
    }

    private DbxController() {
    }

    private SyncPrefsDbx mDbxPrefs;
    private FileWatcherSupport fileWatcherSupport;

    
    public void onStartup() {
        mDbxPrefs = SyncPrefsDbx.getCurrent();
        addFileWatcher();
        addPrefListener();
    }    
    
    public void onShutdown() {
        removeFileWatcher();
        removePrefListener();
    }
    
    private void addFileWatcher() {
        if (!mDbxPrefs.on()) {
            LOG.log(Level.INFO, "Not syncing with DropBox");            
            return;
        }
        File syncDir = mDbxPrefs.getSyncDir();
        if (syncDir == null || !syncDir.isDirectory()) {
            LOG.log(Level.WARNING, "DropBox sync directory is not valid.");            
            return;
        }
        try {
            fileWatcherSupport = new FileWatcherSupport(mFileWatcher);     
            fileWatcherSupport.start();
        } catch (IOException e) {
            LOG.log(Level.INFO, "Problem creating file watcher service.", e);            
        }
    }

    private void removeFileWatcher() {
        if (fileWatcherSupport != null) {
            fileWatcherSupport.stop();
            fileWatcherSupport = null;
        }     
    }

    private void addPrefListener() {
        SyncPrefsDbx.addListener(mPrefListener);
    }
    
    private void removePrefListener() {
        SyncPrefsDbx.removeListener(mPrefListener);
    }
    
    private final FileWatcher mFileWatcher = new FileWatcher() {
        @Override
        public File getDir() {
            return mDbxPrefs.getSyncDir();
        }
        @Override
        public void onFileCreate(String filename) {
            File file = new File(getDir(), filename);
            new DbxSyncThread(file).start();
        }
        @Override
        public void onFileModify(String filename) {
        }
        @Override
        public void onFileDelete(String filename) {
        }
        @Override
        public boolean match(String filename) {
            return filename != null && filename.startsWith("syncup");
        }
    };
    
    private final PropertyChangeListener mPrefListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            removeFileWatcher();
            SyncPrefsDbx oldPrefs = mDbxPrefs;
            mDbxPrefs = SyncPrefsDbx.getCurrent();
            addFileWatcher();
            LOG.log(Level.FINE, "DROPBOX PREFS CHANGE. OLD: {0} NEW: {1}", new Object[] {oldPrefs, mDbxPrefs});            
        }
    };
}
