package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgProject;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgOK;

/**
 * Handle received update project message.
 *
 * @author Jeremy Moore
 */
class HandlerUpdProject extends Handler <RecvMsg> {

    private static final SendMsg ok = new SendMsgOK();

    HandlerUpdProject(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception
    {
        data.addRcvProject((RecvMsgProject)msg);

        sender.send(ok);
    }

}
