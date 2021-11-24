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
 * Goal levels controller.
 *
 * @author Jeremy Moore
 */
public interface LevelsCtrl extends Ctrl {

    /** Property for data change events. */
    public static final String PROP_LEVEL_DATA = "level.data";
    /** Property for insert events. */
    public static final String PROP_LEVEL_INSERT = "level.insert";
    /** Property for delete events. */
    public static final String PROP_LEVEL_DELETE = "level.delete";
    /** Property for update events. */
    public static final String PROP_LEVEL_UPDATE = "level.update";
    /** Property for move up. */
    public static final String PROP_LEVEL_MOVE_UP = "level.up";
    /** Property for move down. */
    public static final String PROP_LEVEL_MOVE_DOWN = "level.down";
    /** Property for reorder. */
    public static final String PROP_LEVEL_REORDER = "level.reorder";

    /**
     * Gets all levels.
     * @return an array of level controller objects.
     */
    public List<LevelCtrl> getLevels();

    /**
     * Gets the level for a given level identifier.
     * @param id The level ID.
     * @return The level controller.
     */
    public LevelCtrl getLevel(Integer id);

    /**
     * Inserts a new level.
     * @param descr The level description.
     * @param goalsHaveProjects Whether goals at this level can have projects.
     * @param goalsHaveStart Whether goals at this level can have a start date.
     * @param goalsHaveEnd Whether goals at this level can have an end date.
     * @return The level controller for the new level.
     */
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
            String goalsIconPath);

    /**
     * Inserts an existing level in the data store.
     * @param levelCtrl The level controller of the level to insert.
     */
    public void insert(LevelCtrl levelCtrl);

    /**
     * Deletes a level.
     * @param levelCtrl The level controller.
     */
    public void deleteLevel(LevelCtrl levelCtrl);

    /**
     * Updates a level.
     * @param levelCtrl The level controller.
     */
    public void updateLevel(LevelCtrl levelCtrl);


    /**
     * Moves a level up in the order.
     * @param index The index of the level to move.
     */
    public void moveUp(int index);

    /**
     * Moves a level down in the order.
     * @param index The index of the level to move.
     */
    public void moveDown(int index);

    /**
     * Reorders levels.
     * @param perm The permutation of level indexes.
     */
    public void reorder(int[] perm);

}
