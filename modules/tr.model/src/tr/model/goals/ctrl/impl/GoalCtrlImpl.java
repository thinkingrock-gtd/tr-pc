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

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;
import tr.model.IDGenerator;
import tr.model.Item.Item;
import tr.model.Item.ItemList;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;
import tr.model.goals.dao.GoalsDAO;
import tr.model.goals.dao.GoalsDAOLookup;
import tr.model.goals.dto.GoalDTO;
import tr.model.project.Project;
import tr.model.topic.Topic;
import tr.model.topic.TopicMap;

public class GoalCtrlImpl extends CtrlImpl implements GoalCtrl {

    private Date created;
    private Integer levelID;
    private Integer topicID;
    private String descr;
    private String vision;
    private String accountability;
    private String rewards;
    private String obstacles;
    private String support;
    private String brainstorming;
    private String notes;
    private Date start;
    private Date end;
    private Date achieved;

    public GoalCtrlImpl(GoalDTO goalDTO) {
        super(goalDTO.id);
        initModel(goalDTO);
    }

    @Override
    public String getName() {
        return getDescr();
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof GoalCtrl goalCtrl) {
            return goalCtrl.getID() == this.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getID();
    }

    @Override
    public String toString() {
        return getDescr();
    }

    @Override
    public Integer getLevelID() {
        return levelID;
    }

    @Override
    public LevelCtrl getLevel() {
        return getLevelsCtrl().getLevel(getLevelID());
    }

    @Override
    public Integer getTopicID() {
        return topicID;
    }

    @Override
    public Topic getTopic() {
        return this.getTopicMap().getTopic(getTopicID());
    }

    @Override
    public String getDescr() {
        return toString(descr);
    }

    @Override
    public String getBrainstorming() {
        return toString(brainstorming);
    }

    @Override
    public String getVision() {
        return toString(vision);
    }

    @Override
    public String getSupport() {
        return toString(support);
    }

    @Override
    public String getRewards() {
        return toString(rewards);
    }

    @Override
    public String getObstacles() {
        return toString(obstacles);
    }

    @Override
    public String getAccountability() {
        return toString(accountability);
    }

    @Override
    public String getNotes() {
        return toString(notes);
    }

    @Override
    public Date getCreatedDate() {
        return created;
    }

    @Override
    public Date getStartDate() {
        return start;
    }

    @Override
    public Date getEndDate() {
        return end;
    }

    @Override
    public Date getAchievedDate() {
        return achieved;
    }

    @Override
    public void setLevel(LevelCtrl newValue) {
        LevelCtrl oldValue = getLevel();
        if (newValue == null) {
            levelID = null;
        } else {
            levelID = newValue.getID();
        }
        firePropertyChange(PROP_LEVEL, oldValue, newValue);
    }

    @Override
    public void setTopic(Topic newValue) {
        Topic oldValue = getTopic();
        if (newValue == null) {
            topicID = null;
        } else {
            topicID = newValue.getID();
        }
        firePropertyChange(PROP_TOPIC, oldValue, newValue);
    }

    @Override
    public void setDescr(String newValue) {
        String oldValue = getDescr();
        descr = newValue;
        firePropertyChange(PROP_DESCR, oldValue, newValue);
    }

    @Override
    public void setBrainstorming(String newValue) {
        String oldValue = getBrainstorming();
        brainstorming = newValue;
        firePropertyChange(PROP_BRAINSTORMING, oldValue, newValue);
    }

    @Override
    public void setVision(String newValue) {
        String oldValue = getVision();
        vision = newValue;
        firePropertyChange(PROP_VISION, oldValue, newValue);
    }

    @Override
    public void setSupport(String newValue) {
        String oldValue = getSupport();
        support = newValue;
        firePropertyChange(PROP_SUPPORT, oldValue, newValue);
    }

    @Override
    public void setRewards(String newValue) {
        String oldValue = getRewards();
        rewards = newValue;
        firePropertyChange(PROP_REWARDS, oldValue, newValue);
    }

    @Override
    public void setObstacles(String newValue) {
        String oldValue = getObstacles();
        obstacles = newValue;
        firePropertyChange(PROP_OBSTACLES, oldValue, newValue);
    }

    @Override
    public void setAccountability(String newValue) {
        String oldValue = getAccountability();
        accountability = newValue;
        firePropertyChange(PROP_ACCOUNTABILITY, oldValue, newValue);
    }

    @Override
    public void setNotes(String newValue) {
        String oldValue = getNotes();
        notes = newValue;
        firePropertyChange(PROP_NOTES, oldValue, newValue);
    }

    @Override
    public void setStartDate(Date newValue) {
        Date oldValue = getStartDate();
        start = newValue;
        firePropertyChange(PROP_START_DATE, oldValue, newValue);
    }

    @Override
    public void setEndDate(Date newValue) {
        Date oldValue = getEndDate();
        end = newValue;
        firePropertyChange(PROP_END_DATE, oldValue, newValue);
    }

    @Override
    public boolean isAchieved() {
        return getAchievedDate() != null;
    }

    @Override
    public void setAchievedDate(Date newValue) {
        Date oldValue = getAchievedDate();
        if (Utils.equal(oldValue, newValue)) {
            return;
        }
        if (!isAchieved() && !areSubgoalsAchieved(this)) {
            String msg = NbBundle.getMessage(getClass(), "msg.confirm.set.achieved");
            String ttl = NbBundle.getMessage(getClass(), "ttl.confirm.set.achieved");
            NotifyDescriptor descriptor = new NotifyDescriptor.Confirmation(msg, ttl, NotifyDescriptor.OK_CANCEL_OPTION);
            if (NotifyDescriptor.OK_OPTION != DialogDisplayer.getDefault().notify(descriptor)) {
                return;
            }
            setSubgoalsAchieved(this, newValue);
        }
        achieved = newValue;
        firePropertyChange(PROP_ACHIEVED_DATE, oldValue, newValue);
    }

    @Override
    public boolean hasSubgoals() {
        return getGoalsDAO().hasSubgoals(getID());
    }

    @Override
    public List<GoalCtrl> getSubgoals() {
        return getGoalsCtrl().getSubgoalCtrls(getID());
    }

    @Override
    public void moveBeforeSubgoal(Integer srcSubgoalID, Integer dstSubgoalID) {
        getGoalsDAO().moveBeforeSubgoal(getID(), srcSubgoalID, dstSubgoalID);
        firePropertyChange(PROP_SUBGOAL_UP, null, this);
    }

    @Override
    public void moveAfterSubgoal(Integer srcSubgoalID, Integer dstSubgoalID) {
        getGoalsDAO().moveAfterSubgoal(getID(), srcSubgoalID, dstSubgoalID);
        firePropertyChange(PROP_SUBGOAL_DOWN, null, this);
    }

    @Override
    public void reorderSubgoals(List<Integer> subgoalIDs) {
        for (int i = 0; i < subgoalIDs.size() - 1; i++) {
            getGoalsDAO().moveAfterSubgoal(getID(), subgoalIDs.get(i+1), subgoalIDs.get(i));
        }
        firePropertyChange(PROP_SUBGOAL_REORDER, null, this);
    }

    @Override
    public boolean isDecendantOf(Integer goalID) {
        return getGoalsDAO().isDecendantOf(getID(), goalID);
    }

    @Override
    public boolean isParentOf(Integer goalID) {
        return getGoalsDAO().isParentOf(getID(), goalID);
    }

    @Override
    public void update() {
        getGoalsDAO().updateGoal(toGoalDTO());
        firePropertyChange(PROP_GOAL_UPDATE, null, this);
    }

    @Override
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
            Date achieved) {

        if (descr == null || descr.trim().length() == 0) {
            throw new IllegalArgumentException("Description must be entered.");
        }
        if (levelID == null) {
            throw new IllegalArgumentException("Level must be entered.");
        }

        GoalDTO subgoalDTO = getGoalsDAO().insertGoal(
                getID(),
                levelID,
                topicID,
                descr,
                vision,
                accountability,
                rewards,
                obstacles,
                support,
                brainstorming,
                notes,
                new Date(),
                start,
                end,
                achieved);

        GoalCtrl subgoalCtrl = new GoalCtrlImpl(subgoalDTO);

        getGoalsCtrl().append(subgoalCtrl);

        firePropertyChange(PROP_SUBGOAL_INSERT, null, subgoalCtrl);

        return subgoalCtrl;
    }

    @Override
    public void insert(GoalCtrl subgoalCtrl) {

        getGoalsDAO().insertGoal(getID(), subgoalCtrl.getID());

        getGoalsCtrl().append(subgoalCtrl);

        firePropertyChange(PROP_SUBGOAL_INSERT, null, subgoalCtrl);
    }

//    @Override
//    public void remove(Integer subgoalID) {
//////////getGoalsDAO().removeGoal(getID(), subgoalID);
//////////getGoalsCtrl().remove(subgoalID);
//        ((GoalsCtrlImpl)getGoalsCtrl()).remove(getID(), subgoalID);
//        firePropertyChange(PROP_SUBGOAL_REMOVE, null, subgoalID);
//    }

    @Override
    public String getPath() {
        return getGoalsDAO().getPath(getID());
    }

    @Override
    public void copyTo(GoalCtrl dstSuperGoal) {
        if (dstSuperGoal == null) {
            return;
        }
        GoalCtrl copyGoalCtrl = dstSuperGoal.insert(
                levelID,
                topicID,
                descr,
                vision,
                accountability,
                rewards,
                obstacles,
                support,
                brainstorming,
                notes,
                start,
                end,
                achieved);

        for (GoalCtrl subgoal : getSubgoals()) {
            subgoal.copyTo(copyGoalCtrl);
        }
    }


//
//    public void move(GoalCtrl srcSupergoal, GoalCtrl dstSupergoal) {
//        if (srcSupergoal == null || dstSupergoal == null) {
//            return;
//        }
//        vv
//        dstSupergoal.insert(this);
//        srcSupergoal.remove(this.getID());
//
//    }

    // PRIVATE
    private GoalsDAO goalsDAO;
    private GoalsCtrl goalsCtrl;
    private LevelsCtrl levelsCtrl;
    private TopicMap topicMap;

    private GoalsDAO getGoalsDAO() {
        if (goalsDAO == null) {
            goalsDAO = GoalsDAOLookup.getGoalsDAO();
        }
        return goalsDAO;
    }

    private GoalsCtrl getGoalsCtrl() {
        if (goalsCtrl == null) {
            goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
        }
        return goalsCtrl;
    }

    private LevelsCtrl getLevelsCtrl() {
        if (levelsCtrl == null) {
            levelsCtrl = LevelsCtrlLookup.getLevelsCtrl();
        }
        return levelsCtrl;
    }

    private TopicMap getTopicMap() {
        if (topicMap == null) {
            topicMap = TopicMap.getDefault();
        }
        return topicMap;
    }

    private void initModel(GoalDTO goalDTO) {
        created = goalDTO.created;
        levelID = goalDTO.levelID;
        topicID = goalDTO.topicID;
        descr = goalDTO.descr;
        vision = goalDTO.vision;
        accountability = goalDTO.accountability;
        rewards = goalDTO.rewards;
        obstacles = goalDTO.obstacles;
        support = goalDTO.support;
        brainstorming = goalDTO.brainstorming;
        notes = goalDTO.notes;
        start = goalDTO.start;
        end = goalDTO.end;
        achieved = goalDTO.achieved;
    }

    private GoalDTO toGoalDTO() {
        return new GoalDTO(
                getID(),
                getLevelID(),
                getTopicID(),
                getDescr(),
                getVision(),
                getAccountability(),
                getRewards(),
                getObstacles(),
                getSupport(),
                getBrainstorming(),
                getNotes(),
                getCreatedDate(),
                getStartDate(),
                getEndDate(),
                getAchievedDate());
    }

    private String toString(String s) {
        return s == null ? "" : s;
    }

    private boolean areSubgoalsAchieved(GoalCtrl goalCtrl) {
        for (GoalCtrl subgoalCtrl : goalCtrl.getSubgoals()) {
            if (subgoalCtrl.getAchievedDate() == null) {
                return false;
            }
            if (!areSubgoalsAchieved(subgoalCtrl)) {
                return false;
            }
        }
        return true;
    }

    private void setSubgoalsAchieved(GoalCtrl goalCtrl, Date newValue) {
        for (GoalCtrl subgoalCtrl : goalCtrl.getSubgoals()) {
            if (!subgoalCtrl.isAchieved()) {
                GoalCtrlImpl subgoalCtrlImpl = (GoalCtrlImpl) subgoalCtrl;
                Date oldValue = subgoalCtrlImpl.achieved;
                subgoalCtrlImpl.achieved = newValue;
                subgoalCtrlImpl.update();
                subgoalCtrlImpl.firePropertyChange(PROP_ACHIEVED_DATE, oldValue, newValue);
            }
            setSubgoalsAchieved(subgoalCtrl, newValue);
        }
    }

    @Override
    public boolean insertGoalProject(Integer projectID) {
        Project project = Project.getProject(projectID);
        if (project == null) {
            return false;
        }

        if (!GoalsCtrlImpl.getDefault().insertGoalProject(getID(), projectID)) {
            return false;
        }
        firePropertyChange(PROP_PROJECT_LINKS, null, projectID);
        project.fireGoalLinkInsertedEvent(getID());
        return true;
    }

    @Override
    public boolean removeGoalProject(Integer projectID) {
        Project project = Project.getProject(projectID);
        if (project == null) {
            return false;
        }
        if (!GoalsCtrlImpl.getDefault().removeGoalProject(getID(), projectID)) {
            return false;
        }
        firePropertyChange(PROP_PROJECT_LINKS, projectID, null);
        project.fireGoalLinkRemovedEvent(getID());
        return true;
    }

    @Override
    public boolean removeGoalProjects() {
        Integer[] projectIDs = getGoalsDAO().getProjectIDs(getID());
        if (projectIDs == null || projectIDs.length == 0) {
            return false;
        }
        boolean dataChanged = false;
        for (Integer projectID : projectIDs) {
            boolean changed = removeGoalProject(projectID);
            dataChanged = dataChanged || changed;
        }
        return dataChanged;
    }

    /**
     * Notifies observers that this goal has been deleted.
     */
    @Override
    public void notifyDeleted() {
        firePropertyChange(PROP_GOAL_DELETE, false, true);
    }

    /**
     * Notifies observers that a subgoal has been removed.
     */
    @Override
    public void notifySubgoalRemoved(Integer subgoalID) {
        firePropertyChange(PROP_SUBGOAL_REMOVE, null, subgoalID);
    }

    /**
     * Notifies observers that a subgoal has been inserted.
     */
    @Override
    public void notifySubgoalInserted(Integer subgoalID) {
        firePropertyChange(PROP_SUBGOAL_INSERT, null, subgoalID);
    }

    public Date getCreated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDescription() {
        return getDescr();
    }

    public void setDescription(String description) {
    }

    public ImageIcon getIcon(boolean opened) {
        return getIcon(opened);
    }

    public ItemList getParent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setParent(ItemList parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Item copy(IDGenerator idGenerator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeFromParent() {
    }

    public boolean isWithin(ItemList list) {
        return false;
    }

    public boolean isEditable() {
        return true;
    }

    public void addObserver(Observer observer) {
    }

    public void removeObserver(Observer observer) {
    }

    public void removeObservers() {
    }

    public void resetObservers() {
    }

    public void update(Observable observable, Object arguement) {
    }

    public int compareTo(Item o) {
        return 0;
    }

    // END OF PRIVATE

    @Override
    public boolean isProject() {
        return false;
    }

    @Override
    public boolean isAction() {
        return false;
    }
}
