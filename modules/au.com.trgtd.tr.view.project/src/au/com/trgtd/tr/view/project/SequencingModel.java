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
package au.com.trgtd.tr.view.project;

import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import tr.model.project.Sequencing;

/**
 * Sequencing ComboBoxModel.
 */ 
public class SequencingModel extends DefaultComboBoxModel<Sequencing> {
    
    private List<Sequencing> types;
    
    /** Creates a new instance. */
    public SequencingModel() {
        super();
        types = new Vector<>();
        types.add(Sequencing.INTO_SUBPROJECTS);
        types.add(Sequencing.OVER_SUBPROJECTS);
    }
    
    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Sequencing getElementAt(int index) {
        return types.get(index);
    }
    
    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return types.size();
    }
    
}
