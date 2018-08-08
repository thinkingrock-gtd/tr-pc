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
