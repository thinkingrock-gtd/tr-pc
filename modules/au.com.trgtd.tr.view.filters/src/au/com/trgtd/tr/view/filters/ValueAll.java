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
import org.openide.util.NbBundle;
import tr.model.IDGenerator;
import tr.model.criteria.Value;

/**
 * 
 * @author Jeremy Moore
 */
public class ValueAll extends Value implements Serializable {
    
    private static final IDGenerator allIDGenerator = new IDGenerator() {
        public int getNextID() {
            return Constants.ID_FILTER_ALL;
        }
    };
    
    /** Constructs a new instance. */
    public ValueAll() {
        super(NbBundle.getMessage(ValueAll.class, "filter-all"), allIDGenerator);
    }
    
    public boolean equals(Object object) {
        return object instanceof ValueAll;
    }
    
}
