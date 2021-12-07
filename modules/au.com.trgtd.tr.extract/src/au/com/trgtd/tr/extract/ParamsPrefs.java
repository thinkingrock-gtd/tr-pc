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
package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Preferences for report and export parameters.
 *
 * @author Jeremy Moore
 */
public class ParamsPrefs {

    private static final Logger LOG = Logger.getLogger("tr.extract");

    private static final Preferences PREFS = Constants.getPrefs("extract/params");   

    /**
     * Gets the value for the parameter ID and key.
     * @param id The parameter ID.
     * @param key The parameter key.
     * @return The parameter value.
     */
    public static final String getParam(String id, String key) {
        return PREFS.get(id + "." + key, null);
    }
    
    /**
     * Sets the value for the parameter ID and key.
     * @param id The parameter ID.
     * @param key The parameter key.
     * @param value The parameter value.
     */
    public static final void setParam(String id, String key, String value) {
        if (key == null || key.trim().length() == 0) {
            return;
        }
        if (value == null) {
            PREFS.remove(key);
        } else {
            PREFS.put(id + "." + key, value);
        } 
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
