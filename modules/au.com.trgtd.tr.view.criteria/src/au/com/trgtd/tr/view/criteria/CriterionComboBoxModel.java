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
package au.com.trgtd.tr.view.criteria;

import javax.swing.DefaultComboBoxModel;
import tr.model.criteria.Criterion; 
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import tr.model.criteria.Value;

/**
 * Criterion ComboBoxModel.
 */
public class CriterionComboBoxModel extends DefaultComboBoxModel<Value> implements Observer {
    
    private final Criterion criterion;
    
    /**
     * Constructs a new instance for the given criterion.
     * @param criterion The criterion.
     */
    public CriterionComboBoxModel(Criterion criterion) {
        super();
        this.criterion = criterion;
        this.criterion.addObserver(this);
    }
    
    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Value getElementAt(int index) {
        return criterion.values.get(index);
    }
    
    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return criterion.values.size();
    }
    
    /**
     * Implement Observer to fire contents changed.
     */
    public void update(Observable o, Object arg) {
        fireContentsChanged(this, 0, getSize());
    }
    
}
