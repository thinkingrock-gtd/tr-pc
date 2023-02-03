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
package au.com.trgtd.tr.sync.device.wifi;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.sync.device.prefs.SyncPrefsWiFi;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Manages the Android WiFi sync service.
 *
 * @author Jeremy Moore
 */
public final class WiFiServiceManager {

    private static final Logger LOG = Logger.getLogger(WiFiServiceManager.class.getName());
    private static final long STOP_SERVER_TIMEOUT = 5000;

    // Singleton instance
    public static WiFiServiceManager instance = new WiFiServiceManager();

    
    /* Singleton instance constructor. */
    private WiFiServiceManager() {
        registry = new WiFiServiceRegistry();
    }
    
    private final WiFiServiceRegistry registry;
    private WiFiServerThread serverThread;
    
    /** 
     * Initiate service management. Call once when application starts. 
     */
    public void initiate() {
        
        startServices();

        // listen for user preferences change to reset service
        SyncPrefsWiFi.addListener((PropertyChangeEvent evt) -> {
            LOG.info("WiFi sync preferences have changed - resetting WiFi service.");
            resetServices();
        });
        
        // listen for data file change to reset service
        Lookup.Result<Data> r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            LOG.info("Data source has changed - resetting WiFi service.");
            resetServices();
        });        
    }

    /** Terminate service management. Call once when application quits. */
    public void terminate() {
        stopServices(null);
    }
    
    
    // PRIVATE 
    
    private void resetServices() {
        stopServices(STOP_SERVER_TIMEOUT);
        startServices();
    }
    
    private void startServices() {
        
        SyncPrefsWiFi wifiSyncPrefs = SyncPrefsWiFi.getCurrent();
        
        // only if user preferences set to use WiFi. 
        if (wifiSyncPrefs.off()) {
            return;
        }        

        final String svcText = getDataID();
        if (svcText == null) {
            LOG.log(Level.SEVERE, "Data file not found.");
            return;
        }

        final int svcPort = SyncPrefsWiFi.getCurrent().getWiFiPort();
        
        startServer(svcPort);        
        registry.registerService(svcPort, svcText);
    }
    
    private void stopServices(Long timeout) {
        registry.deregisterAllServices();
        stopServer(timeout);
    }
    
    
    // Start the server thread.
    private void startServer(int port) {
        
        // first stop server if it is running
        stopServer(STOP_SERVER_TIMEOUT);
        
        // start server
        serverThread = new WiFiServerThread(port);
        serverThread.start();
    }

    // Stop the server thread.
    private void stopServer(Long timeout) {
        if (serverThread == null) {
            return;
        }
        if (!serverThread.isAlive()) {
            serverThread = null;
            return;
        }
        
        serverThread.cancel();            
        
        if (timeout != null) {
            try {
                serverThread.join(timeout);
            } catch (InterruptedException e) {
                LOG.log(Level.WARNING, "Stop server timed out.");
            }
        }

        serverThread = null;
    }    
    
    private static String getDataID() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            return null;
        }
        File datafile = new File(ds.getPath());
        if (!datafile.exists()) {
            return null;
        }
        return datafile.getName();
    }
        
}
