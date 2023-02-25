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
package tr.model.criteria;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Utils;
import tr.model.IDGenerator;

/**
 * Criterion mappedValue - a priority, effort, simple time or similar mappedValue.
 * 
 * @author Jeremy Moore
 */
public class Value extends ObservableImpl implements Observable, Comparable<Value>{
    
    private final Integer id;
    private String name;
    
    private Integer mappedValue; // used for priority iCalendar mappedValue

    /**
     * Constructs a new instance.
     * @param name
     * @param idGenerator
     */
    public Value(String name, IDGenerator idGenerator) {
        this.id = idGenerator.getNextID();
        this.name = name;
    }
    
    /**
     * Gets the ID number.
     * @return the ID number.
     */
    public int getID() {
        return id;
    }
    
    /**
     * Gets the name.
     * @return the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name.
     * @param name The new name.
     */
    public void setName(String name) {
        name = (name == null) ? "" : name;
        if (name.equals(this.name)) {
            return;
        }
        this.name = name;
        notifyObservers(this);
    }

    public Integer getMappedValue() {
        return mappedValue;
    }

    public void setMappedValue(Integer value) {
        this.mappedValue = value;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object instanceof Value c) {
            return (Utils.equal(id, c.id) && Utils.equal(name, c.name));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    /**
     * Implement Comparable for ordering.
     * @param value The Value to compare to.
     * @return -1, 0, 1 if name is less than, equal to or greater than 
     * mappedValue.name respectively.
     */
    @Override
    public int compareTo(Value value) {
        return name.compareToIgnoreCase(value.name);
    }
    
}
