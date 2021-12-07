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
package au.com.trgtd.tr.appl.prefs;

import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.InactivityDetector;
import java.util.Date;
import java.util.logging.Level;

/**
 * General preferences.
 *
 * @author Jeremy Moore
 */
public class ApplicationPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.general");

    private static final Preferences PREFS = Constants.getPrefs("general");

    public static final int VERSION_CHECK_PERIOD_STARTUP = 0;
    public static final int VERSION_CHECK_PERIOD_DAY = 1;
    public static final int VERSION_CHECK_PERIOD_WEEK = 2;
    public static final int VERSION_CHECK_PERIOD_FORTNIGHT = 3;
    public static final int VERSION_CHECK_PERIOD_MONTH = 4;
    public static final int VERSION_CHECK_PERIOD_NEVER = 5;

    public static final int MS_PER_MIN = 60000;     // 1000 * 60
    public static final int MS_PER_HR = 3600000;    // 1000 * 60 * 60 
    
    private static final String KEY_LICENSE_ACCEPTED = "license.accepted";
    private static final boolean DEF_LICENSE_ACCEPTED = false;
    private static final String KEY_VERSION_CHECK_LAST_TIME = "version.check.last.time";
    private static final long DEF_VERSION_CHECK_LAST_TIME = 0;
    private static final String KEY_VERSION_CHECK_PERIOD = "version.check.period";
    private static final int DEF_VERSION_CHECK_PERIOD = VERSION_CHECK_PERIOD_FORTNIGHT;
    private static final String KEY_MESSAGE_CHECK_LAST_TIME = "message.check.last.time";
    private static final long DEF_MESSAGE_CHECK_LAST_TIME = 0;
    private static final String KEY_INACTIVITY_MS = "inactivity.ms";

    /**
     * Gets the value for the user has accepted the license preference.
     *
     * @return The value.
     */
    public static final boolean isLicenceAccepted() {
        return PREFS.getBoolean(KEY_LICENSE_ACCEPTED, DEF_LICENSE_ACCEPTED);
    }

    /**
     * Sets the value for the user has accepted the license preference.
     *
     * @param value The value.
     */
    public static final void setLicenceAccepted(boolean value) {
        PREFS.putBoolean(KEY_LICENSE_ACCEPTED, value);
        flush();
    }

    /**
     * Gets the value for the version check last time preference.
     *
     * @return The value.
     */
    public static final long getCheckVersionLastTime() {
        return PREFS.getLong(KEY_VERSION_CHECK_LAST_TIME, DEF_VERSION_CHECK_LAST_TIME);
    }

    /**
     * Sets the value for the version check last time preference.
     *
     * @param value The value.
     */
    public static final void setCheckVersionLastTime(long value) {
        PREFS.putLong(KEY_VERSION_CHECK_LAST_TIME, value);
        flush();
    }

    /**
     * Gets the value for the version check period preference.
     *
     * @return The value.
     */
    public static final int getVersionCheckPeriod() {
        return PREFS.getInt(KEY_VERSION_CHECK_PERIOD, DEF_VERSION_CHECK_PERIOD);
    }

    /**
     * Sets the value for the version check period preference.
     *
     * @param value The value.
     */
    public static final void setVersionCheckPeriod(int value) {
        PREFS.putInt(KEY_VERSION_CHECK_PERIOD, value);
        flush();
    }

    /**
     * Gets the value for the last message check date preference.
     *
     * @return The value.
     */
    public static final Date getLastMessageCheckDate() {
        return new Date(PREFS.getLong(KEY_MESSAGE_CHECK_LAST_TIME, DEF_MESSAGE_CHECK_LAST_TIME));
    }

    /**
     * Sets the value for the version check period preference.
     *
     * @param date
     */
    public static final void setMessageCheckDate(Date date) {
        PREFS.putLong(KEY_MESSAGE_CHECK_LAST_TIME, date == null ? 0 : date.getTime());
        flush();
    }

    /**
     * Gets the allowed inactivity preference value as milliseconds.
     *
     * @return The total number of milliseconds (0 if not used).
     */
    public static final int getInactivityMs() {
        return PREFS.getInt(KEY_INACTIVITY_MS, 0);
    }

    /** 
     * Gets the allowed inactivity preference number of minutes (excluding whole hours).
     *
     * @return The number of minutes (0 if not used).
     */
    public static final int getInactivityMinutes() {
        return (getInactivityMs() % MS_PER_HR) / MS_PER_MIN;
    }

    /**
     * Gets the allowed inactivity preference number of whole hours.
     *
     * @return The number of whole hours (0 if not used).
     */
    public static final int getInactivityHours() {
        return getInactivityMs() / MS_PER_HR;
    }

    /*
     * Sets the allowed inactivity preference value.
     *
     * @param ms The number of milliseconds (0 if not used).
     */
    private static void setInactivityMs(int ms) {
        int oldValue = getInactivityMs();
        int newValue = ms < 0 ? 0 : ms;
        if (newValue == oldValue) {
            return;
        }
        
        PREFS.putInt(KEY_INACTIVITY_MS, newValue);
        flush();
        
        if (oldValue > 0) {
            InactivityDetector.stop();            
        }
        if (newValue > 0) {
            InactivityDetector.start(newValue);
        }        
    }

    /**
     * Sets the allowed inactivity preference value as hours and minutes.
     *
     * @param hrs The number of whole hours (0 if not used).
     * @param mins The number of additional minutes (0 if not used).
     */
    public static final void setInactivity(int hrs, int mins) {
        setInactivityMs(hrs * MS_PER_HR + mins * MS_PER_MIN);
    }

    /**
     * Is the allowed inactivity preference used?
     *
     * @return true if the number of milliseconds is greater than 0.
     */
    public static final boolean isInactivityUsed() {
        return PREFS.getInt(KEY_INACTIVITY_MS, 0) > 0;
    }

    /**
     * Set the allowed inactivity preference as not used.
     */
    public static final void setInactivityNotUsed() {
        setInactivityMs(0);
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "General preferences error. {0}", ex.getMessage());
        }
    }

}
