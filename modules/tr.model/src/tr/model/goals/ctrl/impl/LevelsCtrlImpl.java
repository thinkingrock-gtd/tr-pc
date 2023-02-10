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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.dao.LevelsDAO;
import tr.model.goals.dao.LevelsDAOLookup;
import tr.model.goals.dto.LevelDTO;

public class LevelsCtrlImpl extends CtrlImpl implements LevelsCtrl, PropertyChangeListener {

    private static LevelsCtrl instance;

    public static LevelsCtrl getDefault() {
        if (instance == null) {
            instance = new LevelsCtrlImpl();
        }
        return instance;
    }

    private final LevelsDAO levelsDAO;
    private final List<LevelCtrl> levelCtrlsList;
    private final Map<Integer, LevelCtrl> levelCtrlsMap;
    private final String name = NbBundle.getMessage(CtrlImpl.class, "levels");

    private LevelsCtrlImpl() {
        super(Constants.ID_ROOT_LEVEL);
        levelsDAO = LevelsDAOLookup.getLevelsDAO();
        levelsDAO.addPropertyChangeListener(LevelsDAO.PROP_DATA, this);
        levelCtrlsList = new Vector<>();
        levelCtrlsMap = new HashMap<>();
        initialise();
    }

    private void initialise() {
        levelCtrlsList.clear();
        levelCtrlsMap.clear();
        for (LevelDTO levelDTO : levelsDAO.getLevelDTOs()) {
            LevelCtrl levelCtrl = new LevelCtrlImpl(levelDTO);
            levelCtrlsList.add(levelCtrl);
            levelCtrlsMap.put(levelDTO.id, levelCtrl);
        }
    }

    private LevelDTO toLevelDTO(LevelCtrl levelCtrl) {
        return new LevelDTO(
                levelCtrl.getID(),
                levelCtrl.getDescr(),
                levelCtrl.isGoalsHaveProjects(),
                levelCtrl.isGoalsHaveStartDate(),
                levelCtrl.isGoalsHaveEndDate(),
                levelCtrl.isGoalsHaveVision(),
                levelCtrl.isGoalsHaveAccountability(),
                levelCtrl.isGoalsHaveRewards(),
                levelCtrl.isGoalsHaveObstacles(),
                levelCtrl.isGoalsHaveSupport(),
                levelCtrl.isGoalsHaveBrainstorming(),
                levelCtrl.getGoalsIconPath());

    }

    @Override
    public String getName() {
        return name;
    }

    public List<LevelCtrl> getLevels() {
        synchronized (this) {
            return levelCtrlsList;
        }
    }

    public LevelCtrl getLevel(Integer id) {
        synchronized (this) {
            return levelCtrlsMap.get(id);
        }
    }

    public LevelCtrl insertLevel(
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
            String goalsIconPath) {
        synchronized (this) {
            if (descr == null || descr.trim().length() == 0) {
                throw new IllegalArgumentException("Description must be entered.");
            }
            LevelDTO levelDTO = levelsDAO.insertLevel(
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
            LevelCtrl levelCtrl = new LevelCtrlImpl(levelDTO);
            levelCtrlsList.add(levelCtrl);
            levelCtrlsMap.put(levelCtrl.getID(), levelCtrl);
            firePropertyChange(PROP_LEVEL_INSERT, null, levelCtrl);
            return levelCtrl;
        }

    }

    public void insert(LevelCtrl levelCtrl) {
        synchronized (this) {
            if (levelCtrl == null) {
                throw new IllegalArgumentException("Insert level failed - can not insert null.");
            }
            if (levelCtrlsMap.containsKey(levelCtrl.getID())) {
                throw new IllegalArgumentException("Insert level failed - level already exists.");
            }
            levelCtrlsList.add(levelCtrl);
            levelCtrlsMap.put(levelCtrl.getID(), levelCtrl);
            firePropertyChange(PROP_LEVEL_INSERT, null, levelCtrl);
        }
    }

    public void deleteLevel(LevelCtrl levelCtrl) {
        synchronized (this) {
            if (levelCtrl == null) {
                throw new IllegalArgumentException("Delete level failed - can not delete null.");
            }
            if (!levelCtrlsMap.containsKey(levelCtrl.getID())) {
                throw new IllegalArgumentException("Delete level failed - level does not exist.");
            }
            try {
                levelsDAO.deleteLevel(levelCtrl.getID());
            } catch (Exception e) {
                String msg = NbBundle.getMessage(getClass(), "can.not.delete.level", levelCtrl.getDescr());
                NotifyDescriptor descriptor = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(descriptor);
                return;
            }
            levelCtrlsList.remove(levelCtrl);
            levelCtrlsMap.remove(levelCtrl.getID());
            firePropertyChange(PROP_LEVEL_DELETE, levelCtrl, null);
        }
    }

    public void updateLevel(LevelCtrl levelCtrl) {
        synchronized (this) {
            if (levelCtrl == null) {
                throw new IllegalArgumentException("Update level failed - can not update null.");
            }
            if (!levelCtrlsMap.containsKey(levelCtrl.getID())) {
                throw new IllegalArgumentException("Update level failed - level does not exist.");
            }
            levelsDAO.updateLevel(toLevelDTO(levelCtrl));
            firePropertyChange(PROP_LEVEL_UPDATE, null, levelCtrl);
        }
    }

    public void moveUp(int index) {
        synchronized (this) {
            levelsDAO.moveUp(index);
            levelCtrlsList.add(index - 1, levelCtrlsList.remove(index));
            firePropertyChange(PROP_LEVEL_MOVE_UP, null, this);
        }

    }

    public void moveDown(int index) {
        synchronized (this) {
            levelsDAO.moveDown(index);
            levelCtrlsList.add(index + 1, levelCtrlsList.remove(index));
            firePropertyChange(PROP_LEVEL_MOVE_DOWN, null, this);
        }

    }

    public void reorder(int[] perm) {
        synchronized (this) {
            levelsDAO.reorder(perm);
            initialise();
            firePropertyChange(PROP_LEVEL_REORDER, null, this);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        initialise();
        firePropertyChange(PROP_LEVEL_DATA, 0, 1);
    }

}
