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
package au.com.trgtd.tr.sync.iphone;

import au.com.trgtd.tr.appl.Constants;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * User preferences for the iPhone sync.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class SyncPrefs {
    
    private static final Logger LOG = Logger.getLogger("tr.sync.iphone.prefs");
    
    private static final Preferences PREFS = Constants.getPrefs("iphonesync");      

    public static final String KEY_PORT = "port";
    public static final int DEF_PORT = 5000;
    
    public static final String KEY_LAST_ADDR = "last.addr";
    public static final String KEY_LAST_NAME = "last.name";

    public static final int SYNC_METHOD_WIFI = 1;
    public static final int SYNC_METHOD_DBOX = 2;
    public static final String KEY_SYNC_METHOD = "sync.method";
    public static final int DEF_SYNC_METHOD = SYNC_METHOD_WIFI;
    
    public static final String KEY_DBOX_PATH = "dropbox.path";

    /**
     * Gets the value for the port preference.
     * @return The value.
     */
    public static int getPort() {
        return PREFS.getInt(KEY_PORT, DEF_PORT);
    }
    
    /**
     * Sets the value for the port preference.
     * @param value The value.
     */
    public static void setPort(int value) {
        PREFS.putInt(KEY_PORT, value);
        flush();
    }

    /**
     * Gets the value for the last used IP address.
     * @return The value.
     */
    public static String getLastAddr() {
        String addr = PREFS.get(KEY_LAST_ADDR, "");
        return addr == null ? "" : addr;
    }

    /**
     * Sets the value for the last used IP address.
     * @param value The value.
     */
    public static void setLastAddr(String value) {
        PREFS.put(KEY_LAST_ADDR, value);
        flush();
    }

    /**
     * Gets the value for the last used address name.
     * @return The value.
     */
    public static String getLastName() {
        String name = PREFS.get(KEY_LAST_NAME, "");
        return name == null ? "" : name;
    }

    /**
     * Sets the value for the last used address name.
     * @param value The value.
     */
    public static void setLastName(String value) {
        PREFS.put(KEY_LAST_NAME, value);
        flush();
    }

    /**
     * Gets the value for the sync method preference.
     * @return The value.
     */
    public static int getMethod() {
        return PREFS.getInt(KEY_SYNC_METHOD, DEF_SYNC_METHOD);
    }

    /**
     * Sets the value for the sync method preference.
     * @param value The value.
     */
    public static void setMethod(int value) {
        if (value == SYNC_METHOD_WIFI || 
            value == SYNC_METHOD_DBOX)
        {
            PREFS.putInt(KEY_SYNC_METHOD, value);
            flush();
        } else {
            LOG.log(Level.WARNING, "Invalid sync method preference.");
        }
    }

    /**
     * Gets the value for the DropBox path preference.
     * @return The value.
     */
    public static String getDropBoxPath() {
        return PREFS.get(KEY_DBOX_PATH, null);
    }

    /**
     * Sets the value for the DropBox path preference.
     * @param value The value.
     */
    public static void setDropBoxPath(String value) {
        if (new File(value).isDirectory()) {
            PREFS.put(KEY_DBOX_PATH, value);
            flush();
        } else {
            LOG.log(Level.WARNING, "Invalid Dropbox path preference.");
        }
    }

    public static void addListener(PreferenceChangeListener pcl) {
        PREFS.addPreferenceChangeListener(pcl);
    }

    public static void removeListener(PreferenceChangeListener pcl) {
        PREFS.removePreferenceChangeListener(pcl);
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Sync preferences error. {0}", ex.getMessage());
        }
    }
    
}
