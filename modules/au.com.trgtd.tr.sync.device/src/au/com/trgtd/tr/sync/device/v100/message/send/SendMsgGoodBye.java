package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Send message goodbye.
 */
public class SendMsgGoodBye extends SendMsg {

    public SendMsgGoodBye() {
        super(SendMsg.Type.GOODBYE);
    }

    @Override
    public String toSendString() {
        return type.getCode();
    }

}
