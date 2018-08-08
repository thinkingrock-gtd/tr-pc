package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.exception.CancelException;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgCancel;

/**
 * Handler for received goodbye message.
 *
 * @author Jeremy Moore
 */
class HandlerCancel extends Handler <RecvMsg> {

    private static final SendMsg cancel = new SendMsgCancel();

    HandlerCancel(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg cancelMsg) throws Exception {
        sender.send(cancel);
        throw new CancelException();
    }

}
