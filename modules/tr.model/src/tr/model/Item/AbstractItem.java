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
package tr.model.Item;

import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import tr.model.IDGenerator;
import tr.model.project.Project;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Utils;

/**
 * Abstract base item.
 *
 * @author Jeremy Moore
 */
public abstract class AbstractItem extends ObservableImpl implements Item {
    
    protected Integer id;
    protected Date created;
    protected ItemList parent;
    protected String description;

    /** 
     * Constructs a new instance.
     */
    public AbstractItem(IDGenerator idGenerator) {
        id = idGenerator.getNextID();
        setCreated();
        setParent(null);
        setDescription("");
    }
    
    private void setCreated() {
        created = Calendar.getInstance().getTime();
    }
    
    @Override
    public String toString() {
        return getDescription();
    }    
    
    @Override
    public boolean equals(Object that) {
        if (that instanceof AbstractItem abstractItem) {
            return this.getID() == abstractItem.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
    /* Item implementation */    
    /**
     * Gets the ID number.
     * @return the ID number.
     */
    public int getID() {
        return id;
    }
    
    /**
     * Gets the creation date.
     * @return the creation date.
     */
    public Date getCreated() {
        return created;
    }
        
    /**
     * Gets the description.
     * @return the description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * @param description The new description.
     */
    public void setDescription(String description) {                
        if (Utils.equal(this.description, description)) return;
        
        this.description = (description == null) ? "" : description;
        
        notifyObservers(this);
    }

    /**
     * Gets the icon representing the action item.
     * @param expanded is not applicable and is ignored.
     * @return The icon.
     */
    public abstract ImageIcon getIcon(boolean expanded);
    
    /**
     * Gets the parent item list.
     * @return The parent.
     */
    public ItemList getParent() {
        return parent;
    }    

    /**
     * Sets the parent item list.
     * @param parent The new parent.
     */
    public void setParent(ItemList parent) {
        if (Utils.equal(this.parent, parent)) {
            return;
        }        
        if (parent instanceof Project project) {
            this.parent = project;
            notifyObservers(this);            
        }        
    }
    
    /**
     * Makes a copy of the action item. The copy has a new ID and creation date.
     * @param idGeneratorid The id generator.
     * @return the copy.
     */
    public abstract Item copy(IDGenerator idGenerator);
    
    /**
     * Removes this action item from its parent item list.
     */
    public void removeFromParent() {
        if (parent != null) {
            parent.remove(this);
        }
    }    
        
    /**
     * Determines whether this item is directly or indirectly within a given
     * list.
     * @param list The given list.
     */
    public boolean isWithin(ItemList list) {
        if (list == null || parent == null) {
            return false;
        }         
        if (list == parent) {
            return true;            
        }                
        return parent.isWithin(list);                
    }    
    
    /**
     * Determines whether the item is user editable.
     * @return true.
     */
    public boolean isEditable() {
        return true;
    }    
    /* End of Item implementation */
    
    /* Observer implementation */    
     /**
     * Handle update by passing on changes to observers.
     */
    public void update(Observable observable, Object arguement) {
        notifyObservers(this, arguement);
    }
    /* End of Observer implementation */
    
    /* Observable implementation */
    /**
     * Resets observing of state, topic and context.
     */
    @Override
    public void resetObservers() {
    }

    /* End of Observable implementation */

}
