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
