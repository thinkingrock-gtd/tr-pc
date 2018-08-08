package au.com.trgtd.tr.sync.device.v100.message.receive;

/**
 * Received message class. Extended for messages containing data.
 *
 * @author Jeremy Moore
 */
public class RecvMsg {

    public final Type type;

    /**
     * Constructs a new instance.
     * @param code The message code.
     */
    public RecvMsg(Type code) {
        this.type = code;
    }


    /** Message type enumerator. */
    public static enum Type {
        SUMMARY,
        NEWTHOUGHT,
        UPDACTION,
        UPDPROJECT,
        REFERENCE,
        NEXTACTION,
        NEXTPROJECT,
        NEXTTOPIC,
        NEXTCONTEXT,
        NEXTTIME,
        NEXTENERGY,
        NEXTPRIORITY,
        NEXTREFERENCE,
        GOODBYE,
        CANCEL;

        /**
         * Gets the message type from a message code.
         * @param code message code string.
         * @return the message type.
         */
        public static Type fromCode(String code) throws Exception {
            return Type.valueOf(code);
        }

        /**
         * Returns the code string.
         * @return the code string.
         */
        public String toCode() {
            return name();
        }
    }
}
