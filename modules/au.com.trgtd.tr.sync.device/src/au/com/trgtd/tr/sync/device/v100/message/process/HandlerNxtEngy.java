package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnergy;

/**
 * Handler for received next energy message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtEngy extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtEngy(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg nextEnergyMsg) throws Exception
    {
        SendMsgEnergy energyMsg = data.getNxtSendMsgEnergy();
        if (energyMsg == null) {
            sender.send(end);
        } else {
            sender.send(energyMsg);
        }
    }

}
