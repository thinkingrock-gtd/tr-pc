package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Send message goodbye.
 */
public class SendMsgOK extends SendMsg {

    public SendMsgOK() {
        super(SendMsg.Type.OK);
    }

    @Override
    public String toSendString() {
        return type.getCode();
    }

}
