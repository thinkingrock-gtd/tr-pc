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
package au.com.trgtd.tr.prefs.data;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import au.com.trgtd.tr.util.DateUtils;
import java.util.logging.Level;

/**
 * Data preferences.
 *
 * @author Jeremy Moore
 */
public class DataPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.data");

    public static final Preferences PREFS = Constants.getPrefs("data");      

    public static final String KEY_RECOVERY_PATH = "recovery.path";
    public static final String KEY_AUTOSAVE_MS = "autosave.ms";
    public static final String KEY_RECOVERY_KEEP_ALL = "recovery.keep.all";
    public static final String KEY_RECOVERY_KEEP_NBR = "recovery.keep.nbr";
    public static final String KEY_BACKUP_INTERVAL_DAYS = "backup.interval.days";
    public static final String KEY_BACKUP_PATH = "backup.path";
    public static final String KEY_BACKUP_KEEP_ALL = "backup.keep.all";
    public static final String KEY_BACKUP_KEEP_NBR = "backup.keep.nbr";

    public static final String DEF_RECOVERY_PATH = "";
    public static final Long DEF_AUTOSAVE_MS = 5 * DateUtils.MS_PER_MIN;
    public static final boolean DEF_RECOVERY_KEEP_ALL = false;
    public static final int DEF_RECOVERY_KEEP_NBR = 10;
    public static final int DEF_BACKUP_INTERVAL_DAYS = 0;
    public static final String DEF_BACKUP_PATH = "";
    public static final boolean DEF_BACKUP_KEEP_ALL = false;
    public static final int DEF_BACKUP_KEEP_NBR = 10;

    public static final int BACKUP_INTERVAL_STARTUP = 0;
    public static final int BACKUP_INTERVAL_NEVER = Integer.MAX_VALUE;


    /**
     * Gets the value for the recovery folder path preference.
     * @return The value.
     */
    public static final String getRecoveryPath() {
        return PREFS.get(KEY_RECOVERY_PATH, DEF_RECOVERY_PATH);
    }

    /**
     * Sets the value for the recovery folder path preference.
     * @param value The value.
     */
    public static final void setRecoveryPath(String value) {
        PREFS.put(KEY_RECOVERY_PATH, value);
        flush();
    }

    /**
     * Gets the value for the automatic save interval (in milliseconds).
     * preference.
     * @return The value.
     */
    public static final long getAutoSaveIntervalMS() {
        return PREFS.getLong(KEY_AUTOSAVE_MS, DEF_AUTOSAVE_MS);
    }

    /**
     * Sets the value for the automatic save interval (in milliseconds).
     * @param value The value.
     */
    public static final void setAutoSaveIntervalMS(long value) {
        PREFS.putLong(KEY_AUTOSAVE_MS, value);
        flush();
    }

    /**
     * Gets the value for the keep all recovery files preference.
     * @return The value.
     */
    public static final boolean isKeepAllRecoveryFiles() {
        return PREFS.getBoolean(KEY_RECOVERY_KEEP_ALL, DEF_RECOVERY_KEEP_ALL);
    }

    /**
     * Sets the value for the keep all recovery files preference.
     * @param value The value.
     */
    public static final void setKeepAllRecoveryFiles(boolean value) {
        PREFS.putBoolean(KEY_RECOVERY_KEEP_ALL, value);
        flush();
    }


    /**
     * Gets the value for the number of recovery files to keep preference.
     * @return The value.
     */
    public static final int getKeepNbrRecoveryFiles() {
        return PREFS.getInt(KEY_RECOVERY_KEEP_NBR, DEF_RECOVERY_KEEP_NBR);
    }

    /**
     * Sets the value for the number of recovery files to keep preference.
     * @param value The value.
     */
    public static final void setKeepNbrRecoveryFiles(int value) {
        PREFS.putInt(KEY_RECOVERY_KEEP_NBR, value);
        flush();
    }

    /**
     * Gets the value for the backup interval days preference.
     * @return The value.
     */
    public static final int getBackupIntervalDays() {
        return PREFS.getInt(KEY_BACKUP_INTERVAL_DAYS, DEF_BACKUP_INTERVAL_DAYS);
    }
    /**
     * Sets the value for the backup interval days preference.
     * @param value The value.
     */
    public static final void setBackupIntervalDays(int value) {
        PREFS.putInt(KEY_BACKUP_INTERVAL_DAYS, value);
        flush();
    }

    /**
     * Gets the value for the backup folder path preference.
     * @return The value.
     */
    public static final String getBackupPath() {
        return PREFS.get(KEY_BACKUP_PATH, DEF_BACKUP_PATH);
    }

    /**
     * Sets the value for the backup folder path preference.
     * @param value The value.
     */
    public static final void setBackupPath(String value) {
        PREFS.put(KEY_BACKUP_PATH, value);
        flush();
    }

    /**
     * Gets the value for the keep all backup files preference.
     * @return The value.
     */
    public static final boolean isKeepAllBackupFiles() {
        return PREFS.getBoolean(KEY_BACKUP_KEEP_ALL, DEF_BACKUP_KEEP_ALL);
    }

    /**
     * Sets the value for the keep all backup files preference.
     * @param value The value.
     */
    public static final void setKeepAllBackupFiles(boolean value) {
        PREFS.putBoolean(KEY_BACKUP_KEEP_ALL, value);
        flush();
    }


    /**
     * Gets the value for the number of backup files to keep preference.
     * @return The value.
     */
    public static final int getKeepNbrBackupFiles() {
        return PREFS.getInt(KEY_BACKUP_KEEP_NBR, DEF_BACKUP_KEEP_NBR);
    }

    /**
     * Sets the value for the number of backup files to keep preference.
     * @param value The value.
     */
    public static final void setKeepNbrBackupFiles(int value) {
        PREFS.putInt(KEY_BACKUP_KEEP_NBR, value);
        flush();
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Data preferences error. {0}", ex.getMessage());
        }
    }
    
}
