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
package au.com.trgtd.tr.sync.device.prefs;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * Android sync user preferences. Note that this class is for storage and access
 * to the user preferences only (and is package private) - application logic 
 * related to these user preferences is obtained via the SyncPrefsADB, 
 * SyncPrefsWiFi and SyncPrefsDbx classes.
 * 
 * @author Jeremy Moore
 */
class SyncPrefs {
    
    private static final Logger LOG = Logger.getLogger("tr.sync.device.prefs");

    private static final Preferences PREFS = Constants.getPrefs("devicesync");
    
    static final int PORT_MIN_DYNAMIC = 49152;
    static final int PORT_MAX_DYNAMIC = 65535;    

    static final String KEY_SYNC_ANDROID = "android.sync";
    static final boolean DEF_SYNC_ANDROID = false;    
   
    static final String KEY_SYNC_WIFI = "android.sync.wifi";
    static final boolean DEF_SYNC_WIFI = false;    
    
    static final String KEY_SYNC_USB = "android.sync.usb";
    static final boolean DEF_SYNC_USB = false;    
    
    static final String KEY_WIFI_PORT = "android.wifi.port";
    static final int DEF_WIFI_PORT = 64641;
    
    static final String KEY_USB_PORT = "android.usb.port";
    static final int DEF_USB_PORT = 64642;
    
    static final String KEY_ADB_PATH = "adb.path";

    static final String KEY_SYNC_DBX = "android.sync.dbx";
    static final boolean DEF_SYNC_DBX = true;   
    
    static final String KEY_DBX_PATH = "dbx.path";
            
    /**
     * Sets the value for the sync method preference.
     * @param value The value.
     */
    static void setSyncAndroid(boolean value) {
        PREFS.putBoolean(KEY_SYNC_ANDROID, value);
        flush();
    }
    static boolean isSyncAndroid() {
        return PREFS.getBoolean(KEY_SYNC_ANDROID, DEF_SYNC_ANDROID);
    }

    
    /**
     * Sets the value for the "Sync using DropBox" preference.
     * @param value The value.
     */
    static void setSyncDbx(boolean value) {
        PREFS.putBoolean(KEY_SYNC_DBX, value);
        flush();
    }

    /**
     * Gets the value for the "Sync using DropBox" preference.
     * @return the value/
     */
    static boolean isSyncDbx() {
        return PREFS.getBoolean(KEY_SYNC_DBX, DEF_SYNC_DBX);
    }
    
    /**
     * Gets the value for the DropBox path preference.
     * @return The value or null if the preference does not exist.
     */
    static String getDbxPath() {
        return PREFS.get(KEY_DBX_PATH, null);
    }
    /**
     * Sets the value for the DropBox path preference. If value is null or empty 
     * the preference will be removed.
     */
    static void setDbxPath(String value) {
        if (value == null || value.trim().length() == 0) {
            remove(KEY_DBX_PATH);
            flush();
        } else {
            PREFS.put(KEY_DBX_PATH, value.trim());
            flush();
        }
    }
    
    /**
     * Sets the value for the "Sync using WiFi" preference.
     * @param value The value.
     */
    static void setSyncWiFi(boolean value) {
        PREFS.putBoolean(KEY_SYNC_WIFI, value);
        flush();
    }

    /**
     * Gets the value for the "Sync using WiFi" preference.
     * @return the value/
     */
    static boolean isSyncWiFi() {
        return PREFS.getBoolean(KEY_SYNC_WIFI, DEF_SYNC_WIFI);
    }

    /**
     * Sets the value for the "Sync using USB" preference.
     * @param value The value.
     */
    static void setSyncUSB(boolean value) {
        PREFS.putBoolean(KEY_SYNC_USB, value);
        flush();
    }

    /**
     * Gets the value for the "Sync using USB" preference.
     * @return the value/
     */
    static boolean isSyncUSB() {
        return PREFS.getBoolean(KEY_SYNC_USB, DEF_SYNC_USB);
    }
        
    /**
     * Gets the value for the ADB path preference.
     * @return The value or null if the preference does not exist.
     */
    static String getADBPath() {
        return PREFS.get(KEY_ADB_PATH, null);
    }
    /**
     * Sets the value for the ADB path preference. If value is null or empty the
     * preference will be removed.
     */
    static void setADBPath(String value) {
        if (value == null || value.trim().length() == 0) {
            remove(KEY_ADB_PATH);
            flush();
        } else {
            PREFS.put(KEY_ADB_PATH, value.trim());
            flush();
        }
    }
    
    /**
     * Gets the value for the WiFi port preference.
     * @return The value.
     */
    static int getWiFiPort() {
        return PREFS.getInt(KEY_WIFI_PORT, DEF_WIFI_PORT);
    }    
    /**
     * Sets the value for the WiFi port preference.
     * @param value The value.
     */
    static void setWiFiPort(int value) {
        PREFS.putInt(KEY_WIFI_PORT, value);
        flush();
    }

    /**
     * Gets the value for the USB port preference.
     * @return The value.
     */
    static int getUSBPort() {
        return PREFS.getInt(KEY_USB_PORT, DEF_USB_PORT);
    }
    /**
     * Sets the value for the USB port preference.
     * @param value The value.
     */
    static void setUSBPort(int value) {
        PREFS.putInt(KEY_USB_PORT, value);
        flush();
    }

    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Sync prefs error. {0}", ex.getMessage());
        }
    }

    private static void remove(String key) {
        try {
            PREFS.remove(key);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Sync prefs error. {0}", ex.getMessage());
        }
    }
    
    public static void addListener(PreferenceChangeListener pcl) {
        PREFS.addPreferenceChangeListener(pcl);
    }

    public static void removeListener(PreferenceChangeListener pcl) {
        PREFS.removePreferenceChangeListener(pcl);
    }

}
