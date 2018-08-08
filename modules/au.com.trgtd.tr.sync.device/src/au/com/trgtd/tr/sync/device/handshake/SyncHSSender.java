package au.com.trgtd.tr.sync.device.handshake;

import au.com.trgtd.tr.sync.device.socket.send.ISocketSender;
import au.com.trgtd.tr.sync.device.socket.send.SocketSenderException;

/**
 * Receiver for obtaining high level messages using a wrapped socket receiver.
 *
 * @author Jeremy Moore
 */
public class SyncHSSender {

    private final ISocketSender sender;

    /**
     * Construct a new instance.
     *
     * @param sender The wrapped socket sender.
     * @throws SocketSenderException if there is an exception thrown by the
     * socket sender.
     */
    public SyncHSSender(ISocketSender sender) throws SocketSenderException {
        if (sender == null) {
            throw new SocketSenderException("Socket sender is null.");
        }
        this.sender = sender;
    }

    public void send(ISyncMsg msg) throws SocketSenderException {
        sender.send(msg.toSendString());
    }

}
