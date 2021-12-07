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

import tr.model.goals.dto.LevelDTO;

/**
 * Controller of goal levels.
 *
 * @author Jeremy Moore
 */
public interface LevelsDAO extends DAO {

    public final static String PROP_DATA = "level.data";

    /**
     * Returns an array of containing all DTOs for each level.
     * @return an array of level DTO objects.
     */
    public LevelDTO[] getLevelDTOs();

    /**
     * Gets the level DTO for a given level identifier.
     * @param id The level ID.
     * @return The level DTO.
     */
    public LevelDTO getLevelDTO(Integer id);

    /**
     * Inserts a new level.
     * @param descr The level description.
     * @param goalsHaveProjects
     * @param goalsHaveStart
     * @param goalsHaveEnd
     * @return The level DTO for the new level.
     */
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
            String goalsIconPath);

    /**
     * Deletes a level.
     * @param levelID The level ID.
     */
    public void deleteLevel(Integer levelID);

    /**
     * Updates a level.
     * @param levelDTO The level DTO.
     */
    public void updateLevel(LevelDTO levelDTO);

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
