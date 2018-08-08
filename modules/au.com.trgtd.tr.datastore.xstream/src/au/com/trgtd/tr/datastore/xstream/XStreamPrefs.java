package au.com.trgtd.tr.datastore.xstream;

import au.com.trgtd.tr.appl.Constants;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Data manager singleton that uses XStream (http://xstream.codehaus.org/) to
 * persist the data model.
 *
 * @author Jeremy Moore
 */
public final class XStreamPrefs {

    private static final Logger LOG = Logger.getLogger("tr.xstream");

    private static final Preferences PREFS = Constants.getPrefs("xstream");

    private static final String KEY_DATAFILE = "data.file";
    private static final String DEF_DATAFILE = "";

    /**
     * Gets the data file path preference.
     * @return The data file path.
     */
    public static final String getPath() {
        return PREFS.get(KEY_DATAFILE, DEF_DATAFILE);
    }

    /**
     * Sets the data file path preference.
     * @param path The data file path.
     */
    public static final void setPath(String path) {
        if (path == null || path.trim().equals("")) {
            PREFS.remove(KEY_DATAFILE);
        } else {
            // if the file is in the user directory then just store the file name
            File file = new File(path);
            String userdir = System.getProperty("user.dir");
            if (userdir.equals(file.getParent())) {
                PREFS.put(KEY_DATAFILE, file.getName());
            } else {
                PREFS.put(KEY_DATAFILE, path);
            }
        }
        flush();
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "XStream preferences exception. {0}", ex.getMessage());
        }
    }

}
