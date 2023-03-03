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

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
 
/**
 * File watcher support.
 */
public class FileWatcherSupport {

    private static final Logger Log = Logger.getLogger(FileWatcherSupport.class.getName());
    private static final Level LEVEL_DEBUG = Level.FINE;
    
    private final WatcherThread mWatcherThread;

    /**
     * Creates a new instance.
     * @param fileWatcher The file watcher.
     * @throws IOException 
     */
    public FileWatcherSupport(FileWatcher fileWatcher) throws IOException {        
        validate(fileWatcher);

        Path path = Paths.get(fileWatcher.getDir().getPath());
        WatchService watchService = path.getFileSystem().newWatchService();
        path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
  
        mWatcherThread = new WatcherThread(watchService, fileWatcher);                
    }

    /**
     * Starts the watch service thread. This method should be called exactly 
     * once to start the service.
     */
    public void start() {
        if (mWatcherThread != null && !mWatcherThread.isAlive()) {
            mWatcherThread.start();                    
        }
    }
    
    /**
     * Stops the watch service thread. This method should be called exactly 
     * once to stop the service.
     */
    public void stop() {
        if (mWatcherThread != null && mWatcherThread.isAlive()) {
            mWatcherThread.cancel();
        }
    }
    
    private void validate(FileWatcher fileWatcher) {
        if (fileWatcher == null) {
            throw new IllegalArgumentException("File watcher can not be null.");            
        }
        File dir = fileWatcher.getDir();
        if (dir == null) {
            throw new IllegalArgumentException("Directory can not be null.");            
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Directory not found: " + dir);            
        }
    }
    
    /*
     * This Thread is used to constantly attempt to take from the watch queue, 
     * and will receive all events that are registered with the watcher service
     * it is associated with.
     */
    private class WatcherThread extends Thread {
  
        private final WatchService watcherService;
        private final FileWatcher fileWatcher;
        
        WatcherThread(WatchService watcherService, FileWatcher fileWatcher) {
            this.watcherService = watcherService;
            this.fileWatcher = fileWatcher;
        }
        
        void cancel() {
            try {
                watcherService.close();
            } catch (IOException ex) {
                Log.log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * In order to implement a file watcher, we loop forever ensuring 
         * requesting to take the next item from the file watchers queue.
         */
        @Override
        public void run() {

            Log.log(LEVEL_DEBUG, "Watcher thread started.");
            
            try {
                // get the first event before looping
                WatchKey key = watcherService.take();
                while (key != null) {
                    
                    // we have a polled event, now we traverse it and receive all the states from it
                    for (WatchEvent<?> event : key.pollEvents()) {

                        Log.log(LEVEL_DEBUG, "Received {0} event for file: {1}", 
                                new Object[] {event.kind(), event.context()});
                        
                        String filename = event.context() == null ? null : event.context().toString();
                        
                        if (fileWatcher.match(filename)) {
                            if (event.kind().equals(ENTRY_CREATE)) {
                                fileWatcher.onFileCreate(filename);
                            } else if (event.kind().equals(ENTRY_MODIFY)) {
                                fileWatcher.onFileModify(filename);
                            } else if (event.kind().equals(ENTRY_DELETE)) {
                                fileWatcher.onFileDelete(filename);
                            }
                        }
                    }
                    key.reset();
                    key = watcherService.take();
                }
            } catch (InterruptedException e) {                
                Log.log(LEVEL_DEBUG, "Watcher thread interrupted.");
            } catch (ClosedWatchServiceException e) {
                Log.log(LEVEL_DEBUG, "Watcher service closed.");
            }

            Log.log(LEVEL_DEBUG, "Watcher thread stopped.");                
        }
    }
}