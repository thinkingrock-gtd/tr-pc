package au.com.trgtd.tr.archive;

import au.com.trgtd.tr.appl.Constants;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Archive preferences.
 *
 * @author jeremyimoore@yahoo.com.au
 */
public class ArchivePrefs {

    private static final Logger LOG = Logger.getLogger("tr.archive");
    
    private static final Preferences PREFS = Constants.getPrefs("archive");

    private static final String KEY_PATH = "path"; // No I18N
    private static final String KEY_DATE = "date"; // No I18N
    private static final String KEY_DONE_PROJECTS_ONLY = "done.projects.only"; // No I18N

    private static final String DEF_PATH = ""; // No I18N
    private static final long DEF_DATE = -1;
    private static final boolean DEF_DONE_PROJECTS_ONLY = true;
    
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
     * Gets the value for the date option preference.
     * @return The value.
     */
    public static final Date getDate() {
        long ms = PREFS.getLong(KEY_DATE, DEF_DATE);
        if (ms == DEF_DATE) {
            return null;
        }        
        return new Date(ms);
    }

    /**
     * Sets the value for the date option preference.
     * @param value The value.
     */
    public static final void setDate(Date date) {
        PREFS.putLong(KEY_DATE, (date == null) ? DEF_DATE : date.getTime());
        flush();        
    }

    
    /**
     * Gets the value for the archive done projects only preference.
     * @return The value.
     */
    public static final boolean isDoneProjectsOnly() {
        return PREFS.getBoolean(KEY_DONE_PROJECTS_ONLY, DEF_DONE_PROJECTS_ONLY);
    }

    /**
     * Sets the value for the archive done projects only preference.
     * @param value The value.
     */
    public static final void setDoneProjectsOnly(boolean b) {
        PREFS.putBoolean(KEY_DONE_PROJECTS_ONLY, b);
        flush();        
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Archive preferences error. {0}", ex.getMessage()); // No I18N
        }
    }
    
}
