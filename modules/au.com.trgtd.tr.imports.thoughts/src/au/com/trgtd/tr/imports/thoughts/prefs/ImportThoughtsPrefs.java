package au.com.trgtd.tr.imports.thoughts.prefs;

import au.com.trgtd.tr.appl.Constants;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Preferences for import thoughts.
 *
 * @author Jeremy Moore
 */
public class ImportThoughtsPrefs {

    private static final Logger LOG = Logger.getLogger("tr.import.thoughts");

    private static final Preferences PREFS = Constants.getPrefs("import/thoughts");      
    
    private static final String KEY_WARNING_FILE_SIZE = "warning.file.size";
    private static final String KEY_DEFAULT_FILE_PATH = "default.file.path";
    private static final String KEY_ENCODING = "thoughts.encoding";

    private static final int DEF_WARNING_FILE_SIZE = 4000;
    private static final String DEF_DEFAULT_FILE_PATH = System.getProperty("user.home") + File.separator + "thoughts.txt";
    private static final String DEF_ENCODING = "";

    /**
     * Gets the value for the warning file size.
     * @return The value.
     */
    public static final int getWarningFileSize() {
        return PREFS.getInt(KEY_WARNING_FILE_SIZE, DEF_WARNING_FILE_SIZE );
    }
    
    /**
     * Sets the value for the warning file size.
     * @param value The value.
     */
    public static final void setWarningFileSize(int value) {
        PREFS.putInt(KEY_WARNING_FILE_SIZE, value);
        flush();
    }
    
    /**
     * Gets the value for the default file path.
     * @return The value.
     */
    public static final String getDefaultFilePath() {
        return PREFS.get(KEY_DEFAULT_FILE_PATH, DEF_DEFAULT_FILE_PATH);
    }
    
    /**
     * Sets the value for the default file path.
     * @param value The value.
     */
    public static final void setDefaultFilePath(String value) {
        PREFS.put(KEY_DEFAULT_FILE_PATH, value);
        flush();
    }
    
    /**
     * Gets the value for encoding.
     * @return The value.
     */
    public static final String getEncoding() {
        return PREFS.get(KEY_ENCODING, DEF_ENCODING);
    }
    
    /**
     * Sets the value for encoding.
     * @param value The value.
     */
    public static final void setEncoding(String value) {
        PREFS.put(KEY_ENCODING, (value == null) ? "" : value.trim());
        flush();
    }
    
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Import thoughts preferences error. {0}", ex.getMessage());
        }
    }
    
}
