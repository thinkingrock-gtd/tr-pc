package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Preferences for report and export parameters.
 *
 * @author Jeremy Moore
 */
public class ParamsPrefs {

    private static final Logger LOG = Logger.getLogger("tr.extract");

    private static final Preferences PREFS = Constants.getPrefs("extract/params");   

    /**
     * Gets the value for the parameter ID and key.
     * @param id The parameter ID.
     * @param key The parameter key.
     * @return The parameter value.
     */
    public static final String getParam(String id, String key) {
        return PREFS.get(id + "." + key, null);
    }
    
    /**
     * Sets the value for the parameter ID and key.
     * @param id The parameter ID.
     * @param key The parameter key.
     * @param value The parameter value.
     */
    public static final void setParam(String id, String key, String value) {
        if (key == null || key.trim().length() == 0) {
            return;
        }
        if (value == null) {
            PREFS.remove(key);
        } else {
            PREFS.put(id + "." + key, value);
        } 
        flush();                        
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Extract preferences error. {0}", ex.getMessage());
        }
    }
    
}
