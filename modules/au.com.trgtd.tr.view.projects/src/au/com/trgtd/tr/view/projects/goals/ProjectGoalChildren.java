package au.com.trgtd.tr.view.projects.goals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.project.Project;

public class ProjectGoalChildren extends Children.Keys<GoalCtrl> implements PropertyChangeListener {

    private final Project project;
    private boolean hideAchieved;
    private Vector<Integer> realIdxs;

    public ProjectGoalChildren(Project project) {
        assert(project != null);
        this.project = project;
        this.realIdxs = new Vector<Integer>();
    }

    @Override
    protected void addNotify() {
        setKeys();
        project.addPropertyChangeListener(Project.PROP_GOAL_LINKS, this);
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        project.removePropertyChangeListener(Project.PROP_GOAL_LINKS, this);
        setKeys(Collections.EMPTY_SET);
        super.removeNotify();
    }

    private void setKeys() {
        realIdxs.clear();
        GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
        if (goalsCtrl == null) {
            setKeys(Collections.EMPTY_LIST);
        }
        List<GoalCtrl> projectGoals = goalsCtrl.getProjectGoals(project.getID());
        setKeys(projectGoals);
    }

    @Override
    protected Node[] createNodes(GoalCtrl goalCtrl) {
        return new Node[] {new ProjectGoalNode(project, goalCtrl)};
    }

    public void propertyChange(PropertyChangeEvent evt) {
        setKeys();
    }

    public boolean isHideAchieved() {
        return hideAchieved;
    }

    public void setHideAchieved(boolean hideAchieved) {
        if (this.hideAchieved != hideAchieved) {
            this.hideAchieved = hideAchieved;
            setKeys();
            super.refresh();
        }
    }
}
