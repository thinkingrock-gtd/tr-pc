package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgTime;

/**
 * Handler for received next time message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtTime extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtTime(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg nextTimeMsg) throws Exception
    {
        SendMsgTime timeMsg = data.getNxtSendMsgTime();
        if (timeMsg == null) {
            sender.send(end);
        } else {
            sender.send(timeMsg);
        }
    }

}
