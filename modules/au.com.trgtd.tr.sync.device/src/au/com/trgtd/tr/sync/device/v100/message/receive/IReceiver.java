package au.com.trgtd.tr.sync.device.v100.message.receive;

/**
 * Receiver for obtaining high level messages.
 *
 * @author Jeremy Moore
 */
public interface IReceiver
{    
    /**
     * Receive high level message.
     * @return the message.
     * @throws Exception
     */
    public RecvMsg recv() throws Exception;

    /**
     * Receive specific high level message.
     * @param type The expected message type.
     * @return the message.
     * @throws Exception
     */
    public RecvMsg recv(RecvMsg.Type type) throws Exception;

}
