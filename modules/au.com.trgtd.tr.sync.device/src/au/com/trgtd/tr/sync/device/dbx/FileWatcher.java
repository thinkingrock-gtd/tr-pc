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

import java.io.File;

/**
 * File watcher interface.
 */
public interface FileWatcher {

    /**
     * Gets the directory to watch.
     * @return the directory, which must exist.
     */
    public File getDir();
    
    /**
     * Matches files of interest.
     * @param filename The filename of a file that may be of interest (that is, 
     * a file within the directory that has had an event fired for it).
     * @return true if the file is of interest.
     */
    public boolean match(String filename);
    
    /**
     * Called when the watched file is created.
     */
    public void onFileCreate(String filename);

    /**
     * Called when the watched file is modified.
     */
    public void onFileModify(String filename);
    
    /**
     * Called when the watched file is deleted.
     */
    public void onFileDelete(String filename);
    
}
