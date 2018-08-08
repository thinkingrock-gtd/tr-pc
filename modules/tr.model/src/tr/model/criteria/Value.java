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
        if (object instanceof Value) {
            Value c = (Value)object;
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
