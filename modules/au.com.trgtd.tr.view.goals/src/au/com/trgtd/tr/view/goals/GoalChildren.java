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
package au.com.trgtd.tr.view.goals;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.view.goals.projects.GoalProjectsNode;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import tr.model.goals.ctrl.Ctrl;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.LevelCtrl;

public class GoalChildren extends Children.Keys<Object> implements PropertyChangeListener {

    private static class ProjectsKey extends Object {
    }
    private static ProjectsKey PROJECTS_KEY = new ProjectsKey();

    private final GoalCtrl goalCtrl;
    private LevelCtrl levelCtrl;
    private final OutlineView view;
    private final List keys;
    private boolean hideDone;
    private boolean showProjects;
    private IndexImpl indexImpl;

    public GoalChildren(OutlineView view, GoalCtrl goalCtrl) {
        assert (goalCtrl != null);
        this.view = view;
        this.goalCtrl = goalCtrl;
        this.keys = new ArrayList<>();
    }

    @Override
    protected void addNotify() {
        setKeys();
        goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_SUBGOAL_INSERT, this);
        goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_SUBGOAL_REMOVE, this);
        goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_SUBGOAL_DOWN, this);
        goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_SUBGOAL_UP, this);
        goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_SUBGOAL_REORDER, this);
        goalCtrl.addPropertyChangeListener(GoalCtrl.PROP_LEVEL, this);
        levelCtrl = goalCtrl.getLevel();
        if (levelCtrl != null) {
            levelCtrl.addPropertyChangeListener(LevelCtrl.PROP_PROJECTS, this);
        }
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        goalCtrl.removePropertyChangeListener(goalCtrl.PROP_SUBGOAL_INSERT, this);
        goalCtrl.removePropertyChangeListener(goalCtrl.PROP_SUBGOAL_REMOVE, this);
        goalCtrl.removePropertyChangeListener(goalCtrl.PROP_SUBGOAL_DOWN, this);
        goalCtrl.removePropertyChangeListener(goalCtrl.PROP_SUBGOAL_UP, this);
        goalCtrl.removePropertyChangeListener(goalCtrl.PROP_SUBGOAL_REORDER, this);
        if (levelCtrl != null) {
            levelCtrl.removePropertyChangeListener(LevelCtrl.PROP_PROJECTS, this);
        }
        keys.clear();
        setKeys(keys);
        super.removeNotify();
    }

    private boolean isRootGoal() {
        return goalCtrl.getID() == Constants.ID_ROOT_GOAL;
    }

    private boolean showLinkedProjects() {
        if (!showProjects || isRootGoal()) {
            return false;
        }
        return goalCtrl.getLevel().isGoalsHaveProjects();
    }

    void setKeys() {
        assert goalCtrl != null;
        assert goalCtrl.getSubgoals() != null;

        keys.clear();

        if (showLinkedProjects()) {
            keys.add(PROJECTS_KEY);
        }

        for (GoalCtrl subgoal : goalCtrl.getSubgoals()) {
            if (!hideDone || !subgoal.isAchieved()) {
                keys.add(subgoal);
            }
        }        

        setKeys(keys);
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof GoalCtrl ctrl) {
            return new Node[]{new GoalNode(view, goalCtrl, ctrl)};
        } else {
            return new Node[]{new GoalProjectsNode(goalCtrl)};
        }
    }

    void bump() {
        for (Node node : getNodes()) {
            if (node instanceof GoalNode goalNode) {
                goalNode.bump();
            }
            else if (node instanceof GoalProjectsNode goalProjectsNode) {
                goalProjectsNode.bump();
            }

        }
    }    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(GoalCtrl.PROP_LEVEL)) {
            if (levelCtrl != null) {
                levelCtrl.removePropertyChangeListener(LevelCtrl.PROP_PROJECTS, this);
            }
            levelCtrl = goalCtrl.getLevel();
            levelCtrl.addPropertyChangeListener(LevelCtrl.PROP_PROJECTS, this);
        }
        
        setKeys();
//      super.refresh();
    }

    public Index getIndex() {
        if (indexImpl == null) {
            indexImpl = new IndexImpl();
        }
        return indexImpl;
    }

    private class IndexImpl extends Index.Support {

        @Override
        public Node[] getNodes() {
            return GoalChildren.this.getNodes();
        }

        @Override
        public int getNodesCount() {
            return getNodes().length;
        }

        @Override
        public void moveDown(int idx) {
            if (idx < 0 || (idx + 1) > (keys.size() - 1)) {
                return;
            }
            Object src = keys.get(idx);
            Object dst = keys.get(idx + 1);
            if (!(src instanceof GoalCtrl) || !(dst instanceof GoalCtrl))  {
                return;
            }

            int row = view.getOutline().getSelectedRow();

            goalCtrl.moveAfterSubgoal(((Ctrl) src).getID(), ((Ctrl) dst).getID());

            view.getOutline().setRowSelectionInterval(row + 1, row + 1);
            fireChangeEvent();
        }

        @Override
        public void moveUp(int idx) {
            if (idx - 1 < 0 || idx > keys.size() - 1) {
                return;
            }
            Object src = keys.get(idx);
            Object dst = keys.get(idx - 1);
            if (!(src instanceof GoalCtrl) || !(dst instanceof GoalCtrl))  {
                return;
            }

            int row = view.getOutline().getSelectedRow();

            goalCtrl.moveBeforeSubgoal(((Ctrl) src).getID(), ((Ctrl) dst).getID());

            view.getOutline().setRowSelectionInterval(row - 1, row - 1);
            fireChangeEvent();
        }
        
        @Override
        public void reorder(int[] perm) {
            List<Integer> reorderedIDs = new ArrayList<>(perm.length);
            for (int index : perm ) {
                Object key = keys.get(index);
                if (key instanceof GoalCtrl ctrl) {
                    reorderedIDs.add(ctrl.getID());
                }
            }            
            goalCtrl.reorderSubgoals(reorderedIDs);
            fireChangeEvent();
        }

        public void fireChangeEvent() {
            fireChangeEvent(new ChangeEvent(IndexImpl.this));
        }
    }

    public boolean isHideDone() {
        return hideDone;
    }

    public void setHideDone(boolean hideAchieved) {
        if (this.hideDone != hideAchieved) {
            this.hideDone = hideAchieved;
            setKeys();
        }
    }

    public boolean isShowProjects() {
        return showProjects;
    }

    public void setShowProjects(boolean show) {
        if (this.showProjects != show) {
            this.showProjects = show;
            setKeys();
            if (show && getNodes().length > 0) {
                view.expandNode(getNodes()[0]);
            }
        }
    }
    
}
