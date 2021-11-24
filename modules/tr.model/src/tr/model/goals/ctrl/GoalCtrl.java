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

import java.util.Date;
import java.util.List;
import tr.model.Item.Item;
import tr.model.Item.Notable;
import tr.model.topic.Topic;

/**
 * Goal controller.
 * 
 * @author Jeremy Moore
 */
public interface GoalCtrl extends Ctrl, Notable, Item {

    @Override
    public int getID();
    public Integer getLevelID();
    public LevelCtrl getLevel();
    public Integer getTopicID();
    public Topic getTopic();
    public String getDescr();
    public String getBrainstorming();
    public String getVision();
    public String getSupport();
    public String getRewards();
    public String getObstacles();
    public String getAccountability();
    @Override
    public String getNotes();
    public Date getCreatedDate();
    public Date getStartDate();
    public Date getEndDate();
    public Date getAchievedDate();
    public boolean isAchieved();
    public String getPath();

    public void setLevel(LevelCtrl level);
    public void setTopic(Topic topic);
    public void setDescr(String value);
    public void setBrainstorming(String value);
    public void setVision(String value);
    public void setSupport(String value);
    public void setRewards(String value);
    public void setObstacles(String value);
    public void setAccountability(String value);
    @Override
    public void setNotes(String value);
    public void setStartDate(Date date);
    public void setEndDate(Date date);
    public void setAchievedDate(Date date);

    /**
     * Determines whether the goal has any subgoals.
     * @return true if the goal has subgoals, false otherwise.
     */
    public boolean hasSubgoals();

    /**
     * Gets the subgoals of the goal.
     * @return The list of subgoal controllers.
     */
    public List<GoalCtrl> getSubgoals();

//    /**
//     * Moves a subgoal up in the order.
//     * @param index The index of the subgoal to move.
//     */
//    public void moveUpSubgoal(int index);
//
//    /**
//     * Moves a subgoal down in the order.
//     * @param index The index of the subgoal to move.
//     */
//    public void moveDownSubgoal(int index);

//    public void moveBeforeSubgoal(int srcIndex, int dstIndex);
//    public void moveAfterSubgoal(int srcIndex, int dstIndex);

    /**
     * Moves the source subgoal before the destination subgoal. 
     * @param srcSubgoalID The ID of the source subgoal.
     * @param dstSubgoalID The ID of the destination subgoal.
     */
    public void moveBeforeSubgoal(Integer srcSubgoalID, Integer dstSubgoalID);

    /**
     * Moves the source subgoal after the destination subgoal.
     * @param srcSubgoalID The ID of the source subgoal.
     * @param dstSubgoalID The ID of the destination subgoal.
     */
    public void moveAfterSubgoal(Integer srcSubgoalID, Integer dstSubgoalID);

    /**
     * Reorders subgoals into the given list order.
     * @param subgoalIDs A list of subgoal IDs in the new order.
     */
    public void reorderSubgoals(List<Integer> subgoalIDs);

    /**
     * Determines whether or not this goal is a descendent of a given goal.
     * @param goalID The possible ancestor goal ID.
     */
    public boolean isDecendantOf(Integer goalID);

    /**
     * Determines whether or not this goal is the parent (super-goal) of a given
     * goal.
     * @param goalID The possible child goal ID.
     */
    public boolean isParentOf(Integer goalID);

    /**
     * Copies the goal and all subgoals to the given destination super-goal.
     * @param dstSuperGoal The goal which will be the super-goal of the copy of
     * this goal.
     */
    public void copyTo(GoalCtrl dstSuperGoal);

//    /**
//     * Moves the goal from the source super-goal to the destination super-goal.
//     * @param srcSupergoal The source super-goal controller.
//     * @param dstSupergoal The destination super-goal controller.
//     */
//    public void move(GoalCtrl srcSupergoal, GoalCtrl dstSupergoal);

    /**
     * Inserts a new subgoal in the data-store.
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
     * @param start
     * @param end
     * @param achieved
     * @return A goal controller for the new goal.
     */
    public GoalCtrl insert(
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
            Date start,
            Date end,
            Date achieved);

    /**
     * Inserts an existing subgoal in the data-store.
     * @param goalCtrl The goal controller of the goal to insert.
     */
    public void insert(GoalCtrl goalCtrl);

//    /**
//     * Removes a subgoal from this goal and if the goal has no other super-goals,
//     * it is removed from the data-store.
//     * @param subgoalID The subgoal ID.
//     */
//    public void remove(Integer subgoalID);

    /**
     * Notify observers that this goal has been deleted.
     */
    public void notifyDeleted();

    /**
     * Notify observers that a subgoal has been removed.
     */
    public void notifySubgoalRemoved(Integer subgoalID);

    /**
     * Notify observers that a subgoal has been inserted.
     */
    public void notifySubgoalInserted(Integer subgoalID);
    

    public boolean insertGoalProject(Integer projectID);

    public boolean removeGoalProject(Integer projectID);

    public boolean removeGoalProjects();
    
    /**
     * Updates the goal in the data-store.
     */
    public void update();
    
    public final static String PROP_LEVEL = "level";
    public final static String PROP_TOPIC = "topic";
    public final static String PROP_DESCR = "descr";
    public final static String PROP_BRAINSTORMING = "brainstorming";
    public final static String PROP_VISION = "vision";
    public final static String PROP_SUPPORT = "support";
    public final static String PROP_REWARDS = "rewards";
    public final static String PROP_OBSTACLES = "obstacles";
    public final static String PROP_ACCOUNTABILITY = "accountability";
    public final static String PROP_NOTES = "notes";
    public final static String PROP_START_DATE = "start.date";
    public final static String PROP_END_DATE = "end.date";
    public final static String PROP_ACHIEVED_DATE = "achieved.date";

    public static final String PROP_GOAL_DELETE = "goal.delete";
    public static final String PROP_GOAL_UPDATE = "goal.update";
    public final static String PROP_SUBGOAL_INSERT = "subgoal.insert";
    public final static String PROP_SUBGOAL_REMOVE = "subgoal.remove";
    public final static String PROP_SUBGOAL_UP = "subgoal.up";
    public final static String PROP_SUBGOAL_DOWN = "subgoal.down";
    public final static String PROP_SUBGOAL_REORDER = "subgoal.reorder";

    public final static String PROP_PROJECT_LINKS = "project.links";
}
