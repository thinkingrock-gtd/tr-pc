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
package au.com.trgtd.tr.view.actns.screens.filters;

import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.filters.ChoiceAll;
import au.com.trgtd.tr.view.filters.ValueAll;
import au.com.trgtd.tr.view.filters.ValueMultiple;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import java.util.logging.Logger;
import tr.model.criteria.Value;

/**
 * MatcherEditor the matches the energy criterion.
 *
 * @author Jeremy Moore
 */
public abstract class FilterCriterion extends FilterChoice
        implements PropertyChangeListener {
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    /** Constructs a new instance. */
    public FilterCriterion() {
    }
    
    protected boolean canExcludeNulls() {
        return true;
    }
    
    public String[] getSerialValues() {
        if (combo == null) {
            return null;
        }
        Value value = (Value)combo.getSelectedItem();
        if (value == null) {
            return null;
        }
        if (value instanceof ValueAll) {
            return new String[] { ChoiceAll.ID };
        }
        if (value instanceof ValueMultiple m) {
            if (m.getChosen() == null || m.getChosen().isEmpty()) {
                return null;
            }
            String[] values = new String[m.getChosen().size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = String.valueOf((m.getChosen().get(i)).getID());
            }
            return values;
        }
        return new String[] { String.valueOf(value.getID()) };
    }
    
    public void setSerialValues(String[] values) {
        if (combo == null || combo.getItemCount() == 0) return;
        
        combo.stopChangeEvents();
        
        if (values == null || values.length == 0) {
            combo.setSelectedItem(null);
        } else if (values.length == 1) {
            if (values[0].equals(ChoiceAll.ID)) {
                combo.setSelectedIndex(0); // all
            } else {
                combo.setSelectedItem(getValue(values[0]));
            }
        } else if (values.length > 1) {
            combo.setSelectedIndex(1); // multiple
            
            ValueMultiple m = (ValueMultiple)combo.getItemAt(1);
            
            m.setChosen(new Vector<>());
            for (String id : values) {
                m.getChosen().add(getValue(id));
            }
        }
        
        combo.startChangeEvents();
    }
    
    protected abstract Value getValue(String id);
    
    public boolean equals(Object object) {
        if (!(object instanceof FilterCriterion)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Value thisValue = (Value)combo.getSelectedItem();
        Value thatValue = (Value) ((FilterChoice) object).combo.getSelectedItem();
        return Utils.equal(thisValue, thatValue);
    }
    
}
