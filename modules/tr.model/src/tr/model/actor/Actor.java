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
package tr.model.actor;

import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Utils;
import java.util.Objects;

/**
 * A doer of actions. Initially for delegates.
 *
 * @author Jeremy Moore
 */
public class Actor extends ObservableImpl {
    
    private Integer id;
    private String name;
    private String email;
    private boolean inactive;
    private boolean team;

    /**
     * Constructs a new instance.
     * @param id
     */
    public Actor(int id) {
        this.id = id;
    }

    public void initID(int id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public int getID() {
        return id;
    }
    
    /**
     * Gets the name.
     * @return The name.
     */
    public String getName() {
        return name == null ? "" : name;
    }
    
    /**
     * Sets the name.
     * @param name The name.
     */
    public void setName(String name) {
        if (Utils.equal(name, this.name)) {
            return;            
        }
        this.name = name.trim();
        notifyObservers(this);
    }
    
    /**
     * Gets the email address.
     * @return The email address.
     */
    public String getEmail() {
        return email == null ? "" : email;
    }
    
    /**
     * Sets the email address.
     * @param email The email address.
     */
    public void setEmail(String email) {
        if (Utils.equal(email, this.email)) {
            return;
        }
        this.email = email.trim();
        notifyObservers(this);
    }
       
    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        if (this.inactive == inactive) {
            return;
        }
        this.inactive = inactive;
        notifyObservers(this);
    }

    public boolean isTeam() {
        return team;
    }

    public void setTeam(boolean team) {
        if (this.team == team) {
            return;
        }
        this.team = team;
        notifyObservers(this);
    }
    
    /**
     * Gets the string representation.
     * @return the name.
     */
    @Override
    public String toString() {
        return getName();
    }
    
    /**
     * Overrides equals to compare with another object for equality.
     * @param object
     * @return true if the object is an Actor with equal id.
     */
    @Override
    public boolean equals(Object object) {
        return (object instanceof Actor && Objects.equals(id, ((Actor)object).id));
    }

    /**
     * Overrides hashCode to return id.
     * @return the id.
     */
    @Override
    public int hashCode() {
        return id;
    }
    
}
