package au.com.trgtd.tr.export.data.model;

import java.util.Date;

public final class SomedayBean {

    private final int id;
    private final String title;
    private final String notes;
    private final Integer thoughtID;
    private final Integer topicID;
    private final Date created;
    private final Date tickle;

    public SomedayBean(int id, String title, String notes, Integer thoughtID, Integer topicID, Date created, Date tickle) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.thoughtID = thoughtID;
        this.topicID = topicID;
        this.created = created;
        this.tickle = tickle;
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

    public Integer getThoughtID() {
        return thoughtID;
    }

    public Integer getTopicID() {
        return topicID;
    }

    public Date getCreated() {
        return created;
    }

    public Date getTickle() {
        return tickle;
    }
    
}
