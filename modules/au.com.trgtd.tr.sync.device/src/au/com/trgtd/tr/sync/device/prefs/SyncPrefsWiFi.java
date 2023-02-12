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

import static au.com.trgtd.tr.sync.device.prefs.SyncPrefs.PORT_MAX_DYNAMIC;
import static au.com.trgtd.tr.sync.device.prefs.SyncPrefs.PORT_MIN_DYNAMIC;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Application logic for a snapshot of the Android WiFi synchronization user
 * preferences.
 *
 * @author Jeremy Moore
 */
public final class SyncPrefsWiFi {

    private static final Object SOURCE = new Object();
    private static final PropertyChangeSupport PCS = new PropertyChangeSupport(SOURCE);
    
    public static void addListener(PropertyChangeListener listener) {
        PCS.addPropertyChangeListener(listener);
    }
    
    public static void removeListener(PropertyChangeListener listener) {
        PCS.removePropertyChangeListener(listener);
    }
    
    static {
        SyncPrefs.addListener((PreferenceChangeEvent evt) -> {
            if (evt.getKey().equals(SyncPrefs.KEY_SYNC_ANDROID)
                    || evt.getKey().equals(SyncPrefs.KEY_SYNC_WIFI)
                    || evt.getKey().equals(SyncPrefs.KEY_WIFI_PORT)) {
                PCS.firePropertyChange("changed", false, true);
            }            
        });        
    }
    
    /**
     * Gets the current WiFi sync user preferences.
     * @return The current ADB sync user preferences.
     */
    public static SyncPrefsWiFi getCurrent() {
        return new SyncPrefsWiFi(SyncPrefs.isSyncAndroid() && SyncPrefs.isSyncWiFi(),
                                 SyncPrefs.getWiFiPort());
    }

    private final boolean sync; // sync using WiFi
    private final int port;     // WiFi port to use

    /**
     * Constructor.
     * @param sync Whether or not to sync using WiFi.
     * @param port The port to use.
     */
    private SyncPrefsWiFi(boolean sync, int port) {
        this.sync = sync;
        this.port = port;
    }

    /**
     * Determines whether synchronization using WiFi should be done.
     * @return true if the user preferences are set to do WiFi sync and the 
     * port number is valid.
     */
    public boolean on() {
        return sync && isValidPort();
    }

    /**
     * Determines whether synchronization using WiFi should not be done.
     * @return true if not on.
     */
    public boolean off() {
        return !on();
    }

    /**
     * Determines whether a valid port is specified.
     * @return true if there is a valid port set in the user preferences. The
     * port must be in the range 49152..65535.
     */
    public boolean isValidPort() {
        return port >= PORT_MIN_DYNAMIC && port <= PORT_MAX_DYNAMIC;
    }

    /**
     * Gets the WiFi port number from the user preferences.
     * @return The port number user preference.
     */
    public int getWiFiPort() {
        return port;
    }

    @Override
    public String toString() {
        return "SyncPrefsWiFi{" + "sync=" + sync + ", port=" + port + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.sync ? 1 : 0);
        hash = 47 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object object) {        
        if (object == null) {
            return false;
        }        
        if (getClass() != object.getClass()) {
            return false;
        }
        final SyncPrefsWiFi that = (SyncPrefsWiFi)object;
        return this.sync == that.sync && 
               this.port == that.port;
    }
    
}
