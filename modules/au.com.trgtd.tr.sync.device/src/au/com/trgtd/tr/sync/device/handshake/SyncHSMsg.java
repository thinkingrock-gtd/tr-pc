package au.com.trgtd.tr.sync.device.handshake;

import au.com.trgtd.tr.sync.device.Constants;
import java.text.MessageFormat;

/**
 * Handshake message.
 */
public class SyncHSMsg implements ISyncMsg {

    public final String code = Constants.CODE_HANDSHAKE;
    public final String version;

    public SyncHSMsg(String version) {
        this.version = version;
    }


    public static final String PATTERN =
            Constants.CODE_HANDSHAKE + Constants.DELIM +
            Constants.FIELD_VERSION + Constants.DELIM + "{0}" + Constants.DELIM;

    @Override
    public String toSendString() {
        return MessageFormat.format(PATTERN, version);
    }

}
