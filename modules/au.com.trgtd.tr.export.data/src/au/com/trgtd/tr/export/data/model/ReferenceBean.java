package au.com.trgtd.tr.export.data.model;

import java.util.Date;

public final class ReferenceBean {

    private final int id;
    private final String title;
    private final String notes;
    private final Integer topicID;
    private final Date created;
    
    public ReferenceBean(int id, String title, String notes, Integer topicID, Date created) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.topicID = topicID;
        this.created = created;
    }

    public int getId() {
        return id;
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

    public Date getCreated() {
        return created;
    }
    
}
