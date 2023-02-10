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
package au.com.trgtd.tr.data.recent;

import au.com.trgtd.tr.appl.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Preferences for recent files.
 *
 * @author Jeremy Moore
 */
public class Prefs {
    
    private static final Logger LOG = Logger.getLogger("tr.data.files");
    
    private static final Preferences PREFS = Constants.getPrefs("recent");
    
    private static final String KEY_PATH_PREFIX = "path";
    
    /**
     * Gets the value for the path preference.
     * @param n The path number.
     * @return The path value or null if there is no value stored.
     */
    private static String getPath(int n) {
        return PREFS.get(KEY_PATH_PREFIX + n, null);
    }
    
    /**
     * Sets the value for the path preference.
     * @param n The path number.
     * @param p The path value.
     */
    private static void setPath(int n, String p) {
        PREFS.put(KEY_PATH_PREFIX + n, p);
    }
    
    /**
     * Sets the values of the paths preference.
     * @param paths The paths.
     * @param max The maximum number of paths to store.
     */
    public static final void setPaths(List<String> paths, int max) {
        try {
            
            PREFS.clear();
            
            if (paths != null) {
                for (int i = 0; i < paths.size() && i < max; i++) {
                    setPath(i, paths.get(i));
                }
            }
            
            PREFS.flush();
            
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Preferences backing store exception. {0}", ex.getMessage());
        }
    }
    
    /**
     * Gets the values of the paths preference.
     * @return The paths.
     */
    public static final List<String> getPaths() {
        
        List<String> paths = new ArrayList<>();
        
        int i = 0;
        
        while (true) {
            String path = getPath(i++);
            if (path == null) {
                break;
            }
            paths.add(path);
        }
        
        return paths;
    }
    
}
