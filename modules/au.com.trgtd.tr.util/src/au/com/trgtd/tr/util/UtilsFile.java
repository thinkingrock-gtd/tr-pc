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
package au.com.trgtd.tr.util;

import java.io.*;

/**
 * File utilities.
 * 
 * @author Jeremy Moore
 */
public final class UtilsFile {
    
    /* This utility class does not need to be instantiated. */
    private UtilsFile() {
    }
    
    /**
     * Copies the specified source file to the specified target file.
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        if (sourceFile == null || targetFile == null) {
            throw new NullPointerException("Source file and target file must not be null"); // NOI18N
        }
        
        // ensure existing parent directories
        File directory = targetFile.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Could not create directory '" + directory + "'"); // NOI18N
        }
        
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(targetFile));
            
            try {
                byte[] buffer = new byte[32768];
                for (int readBytes = inputStream.read(buffer);
                readBytes > 0;
                readBytes = inputStream.read(buffer)) {
                    outputStream.write(buffer, 0, readBytes);
                }
            } catch (IOException ex) {
                targetFile.delete();
                throw ex;
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
    }
    
    /**
     * Do the best to rename the file.
     * @param orig regular file
     * @param dest regular file (if exists it's rewritten)
     */
    public static void renameFile(File orig, File dest) throws IOException {
        boolean destExists = dest.exists();
        if (destExists) {
            for (int i = 0; i < 3; i++) {
                if (dest.delete()) {
                    destExists = false;
                    break;
                }
                try {
                    Thread.sleep(71);
                } catch (InterruptedException e) {
                }
            }
        }
        
        if (destExists == false) {
            for (int i = 0; i < 3; i++) {
                if (orig.renameTo(dest)) {
                    return;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
        
        // requires less permisions than renameTo
        UtilsFile.copyFile(orig, dest);
        
        for (int i = 0; i < 3; i++) {
            if (orig.delete()) {
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        throw new IOException("Can not delete: " + orig.getAbsolutePath());  // NOI18N
    }
    
    /**
     * Utility method to get a writable temporary directory.  This will be
     * either the system temp directory (System.getProperty("java.io.tmpdir"))
     * or the user home directory.
     * @return A temporary directory.
     * @throws IOException if the system temp directory and the user home
     * directory do not exist or are not writable directories.
     */
    public static File getTempDir() throws IOException {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File dir = new File(tmpdir);
        if (dir.isDirectory() && dir.canWrite()) {
            return dir;
        }
        tmpdir = System.getProperty("user.home");
        dir = new File(tmpdir);
        if (dir.isDirectory() && dir.canWrite()) {
            return dir;
        }
        throw new IOException("A writeable temporary directory could not be found.");
    }
    
    /**
     * Gets the extension of a filename.
     * @param filename The file name. 
     * @return The file extension (as lowercase) or null if name is null or does
     * not have an extension.
     */
    public static String getExtension(String filename) {
        if (filename != null) {
            int index = filename.lastIndexOf('.');
            if (index > 0 &&  index < filename.length() - 1) {
                return filename.substring(index+1).toLowerCase();
            }
        }
        return null;
    }
    
    /**
     * Sets an extension on a file path.
     * @param path The path.
     * @param extn The extension.
     * @return path with the new extension added or null if either path or extn
     * are null.
     */
    public static String setExtension(String path, String extn) {
        if (path == null || extn == null) return null;
        
//	// remove any trailing full stops from path
//      while (path.endsWith(".")) {
//    	    path = path.substring(0, path.length() - 2);
//      }
        
        return path + "." + extn;
    }
    
    /**
     * Removes the last extension from a file path.
     * @param path The path.
     * @return path with the last extension removed.
     */
    public static String removeExtension(String path) {
        if (path == null) return null;
        
        int index = path.lastIndexOf(".");
        if (index < 0) {
            return path;
        } else {
            return path.substring(0, index);
        }
        
    }

    /**
     * Sets a file as executable if possible.
     * @param file The file.
     * @return true if and only if the file is now executable.
     */
    public static boolean setExecutable(File file) {
        
        if (file == null) {
            return false;
        }
        
        if (!file.isFile()) {
            return false;
        }

        if (file.canExecute()) {
            return true;
        }
        
        try {
            if (file.setExecutable(false /* owner only */)) {
                return true;
            }
        } catch (SecurityException ex) {
        }

        try {
            if (file.setExecutable(true /* owner only */)) {
                return true;
            }
        } catch (SecurityException ex) {
        }
        
        return file.canExecute();
    }
    
}
