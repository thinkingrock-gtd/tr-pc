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
package tr.model.goals.ctrl;

import java.util.List;

/**
 * Goals controller keeps a map of controllers for all goals (keyed
 * by goal ID number) and provides operations on the set of goals.
 *
 * @author Jeremy Moore
 */
public interface GoalsCtrl extends Ctrl {

    /** Property for data change events. */
    public static final String PROP_GOALS_DATA = "goals.data";

    /**
     * Gets the root goal controller.
     * @return The goal controller for the root goal.
     */
    public GoalCtrl getRootGoal();

    /**
     * Gets the goal controller for a givern goal ID.
     * @param goalID The goal ID.
     * @return The goal controller.
     */
    public GoalCtrl getGoalCtrl(Integer goalID);

    /**
     * Appends a goal controller to the list.
     * @param goalCtrl The goal controller.
     */
    public void append(GoalCtrl goalCtrl);

//    /**
//     * Removes a goal controller from the list.
//     * @param goalID The goal ID.
//     */
//    public void remove(Integer goalID);


    public void move(Integer moveGoalID, Integer oldSupergoalID, Integer newSupergoalID);
    
    /**
     * Deletes the given goal controller.
     * @param supergoalID The ID of the super-goal.
     * @param goalID The ID of the goal to delete.
     */
    public void delete(Integer supergoalID, Integer goalID);
    
    public List<GoalCtrl> getSubgoalCtrls(Integer goalID);

    public List<GoalCtrl> getProjectGoals(Integer projectID);

    public Integer[] getGoalProjects(Integer goalID);

    public boolean insertGoalProject(Integer goalID, Integer projectID);
    
    public boolean removeGoalProject(Integer goalID, Integer projectID);

    /**
     * Determines whether there are goals of the given level that have projects
     * assigned.
     * @param level The level controller.
     * @return true if any are found.
     */
    public boolean existGoalProjects(LevelCtrl level);

    /**
     * Determines whether there the given goal has any projects assigned.
     * @param goal The goal controller.
     * @return true if any are found.
     */
    public boolean existGoalProjects(GoalCtrl goal);

    /**
     * Removes all project links to goals which have the given level.
     * @param level The level controller.
     */
    public void removeProjects(LevelCtrl level);

}
