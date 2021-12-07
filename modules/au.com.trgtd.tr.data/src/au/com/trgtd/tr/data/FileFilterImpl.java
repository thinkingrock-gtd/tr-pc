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

import java.io.File; 

import javax.swing.filechooser.FileFilter;
import au.com.trgtd.tr.util.UtilsFile;

/** 
 * File filter implementation for a given description, accepted extensions and
 * option to accept directories.
 *
 * @author Jeremy Moore
 */
public class FileFilterImpl extends FileFilter {
    
    private final String desc;
    private final String[] exts;
    private final boolean dirs;
    
    /**
     * Constructs a new instance.
     * @param desc The file filter description.
     * @param exts The acceptable file extensions.
     * @param dirs Whether or not directories are to be accepted.
     */
    public FileFilterImpl(String desc, String[] exts, boolean dirs) {
        this.desc = desc;
        this.exts = exts;
        this.dirs = dirs;
    }
    
    /**
     * Accepts appropriate directories and files.
     * @param file The file to examine.
     * @return true if the file has one of the given extensions or the file is
     * a directory and directories are to be accepted.
     */
    @Override
    public boolean accept(File file) {
        if (dirs && file.isDirectory()) {
            return true;
        }
        String fileExt = UtilsFile.getExtension(file.getName());
        if (fileExt != null) {
            for (String ext : exts) {
                if (fileExt.equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets the description of this filter.
     * @return the description.
     */
    @Override
    public String getDescription() {
        return desc;
    }
}
