package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgPriority;

/**
 * Handler for received next priority message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtPrty extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtPrty(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg nextPriorityMsg) throws Exception
    {
        SendMsgPriority priorityMsg = data.getNxtSendMsgPriority();
        if (priorityMsg == null) {
            sender.send(end);
        } else {
            sender.send(priorityMsg);
        }
    }

}
