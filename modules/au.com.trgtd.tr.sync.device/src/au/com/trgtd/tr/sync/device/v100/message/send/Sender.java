package au.com.trgtd.tr.sync.device.v100.message.send;

import au.com.trgtd.tr.sync.device.socket.send.ISocketSender;
import au.com.trgtd.tr.sync.device.socket.send.SocketSenderException;

/**
 * Sender of  high level messages using a wrapped socket sender.
 *
 * @author Jeremy Moore
 */
public class Sender implements ISender
{    
    private final ISocketSender socketSender;

    /**
     * Construct instance.
     * @param socketSender The socket sender.
     * @throws Exception
     */
    public Sender(ISocketSender socketSender) throws Exception
    {
        if (socketSender == null) {
            throw new SocketSenderException("Socket sender is null.");
        }
        this.socketSender = socketSender;
    }

    @Override
    public void send(SendMsg msg) throws Exception
    {
        socketSender.send(msg.toSendString());
    }

}
