package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgAction;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgOK;

/**
 * Handle received update action message.
 *
 * @author Jeremy Moore
 */
class HandlerUpdActn extends Handler <RecvMsg> {

    private static final SendMsg ok = new SendMsgOK();

    HandlerUpdActn(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception {
        data.addRcvAction((RecvMsgAction)msg);
        sender.send(ok);
    }

}
