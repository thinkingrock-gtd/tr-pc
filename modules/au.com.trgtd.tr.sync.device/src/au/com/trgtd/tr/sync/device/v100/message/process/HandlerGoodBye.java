package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgGoodBye;

/**
 * Handler for received goodbye message.
 *
 * @author Jeremy Moore
 */
class HandlerGoodBye extends Handler <RecvMsg> {

    private static final SendMsgGoodBye goodbye = new SendMsgGoodBye();

    HandlerGoodBye(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg recvMsg) throws Exception {
        sender.send(goodbye);
    }

}
