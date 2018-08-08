package tr.model.goals.data;

import java.util.Date;

public class Goal {

    private final Integer id;               // manditory
    private Integer levelID;                // manditory except root goal
    private Integer topicID;                // null or a topic ID
    private String descr;                   // manditory
    private String vision;
    private String accountability;
    private String rewards;
    private String obstacles;
    private String support;
    private String brainstorming;
    private String notes;
    private final Date created;             // manditory
    private Date start;                     // null if level does not have start dates, null or a date if it does.
    private Date end;                       // null if level does not have end dates, null or a date if it does.
    private Date achieved;

    public Goal(
            Integer id,
            Integer levelID,
            Integer topicID,
            String descr,
            String vision,
            String accountability,
            String rewards,
            String obstacles,
            String support,
            String brainstorming,
            String notes,
            Date created,
            Date start,
            Date end,
            Date achieved)
    {
        this.id = id;
        this.levelID = levelID;
        this.topicID = topicID;
        this.descr = descr;
        this.vision = vision;
        this.accountability = accountability;
        this.rewards = rewards;
        this.obstacles = obstacles;
        this.support = support;
        this.brainstorming = brainstorming;
        this.notes = notes;
        this.created = created;
        this.start = start;
        this.end = end;
        this.achieved = achieved;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof Goal) {
            return ((Goal)that).getID() == this.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getID() {
        return id;
    }

    public Date getCreatedDate() {
        return created;
    }

    public Integer getLevelID() {
        return levelID;
    }

    public void setLevelID(Integer levelID) {
        this.levelID = levelID;
    }

    public Integer getTopicID() {
        return topicID;
    }

    public void setTopicID(Integer topicID) {
        this.topicID = topicID;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getAccountability() {
        return accountability;
    }

    public void setAccountability(String accountability) {
        this.accountability = accountability;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public String getObstacles() {
        return obstacles;
    }

    public void setObstacles(String obstacles) {
        this.obstacles = obstacles;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getBrainstorming() {
        return brainstorming;
    }

    public void setBrainstorming(String brainstorming) {
        this.brainstorming = brainstorming;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getStartDate() {
        return start;
    }

    public void setStartDate(Date start) {
        this.start = start;
    }

    public Date getEndDate() {
        return end;
    }

    public void setEndDate(Date end) {
        this.end = end;
    }

    public Date getAchievedDate() {
        return achieved;
    }

    public void setAchievedDate(Date achieved) {
        this.achieved = achieved;
    }

}
