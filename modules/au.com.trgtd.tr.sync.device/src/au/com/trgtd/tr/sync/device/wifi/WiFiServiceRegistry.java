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
