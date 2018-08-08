package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgThought;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgOK;

/**
 * Handler for received new thought message.
 *
 * @author Jeremy Moore
 */
class HandlerNewThgt extends Handler <RecvMsg> {

    private static final SendMsg ok = new SendMsgOK();

    HandlerNewThgt(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception
    {
        data.addRcvThought((RecvMsgThought)msg);

        sender.send(ok);
    }

}
