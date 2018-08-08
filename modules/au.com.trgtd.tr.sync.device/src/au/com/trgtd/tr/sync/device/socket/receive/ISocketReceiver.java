package au.com.trgtd.tr.sync.device.socket.receive;

/**
 * Message receiver interface.
 *
 * @author Jeremy Moore
 */
public interface ISocketReceiver
{
    /**
     * Receive a message.
     * @return the received message.
     * @throws ReceiverException if the message is invalid or an exception is
     * thrown in the underlying receiving mechanism.
     */
    public String recv() throws SocketReceiverException;

}
