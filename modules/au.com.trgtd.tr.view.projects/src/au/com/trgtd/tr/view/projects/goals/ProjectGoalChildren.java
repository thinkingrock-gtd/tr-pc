/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
        this.realIdxs = new Vector<>();
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
