package au.com.trgtd.tr.sync.device.v100.message.send;

import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Priority.ID;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Priority.TITLE;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import tr.model.criteria.Value;

/**
 * Priority send message.
 */
public class SendMsgPriority extends SendMsg {

    private final Value priority;

    public SendMsgPriority(Value priority) {
        super(SendMsg.Type.PRIORITY);
        this.priority = priority;
    }

    @Override
    public String toSendString() {
        return MessageFormat.format(
                PATTERN,
                getID(),
                getTitle());
    }

    private static final String PATTERN =
            SendMsg.Type.PRIORITY.getCode() + DELIM +
            ID           + DELIM + "{0}"  + DELIM +
            TITLE        + DELIM + "{1}"  + DELIM;

    private String getID() {
        return String.valueOf(priority.getID());
    }
    private String getTitle() {
        return SyncUtils.escape(priority.getName());
    }
    
    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Priority.ID, factory.numberNode(priority.getID()));
        node.set(Fields.Send.Priority.TITLE, factory.textNode(priority.getName()));
        return node;
    } 

}
