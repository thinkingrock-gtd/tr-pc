package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Send message goodbye.
 */
public class SendMsgCancel extends SendMsg {

    public SendMsgCancel() {
        super(SendMsg.Type.CANCEL);
    }

    @Override
    public String toSendString() {
        return type.getCode();
    }

}
