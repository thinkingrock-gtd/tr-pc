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
package au.com.trgtd.tr.view.actns.screens;

import java.util.Comparator;
import tr.model.criteria.Value;

/**
 * Comparator for the time criteria.
 *
 * @author Jeremy Moore
 */
public class ComparatorValues implements Comparator<StyledValue> {
    
    private final ValueIDsProvider provider;
    
    public ComparatorValues(ValueIDsProvider provider) {
        this.provider = provider;
    }
    
    /**
     * Ordering is first by time values in time definition order, then nulls.
     */
    public int compare(StyledValue sv1, StyledValue sv2) {
        Value v1 = sv1.value;
        Value v2 = sv2.value;
        
        if (v1 == v2) return 0;
        if (v1 == null) return 1;
        if (v2 == null) return -1;
        
        int p1 = provider.getIDs().indexOf(v1.getID());
        int p2 = provider.getIDs().indexOf(v2.getID());
        
        if (p1 == -1) return 1;
        if (p2 == -1) return -1;
        
        return p1 - p2;
    }
    
}

