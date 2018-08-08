package au.com.trgtd.tr.sync.device.v100.message.send;

import static au.com.trgtd.tr.appl.Constants.ID_DEFAULT_TOPIC;
import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Reference.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import tr.model.information.Information;
import tr.model.topic.Topic;

/**
 * Topic send message.
 */
public class SendMsgReference extends SendMsg {

    private final Information info;

    // Change fields and setters (for insert or update).
    private Integer insertID;
    private String changeTitle;
    private String changeNotes;
    private Integer changeTopicID;
    
    public void setChangeNotes(String changeNotes) {
        this.changeNotes = changeNotes;
    }
    public void setChangeTitle(String changeTitle) {
        this.changeTitle = changeTitle;
    }
    public void setChangeTopicID(Integer changeTopicID) {
        this.changeTopicID = changeTopicID;
    }
    public void setInsertID(Integer insertID) {
        this.insertID = insertID;
    }
    // End change fields and setters
    
    public SendMsgReference(Information info) {
        super(SendMsg.Type.REFERENCE);
        this.info = info;
    }

    @Override
    public String toSendString() {
        return MessageFormat.format(PATTERN, 
                String.valueOf(getID()), 
                SyncUtils.escape(getTitle()), 
                SyncUtils.escape(getNotes()), 
                String.valueOf(getTopicID()));
    }

    private static final String PATTERN =
            SendMsg.Type.REFERENCE.getCode() + DELIM +
            ID           + DELIM + "{0}"  + DELIM +
            TITLE        + DELIM + "{1}"  + DELIM +
            NOTES        + DELIM + "{2}"  + DELIM +
            TOPIC_ID     + DELIM + "{3}"  + DELIM;

    private int getID() {
        return insertID == null ? info.getID() : insertID;         
    }

    private String getTitle() {
        return changeTitle == null ? info.getDescription() : changeTitle; 
    }
    
    private String getNotes() {
        return changeNotes == null ? info.getNotes() : changeNotes;
    }
    
    private int getTopicID() {
        if (changeTopicID == null) {
            Topic topic = info.getTopic();
            return topic == null ? ID_DEFAULT_TOPIC : topic.getID();
        } else {
            return changeTopicID;
        }
    }

    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Reference.ID, factory.numberNode(getID()));
        node.set(Fields.Send.Reference.TITLE, factory.textNode(getTitle()));
        node.set(Fields.Send.Reference.NOTES, factory.textNode(getNotes()));
        node.set(Fields.Send.Reference.TOPIC_ID, factory.numberNode(getTopicID()));
        return node;
    } 
    
}
