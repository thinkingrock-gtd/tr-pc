package au.com.trgtd.tr.sync.device.socket.send;

/**
 * Message sender interface.
 *
 * @author Jeremy Moore
 */
public interface ISocketSender
{
    /**
     * Send a message and check for exception.
     * @param msg The message
     * @throws SenderException if an exception is thrown in the underlying
     * sending mechanism.
     */
    public void send(String msg) throws SocketSenderException;
    
}
