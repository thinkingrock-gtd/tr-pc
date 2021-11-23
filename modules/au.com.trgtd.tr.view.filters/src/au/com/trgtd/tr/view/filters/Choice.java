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

import java.io.Serializable;

/**
 * Choice base class.
 *
 * @author Jeremy Moore
 */
public abstract class Choice implements Serializable, Comparable<Choice> {
    
    /** Constructs a new instance. */
    public Choice() {}
    
    public abstract String getLabel();
    
    public abstract String getID();
    
    public String toString() {
        return getLabel();
    }
    
    public int compareTo(Choice choice) {
        if (choice.equals(this)) {
            return 0;            
        }        
        if (choice instanceof ChoiceAll) {
            return 1; // after the All choice
        }
        if (choice instanceof ChoiceMultiple) {
            return 1; // after the Multiple choice
        }
        if (choice instanceof ChoiceMultipleEdit) {
            return 1; // after the MultipleEdit choice
        }
        return toString().compareToIgnoreCase(choice.toString());
    }
    
    public boolean equals(Object object) {
        return object instanceof Choice && getID().equals(((Choice)object).getID());
    }
    
    
}
