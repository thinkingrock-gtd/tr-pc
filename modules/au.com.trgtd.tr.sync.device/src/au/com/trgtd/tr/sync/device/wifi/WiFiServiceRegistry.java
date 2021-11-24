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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Registers and de-registers the Android WiFi sync service.
 *
 * @author Jeremy Moore
 */
final class WiFiServiceRegistry {

    private static final String SVC_TYPE = "_trgtd._tcp.local.";
    private static final String SVC_NAME = "TR Sync";
    private static final JmDNSRegistration sRegistration = new JmDNSRegistration();

    WiFiServiceRegistry() {
    }

    /**
     * Register the service.
     */
    void registerService(int port, String text) {
        
        Logger.getLogger("WiFiServiceRegistry").log(Level.INFO, "REGISTER WIFI SYNC SERVICE, DATA ID: {0}", text);       
        
        sRegistration.registerService(SVC_TYPE, SVC_NAME, port, text);
    }

    /**
     * De-registers all registered services.
     */
    void deregisterAllServices() {
        sRegistration.deregisterAllServices();
    }
    
}
