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

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User preferences for internationalization.
 *
 * @author Jeremy Moore
 */
public class SaveToPrefs {

    private static final Logger LOG = Logger.getLogger("tr.data");

    private static final Preferences PREFS = Constants.getPrefs("file");

    // Default file path key.
    static final String KEY_SAVE_TO_PATH = "saveto.path";
    // Default file path default.
    static final String DEF_SAVE_TO_PATH = "";

    /**
     * Gets the value for the default file path.
     * @return The value.
     */
    public static final String getSaveToPath() {
        return PREFS.get(KEY_SAVE_TO_PATH, DEF_SAVE_TO_PATH);
    }

    /**
     * Sets the value for the default file path.
     * @param value The value.
     */
    public static final void setSaveToPath(String value) {
        PREFS.put(KEY_SAVE_TO_PATH, value);
        flush();
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Save to preferences error. {0}", ex.getMessage());
        }
    }

}
