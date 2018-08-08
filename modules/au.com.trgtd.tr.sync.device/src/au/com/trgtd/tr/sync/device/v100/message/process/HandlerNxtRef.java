package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgReference;

/**
 * Handler of received next reference message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtRef extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtRef(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception
    {
        SendMsgReference refMsg = data.getNxtSendMsgRef();
        if (refMsg == null) {
            sender.send(end);
        } else {
            sender.send(refMsg);
        }
    }

}
