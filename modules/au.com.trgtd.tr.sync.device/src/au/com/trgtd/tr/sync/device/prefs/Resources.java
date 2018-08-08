package au.com.trgtd.tr.sync.device.prefs;

import java.util.MissingResourceException;
import org.openide.util.NbBundle;

/**
 * Resources.
 * @author Jeremy Moore
 */
public class Resources {
    /**
     * Gets the text for the given key from the resource Bundle.properties file.
     * @param key The key value.
     * @return the text value if the key exists, otherwise the key.
     */
    public static String getText(String key) {
        try {
            return NbBundle.getMessage(Resources.class, key);
        } catch (MissingResourceException x) {
            return key;
        }
    }
}
