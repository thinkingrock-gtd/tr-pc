package tr.model.goals.dto;

import java.util.Date;

public class GoalDTO {

    public final int id;
    public final Integer levelID;
    public final Integer topicID;
    public final String descr;
    public final String vision;
    public final String accountability;
    public final String rewards;
    public final String obstacles;
    public final String support;
    public final String brainstorming;
    public final String notes;
    public final Date created;
    public final Date start;
    public final Date end;
    public final Date achieved;

    public GoalDTO(
            int id,
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
            Date achieved) {
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
        if (that instanceof GoalDTO) {
            return ((GoalDTO)that).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
