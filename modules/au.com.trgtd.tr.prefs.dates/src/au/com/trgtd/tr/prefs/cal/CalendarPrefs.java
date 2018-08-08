package au.com.trgtd.tr.prefs.cal;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User preferences for the Calendar views.
 *
 * @author Jeremy Moore
 */
public class CalendarPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.calendar");
    
    private static final Preferences PREFS = Constants.getPrefs("calendarview");      

    public static final int HOUR_DISPLAY_AMPM = 12;
    public static final int HOUR_DISPLAY_24HR = 24;

    private static final String KEY_HOUR_DISPLAY = "hour.diaplay";
    private static final int DEF_HOUR_DISPLAY = HOUR_DISPLAY_AMPM;


    public static boolean isHourDisplay24hr() {
        return getHourDisplay() == CalendarPrefs.HOUR_DISPLAY_24HR;
    }

    public static boolean isHourDisplayAMPM() {
        return getHourDisplay() != CalendarPrefs.HOUR_DISPLAY_24HR;
    }

    /**
     * Gets the value for the hour display preference.
     * @return The value.
     */
    public static int getHourDisplay() {
        return PREFS.getInt(KEY_HOUR_DISPLAY, DEF_HOUR_DISPLAY);
    }
    
    /**
     * Sets the value for the hour display preference.
     * @param value The value.
     */
    public static void setHourDisplay(int value) {
        if (value == HOUR_DISPLAY_AMPM || value == HOUR_DISPLAY_24HR) {
            PREFS.putInt(KEY_HOUR_DISPLAY, value);
        } else {
            PREFS.putInt(KEY_HOUR_DISPLAY, DEF_HOUR_DISPLAY);
        }
        flush();
    }
    
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Calendar preferences error. {0}", ex.getMessage());
        }
    }
    
}
