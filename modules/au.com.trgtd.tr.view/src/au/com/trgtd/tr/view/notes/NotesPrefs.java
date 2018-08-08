package au.com.trgtd.tr.view.notes;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Preferences for notes.
 */
public class NotesPrefs {

    private static final Logger LOG = Logger.getLogger("tr.view.notes.prefs");

    private static final Preferences PREFS = Constants.getPrefs("notes");
    
    private static final String KEY_LAST_FILE_PATH = "last.file.path";
    
    /**
     * Gets the preference value for the last used file path.
     * @return The value.
     */
    public static final String getLastFilePath() {
        return PREFS.get(KEY_LAST_FILE_PATH, null);
    }

    /**
     * Sets the preference value for the last used file path.
     * @param value The value.
     */
    public static final void setLastFilePath(String value) {
        PREFS.put(KEY_LAST_FILE_PATH, value);
        flush();
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.WARNING, "Notes preferences error. {0}", ex.getMessage());
        }
    }

}
