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
package tr.model.goals.dao;

import java.util.Date;
import tr.model.goals.dto.GoalDTO;

/**
 * Goals data access object.
 *
 * @author Jeremy Moore
 */
public interface GoalsDAO extends DAO {

    /** Property for data change events. */
    public final static String PROP_DATA = "goals.data";

    /**
     * Gets the root goal.
     * @return The root goal DTO.
     */
    public GoalDTO getRootGoal();

    /**
     * Gets the goal for the given ID.
     * @param id The goal ID.
     * @return The goal DTO.
     */
    public GoalDTO getGoal(Integer id);

    /**
     * Gets all goals.
     * @return The goals DTOs.
     */
    public GoalDTO[] getGoals();

    /**
     * Determines whether a goal has any subgoals.
     * @param GoalID The goal ID.
     * @return true if the goal has subgoals, otherwise false.
     */
    public boolean hasSubgoals(Integer goalID);

    /**
     * Gets the subgoals of a goal.
     * @param GoalID The goal ID.
     * @return The subgoals DTOs.
     */
    public GoalDTO[] getSubgoals(Integer goalID);



    public Integer[] getSubGoalIDs(Integer goalID);

    

    /**
     * Gets the goals of a project.
     * @param projectID The project ID.
     * @return The goal IDs.
     */
    public Integer[] getGoalIDs(int projectID);

    /**
     * Gets the projects of a goal.
     * @param goalID The goal ID.
     * @return The project IDs.
     */
    public Integer[] getProjectIDs(int goalID);

    /**
     * Determines whether or not a goal is a descendent of another goal.
     * @param decendantGoalID The possible descendent goal ID.
     * @param ancestorGoalID The possible ancestor goal ID.
     */
    public boolean isDecendantOf(Integer decendantGoalID, Integer ancestorGoalID);

    /**
     * Determines whether or not a goal is a parent of another goal.
     * @param parentGoalID The possible parent goal ID.
     * @param childGoalID The possible child goal ID.
     */
    public boolean isParentOf(Integer parentGoalID, Integer childGoalID);
    
//    /**
//     * Moves a subgoal up in the order.
//     * @param GoalID The goal ID.
//     * @param subgoalIndex The index of the subgoal to move.
//     */
//    public void moveUpSubgoal(Integer goalID, int subgoalIndex);
//
//    /**
//     * Moves a subgoal down in the order.
//     * @param GoalID The goal ID.
//     * @param subgoalIndex The index of the subgoal to move.
//     */
//    public void moveDownSubgoal(Integer goalID, int subgoalIndex);

//    public void moveBeforeSubgoal(Integer goalID, int srcIndex, int dstIndex);
//    public void moveAfterSubgoal(Integer goalID, int srcIndex, int dstIndex);

    public void moveBeforeSubgoal(Integer goalID, Integer srcSubgoalID, Integer dstSubgoalID);

    public void moveAfterSubgoal(Integer goalID, Integer srcSubgoalID, Integer dstSubgoalID);
    
//    /**
//     * Reorders subgoals.
//     * @param goalID The goal ID.
//     * @param perm The permutation of subgoal indexes.
//     */
//    public void reorderSubgoals(Integer goalID, int[] perm);

    /**
     * Insert a new goal.
     * @param supergoalID
     * @param levelID
     * @param topicID
     * @param descr
     * @param vision
     * @param accountability
     * @param rewards
     * @param obstacles
     * @param support
     * @param brainstorming
     * @param notes
     * @param created
     * @param start
     * @param end
     * @param achieved
     * @return The goal DTO.
     */
    public GoalDTO insertGoal(
            Integer supergoalID,
            Integer levelID,
            Integer topicID,
            String descr,
            String vision,
            String accountability,
            String rewards,
            String obstacles,
            String support,
            String brainstorming,
            String notes,
            Date created,
            Date start,
            Date end,
            Date achieved);

    /**
     * insert an existing goal into a super-goal.
     * @param supergoalID The super-goal ID.
     * @param goalID The goal ID.
     */
    public void insertGoal(Integer supergoalID, Integer goalID);

    /**
     * Remove an existing goal from a super-goal.
     * @param supergoalID The super-goal ID.
     * @param goalID The goal ID.
     */
    public void removeGoal(Integer supergoalID, Integer goalID);

    /**
     * Move an existing goal from old super-goal to new super-goal.
     * @param goalID The ID of the goal to move.
     * @param oldSupergoalID The ID of the old (current) super-goal.
     * @param newSupergoalID The ID of the new super-goal.
     */
    public void moveGoal(Integer goalID, Integer oldSupergoalID, Integer newSupergoalID);
    
    /**
     * Update a goal.
     * @param goalDTO The updated goal DTO.
     */
    public void updateGoal(GoalDTO goalDTO);

    /**
     * Gets the goal path.
     * @param goalID The goal.
     * @return The goal path.
     */
    public String getPath(Integer goalID);

    /**
     * inserts link between a goal and a project.
     * @param goalID The goal ID.
     * @param projectID The project ID.
     * @return true if successful and data has changed;
     */
    public boolean insertGoalProject(Integer goalID, Integer projectID);

    /**
     * Removes link between a goal and a project.
     * @param goalID The goal ID.
     * @param projectID The project ID.
     * @return true if successful and data has changed.
     */
    public boolean removeGoalProject(Integer goalID, Integer projectID);


}
