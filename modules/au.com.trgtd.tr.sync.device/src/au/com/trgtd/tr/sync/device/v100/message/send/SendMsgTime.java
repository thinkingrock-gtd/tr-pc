package au.com.trgtd.tr.sync.device.v100.message.send;

import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Time.ID;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Time.TITLE;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import tr.model.criteria.Value;

/**
 * Time send message.
 */
public class SendMsgTime extends SendMsg {

    private final Value time;

    public SendMsgTime(Value time) {
        super(SendMsg.Type.TIME);
        this.time = time;
    }

    @Override
    public String toSendString() {
        return MessageFormat.format(
                PATTERN,
                getID(),
                getTitle());
    }

    private static final String PATTERN =
            SendMsg.Type.TIME.getCode() + DELIM +
            ID           + DELIM + "{0}"  + DELIM +
            TITLE        + DELIM + "{1}"  + DELIM;

    private String getID() {
        return String.valueOf(time.getID());
    }
    private String getTitle() {
        return SyncUtils.escape(time.getName());
    }

    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Time.ID, factory.numberNode(time.getID()));
        node.set(Fields.Send.Time.TITLE, factory.textNode(time.getName()));
        return node;
    } 
    
}
