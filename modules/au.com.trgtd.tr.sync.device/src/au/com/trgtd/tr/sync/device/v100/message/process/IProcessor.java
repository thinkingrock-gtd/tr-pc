package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;

/**
 * Processor of received messages.
 *
 * @author Jeremy Moore
 */
public interface IProcessor
{    
    /**
     * Process a received message.
     * @param recvMsg The received message.
     */
    public void process(RecvMsg recvMsg) throws Exception;

}
