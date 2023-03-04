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
package au.com.trgtd.tr.view.goals.levels.combo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

/**
 * Goal Levels ComboBox model.
 *
 * @author Jeremy Moore
 */
public class LevelsComboBoxModel extends DefaultComboBoxModel<LevelCtrl> implements PropertyChangeListener {
    
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
    public LevelCtrl getElementAt(int index) {
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
