package au.com.trgtd.tr.sync.device.wifi;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

/**
 * This class is used to register and deregister services using JmDNS.
 */
final class JmDNSRegistration {
    
    private static final Logger LOG = Logger.getLogger(JmDNSRegistration.class.getName());

    private final Stack<Entry> registrations;;

    // Constructor.
    JmDNSRegistration() {
        this.registrations = new Stack<>();
    }

    /**
     * Register a jmDNS service.
     * @param sName The service name.
     * @param sType The service type.
     * @param sText The service text.
     * @param sPort The service port.
     */
    void registerService(String sType, String sName, int sPort, String sText) {
        try {
            JmDNS jmdns;
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface i = e.nextElement();

                Enumeration<InetAddress> ie = i.getInetAddresses();
                while (ie.hasMoreElements()) {
                    InetAddress addr = ie.nextElement();
                    // We don't support IPv6 yet.
                    if (addr.isAnyLocalAddress()
                     || addr.isMulticastAddress()
                     || addr.isLoopbackAddress()
                     || addr instanceof Inet6Address) 
                    {
                        // list(addr, false); // DEBUG
                        continue;
                    }
                    // list(addr, true); // DEBUG
                    jmdns = JmDNS.create(addr);
                    jmdns.requestServiceInfo(sType, sName);
//                  String text = jmdns.getHostName();
                    ServiceInfo sInfo = ServiceInfo.create(sType, sName, sPort, sText);
                    jmdns.registerService(sInfo);
                    registrations.push(new Entry(sInfo, jmdns));
                }
            }
        } catch (IOException e) {
            String msg = "Failed to register JmDNS service.";
            LOG.log(Level.SEVERE, msg, e);
        }
    }

    /** Unregister the service. */
    void deregisterAllServices() {
        while (!registrations.isEmpty()) {
            Entry registration = registrations.pop();
            registration.jmdns.unregisterService(registration.sInfo);
        }
    }
    
    private final class Entry {
        final ServiceInfo sInfo;
        final JmDNS jmdns;
        Entry(ServiceInfo sInfo, JmDNS jmdns) {
            this.sInfo = sInfo;
            this.jmdns = jmdns;
        }
    }

}
