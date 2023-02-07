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
package au.com.trgtd.tr.view.filters;

import au.com.trgtd.tr.appl.Constants;
import java.io.Serializable;
import java.util.Vector;
import org.openide.util.NbBundle;
import tr.model.IDGenerator;
import tr.model.criteria.Value;
 
/**
 *
 * @author Jeremy Moore
 */
public class ValueMultiple extends Value implements Serializable {
    
    private static final IDGenerator multipleIDGenerator = new IDGenerator() {
        public int getNextID() {
            return Constants.ID_FILTER_MULTIPLE;
        }
    };
    
    private Vector<Value> chosen;
    
    /** Constructs a new instance. */
    public ValueMultiple() {
        super(NbBundle.getMessage(ValueMultiple.class, "filter-multiple"), multipleIDGenerator);
    }
    
    public Vector<Value> getChosen() {
        return (chosen == null) ? new Vector<>() : chosen;
    }
    
    public void setChosen(Vector<Value> chosen) {
        this.chosen = chosen;
    }
    
    public boolean equals(Object object) {
        return object instanceof ValueMultiple;
    }
    
}
