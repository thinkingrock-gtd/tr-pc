package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgProject;

/**
 * Handler of received next project message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtProject extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtProject(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg msg) throws Exception
    {
        SendMsgProject projectMsg = data.getNxtSendMsgProject();
        if (projectMsg == null) {
            sender.send(end);
        } else {
            sender.send(projectMsg);
        }
    }

}
