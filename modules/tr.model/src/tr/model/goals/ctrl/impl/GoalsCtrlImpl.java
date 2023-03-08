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
package tr.model.goals.ctrl.impl;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.util.Utils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.dao.GoalsDAO;
import tr.model.goals.dao.GoalsDAOLookup;
import tr.model.goals.dto.GoalDTO;

public class GoalsCtrlImpl extends CtrlImpl implements GoalsCtrl, PropertyChangeListener {

    private static final Object syncObject = new Object();
    private static GoalsCtrlImpl instance;

    public static GoalsCtrlImpl getDefault() {
        synchronized (syncObject) {
            if (instance == null) {
                instance = new GoalsCtrlImpl();
            }
            return instance;
        }
    }


    private final GoalsDAO goalsDAO;
    private final Map<Integer, GoalCtrl> goalsMap;
    private GoalCtrl rootGoalCtrl;

    private GoalsCtrlImpl() {
        super(Constants.ID_ROOT_GOAL);
        goalsMap = new HashMap<>();
        goalsDAO = GoalsDAOLookup.getGoalsDAO();
        goalsDAO.addPropertyChangeListener(GoalsDAO.PROP_DATA, this);
        initialise();
    }

    @Override
    public GoalCtrl getRootGoal() {
        synchronized (this) {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return null;
            }
            if (rootGoalCtrl == null) {
                rootGoalCtrl = goalsMap.get(Constants.ID_ROOT_GOAL);
            }
            return rootGoalCtrl;
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(GoalsCtrlImpl.class, "goals");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        initialise();
        firePropertyChange(PROP_GOALS_DATA, 0, 1);
    }

    private void initialise() {
        synchronized (this) {
            rootGoalCtrl = null;
            goalsMap.clear();
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return;
            }
            for (GoalDTO goalDTO : goalsDAO.getGoals()) {
                goalsMap.put(goalDTO.id, new GoalCtrlImpl(goalDTO));
            }
            rootGoalCtrl = goalsMap.get(Constants.ID_ROOT_GOAL);
        }
    }

    @Override
    public List<GoalCtrl> getSubgoalCtrls(Integer goalID) {
        synchronized (this) {
            Vector<GoalCtrl> subgoalCtrls = new Vector<>();
            for (Integer subgoalID : goalsDAO.getSubGoalIDs(goalID)) {
                subgoalCtrls.add(goalsMap.get(subgoalID));
            }
            return subgoalCtrls;
        }
    }

    @Override
    public GoalCtrl getGoalCtrl(Integer goalID) {
        synchronized (this) {
            return goalsMap.get(goalID);
        }
    }

    @Override
    public List<GoalCtrl> getProjectGoals(Integer projectID) {
        synchronized (this) {
            Vector<GoalCtrl> goalCtrls = new Vector<>();
            for (Integer goalID : goalsDAO.getGoalIDs(projectID)) {
                goalCtrls.add(goalsMap.get(goalID));
            }
            return goalCtrls;
        }
    }

    @Override
    public Integer[] getGoalProjects(Integer goalID) {
        return goalsDAO.getProjectIDs(goalID);
    }

    @Override
    public boolean insertGoalProject(Integer goalID, Integer projectID) {
        return goalsDAO.insertGoalProject(goalID, projectID);
    }

    @Override
    public boolean removeGoalProject(Integer goalID, Integer projectID) {
        return goalsDAO.removeGoalProject(goalID, projectID);
    }

    private boolean removeAllGoalProjects(Integer goalID) {
        Integer[] projectIDs = goalsDAO.getProjectIDs(goalID);
        if (projectIDs == null || projectIDs.length == 0) {
            return false;
        }
        boolean dataChanged = false;
        for (Integer projectID : projectIDs) {
            boolean changed = getGoalCtrl(goalID).removeGoalProject(projectID);
            dataChanged = dataChanged || changed;
        }
        return dataChanged;
    }

    /**
     * Appends the given goal controller to the list of goals.
     * @param goalCtrl The new goal controller.
     */
    @Override
    public void append(GoalCtrl goalCtrl) {
        if (goalCtrl == null) {
            return;
        }
        synchronized (this) {
            goalsMap.put(goalCtrl.getID(), goalCtrl);
        }
    }



//    /**
//     * Removes the given goal controller from the list of goals.
//     * @param goalCtrl The new goal controller.
//     */
//    @Override
//    public void remove(Integer goalCtrlID) {
//        if (goalCtrlID == null) {
//            return;
//        }
//        synchronized (this) {
//            GoalCtrl removedGoal = goalsMap.remove(goalCtrlID);
//            if (removedGoal != null) {
//                removedGoal.notifyDeleted();
//            }
//        }
//    }



//    void remove(Integer subgoalID, Integer goalID) {
//        goalsDAO.removeGoal(subgoalID, goalID);
//        initialise();
//    }


    /**
     * Deletes the given goal controller.
     * @param supergoalID The ID of the super-goal.
     * @param goalID The ID of the goal to delete.
     */
    @Override
    public void delete(Integer supergoalID, Integer goalID) {
        if (supergoalID == null || goalID == null) {
            return;
        }
        synchronized (this) {
            GoalCtrl supergoal = goalsMap.get(supergoalID);
            GoalCtrl goal = goalsMap.get(goalID);
            if (supergoal == null || goal == null) {
                return;
            }
            goalsDAO.removeGoal(supergoalID, goalID);
            goalsMap.remove(goalID);

            goal.notifyDeleted();
            supergoal.notifySubgoalRemoved(goalID);
        }
    }

    @Override
    public void move(Integer goalID, Integer oldSupergoalID, Integer newSupergoalID) {
        if (goalID == null || oldSupergoalID == null || newSupergoalID == null) {
            return;
        }
        if (oldSupergoalID == newSupergoalID) {
            return;
        }
        GoalCtrl oldSupergoal = goalsMap.get(oldSupergoalID);
        if (oldSupergoal == null) {
            return;
        }
        GoalCtrl newSupergoal = goalsMap.get(newSupergoalID);
        if (newSupergoal == null) {
            return;
        }

        goalsDAO.moveGoal(goalID, oldSupergoalID, newSupergoalID);

        oldSupergoal.notifySubgoalRemoved(goalID);
        newSupergoal.notifySubgoalInserted(goalID);
    }

    private List<GoalCtrl> getGoals(Integer levelID) {
        List<GoalCtrl> goals = new ArrayList<>();
        for (GoalCtrl g : goalsMap.values()) {
            if (g.getLevelID().equals(levelID)) {
                goals.add(g);
            }
        }
        return goals;
    }

    @Override
    public boolean existGoalProjects(LevelCtrl level) {
        for (GoalCtrl goalCtrl : goalsMap.values()) {
            if (Utils.equal(goalCtrl.getLevelID(), level.getID())) {
                if (existGoalProjects(goalCtrl)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean existGoalProjects(GoalCtrl goal) {
        Integer[] projectIDs = goalsDAO.getProjectIDs(goal.getID());
        return projectIDs != null && projectIDs.length > 0;
    }

    @Override
    public void removeProjects(LevelCtrl level) {
        for (GoalCtrl goalCtrl : goalsMap.values()) {
            if (Utils.equal(goalCtrl.getLevelID(), level.getID())) {
                if (existGoalProjects(goalCtrl)) {
                    removeAllGoalProjects(goalCtrl.getID());
                }
            }
        }
    }

}
