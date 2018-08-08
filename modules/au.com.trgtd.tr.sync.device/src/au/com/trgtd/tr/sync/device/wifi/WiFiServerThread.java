package au.com.trgtd.tr.sync.device.wifi;

import au.com.trgtd.tr.sync.device.SyncHandlerThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Android WiFi sync server thread. Polls for connection and starts handler.
 *
 * @author Jeremy Moore
 */
final class WiFiServerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(WiFiServerThread.class.getName());
    private static final int TIMEOUT = 1000;
    
    private final int port;
    private ServerSocket serverSocket;
    private boolean cancel;

    WiFiServerThread(int port) {
        super("TR WiFi Sync Server");
        this.port = port;
        this.cancel = false;
    }

    void cancel() {
        cancel = true;
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(TIMEOUT);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not create server socket.", e);
            return;
        }
                              
        LOG.log(Level.INFO, "Android WiFi server started on port: {0}", port);                        
        
        while (!cancel) {
            try {
                Socket clientSocket = serverSocket.accept();
                SyncHandlerThread handler = new SyncHandlerThread(clientSocket, null);
                handler.start();            
            } catch (SocketTimeoutException stx) {
                // expected - ignore
            } catch (IOException iox) {
                // unexpected
                LOG.log(Level.WARNING, "Could not accept connection.", iox);
            }
        }
        
        try {
            serverSocket.close();
            LOG.log(Level.INFO, "Server closed.");                                    
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not close server socket.", e);
        }
        
        LOG.log(Level.INFO, "Android WiFi server stopped");                        
    }

}
