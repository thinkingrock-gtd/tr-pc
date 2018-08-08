package au.com.trgtd.tr.sync.device.handshake;

/**
 * Handshake message.
 */
public class SyncHSMsgCancel implements ISyncMsg {

    public final String CANCEL = "CANCEL";

    @Override
    public String toSendString() {
        return CANCEL;
    }

}
