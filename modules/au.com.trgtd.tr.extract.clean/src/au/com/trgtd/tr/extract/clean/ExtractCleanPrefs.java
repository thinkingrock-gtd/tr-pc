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
package au.com.trgtd.tr.extract.clean;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.UtilsPrefs;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;

/**
 * Extract clean preferences.
 *
 * @author Jeremy Moore
 */
public class ExtractCleanPrefs {

    /** Clean interval days value for Every Startup */
    public static final int CLEAN_INTERVAL_STARTUP = 0;
    /** Clean interval days value for Never */
    public static final int CLEAN_INTERVAL_NEVER = Integer.MAX_VALUE;

    private static final Logger LOG = Logger.getLogger("tr.extract.clean");
    private static final String PATHNAME = "extract";

    private static final String KEY_CLEAN_LAST_DATE_MS = "clean.last.date.ms";
    private static final String KEY_CLEAN_INTERVAL_DAYS = "clean.interval.days";
    private static final String KEY_CLEAN_AGE_DAYS = "clean.age.days";

    private static final long DEF_CLEAN_LAST_DATE_MS = 0;
    private static final int DEF_CLEAN_INTERVAL_DAYS = CLEAN_INTERVAL_STARTUP;
    private static final int DEF_CLEAN_AGE_DAYS = 0;

    private static final ExtractCleanPrefs instance = new ExtractCleanPrefs();

    private Preferences prefs;

    private ExtractCleanPrefs() {
        try {
            Preferences newroot = Preferences.userRoot().node(Constants.PREFS_PATH_NEW);
            if (newroot.nodeExists(PATHNAME)) {
                // use existing
                prefs = newroot.node(PATHNAME);
            } else {
                // create new
                prefs = newroot.node(PATHNAME);
                // copy old preferences if they exist
                Preferences oldroot = Preferences.userRoot().node(Constants.PREFS_PATH_OLD);
                if (oldroot.nodeExists(PATHNAME)) {
                    Preferences oldprefs = oldroot.node(PATHNAME);
                    UtilsPrefs.copy(oldprefs, prefs, KEY_CLEAN_LAST_DATE_MS, DEF_CLEAN_LAST_DATE_MS);
                    UtilsPrefs.copy(oldprefs, prefs, KEY_CLEAN_INTERVAL_DAYS, DEF_CLEAN_INTERVAL_DAYS);
                    UtilsPrefs.copy(oldprefs, prefs, KEY_CLEAN_AGE_DAYS, DEF_CLEAN_AGE_DAYS);
                    prefs.flush();
                }
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Extract clean preferences error. {0}", ex.getMessage());
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Gets the value for the last clean date milliseconds preference.
     * @return The value.
     */
    public static final long getCleanLastDateMS() {
        return instance.prefs.getLong(KEY_CLEAN_LAST_DATE_MS, DEF_CLEAN_LAST_DATE_MS);
    }
    /**
     * Sets the value for the last clean date milliseconds preference.
     * @param value The value.
     */
    public static final void setCleanLastDateMS(long value) {
        instance.prefs.putLong(KEY_CLEAN_LAST_DATE_MS, value);
        flush();                
    }
    
    /**
     * Gets the value for the clean interval days preference.
     * @return The value.
     */
    public static final int getCleanIntervalDays() {
        return instance.prefs.getInt(KEY_CLEAN_INTERVAL_DAYS, DEF_CLEAN_INTERVAL_DAYS);
    }
    /**
     * Sets the value for the clean interval days preference.
     * @param value The value.
     */
    public static final void setCleanIntervalDays(int value) {
        instance.prefs.putInt(KEY_CLEAN_INTERVAL_DAYS, value);
        flush();                
    }
    
    /**
     * Gets the value for the clean file age days preference.
     * @return The value.
     */
    public static final int getCleanAgeDays() {
        return instance.prefs.getInt(KEY_CLEAN_AGE_DAYS, DEF_CLEAN_AGE_DAYS);
    }
    /**
     * Sets the value for the clean file age days preference.
     * @param value The value.
     */
    public static final void setCleanAgeDays(int value) {
        instance.prefs.putInt(KEY_CLEAN_AGE_DAYS, value);
        flush();                
    }

    private static final void flush() {
        try {
            instance.prefs.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Extract clean preferences error. {0}", ex.getMessage());
        }
    }

}
