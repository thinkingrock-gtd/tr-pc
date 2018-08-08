package au.com.trgtd.tr.view.goals.chooser;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.LevelCtrl;

public class ChooserGoalChildren extends Children.Keys<GoalCtrl> {

    private final GoalCtrl goal;
    private final boolean showAll;
    private final AbstractButton selectButton;

    public ChooserGoalChildren(GoalCtrl goal, AbstractButton selectButton, boolean showAll) {
        this.goal = goal;
        this.showAll = showAll;
        this.selectButton = selectButton;
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        setKeys();
    }

    @Override
    protected Node[] createNodes(GoalCtrl subgoal) {
        return new Node[] {new ChooserGoalNode(subgoal, selectButton, showAll)};
    }

    public void setKeys() {
        List<GoalCtrl> keys = new ArrayList<GoalCtrl>();
        for (GoalCtrl subgoal : goal.getSubgoals()) {
            if (showGoal(subgoal)) {
                keys.add(subgoal);
            }
        }
        setKeys(keys);
    }

    private boolean showGoal(GoalCtrl goal) {
        if (goal == null) {
            return false;
        }
        if (goal.isAchieved()) {
            // assume subgoals are achieved as well
            return false;
        }
        if (showAll) {
            return true;
        }
        LevelCtrl level = goal.getLevel();
        if (level != null && level.isGoalsHaveProjects()) {
            return true;
        }
        return hasSubgoalsToShow(goal);
    }

    private boolean hasSubgoalsToShow(GoalCtrl goal) {
        for (GoalCtrl subgoal : goal.getSubgoals()) {
            if (showGoal(subgoal)) {
                return true;
            }
        }
        return false;
    }

}
