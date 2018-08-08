package tr.model.goals.data;

/**
 * Records the fact that a project has a goal in the many to many relationship
 * between projects and goals.
 *
 * @author Jeremy Moore
 */
public class ProjectGoal {

    public final int goalID;
    public final int projectID;

    public ProjectGoal(int goalID, int projectID) {
        this.goalID = goalID;
        this.projectID = projectID;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProjectGoal) {
            ProjectGoal that = (ProjectGoal)object;
            return that.goalID == this.goalID && that.projectID == this.projectID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.goalID;
        hash = 73 * hash + this.projectID;
        return hash;
    }

}
