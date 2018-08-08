package au.com.trgtd.tr.sync.device.v100.message.parse;

import au.com.trgtd.tr.sync.device.exception.ParserException;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.openide.util.NotImplementedException;

/**
 * Default simple message parser.
 */
class ParserDefault implements IParser {

    private static final RecvMsg.Type[] SIMPLE_MSG_TYPES = {
        RecvMsg.Type.GOODBYE,
        RecvMsg.Type.NEXTACTION,
        RecvMsg.Type.NEXTPROJECT,
        RecvMsg.Type.NEXTTOPIC,
        RecvMsg.Type.NEXTCONTEXT,
        RecvMsg.Type.NEXTTIME,
        RecvMsg.Type.NEXTENERGY,
        RecvMsg.Type.NEXTPRIORITY,
        RecvMsg.Type.NEXTREFERENCE,
        RecvMsg.Type.CANCEL,
    };

    private static final Map<RecvMsg.Type, RecvMsg> SIMPLE_MSG_MAP;
    static {
        EnumMap<RecvMsg.Type, RecvMsg> map = new EnumMap<RecvMsg.Type, RecvMsg>(RecvMsg.Type.class);

        for (RecvMsg.Type msgType : SIMPLE_MSG_TYPES) {
            map.put(msgType, new RecvMsg(msgType));
        }

        SIMPLE_MSG_MAP = Collections.unmodifiableMap(map);
    }

    @Override
    public RecvMsg parse(String string) throws Exception {
        throw new NotImplementedException();
    }

    /**
     * Returns a message object for the given simple message type.
     * @param type The message type.
     * @return a message object.
     * @throws Exception if the type is not known.
     */
    public RecvMsg getMsg(RecvMsg.Type type) throws Exception {
        RecvMsg msg = SIMPLE_MSG_MAP.get(type);
        if (msg == null) {
            throw new ParserException("Unknown message type: " + type.toCode());
        }
        return msg;
    }

}
