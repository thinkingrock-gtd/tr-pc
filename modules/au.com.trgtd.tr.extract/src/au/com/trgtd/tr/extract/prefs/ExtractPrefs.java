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
package au.com.trgtd.tr.extract.prefs;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Extract preferences.
 *
 * @author Jeremy Moore
 */
public class ExtractPrefs {

    private static final Logger LOG = Logger.getLogger("tr.extract.prefs");
    
    private static final Preferences PREFS = Constants.getPrefs("extract");      

    private static final String KEY_PATH = "path";
    private static final String KEY_ENCODING = "encoding";

    private static final String DEF_PATH = System.getProperty("user.home");
    private static final String DEF_ENCODING = "";
    
    /**
     * Gets the value for the path preference.
     * @return The value.
     */
    public static final String getPath() {
        return PREFS.get(KEY_PATH, DEF_PATH);
    }
    
    /**
     * Sets the value for the path preference.
     * @param value The value.
     */
    public static final void setPath(String value) {
        PREFS.put(KEY_PATH, value);
        flush();
    }
    
    /**
     * Gets the value for encoding.
     * @return The value.
     */
    public static final String getEncoding() {
        return PREFS.get(KEY_ENCODING, DEF_ENCODING);
    }
    
    /**
     * Sets the value for encoding.
     * @param value The value.
     */
    public static final void setEncoding(String value) {
        PREFS.put(KEY_ENCODING, (value == null) ? "" : value.trim());
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Extract preferences error. {0}", ex.getMessage());
        }
    }
    
}
