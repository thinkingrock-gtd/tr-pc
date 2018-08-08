package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Send message end (ie there are no more items to send).
 */
public class SendMsgEnd extends SendMsg {

    public SendMsgEnd() {
        super(SendMsg.Type.END);
    }

    @Override
    public String toSendString() {
        return type.getCode();
    }

}
