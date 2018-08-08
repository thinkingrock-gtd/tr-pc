package au.com.trgtd.tr.appl.prefs;

import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import au.com.trgtd.tr.appl.Constants;
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

    /**
     * Gets the value for the user has accepted the license preference.
     * @return The value.
     */
    public static final boolean isLicenceAccepted() {
        return PREFS.getBoolean(KEY_LICENSE_ACCEPTED, DEF_LICENSE_ACCEPTED);
    }
    /**
     * Sets the value for the user has accepted the license preference.
     * @param value The value.
     */
    public static final void setLicenceAccepted(boolean value) {
        PREFS.putBoolean(KEY_LICENSE_ACCEPTED, value);
        flush();
    }
    
    /**
     * Gets the value for the version check last time preference.
     * @return The value.
     */
    public static final long getCheckVersionLastTime() {
        return PREFS.getLong(KEY_VERSION_CHECK_LAST_TIME, DEF_VERSION_CHECK_LAST_TIME);
    }
    /**
     * Sets the value for the version check last time preference.
     * @param value The value.
     */
    public static final void setCheckVersionLastTime(long value) {
        PREFS.putLong(KEY_VERSION_CHECK_LAST_TIME, value);
        flush();
    }
    
    /**
     * Gets the value for the version check period preference.
     * @return The value.
     */
    public static final int getVersionCheckPeriod() {
        return PREFS.getInt(KEY_VERSION_CHECK_PERIOD, DEF_VERSION_CHECK_PERIOD);
    }
    /**
     * Sets the value for the version check period preference.
     * @param value The value.
     */
    public static final void setVersionCheckPeriod(int value) {
        PREFS.putInt(KEY_VERSION_CHECK_PERIOD, value);
        flush();
    }
   
    /**
     * Gets the value for the last message check date preference.
     * @return The value.
     */
    public static final Date getLastMessageCheckDate() {
        return new Date(PREFS.getLong(KEY_MESSAGE_CHECK_LAST_TIME, DEF_MESSAGE_CHECK_LAST_TIME));
    }
    /**
     * Sets the value for the version check period preference.
     * @param date
     */
    public static final void setMessageCheckDate(Date date) {
        PREFS.putLong(KEY_MESSAGE_CHECK_LAST_TIME, date == null ? 0 : date.getTime());
        flush();
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "General preferences error. {0}", ex.getMessage());
        }
    }
    
}
