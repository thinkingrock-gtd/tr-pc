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
package au.com.trgtd.tr.sync.dropbox;

import au.com.trgtd.tr.sync.iphone.SyncPrefs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.RequestProcessor;

/**
 * DropBox sync manager. If the sync method user preference is DropBox, polls
 * for new DropBox file and starts DropBox sync task.
 * 
 * @author Jeremy Moore
 */
public class DBoxSyncMgr extends Thread {

    private static final Logger LOG = Logger.getLogger(DBoxSyncMgr.class.getName());
    private static final RequestProcessor THREAD_POOL = new RequestProcessor("DropBox Synchronizer");

    private final long SLEEP_SECONDS = 15;
    private Thread dboxSyncThread;
    private boolean cancelled;

    public DBoxSyncMgr() {
        deleteDropBoxFile();
    }

    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public void run() {

        long lastModified = 0;

        while (!cancelled) {

            if (isDBoxMethod()) {                
                
                File file = getDBoxFile();                
                
                if (file != null && file.isFile()) {
                
                    if (file.lastModified() > lastModified) {
                        LOG.log(Level.INFO, "Modified DropBox sync file found.");
                        startSyncThread(file);                    
                        lastModified = file.lastModified();                    
                    } 
                }
            }

            snooze();
        }
    }

    private void snooze() {
        try {
            Thread.sleep(SLEEP_SECONDS * 1000);
        } catch (InterruptedException ex) {
        }
    }

    private boolean isDBoxMethod() {
//        boolean b = SyncPrefs.getMethod() == SyncPrefs.SYNC_METHOD_DBOX;
//        LOG.log(Level.INFO, "DropBox sync method: {0}", b);
//        return b;
        return SyncPrefs.getMethod() == SyncPrefs.SYNC_METHOD_DBOX;
    }

    private File getDBoxFile() {
        try {
            File dboxFolder = new File(SyncPrefs.getDropBoxPath());
            if (!dboxFolder.isDirectory()) {
                LOG.log(Level.WARNING, "DropBox sync folder is not valid.");
                return null;
            }

            File file = new File(dboxFolder, Constants.FILE_SYNC_UP);

            LOG.log(Level.INFO, "DropBox sync file exists? {0}", file.isFile());
            
            return file;

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "DropBox sync file exception: {0}", ex.getMessage());
            return null;            
        }
    }

    private void deleteDropBoxFile() {
        try {
            File dboxFolder = new File(SyncPrefs.getDropBoxPath());
            if (dboxFolder.isDirectory()) {
                File dboxFile = new File(dboxFolder, Constants.FILE_SYNC_UP);
                if (dboxFile.isFile()) {
                    if (dboxFile.delete()) {
                        LOG.log(Level.INFO, "Existing DropBox sync file deleted.");
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    private void startSyncThread(File file) {

        if (null != dboxSyncThread && dboxSyncThread.isAlive()) {
            LOG.log(Level.INFO, "DropBox sync thread is already running.");
            return;
        }

        LOG.log(Level.INFO, "DropBox sync thread starting ...");

//        dboxSyncThread = new DBoxSync103(file);
//        dboxSyncThread.start();

//        LOG.log(Level.INFO, "Waiting for DropBox sync thread to finish.");
//        LOG.log(Level.INFO, "DropBox sync thread finished.");
        
        
        BufferedReader reader = null;
        try {
            reader = getReader(file);
            String line = reader.readLine();
            if (line.startsWith(VERSION_103 + "|HELLO|")) {
                dboxSyncThread = new DBoxSync103(file);
                dboxSyncThread.start();
            } else if (line.startsWith(VERSION_104 + "|HELLO|")) {
                dboxSyncThread = new DBoxSync104(file);
                dboxSyncThread.start();
            } else {
                LOG.log(Level.SEVERE, "DropBox sync up file handshake error.");                
            }
            
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "DropBox sync up file exception: {0}", ex.getMessage());                
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ex) {
                }
            }
        }
    }
    
    private BufferedReader getReader(File file) throws Exception {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), Constants.FILE_ENCODING));
    }

    public static final String VERSION_103 = "V1.03";
    public static final String VERSION_104 = "V1.04";
}
