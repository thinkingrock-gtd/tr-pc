/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
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
