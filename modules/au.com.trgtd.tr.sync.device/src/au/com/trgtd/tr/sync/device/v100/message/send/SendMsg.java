package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Send message abstract base class.
 *
 * @author Jeremy Moore
 */
public abstract class SendMsg {

    /** Send message type enumerator. */
    public static enum Type {
        SUMMARY,
        ACTION,
        TOPIC,
        CONTEXT,
        TIME,
        ENERGY,
        PRIORITY,
        REFERENCE,
        PROJECT,
        END,
        OK,
        CANCEL,
        GOODBYE;
        public String getCode() {
            return name();
        }
    }

    public final Type type;

    /**
     * Constructs a new instance.
     * @param type The message type.
     */
    public SendMsg(Type type) {
        this.type = type;
    }

    public abstract String toSendString();

}
