/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.datastore.xstream;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.appl.UtilsPrefs;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.openide.util.Exceptions;

/**
 * Data manager singleton that uses XStream (http://xstream.codehaus.org/) to
 * persist the data model.
 *
 * @author Jeremy Moore
 */
public final class XStreamPrefs {

    private static final Logger LOG = Logger.getLogger("tr.xstream");
    private static final String PATHNAME = "xstream";

    private static final String KEY_DATAFILE = "data.file";
    private static final String DEF_DATAFILE = "";

    private static final XStreamPrefs instance = new XStreamPrefs();

    private Preferences prefs;

    private XStreamPrefs() {
        try {
            Preferences newroot = Preferences.userRoot().node(Constants.PREFS_PATH_NEW);
            if (newroot.nodeExists(PATHNAME)) {
                // use existing
                prefs = newroot.node(PATHNAME);
            } else {
                // create new
                prefs = newroot.node(PATHNAME);
                // copy old preferences if they exist
                Preferences oldroot = Preferences.userRoot().node(Constants.PREFS_PATH_OLD);
                if (oldroot.nodeExists(PATHNAME)) {
                    Preferences oldprefs = oldroot.node(PATHNAME);
                    UtilsPrefs.copy(oldprefs, prefs, KEY_DATAFILE, DEF_DATAFILE);
                    prefs.flush();
                }
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "XStream preferences error. {0}", ex.getMessage());
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Gets the data file path preference.
     * @return The data file path.
     */
    public static final String getPath() {
        return instance.prefs.get(KEY_DATAFILE, DEF_DATAFILE);
    }

    /**
     * Sets the data file path preference.
     * @param path The data file path.
     */
    public static final void setPath(String path) {
        if (path == null || path.trim().equals("")) {
            instance.prefs.remove(KEY_DATAFILE);
        } else {
            // if the file is in the user directory then just store the file name
            File file = new File(path);
            String userdir = System.getProperty("user.dir");
            if (userdir.equals(file.getParent())) {
                instance.prefs.put(KEY_DATAFILE, file.getName());
            } else {
                instance.prefs.put(KEY_DATAFILE, path);
            }
        }
        flush();
    }

    private static final void flush() {
        try {
            instance.prefs.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "XStream preferences exception. {0}", ex.getMessage());
        }
    }

}
