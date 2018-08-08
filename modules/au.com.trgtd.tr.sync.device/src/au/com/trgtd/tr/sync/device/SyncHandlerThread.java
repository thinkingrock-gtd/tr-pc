package au.com.trgtd.tr.sync.device;

import au.com.trgtd.tr.sync.device.exception.VersionException;
import au.com.trgtd.tr.sync.device.handshake.SyncHSMsg;
import au.com.trgtd.tr.sync.device.handshake.SyncHSMsgCancel;
import au.com.trgtd.tr.sync.device.handshake.SyncHSReceiver;
import au.com.trgtd.tr.sync.device.handshake.SyncHSSender;
import au.com.trgtd.tr.sync.device.socket.receive.ISocketReceiver;
import au.com.trgtd.tr.sync.device.socket.receive.SocketReceiver;
import au.com.trgtd.tr.sync.device.socket.receive.SocketReceiverLogger;
import au.com.trgtd.tr.sync.device.socket.send.ISocketSender;
import au.com.trgtd.tr.sync.device.socket.send.SocketSender;
import au.com.trgtd.tr.sync.device.socket.send.SocketSenderLogger;
import au.com.trgtd.tr.sync.device.v100.SyncHandlerV100;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sync handler thread.
 *
 * @author Jeremy Moore
 */
public final class SyncHandlerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(SyncHandlerThread.class.getName());
    private final Socket socket;
    private final BufferedReader reader;

    public SyncHandlerThread(Socket socket, BufferedReader reader) {
        this.socket = socket;
        this.reader = reader;
    }

    @Override
    public void run() {
        LOG.log(Level.INFO, "Sync handler started.");                                
        
        if (socket == null) {
            LOG.severe("Socket is null.");
            return;
        }
        if (socket.isClosed()) {
            LOG.severe("Socket is closed.");
            return;
        }
        try {
            sync();
            LOG.log(Level.INFO, "Sync handler finished.");                                            
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Sync handler exception.", ex);                                
        } finally {
            closeConnection();
        }
    }

    private void sync() throws Exception {
        ISocketReceiver socketReceiver = new SocketReceiverLogger(new SocketReceiver(socket, reader));
        ISocketSender socketSender = new SocketSenderLogger(new SocketSender(socket));

        SyncHSReceiver hsReceiver = new SyncHSReceiver(socketReceiver);
        SyncHSSender hsSender = new SyncHSSender(socketSender);

        SyncHSMsg handshake = hsReceiver.receive();
        
//      if ("2.0.0".equals(handshake.version)) {
        if ("2.1.0".equals(handshake.version)) {
            hsSender.send(handshake);
            ISyncHandler syncHandler = new SyncHandlerV100(socketReceiver, socketSender, new SyncState(this));
            syncHandler.sync();
        } else {
//          hsSender.send(new SyncHSMsgCancel());
            hsSender.send(new SyncHSMsg("2.1.0"));
            throw new VersionException();
        }
    }

    // Make sure connection is closed.
    private void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Failed to close socket.", ex);
            }
        }
    }
}