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
package tr.model.goals.data;

import au.com.trgtd.tr.appl.Constants;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.goals.dao.GoalsDAO;
import tr.model.goals.dto.GoalDTO;

/**
 * Goals data access object implementation.
 *
 * @author Jeremy Moore
 */
public class GoalsDAOImpl extends DAOImpl implements GoalsDAO {

    // SINGLETON
    private static GoalsDAOImpl instance;

    /**
     * Gets the default instance.
     * @return the default instance.
     */
    public static GoalsDAOImpl getDefault() {
        if (instance == null) {
            instance = new GoalsDAOImpl();
        }
        return instance;
    }
    // END OF SINGLETON

    // GoalsDAO IMPLEMENTATION
    @Override
    public GoalDTO getRootGoal() {
        synchronized (this) {
            return goalDTOMap.get(Constants.ID_ROOT_GOAL);
        }
    }

    @Override
    public GoalDTO getGoal(Integer id) {
        synchronized (this) {
            return goalDTOMap.get(id);
        }
    }

    @Override
    public GoalDTO[] getGoals() {
        synchronized (this) {
            if (data == null) {
                return new GoalDTO[0];
            }
            GoalDTO[] goalDTOs = new GoalDTO[data.getGoals().size()];
            int index = 0;
            for (Goal goal : data.getGoals()) {
                goalDTOs[index++] = getGoal(goal.getID());
            }
            return goalDTOs;
        }
    }

    @Override
    public boolean hasSubgoals(Integer goalID) {
        List<Integer> subgoals = data.getGoalSubgoalsMap().get(goalID);
        return subgoals != null && !subgoals.isEmpty();

    }

    @Override
    public GoalDTO[] getSubgoals(Integer goalID) {
        synchronized (this) {
            if (goalID == null || !goalMap.containsKey(goalID)) {
                return new GoalDTO[0];
            }
            List<Integer> subgoalIDs = this.getSubgoalIDs(goalID);
            int index = 0;
            GoalDTO[] subgoalDTOs = new GoalDTO[subgoalIDs.size()];
            for (Integer subgoalID : subgoalIDs) {
                subgoalDTOs[index++] = getGoal(subgoalID);
            }
            return subgoalDTOs;
        }
    }

    @Override
    public boolean isDecendantOf(Integer decendantGoalID, Integer ancestorGoalID) {
        if (isParentOf(ancestorGoalID, decendantGoalID)) {
            return true;
        }
        for (Integer ancestorChildGoalID : getSubgoalIDs(ancestorGoalID)) {
            if (isDecendantOf(decendantGoalID, ancestorChildGoalID)) {
                return true;
            }
        }
        return false;
    }

    public boolean isParentOf(Integer parentGoalID, Integer childGoalID) {
        return getSubgoalIDs(parentGoalID).contains(childGoalID);
    }

//    public void moveUpSubgoal(Integer goalID, int subgoalIndex) {
//        synchronized (this) {
//            if (!goalDTOMap.containsKey(goalID)) {
//                throw new IllegalArgumentException("Move up subgoal failed - goal does not exist.");
//            }
//            Vector<Integer> subgoals = getSubgoalIDs(goalID);
//            if (subgoalIndex < 1) {
//                throw new IllegalArgumentException("Move up subgoal failed - index < 1");
//            }
//            if (subgoalIndex > subgoals.size() - 1) {
//                throw new IllegalArgumentException("Move up subgoal failed - index > size - 1");
//            }
//            subgoals.add(subgoalIndex - 1, subgoals.remove(subgoalIndex));
//            data.setChanged(true);
//        }
//    }
//
//    public void moveDownSubgoal(Integer goalID, int subgoalIndex) {
//        synchronized (this) {
//            if (!goalDTOMap.containsKey(goalID)) {
//                throw new IllegalArgumentException("Move down subgoal failed - goal does not exist.");
//            }
//            if (subgoalIndex < 0) {
//                throw new IllegalArgumentException("Move down subgoal failed - index < 0");
//            }
//            Vector<Integer> subgoalIDs = getSubgoalIDs(goalID);
//            if (subgoalIndex > subgoalIDs.size() - 2) {
//                throw new IllegalArgumentException("Move down subgoal failed - index > size - 2");
//            }
//            subgoalIDs.add(subgoalIndex + 1, subgoalIDs.remove(subgoalIndex));
//            data.setChanged(true);
//        }
//    }

//    public void moveBeforeSubgoal(Integer goalID, int srcIndex, int dstIndex) {
//        synchronized (this) {
//            if (!goalDTOMap.containsKey(goalID)) {
//                throw new IllegalArgumentException("Move before subgoal failed - goal does not exist.");
//            }
//            Vector<Integer> subgoals = getSubgoalIDs(goalID);
//            if (srcIndex < 0) {
//                throw new IllegalArgumentException("Move before subgoal failed - source index < 0");
//            }
//            if (srcIndex > subgoals.size() - 1) {
//                throw new IllegalArgumentException("Move before subgoal failed - source index > size - 1");
//            }
//            if (dstIndex < 0) {
//                throw new IllegalArgumentException("Move before subgoal failed - destination index < 0");
//            }
//            if (dstIndex > subgoals.size() - 1) {
//                throw new IllegalArgumentException("Move before subgoal failed - destination index > size - 1");
//            }
//            subgoals.add(dstIndex, subgoals.remove(srcIndex));
//            data.setChanged(true);
//        }
//    }
//
//    public void moveAfterSubgoal(Integer goalID, int srcIndex, int dstIndex) {
//        synchronized (this) {
//            if (!goalDTOMap.containsKey(goalID)) {
//                throw new IllegalArgumentException("Move after subgoal failed - goal does not exist.");
//            }
//            Vector<Integer> subgoals = getSubgoalIDs(goalID);
//            if (srcIndex < 0) {
//                throw new IllegalArgumentException("Move after subgoal failed - source index < 0");
//            }
//            if (srcIndex > subgoals.size() - 1) {
//                throw new IllegalArgumentException("Move after subgoal failed - source index > size - 1");
//            }
//            if (dstIndex < 0) {
//                throw new IllegalArgumentException("Move after subgoal failed - destination index < 0");
//            }
//            if (dstIndex > subgoals.size() - 1) {
//                throw new IllegalArgumentException("Move after subgoal failed - destination index > size - 1");
//            }
//            subgoals.add(dstIndex + 1, subgoals.remove(srcIndex));
//            data.setChanged(true);
//        }
//    }

    @Override
    public void moveBeforeSubgoal(Integer goalID, Integer srcSubgoalID, Integer dstSubgoalID) {
        synchronized (this) {
            if (!goalDTOMap.containsKey(goalID)) {
                throw new IllegalArgumentException("Move before subgoal failed - goal not found.");
            }
            List<Integer> subgoals = getSubgoalIDs(goalID);
            if (subgoals == null || subgoals.isEmpty()) {
                throw new IllegalArgumentException("Move before subgoal failed - subgoals not found.");
            }
            int srcIndex = subgoals.indexOf(srcSubgoalID);
            if (srcIndex < 0) {
                throw new IllegalArgumentException("Move before subgoal failed - src subgoal not found");
            }
            int dstIndex = subgoals.indexOf(dstSubgoalID);
            if (dstIndex < 0) {
                throw new IllegalArgumentException("Move before subgoal failed - dst subgoal not found");
            }
            if (srcIndex > dstIndex) {
                subgoals.add(dstIndex, subgoals.remove(srcIndex));
                data.setChanged(true);
            }
        }
    }

    @Override
    public void moveAfterSubgoal(Integer goalID, Integer srcSubgoalID, Integer dstSubgoalID) {
        synchronized (this) {
            if (!goalDTOMap.containsKey(goalID)) {
                throw new IllegalArgumentException("Move after subgoal failed - goal not found.");
            }
            List<Integer> subgoals = getSubgoalIDs(goalID);
            if (subgoals == null || subgoals.isEmpty()) {
                throw new IllegalArgumentException("Move after subgoal failed - subgoals not found.");
            }
            int srcIndex = subgoals.indexOf(srcSubgoalID);
            if (srcIndex < 0) {
                throw new IllegalArgumentException("Move after subgoal failed - src subgoal not found");
            }
            int dstIndex = subgoals.indexOf(dstSubgoalID);
            if (dstIndex < 0) {
                throw new IllegalArgumentException("Move after subgoal failed - dst subgoal not found");
            }
            if (srcIndex < dstIndex) {
                subgoals.add(dstIndex, subgoals.remove(srcIndex));
                data.setChanged(true);
            }
        }
    }

    @Override
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
            Date achieved) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Insert goal failed - data not found.");
            }
            if (!goalDTOMap.containsKey(supergoalID)) {
                throw new IllegalArgumentException("Insert goal failed - supergoal does not exist.");
            }
            if (descr == null || descr.trim().length() == 0) {
                throw new IllegalArgumentException("Create goal failed - description manditory.");
            }
            if (levelID == null) {
                throw new IllegalArgumentException("Create goal failed - level manditory.");
            }
            if (topicID == null) {
                throw new IllegalArgumentException("Create goal failed - topic manditory.");
            }
            // create goal and goal-subgoal and add to data
            Goal goal = new Goal(
                    data.getNextID(),
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
                    created,
                    start,
                    end,
                    achieved);

            data.getGoals().add(goal);
            data.getGoalSubgoalsMap().put(goal.getID(), new Vector<>());
            data.getGoalSubgoalsMap().get(supergoalID).add(goal.getID());
            data.setChanged(true);
            // add goal to goal map
            goalMap.put(goal.getID(), goal);
            // add goal DTO to goal DTO map
            GoalDTO goalDTO = createGoalDTO(goal);
            goalDTOMap.put(goal.getID(), goalDTO);
            return goalDTO;
        }
    }

    @Override
    public void insertGoal(Integer supergoalID, Integer goalID) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Insert goal failed - data not found.");
            }
            if (!goalDTOMap.containsKey(supergoalID)) {
                throw new IllegalArgumentException("Insert goal failed - supergoal does not exist.");
            }
            if (!goalDTOMap.containsKey(goalID)) {
                throw new IllegalArgumentException("Insert goal failed - goal does not exist.");
            }
            data.getGoalSubgoalsMap().get(supergoalID).add(goalID);
            data.setChanged(true);
        }
    }

    @Override
    public void moveGoal(Integer goalID, Integer oldSupergoalID, Integer newSupergoalID) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Move goal failed - data not found.");
            }
            if (!goalDTOMap.containsKey(oldSupergoalID)) {
                throw new IllegalArgumentException("Move goal failed - old supergoal does not exist.");
            }
            if (!goalDTOMap.containsKey(newSupergoalID)) {
                throw new IllegalArgumentException("Move goal failed - new supergoal does not exist.");
            }
            if (!goalDTOMap.containsKey(goalID)) {
                throw new IllegalArgumentException("Move goal failed - goal does not exist.");
            }
            data.getGoalSubgoalsMap().get(oldSupergoalID).remove(goalID);
            data.getGoalSubgoalsMap().get(newSupergoalID).add(goalID);
            data.setChanged(true);
        }
    }

    public void updateGoal(GoalDTO goalDTO) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Update goal failed - data not found.");
            }
            if (goalDTO == null) {
                throw new IllegalArgumentException("Update goal failed - goalDTO can not be null.");
            }
            Goal goal = goalMap.get(goalDTO.id);
            if (goal == null) {
                throw new IllegalArgumentException("Update goal failed - goal does not exist.");
            }
            // update data
            goal.setLevelID(goalDTO.levelID);
            goal.setTopicID(goalDTO.topicID);
            goal.setDescr(goalDTO.descr);
            goal.setVision(goalDTO.vision);
            goal.setAccountability(goalDTO.accountability);
            goal.setRewards(goalDTO.rewards);
            goal.setObstacles(goalDTO.obstacles);
            goal.setSupport(goalDTO.support);
            goal.setBrainstorming(goalDTO.brainstorming);
            goal.setNotes(goalDTO.notes);
            goal.setStartDate(goalDTO.start);
            goal.setEndDate(goalDTO.end);
            goal.setAchievedDate(goalDTO.achieved);
            data.setChanged(true);

            // update goalDTO in goalDTO map
            goalDTOMap.put(goalDTO.id, goalDTO);
        }
    }

    @Override
    public void removeGoal(Integer supergoalID, Integer goalID) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Delete goal failed - data not found.");
            }
            if (!goalDTOMap.containsKey(supergoalID)) {
                throw new IllegalArgumentException("Delete goal failed - supergoal does not exist.");
            }
            if (!goalDTOMap.containsKey(goalID)) {
                throw new IllegalArgumentException("Delete goal failed - goal does not exist.");
            }

            // remove goal from supergoal in data
            data.getGoalSubgoalsMap().get(supergoalID).remove(goalID);
            data.setChanged(true);
            // check that goal is not a subgoal of any other goal
            for (List<Integer> subgoals : data.getGoalSubgoalsMap().values()) {
                if (subgoals.contains(goalID)) {
                    return;
                }
            }
            // remove goal from data
            data.getGoals().remove(goalMap.get(goalID));
            data.getGoalSubgoalsMap().remove(goalID);
            data.getGoalProjectsMap().remove(goalID);

            // remove goal from goalMap
            goalMap.remove(goalID);
            // remove goalDTO from goalDTOMap
            goalDTOMap.remove(goalID);
        }
    }

    @Override
    public String getPath(Integer goalID) {
        String path = getPath(goalID, goalMap.get(Constants.ID_ROOT_GOAL), "");
        return path == null ? "" : path;
    }

    @Override
    public Integer[] getGoalIDs(int projectID) {
        if (data == null) {
            return new Integer[0];
        }

        List<Integer> goalIDs = new Vector<>();

        for (Entry<Integer, Vector<Integer>> entry : data.getGoalProjectsMap().entrySet()) {
            if (entry.getValue().contains(projectID)) {
                goalIDs.add(entry.getKey());
            }
        }

        return goalIDs.toArray(new Integer[0]);

    }

    public Integer[] getProjectIDs(int goalID) {
        if (data == null) {
            return new Integer[0];
        }
        List<Integer> projectIDs = data.getGoalProjectsMap().get(goalID);
        if (projectIDs == null || projectIDs.isEmpty()) {
            return new Integer[0];
        }
        return projectIDs.toArray(new Integer[projectIDs.size()]);
    }

    // END OF GoalsDAO IMPLEMENTATION
    // PRIVATE
    private Data data;
    private Map<Integer, Goal> goalMap;
    private Map<Integer, GoalDTO> goalDTOMap;

    private GoalsDAOImpl() {
        initialise();
    }

    private void initialise() {
        synchronized (this) {
            data = DataLookup.instance().lookup(Data.class);
            Lookup.Result lookupResult = DataLookup.instance().lookupResult(Data.class);
            lookupResult.addLookupListener((LookupEvent lookupEvent) -> {
                initialise();
                firePropertyChange(PROP_DATA, 0, 1);
            });
            if (goalMap == null) {
                goalMap = new HashMap<>();
            } else {
                goalMap.clear();
            }
            if (goalDTOMap == null) {
                goalDTOMap = new HashMap<>();
            } else {
                goalDTOMap.clear();
            }
            if (data == null) {
                return;
            }
            for (Goal goal : data.getGoals()) {
                goalMap.put(goal.getID(), goal);
                goalDTOMap.put(goal.getID(), createGoalDTO(goal));
            }
        }
    }

    private Vector<Integer> getSubgoalIDs(Integer goalID) {
        if (data == null) {
            return new Vector<>();
        }
        Vector<Integer> subgoalIDs = data.getGoalSubgoalsMap().get(goalID);
        if (subgoalIDs == null) {
            subgoalIDs = new Vector<>();
            data.getGoalSubgoalsMap().put(goalID, subgoalIDs);
        }
        return subgoalIDs;
    }

    private GoalDTO createGoalDTO(Goal goal) {
        return new GoalDTO(
                goal.getID(),
                goal.getLevelID(),
                goal.getTopicID(),
                goal.getDescr(),
                goal.getVision(),
                goal.getAccountability(),
                goal.getRewards(),
                goal.getObstacles(),
                goal.getSupport(),
                goal.getBrainstorming(),
                goal.getNotes(),
                goal.getCreatedDate(),
                goal.getStartDate(),
                goal.getEndDate(),
                goal.getAchievedDate());
    }

    private String getPath(Integer forGoalID, Goal atGoal, String path) {
        if (atGoal == null) {
            return null;
        }
        if (atGoal.getID() == forGoalID) {
            return path;
        }
        for (Integer subgoalID : getSubgoalIDs(atGoal.getID())) {
            Goal subgoal = goalMap.get(subgoalID);
            if (subgoal != null) {
                String resultPath = getPath(forGoalID, subgoal, path + "/" + subgoal.getDescr());
                if (resultPath != null) {
                    return resultPath;
                }
            }
        }
        return null;
    }

    // END PRIVATE

     public Integer[] getSubGoalIDs(Integer goalID) {
        if (data == null) {
            return new Integer[0];
        }        
        List<Integer> subgoalIDs = data.getGoalSubgoalsMap().get(goalID);
        if (subgoalIDs == null) {
            return new Integer[0];
        }
        return subgoalIDs.toArray(new Integer[subgoalIDs.size()]);
     }

    @Override
    public boolean insertGoalProject(Integer goalID, Integer projectID) {
        if (data == null) {
            return false;
        }
        if (!goalMap.containsKey(goalID)) {
            return false;
        }
        Vector<Integer> projectsList = data.getGoalProjectsMap().get(goalID);
        if (projectsList == null) {
            projectsList = new Vector<>();
            data.getGoalProjectsMap().put(goalID, projectsList);
            projectsList.add(projectID);
            return true;
        }
        if (projectsList.contains(projectID)) {
            return false;            
        }
        projectsList.add(projectID);
        return true;
    }

    @Override
    public boolean removeGoalProject(Integer goalID, Integer projectID) {
        if (data == null) {
            return false;
        }
        Vector<Integer> projectsList = data.getGoalProjectsMap().get(goalID);
        if (projectsList == null || projectsList.isEmpty()) {
            return false;
        }
        return projectsList.remove(projectID);
    }

}
