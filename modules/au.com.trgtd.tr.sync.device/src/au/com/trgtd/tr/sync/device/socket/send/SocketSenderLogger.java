package au.com.trgtd.tr.sync.device.socket.send;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Message sender that wraps another sender and logs sent messages.
 *
 * @author Jeremy Moore
 */
public class SocketSenderLogger implements ISocketSender
{
    private static final Logger LOG = Logger.getLogger("tr.sync.sender");
    private final ISocketSender sender;

    /**
     * Constructs a sender that wraps another sender and logs sent messages.
     * @param sender The wrapped sender.
     */
    public SocketSenderLogger(ISocketSender sender) {
        this.sender = sender;
    }

    @Override
    public void send(String msg) throws SocketSenderException
    {
        sender.send(msg);

        LOG.log(Level.INFO, "Sent: {0}", msg);
    }

}
