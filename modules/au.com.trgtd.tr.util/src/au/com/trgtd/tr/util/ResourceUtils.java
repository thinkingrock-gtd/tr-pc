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

import java.io.File;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Utilities;

/**
 * Resource handling static methods.
 *
 * @author Jeremy Moore
 */
public class ResourceUtils {
    
    private static final String CODE_NAME_BASE= "au.com.trgtd.tr.util";
    
    /* Private constructor. */
    private ResourceUtils() {
    }
    
    /**
     * Loads and returns an IconImage for the path of an installed icon file.
     * @param path The icon path (e.g. "resource/images/MyIcon16.gif").
     * @return The ImageIcon or null.
     */
    public static ImageIcon loadInstalledIcon(String path) {
        File file = InstalledFileLocator.getDefault().locate(path, CODE_NAME_BASE, false);
        if (file != null && file.isFile()) {
            try {
                return new ImageIcon(Utilities.toURI(file).toURL());
            } catch (MalformedURLException ex) {
            }
        }
        System.err.println("Could not load image for path: " + path);
        return new ImageIcon(); // return a default one.
    }
    
}
