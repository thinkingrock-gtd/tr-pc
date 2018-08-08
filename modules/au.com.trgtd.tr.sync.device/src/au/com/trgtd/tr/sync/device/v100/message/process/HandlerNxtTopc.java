package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsg;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgEnd;
import au.com.trgtd.tr.sync.device.v100.message.send.SendMsgTopic;

/**
 * Handler for received next topic message.
 *
 * @author Jeremy Moore
 */
class HandlerNxtTopc extends Handler <RecvMsg> {

    private static SendMsg end = new SendMsgEnd();

    HandlerNxtTopc(ISender sender, DataMgr data) {
        super(sender, data);
    }

    @Override
    void handle(RecvMsg nextTopicMsg) throws Exception
    {
        SendMsgTopic topicMsg = data.getNxtSendMsgTopc();
        if (topicMsg == null) {
            sender.send(end);
        } else {
            sender.send(topicMsg);
        }
    }

}
