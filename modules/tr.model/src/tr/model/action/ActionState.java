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
package tr.model.action;

import java.util.Date;

import au.com.trgtd.tr.util.ObservableImpl;

/**
 * Action state abstract base class.
 *
 * @author Jeremy Moore
 */
public abstract class ActionState extends ObservableImpl {
    
    protected Date created;
    
    public enum Type { DOASAP, INACTIVE, DELEGATED, SCHEDULED };
    
    /**
     * Constructs a new instance.
     */
    public ActionState() {
        created = new Date();
    }
    
    /**
     * Makes an exact copy of itself.
     * @return The copy.
     */
    public abstract ActionState copy();
    
    /**
     * Gets the creation date.
     * @return The creation date.
     */
    public Date getCreated() {
        return created;
    }
    
    public abstract Type getType();
    
    /**
     * Overrides equals to compare with another object for equality.
     * @param object The object to compare with.
     * @return true if the object is an ActionState instance and its creation
     * date equals this state's creation date.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        return created.equals(((ActionState)object).created);
    }

    @Override
    public int hashCode() {
        return created == null ? 0 : created.hashCode();
    }
    
}
