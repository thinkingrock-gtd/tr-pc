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
package au.com.trgtd.tr.runtime;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.openide.util.Exceptions;

/**
 * Runtime commands for opening a file.
 *
 * @author Jeremy Moore
 */
public class Open {
    
    /**
     * Try to open a file using the runtime exec facility.
     * @param file the file to open.
     */
    public static void openFile(File file) {
        if (file == null) return;
        
        // TODO: test that the file is a file and is readable.
        
        String filename = getFilePath(file);
        
        // try using JDIC
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
                return;
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        // otherwise try Runtime
        if (Runtime.MAC) {
            Runtime.exec(new String[] {"open", filename});
        } else if (Runtime.LINUX || Runtime.UNIX) {
            Runtime.exec(new String[] {"xdg-open", filename});
        } else if (Runtime.WIN95 || Runtime.WIN98) {
            Runtime.exec(new String[] {"command.com", "/C", "start", filename});
        } else if (Runtime.WINDOWS) {
            Runtime.exec(new String[] {"cmd.exe", "/C", "start", filename});
        } else {
            Runtime.exec(new String[] {"open", filename});
        }
    }
    
    /* Gets the file path for opening on the various platforms.  */
    private static String getFilePath(File file) {
        if (file == null) return null;
        
        String path = file.getPath();
        
        if (Runtime.WINDOWS || Runtime.LINUX) {
            path = "file:///" + path.replace("\u0020", "%20");
        }
        
        return path;
    }
    
    /**
     * Try to open a text file using the runtime exec facility.
     * @param file the file to open.
     */
    public static void openTextFile(File file) {
        if (file == null) return;
        
        // TODO: test that the file is a file and is readable.
        
        String filename = getFilePath(file);
        
        if (Runtime.MAC) {
            Runtime.exec(new String[] {"open", "-t", filename});
        } else if (Runtime.LINUX || Runtime.UNIX) {
            Runtime.exec(new String[] {"xdg-open", filename});
        } else if (Runtime.WIN95 || Runtime.WIN98) {
            Runtime.exec(new String[] {"command.com", "/C", "start", filename});
        } else if (Runtime.WINDOWS) {
            Runtime.exec(new String[] {"cmd.exe", "/C", "start", filename});
        } else {
            Runtime.exec(new String[] {"open", filename});
        }
    }
    
    /**
     * Try to open a URL in the default browser using the runtime exec facility.
     * @param url the URL to open.
     */
    public static void open(URL url) {
        if (url == null) return;

        String urlString = url.toExternalForm();

        if (Runtime.WINDOWS || Runtime.LINUX) {
            urlString = urlString.replace("\u0020", "%20");
        }       
        if (Runtime.WINDOWS) {
            urlString = urlString.replace("\u0026", "^&");
        }
        
        if (Runtime.MAC) {
            Runtime.exec(new String[] {"open", urlString});
        } else if (Runtime.LINUX || Runtime.UNIX) {
            Runtime.exec(new String[] {"xdg-open", urlString});
        } else if (Runtime.WIN95 || Runtime.WIN98) {
            Runtime.exec(new String[] {"command.com", "/c", "start", urlString});
        } else if (Runtime.WINDOWS) {
            Runtime.exec(new String[] {"cmd.exe", "/c", "start", urlString});
        } else {
            Runtime.exec(new String[] {"open", urlString});
        }
    }
    
}
