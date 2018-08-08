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
