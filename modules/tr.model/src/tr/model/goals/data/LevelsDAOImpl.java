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

import au.com.trgtd.tr.util.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.goals.dao.LevelsDAO;
import tr.model.goals.dto.LevelDTO;

/**
 * Goal levels data access object implementation.
 *
 * @author Jeremy Moore
 */
public class LevelsDAOImpl extends DAOImpl implements LevelsDAO {

    // SINGLETON
    private static LevelsDAOImpl instance;

    /**
     * Gets the default instance.
     * @return the default instance.
     */
    public static LevelsDAOImpl getDefault() {
        if (instance == null) {
            instance = new LevelsDAOImpl();
        }
        return instance;
    }
    // End of SINGLETON

    // LevelsDAO IMPLEMENTATION
    
    public LevelDTO[] getLevelDTOs() {
        synchronized (this) {
            return levelDTOList.toArray(new LevelDTO[0]);
        }
    }

    public LevelDTO getLevelDTO(Integer id) {
        synchronized (this) {
            return levelDTOMap.get(id);
        }
    }

    public LevelDTO insertLevel(
            String descr,
            boolean goalsHaveProjects,
            boolean goalsHaveStart,
            boolean goalsHaveEnd,
            boolean goalsHaveVision,
            boolean goalsHaveAccountability,
            boolean goalsHaveRewards,
            boolean goalsHaveObstacles,
            boolean goalsHaveSupport,
            boolean goalsHaveBrainstorming,
            String goalsIconPath)
    {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Insert level failed - data not found.");
            }
            if (descr == null || descr.trim().length() == 0) {
                throw new IllegalArgumentException("Insert level failed - description manditory.");
            }
            // create level and add to data
            Level level = new Level(
                    data.getNextID(),
                    descr,
                    goalsHaveProjects,
                    goalsHaveStart,
                    goalsHaveEnd,
                    goalsHaveVision,
                    goalsHaveAccountability,
                    goalsHaveRewards,
                    goalsHaveObstacles,
                    goalsHaveSupport,
                    goalsHaveBrainstorming,
                    goalsIconPath);
            
            data.getLevels().add(level);
            data.setChanged(true);
            // add level to level map
            levelMap.put(level.getID(), level);
            // add level DTO to level DTO map and list
            LevelDTO levelDTO = createLevelDTO(level);
            levelDTOMap.put(level.getID(), levelDTO);
            levelDTOList.add(levelDTO);

            return levelDTO;
        }
    }

    public void deleteLevel(Integer levelID) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Delete level failed - data not found.");
            }
            // check that level exists
            Level level = levelMap.get(levelID);
            if (level == null) {
                throw new IllegalArgumentException("Delete level failed - level does not exist.");
            }
            // Check that the level is not in use
            for (Goal goal : data.getGoals()) {
                if (Utils.equal(goal.getLevelID(), levelID)) {
                    throw new IllegalArgumentException("Delete level failed - level is in use.");
                }
            }
            // remove level from data
            data.getLevels().remove(level);
            data.setChanged(true);
            // remove level from level map
            levelMap.remove(level.getID());
            // remove levelDTO from map
            LevelDTO levelDTO = levelDTOMap.remove(levelID);
            // remove levelDTO from list
            levelDTOList.remove(levelDTO);
        }
    }

    public void updateLevel(LevelDTO levelDTO) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Update level failed - data not found.");
            }
            Level level = levelMap.get(levelDTO.id);
            if (level == null) {
                throw new IllegalArgumentException("Update level failed - level does not exist.");
            }
            level.setDescr(levelDTO.descr);
            level.setGoalsHaveProjects(levelDTO.goalsHaveProjects);
            level.setGoalsHaveStart(levelDTO.goalsHaveStart);
            level.setGoalsHaveEnd(levelDTO.goalsHaveEnd);
            level.setGoalsHaveAccountability(levelDTO.goalsHaveAccountability);
            level.setGoalsHaveBrainstorming(levelDTO.goalsHaveBrainstorming);
            level.setGoalsHaveObstacles(levelDTO.goalsHaveObstacles);
            level.setGoalsHaveRewards(levelDTO.goalsHaveRewards);
            level.setGoalsHaveSupport(levelDTO.goalsHaveSupport);
            level.setGoalsHaveVision(levelDTO.goalsHaveVision);
            level.setGoalsIconPath(levelDTO.goalsIconPath);
            data.setChanged(true);
        }
    }

    public void moveUp(int index) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Move up level failed - Data not found.");
            }
            Vector<Level> levels = data.getLevels();
            if (index < 1) {
                throw new IllegalArgumentException("Move up level failed - index < 1");
            }
            if (index > levels.size() - 1) {
                throw new IllegalArgumentException("Move up level failed - index > size - 1");
            }
            levels.add(index - 1, levels.remove(index));
            data.setChanged(true);
        }
    }

    public void moveDown(int index) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Move down level failed - data not found.");
            }
            Vector<Level> levels = data.getLevels();
            if (index < 0) {
                throw new IllegalArgumentException("Move down level failed - index < 0");
            }
            if (index > levels.size() - 2) {
                throw new IllegalArgumentException("Move down level failed - index > size - 2");
            }
            levels.add(index + 1, levels.remove(index));
            data.setChanged(true);
        }
    }

    public void reorder(int[] permutation) {
        synchronized (this) {
            if (data == null) {
                throw new NullPointerException("Reorder levels failed - data not found.");
            }
            if (permutation == null || permutation.length == 0) {
                throw new IllegalArgumentException("Reorder levels failed - index permutation null or zero length");
            }
            Vector<Level> levels = data.getLevels();
            if (levels.size() != permutation.length) {
                throw new IllegalArgumentException("Reorder levels failed - incorrect index permutation size");
            }
            Level[] reorderedLevels = new Level[permutation.length];
            for (int i = 0; i < permutation.length; i++) {
                reorderedLevels[permutation[i]] = levels.get(i);
            }
            levels.clear();
            levels.addAll(Arrays.asList(reorderedLevels));
            data.setChanged(true);

            // reorder DTO list
            levelDTOList.clear();
            for (Level level : reorderedLevels) {
                levelDTOList.add(levelDTOMap.get(level.getID()));
            }
        }
    }

    // END OF LevelsDAO IMPLEMENTATION

    // PRIVATE
    private Data data;
    private HashMap<Integer, Level> levelMap;
    private Vector<LevelDTO> levelDTOList;
    private HashMap<Integer, LevelDTO> levelDTOMap;

    private LevelsDAOImpl() {
        data = DataLookup.instance().lookup(Data.class);
        Lookup.Result dlr = DataLookup.instance().lookupResult(Data.class);
        dlr.addLookupListener((LookupEvent lookupEvent) -> {
            data = DataLookup.instance().lookup(Data.class);
            initialise();
            firePropertyChange(PROP_DATA, 0, 1);
        });
        initialise();
    }

    private void initialise() {
        synchronized (this) {
            if (levelDTOList == null) {
                levelDTOList = new Vector<>();
            } else {
                levelDTOList.clear();
            }
            if (levelDTOMap == null) {
                levelDTOMap = new HashMap<>();
            } else {
                levelDTOMap.clear();
            }
            if (levelMap == null) {
                levelMap = new HashMap<>();
            } else {
                levelMap.clear();
            }
            if (data == null) {
                return;
            }
            for (Level level : data.getLevels()) {
                levelMap.put(level.getID(), level);
                LevelDTO dto = createLevelDTO(level);
                levelDTOList.add(dto);
                levelDTOMap.put(dto.id, dto);
            }
        }
    }

    private LevelDTO createLevelDTO(Level level) {
        return new LevelDTO(
                level.getID(),
                level.getDescr(),
                level.isGoalsHaveProjects(),
                level.isGoalsHaveStart(),
                level.isGoalsHaveEnd(),
                level.isGoalsHaveVision(),
                level.isGoalsHaveAccountability(),
                level.isGoalsHaveRewards(),
                level.isGoalsHaveObstacles(),
                level.isGoalsHaveSupport(),
                level.isGoalsHaveBrainstorming(),
                level.getGoalsIconPath());
    }

    // END PRIVATE
}
