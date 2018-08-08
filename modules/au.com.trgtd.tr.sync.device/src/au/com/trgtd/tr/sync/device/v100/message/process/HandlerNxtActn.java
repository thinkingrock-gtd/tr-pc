package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgAction;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;

/**
 * Handler of received next action message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtActn extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtActn(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception
    {
        SendMsgAction actionMsg = data.getNxtSendMsgActn();
        if (actionMsg == null) {
            sender.send(end);
        } else {
            sender.send(actionMsg);
        }
    }

}
