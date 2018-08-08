package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;

/**
 * Message handler abstract base class.
 *
 * @author Jeremy Moore
 */
abstract class Handler <RecvMsg> {

//  protected final ISocketSender sender;
    protected final ISender sender;
    protected final DataMgr data;

    /**
     * Construct the handler.
     * @param sender The message sender.
     */
//  Handler(ISocketSender sender, DataMgr data) {
    Handler(ISender sender, DataMgr data) {
        this.sender = sender;
        this.data = data;
    }

    /**
     * Handle a received message.
     * @param recvMsg The received message.
     */
    abstract void handle(RecvMsg recvMsg) throws Exception;
    
}
