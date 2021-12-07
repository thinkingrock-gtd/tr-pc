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
package tr.model.thought;

import java.util.Calendar;
import java.util.Date;
import tr.model.action.Action;
import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Utils;
import tr.model.util.delegation.DelegationData.Type;
import static tr.model.util.delegation.DelegationData.Type.DELEGATION;
import static tr.model.util.delegation.DelegationData.Type.RESPONSE;

/**
 * Represents a thought.
 *
 * @author Jeremy Moore
 */
public class Thought extends ObservableImpl implements TopicChangeCookie {
    
    private Date created;
    private String description;
    private Topic topic;
    private boolean processed;
    private Action action;  // reprocessed or delegated action
    private String notes;
    private Integer id;   
    
    private Integer delegationType;     // DELEGATION.ordinal() | RESPONSE.ordinal()
    private Integer delegationActionID; // Delegated action ID or null
    private Date    delegationDone;     // Delegation done date    
    private String  delegationReply;    // Deletation reply email address
    
    /** Observable changes to thoughts. @since 2.0 */
    public static enum Change { DESCRIPTION, TOPIC, NOTES, PROCESSED }
    
    public transient String key;
    
    /**
     * Creates a new instance.
     * @param id The ID.
     */
    public Thought(int id) {
        this.id = id;
        this.created = Calendar.getInstance().getTime();
        this.description = "";
        this.topic = Topic.getDefault();
        this.processed = false;
    }

    public void initID(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    /**
     * Gets the ID number.
     * @return the ID number.
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the creation date.
     * @return The creation date.
     */
    public Date getCreated() {
        return created;
    }
    
    /**
     * Gets the description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * @param description The description.
     */
    public void setDescription(String description) {
        if (Utils.equal(this.description, description)) return;
        
        this.description = description;
        
        notifyObservers(this, Change.DESCRIPTION);
    }
    
    /**
     * Gets the topic.
     * @return The topic.
     */
    public Topic getTopic() {
        if (topic == null) {
            return Topic.getDefault();
        }
        if (topic.getName().equals(Topic.getDefault().getName())) {
            return Topic.getDefault();
        }
        return topic;
    }
    
    /**
     * Sets the topic.
     * @param topic The topic.
     */
    @Override
    public void setTopic(Topic topic) {
        if (Utils.equal(this.topic, topic)) return;
        
        this.topic = topic;
        
        notifyObservers(this, Change.TOPIC);
    }
    
    /**
     * Gets the notes.
     * @return the notes.
     */
    public String getNotes() {
        return (notes == null) ? "" : notes;
    }
    
    /**
     * Sets the notes.
     * @param notes The notes.
     */
    public void setNotes(String notes) {
        if (notes != null && notes.trim().length() == 0) {
            notes = null;
        }
        if (Utils.equal(this.notes, notes)) {
            return;
        }
        
        this.notes = notes;
        
        notifyObservers(this, Change.NOTES);
    }
    
    /**
     * Determines whether the process flag is set to true.
     * @return true if the processed flag is set to true.
     */
    public boolean isProcessed() {
        return processed;
    }
    
    /**
     * Sets the processed flag.
     * @param processed The new value.
     */
    public void setProcessed(boolean processed) {
        if (this.processed == processed) return;
        
        this.processed = processed;
        
        notifyObservers(this, Change.PROCESSED);
    }
    
    /**
     * Sets the reprocess action.
     * @param action The action.
     * @since 2.0
     */
    public void setAction(Action action) {
        if (this.action == action) return;
        
        this.action = action;
        
        notifyObservers(this);
    }
    
    /**
     * Gets the reprocessed or delegated action.
     * @return action The action.
     */
    public Action getAction() {
        return action;
    }
        
    public Integer getDelegationType() {
        return delegationType;
    }
    
    public void setDelegationType(Type type) {
        this.delegationType = type == null ? null : type.ordinal();
    }
   
    public boolean isDelegation() {
        if (delegationType == null) {
            return false;
        }
        return delegationType == DELEGATION.ordinal();
    }

    public boolean isDelegationResponse() {
        if (delegationType == null) {
            return false;
        }
        return delegationType == RESPONSE.ordinal();
    }
    
    public boolean isDelegationDone() {
        return isDelegationResponse() && delegationDone != null;
    }

    public boolean isDelegationNotDone() {
        return isDelegationResponse() && delegationDone == null;
    }
    
    public void setDelegationReply(String email) {
        delegationReply = email;
    }
    
    public String getDelegationReply() {
        return delegationReply;
    }

    public void setDelegationActionID(Integer id) {
        delegationActionID = id;
    }
    
    public Integer getDelegationActionID() {
        return delegationActionID;
    }

    public void setDelegationDone(Date date) {
        this.delegationDone = date;
    }

    public Date getDelegationDone() {
        return delegationDone;
    }
    
    /**
     * Override equals.
     * @param object
     * @see java.Object.equals()
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Thought)) return false;

        Thought thought = (Thought)object;
        
        if (!Utils.equal(this.created, thought.created)) return false;
        if (!Utils.equal(this.description, thought.description)) return false;
        if (!Utils.equal(this.topic, thought.topic)) return false;
        return this.processed == thought.processed;
    }
    
    @Override
    public String toString() {
        return description;
    }
    
}
