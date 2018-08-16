package au.com.trgtd.tr.appl.prefs;

import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.tasks.idlequit.IdleQuitTaskScheduler;
import au.com.trgtd.tr.util.DateUtils;
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

    private static final String KEY_LICENSE_ACCEPTED = "license.accepted";
    private static final boolean DEF_LICENSE_ACCEPTED = false;
    private static final String KEY_VERSION_CHECK_LAST_TIME = "version.check.last.time";
    private static final long DEF_VERSION_CHECK_LAST_TIME = 0;
    private static final String KEY_VERSION_CHECK_PERIOD = "version.check.period";
    private static final int DEF_VERSION_CHECK_PERIOD = VERSION_CHECK_PERIOD_FORTNIGHT;
    private static final String KEY_MESSAGE_CHECK_LAST_TIME = "message.check.last.time";
    private static final long DEF_MESSAGE_CHECK_LAST_TIME = 0;
    private static final String KEY_IDLE_QUIT_MS = "idle.quit.ms";

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
     * Gets the idle quit preference value as total milliseconds.
     *
     * @return The number of milliseconds (0 if not used).
     */
    public static final long getIdleQuitMs() {
        return PREFS.getLong(KEY_IDLE_QUIT_MS, 0);
    }

    /** 
     * Gets the idle quit preference value minutes (excluding hours).
     *
     * @return The number of minutes (0 if not used).
     */
    public static final int getIdleQuitMins() {
        return (int)((getIdleQuitMs() % DateUtils.MS_PER_HR) / DateUtils.MS_PER_MIN);
    }

    /**
     * Gets the idle quit preference value hours.
     *
     * @return The number of hours (0 if not used).
     */
    public static final int getIdleQuitHrs() {
        return (int)(getIdleQuitMs() / DateUtils.MS_PER_HR);
    }

    /**
     * Sets the idle quit preference value as total milliseconds.
     *
     * @param ms The total number of milliseconds (0 if not used).
     */
    public static final void setIdleQuitMs(long ms) {
        long oldValue = getIdleQuitMs();
        long newValue = ms < 0 ? 0 : ms;
        if (newValue == oldValue) {
            return;
        }
        
        PREFS.putLong(KEY_IDLE_QUIT_MS, newValue);
        flush();
        
        if (newValue == 0) {
            IdleQuitTaskScheduler.instance().stop();
        } else {
            IdleQuitTaskScheduler.instance().start(newValue);
        }        
    }

    /**
     * Sets the idle quit preference value.
     *
     * @param hrs The number of hours (0 if not used).
     * @param mins The number of minutes (0 if not used).
     */
    public static final void setIdleQuit(int hrs, int mins) {
        hrs = hrs < 0 ? 0 : hrs;
        mins = mins < 0 ? 0 : mins;        
        setIdleQuitMs(hrs * DateUtils.MS_PER_HR + mins * DateUtils.MS_PER_MIN);
    }

    /**
     * Is the idle quit preference used?
     *
     * @return true if the number of milliseconds is greater than 0.
     */
    public static final boolean isIdleQuitUsed() {
        return PREFS.getLong(KEY_IDLE_QUIT_MS, 0) > 0;
    }

    /**
     * Set the idle quit preference as not used.
     */
    public static final void setIdleQuitNotUsed() {
        setIdleQuitMs(0);
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "General preferences error. {0}", ex.getMessage());
        }
    }

}
