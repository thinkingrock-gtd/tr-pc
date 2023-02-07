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
package au.com.trgtd.tr.view.goals.projects;

import au.com.trgtd.tr.services.Services;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.project.Project;

public class GoalProjectChildren extends Children.Keys<Project> implements PropertyChangeListener {

    private final GoalCtrl goal;
    private boolean hideDone;

    public GoalProjectChildren(GoalCtrl goal) {
        assert(goal != null);
        this.goal = goal;
    }

    @Override
    protected void addNotify() {
        setKeys();
        goal.addPropertyChangeListener(GoalCtrl.PROP_PROJECT_LINKS, this);
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        goal.removePropertyChangeListener(GoalCtrl.PROP_PROJECT_LINKS, this);
        setKeys(Collections.EMPTY_SET);
        super.removeNotify();
    }

    private void setKeys() {
        GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
        if (goalsCtrl == null) {
            setKeys(Collections.EMPTY_LIST);
            return;
        }
        Integer[] goalProjectIDs = goalsCtrl.getGoalProjects(goal.getID());
        if (goalProjectIDs == null || goalProjectIDs.length == 0) {
            setKeys(Collections.EMPTY_LIST);
            return;
        }

        java.util.Map<Integer, Project> projectsMap = new HashMap<>();
        for (Project project : Services.instance.getProjects()) {
            projectsMap.put(project.getID(), project);
        }

        ArrayList<Project> goalProjects = new ArrayList<>(goalProjectIDs.length);
        for (Integer id : goalProjectIDs) {
            Project project = projectsMap.get(id);
            if (project != null) {
                if (!project.isDone() || !hideDone) {
                    goalProjects.add(project);
                }
            }
        }

        setKeys(goalProjects);
    }

    @Override
    protected Node[] createNodes(Project project) {
        return new Node[] {new GoalProjectNode(project, goal)};
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setKeys();
    }

    public boolean isHideDoneProjects() {
        return hideDone;
    }

    public void setHideDoneProjects(boolean hide) {
        if (this.hideDone != hide) {
            this.hideDone = hide;
            setKeys();
            super.refresh();
        }
    }

    public void bump() {
        setKeys();
    }

}
