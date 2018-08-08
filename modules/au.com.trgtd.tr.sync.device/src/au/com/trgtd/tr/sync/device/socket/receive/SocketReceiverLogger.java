package au.com.trgtd.tr.sync.device.socket.receive;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Message receiver that wraps another receiver and logs messages.
 *
 * @author Jeremy Moore
 */
public class SocketReceiverLogger implements ISocketReceiver
{
    private static final Logger LOG = Logger.getLogger("tr.sync.receiver");
    private final ISocketReceiver receiver;

    /**
     * Constructs a receiver that wraps another receiver and logs messages.
     * @param receiver The wrapped receiver.
     */
    public SocketReceiverLogger(ISocketReceiver receiver)
    {
        this.receiver = receiver;
    }

    @Override
    public String recv() throws SocketReceiverException
    {
        String msg = receiver.recv();

        LOG.log(Level.INFO, "Recv: {0}", msg);

        return msg;
    }

}
