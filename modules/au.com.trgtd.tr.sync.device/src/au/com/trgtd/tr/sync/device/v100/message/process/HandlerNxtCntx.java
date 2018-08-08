package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgContext;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;

/**
 * Handler for received next context message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtCntx extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtCntx(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg nextContextMsg) throws Exception
    {
        SendMsgContext contextMsg = data.getNxtSendMsgCntx();
        if (contextMsg == null) {
            sender.send(end);
        } else {
            sender.send(contextMsg);
        }
    }

}



