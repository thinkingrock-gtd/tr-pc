package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Sender of high level messages.
 *
 * @author Jeremy Moore
 */
public interface ISender
{    
    /**
     * Send high level message.
     * @param msg The send message.
     * @return the message.
     * @throws Exception
     */
    public void send(SendMsg msg) throws Exception;

}
