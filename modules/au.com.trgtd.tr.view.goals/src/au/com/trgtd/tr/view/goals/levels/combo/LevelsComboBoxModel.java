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
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.view.goals.levels.combo;

import java.beans.PropertyChangeEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import java.beans.PropertyChangeListener;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

/**
 * Goal Levels ComboBox model.
 *
 * @author Jeremy Moore
 */
public class LevelsComboBoxModel extends DefaultComboBoxModel implements PropertyChangeListener {
    
    private final LevelsCtrl levelsCtrl;
    private List<LevelCtrl> levels;

    /**
     * Creates a new default instance.
     */
    public LevelsComboBoxModel() {
        super();
        levelsCtrl = LevelsCtrlLookup.getLevelsCtrl();
        levelsCtrl.addPropertyChangeListener(this);
        levels = levelsCtrl.getLevels();
    }
    
    @Override
    public Object getElementAt(int index) {
        synchronized(this) {
            return levels.get(index);
        }
    }
    
    @Override
    public int getSize() {
        synchronized(this) {
            return levels.size();
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        synchronized(this) {
            levels = levelsCtrl.getLevels();
        }
        fireContentsChanged(this, 0, getSize());
    }
    
}
