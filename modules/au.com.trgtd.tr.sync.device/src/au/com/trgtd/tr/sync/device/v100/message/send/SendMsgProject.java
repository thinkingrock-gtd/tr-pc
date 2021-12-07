/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.sync.device.v100.message.send;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.sync.device.Constants;
import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.SyncUtils;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Project.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.text.MessageFormat;
import java.util.Date;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.topic.Topic;

/**
 * Topic send message.
 */
public class SendMsgProject extends SendMsg {

    private final Project project;
    
    public Project getProject() {
        return project;
    }
    
    public int getID() {
        return project.getID();
    }
    
    
    // ADDED to enable listing of actions within projects on Android app.
    // Order number of project within parent (starting at 0).
    private int ordinal;
    private String getOrdinal() {
        return String.valueOf(ordinal);
    }    
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }    
    // END ADDED
    
    
    // Change fields and setters (for insert or update).
    private String changedNotes;
    private String changedPurpose;
    private String changedVision;
    private String changedBrainstorm;
    private String changedOrganise;
    private Date   changedDueDate;
    public void setChangedBrainstorm(String changedBrainstorm) {
        this.changedBrainstorm = changedBrainstorm;
    }
    public void setChangedDueDate(Date changedDueDate) {
        this.changedDueDate = changedDueDate;
    }
    public void setChangedNotes(String changedNotes) {
        this.changedNotes = changedNotes;
    }
    public void setChangedOrganise(String changedOrganise) {
        this.changedOrganise = changedOrganise;
    }
    public void setChangedPurpose(String changedPurpose) {
        this.changedPurpose = changedPurpose;
    }
    public void setChangedVision(String changedVision) {
        this.changedVision = changedVision;
    }
    // End change fields and setters
    
    public SendMsgProject(Project project) {
        super(SendMsg.Type.PROJECT);
        this.project = project;
        this.ordinal = 0; // default
    }

    @Override
    public String toSendString() {
        final String path = getPath();
        
        return MessageFormat.format(PATTERN, 
                getIDString(), 
                SyncUtils.escape(getTitle()), 
                SyncUtils.escape(getNotes()), 
                SyncUtils.escape(getPurpose()),
                SyncUtils.escape(getVision()),
                SyncUtils.escape(getBrainstorm()),
                SyncUtils.escape(getOrganise()),
                SyncUtils.escape(path),
                getDepthString(path),
                getDueDateString(),
                getParentIDString(),
                getTopicID(),
                getPriorityID(),
                getOrdinal());
    }

    private static final String PATTERN =
            SendMsg.Type.PROJECT.getCode() + DELIM +
            ID          + DELIM + "{0}"  + DELIM +
            TITLE       + DELIM + "{1}"  + DELIM +
            NOTES       + DELIM + "{2}"  + DELIM +
            PURPOSE     + DELIM + "{3}"  + DELIM +
            VISION      + DELIM + "{4}"  + DELIM +
            BRAINSTORM  + DELIM + "{5}"  + DELIM +
            ORGANISE    + DELIM + "{6}"  + DELIM +
            PATH        + DELIM + "{7}"  + DELIM +
            DEPTH       + DELIM + "{8}"  + DELIM +
            DUE         + DELIM + "{9}"  + DELIM +
            PARENT_ID   + DELIM + "{10}" + DELIM +            
            TOPIC_ID    + DELIM + "{11}" + DELIM +
            PRIORITY_ID + DELIM + "{12}" + DELIM +
            ORDINAL     + DELIM + "{13}" + DELIM;
    
    private String getIDString() {
        return String.valueOf(project.getID());
    }

    private String getTitle() {
        return project.getDescription();
    }
    
    private String getNotes() {
        return changedNotes != null ? changedNotes : project.getNotes();
    }
    
    private String getTopicID() {
        Topic topic = project.getTopic();
        if (null == topic) {
            topic = Topic.getDefault();
        } 
        return String.valueOf(topic.getID());
    }
    
    private String getPurpose() {
        if (changedPurpose != null) {
            return changedPurpose;
        }        
        return project.getPurpose();
    }
    
    private String getVision() {
        if (changedVision != null) {
            return changedVision;
        }                
        return project.getVision();
    }
    
    private String getBrainstorm() {
        if (changedBrainstorm != null) {
            return changedBrainstorm;
        }        
        
        return project.getBrainstorming();
    }

    private String getOrganise() {
        if (changedOrganise != null) {
            return changedOrganise;
        }                
        return project.getOrganising();
    }
    
    private String getPath() {
        return Services.instance.getPath(project);
    }
    
    private String getDepthString(String path) {        
        return String.valueOf(getDepth(path));        
    }        

    private int getDepth(String path) {        
        int count = 0;
        for (char c : path.toCharArray()) {
            if (c == '/') {
                count++;
            }
        }             
        return count;        
    }        
    
    private Date getDueDate() {
        return changedDueDate != null ? changedDueDate : project.getDueDate();
    }

    private String getDueDateString() {
        final Date d = getDueDate();
        return d == null ? "" : Constants.DF_DATE.format(d);
    }

    private String getParentIDString() {
        Project parent = (Project)project.getParent();        
        return (null == parent || parent.isRoot() ? "" : String.valueOf(parent.getID()));
    }

    private Integer getParentID() {
        Project parent = (Project)project.getParent();        
        return (null == parent || parent.isRoot() ? null : parent.getID());
    }
    
    private String getPriorityID() {
        Data data = getData();
        if (data == null || !data.getPriorityCriterion().isUse()) {
            return "";
        }
        Value value = project.getPriority();
        if (value == null) {
            return "";            
        }
        return String.valueOf(value.getID());
    }

    private Data mData;
    private Data getData() {
        if (mData == null) {
            mData = DataLookup.instance().lookup(Data.class);
        }
        return mData;
    }
    
    public JsonNode toJsonNode(JsonNodeFactory factory) {
        ObjectNode node = factory.objectNode();
        node.set(Fields.Send.Project.ID, factory.numberNode(getID()));
        node.set(Fields.Send.Project.TITLE, factory.textNode(project.getDescription()));
        node.set(Fields.Send.Project.NOTES, factory.textNode(getNotes()));
        node.set(Fields.Send.Project.PURPOSE, factory.textNode(getPurpose()));
        node.set(Fields.Send.Project.VISION, factory.textNode(getVision()));
        node.set(Fields.Send.Project.BRAINSTORM, factory.textNode(getBrainstorm()));
        node.set(Fields.Send.Project.ORGANISE, factory.textNode(getOrganise()));
        node.set(Fields.Send.Project.PATH, factory.textNode(getPath()));
        node.set(Fields.Send.Project.DEPTH, factory.numberNode(getDepth(getPath())));
        Date due = getDueDate();
        if (due == null) {
            node.set(Fields.Send.Project.DUE, factory.nullNode());            
        } else {
            node.set(Fields.Send.Project.DUE, factory.numberNode(due.getTime()));            
        }
        Integer parentID = getParentID();
        if (parentID == null) {
            node.set(Fields.Send.Project.PARENT_ID, factory.nullNode());                        
        } else {
            node.set(Fields.Send.Project.PARENT_ID, factory.numberNode(parentID));            
        }
        node.set(Fields.Send.Project.TOPIC_ID, factory.numberNode(project.getTopic().getID()));            
        
        Value priority = project.getPriority();        
        if (priority == null) {
            node.set(Fields.Send.Project.PRIORITY_ID, factory.nullNode());                        
        } else {
            node.set(Fields.Send.Project.PRIORITY_ID, factory.numberNode(priority.getID()));                        
        }
        node.set(Fields.Send.Project.ORDINAL, factory.numberNode(ordinal));            
        return node;
    } 
}
