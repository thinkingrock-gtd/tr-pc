package au.com.trgtd.tr.sync.device.v100.message.send;

import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Energy.ID;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Energy.TITLE;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import tr.model.criteria.Value;

/**
 * Energy send message.
 */
public class SendMsgEnergy extends SendMsg {

    private final Value energy;

    public SendMsgEnergy(Value energy) {
        super(SendMsg.Type.ENERGY);
        this.energy = energy;
    }

    @Override
    public String toSendString() {
        return MessageFormat.format(PATTERN,
                getID(),
                getTitle());
    }

    private static final String PATTERN =
            SendMsg.Type.ENERGY.getCode() + DELIM +
            ID           + DELIM + "{0}"  + DELIM +
            TITLE        + DELIM + "{1}"  + DELIM;

    private String getID() {
        return String.valueOf(energy.getID());
    }
    private String getTitle() {
        return SyncUtils.escape(energy.getName());
    }
    
    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Energy.ID, factory.numberNode(energy.getID()));
        node.set(Fields.Send.Energy.TITLE, factory.textNode(energy.getName()));
        return node;
    } 
}
