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
import java.io.File;
import java.util.Objects;

/**
 * Application logic for a snapshot of the Android ADB synchronization user
 * preferences.
 *
 * @author Jeremy Moore
 */
public final class SyncPrefsADB {

    /** 
     * Gets the current ADB sync user preferences. 
     * @return The current ADB sync user preferences,
     */
    public static SyncPrefsADB getCurrent() {
        return new SyncPrefsADB(SyncPrefs.isSyncAndroid() && SyncPrefs.isSyncUSB(),
                                SyncPrefs.getADBPath(),
                                SyncPrefs.getUSBPort());
    }

    private final boolean sync;
    private final String path;
    private final int port;

    /**
     * Constructor.
     *
     * @param sync Whether or not to sync using ADB.
     * @param path The path of the ADB program.
     * @param port The port forwarding port to use.
     */
    public SyncPrefsADB(boolean sync, String path, int port) {
        this.sync = sync;
        this.path = path;
        this.port = port;
    }

    /**
     * Determines whether synchronization using ADB should be done.
     *
     * @return true if the user preferences are set to do ADB synchronization
     * and the ADB program and port number are valid.
     */
    public boolean on() {
        return sync && isValidADBPath() && isValidADBPort();
    }

    /**
     * Determines whether synchronization using ADB should not be done.
     *
     * @return true if not on.
     */
    public boolean off() {
        return !on();
    }

    /**
     * Determines whether a valid ADB program specified.
     *
     * @return true if there is a path of an executable file set in the user
     * preferences.
     */
    public boolean isValidADBPath() {
        File adbFile = new File(path);
        return adbFile.isFile() && adbFile.canExecute();
    }

    /**
     * Determines whether a valid port forwarding port is specified.
     *
     * @return true if there is a valid port set in the user preferences. The
     * port must be in the range 49152..65535.
     */
    public boolean isValidADBPort() {
        return port >= PORT_MIN_DYNAMIC && port <= PORT_MAX_DYNAMIC;
    }

    /**
     * Gets the ADB program path from the user preferences.
     *
     * @return The ADB path user preference.
     */
    public String getADBPath() {
        return path;
    }

    /**
     * Gets the ADB port forwarding port from the user preferences.
     *
     * @return The port number user preference.
     */
    public int getADBPort() {
        return port;
    }

    @Override
    public String toString() {
        return "SyncPrefsADB{" + "sync=" + sync + ", path=" + path + ", port=" + port + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.sync ? 1 : 0);
        hash = 47 * hash + Objects.hashCode(this.path);
        hash = 47 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SyncPrefsADB other = (SyncPrefsADB)obj;
        if (this.sync != other.sync) {
            return false;
        }
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        return this.port == other.port;
    }

}
