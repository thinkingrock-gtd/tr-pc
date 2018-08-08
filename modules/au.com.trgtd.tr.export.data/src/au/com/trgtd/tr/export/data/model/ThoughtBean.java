package au.com.trgtd.tr.export.data.model;

import java.util.Date;

public final class ThoughtBean {

    private final int id;
    private final Date created;
    private final String title;
    private final String notes;
    private final Integer topicID;
    private final Integer actionID;
    private final boolean processed;
    private final Integer delegationType;     // DELEGATION.ordinal() | RESPONSE.ordinal()
    private final Integer delegationActionID; // Delegated action ID or null
    private final Date delegationDone;        // Delegation done date    
    private final String delegationReply;     // Deletation reply email address

    public ThoughtBean(int id, Date created, String title, String notes, Integer topicID, Integer actionID, boolean processed,
            Integer delegationType, Integer delegationActionID, Date delegationDone, String delegationReply) {
        this.id = id;
        this.created = created;
        this.title = title;
        this.notes = notes;
        this.topicID = topicID;
        this.actionID = actionID;
        this.processed = processed;
        this.delegationType = delegationType;
        this.delegationActionID = delegationActionID;
        this.delegationDone = delegationDone;
        this.delegationReply = delegationReply;
    }

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public Integer getTopicID() {
        return topicID;
    }

    public Integer getActionID() {
        return actionID;
    }

    public boolean isProcessed() {
        return processed;
    }

    public Integer getDelegationType() {
        return delegationType;
    }

    public Integer getDelegationActionID() {
        return delegationActionID;
    }

    public Date getDelegationDone() {
        return delegationDone;
    }

    public String getDelegationReply() {
        return delegationReply;
    }

}
