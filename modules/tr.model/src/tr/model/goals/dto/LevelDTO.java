package tr.model.goals.dto;

public class LevelDTO {

    public final int id;
    public final String descr;
    public final String goalsIconPath;
    public final boolean goalsHaveProjects;
    public final boolean goalsHaveStart;
    public final boolean goalsHaveEnd;
    public final boolean goalsHaveVision;
    public final boolean goalsHaveAccountability;
    public final boolean goalsHaveRewards;
    public final boolean goalsHaveObstacles;
    public final boolean goalsHaveSupport;
    public final boolean goalsHaveBrainstorming;

    public LevelDTO(
            int id,
            String descr,
            boolean goalsHaveProjects,
            boolean goalsHaveStart,
            boolean goalsHaveEnd,
            boolean goalsHaveVision,
            boolean goalsHaveAccountability,
            boolean goalsHaveRewards,
            boolean goalsHaveObstacles,
            boolean goalsHaveSupport,
            boolean goalsHaveBrainstorming,
            String goalsIconPath)
    {
        this.id = id;
        this.descr = descr;
        this.goalsHaveProjects = goalsHaveProjects;
        this.goalsHaveStart = goalsHaveStart;
        this.goalsHaveEnd = goalsHaveEnd;
        this.goalsHaveVision = goalsHaveVision;
        this.goalsHaveAccountability = goalsHaveAccountability;
        this.goalsHaveRewards = goalsHaveRewards;
        this.goalsHaveObstacles = goalsHaveObstacles;
        this.goalsHaveSupport = goalsHaveSupport;
        this.goalsHaveBrainstorming = goalsHaveBrainstorming;
        this.goalsIconPath = goalsIconPath;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof LevelDTO) {
            return ((LevelDTO)that).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
