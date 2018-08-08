package tr.model.goals.dto;

public class GoalSubgoalDTO {

    public final Integer goalID;
    public final Integer subgoalID;

    public GoalSubgoalDTO(Integer goalID, Integer subgoalID) {
        this.goalID = goalID;
        this.subgoalID = subgoalID;
    }

}
