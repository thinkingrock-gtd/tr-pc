package au.com.trgtd.tr.sync.device.handshake;

import au.com.trgtd.tr.sync.device.exception.ParserException;
import au.com.trgtd.tr.sync.device.socket.receive.ISocketReceiver;
import au.com.trgtd.tr.sync.device.socket.receive.SocketReceiverException;

/**
 * Receiver for handshake message using a wrapped socket receiver.
 *
 * @author Jeremy Moore
 */
public class SyncHSReceiver
{    
    private final ISocketReceiver receiver;
    private final SyncHSParser parser;

    /**
     * Construct a new instance.
     * @param receiver The wrapped socket receiver.
     * @throws ReceiverException if there is an exception thrown by the socket
     * receiver.
     * @throws MsgException if there is an exception parsing the low level
     * message string.
     */
    public SyncHSReceiver(ISocketReceiver receiver) throws SocketReceiverException
    {
        if (receiver == null) {
            throw new SocketReceiverException("Socket receiver is null.");
        }
        this.receiver = receiver;
        this.parser = new SyncHSParser();
    }

    public SyncHSMsg receive() throws ParserException, SocketReceiverException
    {
        return parser.parse(receiver.recv());
    }

}
