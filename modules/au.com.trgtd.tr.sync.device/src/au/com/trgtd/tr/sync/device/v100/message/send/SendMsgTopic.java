package au.com.trgtd.tr.sync.device.v100.message.send;

import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Topic.ID;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Topic.TITLE;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import tr.model.topic.Topic;

/**
 * Topic send message.
 */
public class SendMsgTopic extends SendMsg {

    private final Topic topic;

    public SendMsgTopic(Topic topic) {
        super(SendMsg.Type.TOPIC);
        this.topic = topic;
    }

    @Override
    public String toSendString() {
        return MessageFormat.format(PATTERN, getID(), getTitle());
    }

    private static final String PATTERN =
            SendMsg.Type.TOPIC.getCode() + DELIM +
            ID           + DELIM + "{0}"  + DELIM +
            TITLE        + DELIM + "{1}"  + DELIM;

    private String getID() {
        return String.valueOf(topic.getID());
    }
    private String getTitle() {
        return SyncUtils.escape(topic.getName());
    }

    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Topic.ID, factory.numberNode(topic.getID()));
        node.set(Fields.Send.Topic.TITLE, factory.textNode(topic.getName()));
        return node;
    } 
}
