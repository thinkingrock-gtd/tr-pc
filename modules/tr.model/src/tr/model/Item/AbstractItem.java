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
        if (that instanceof AbstractItem) {
            return this.getID() == ((AbstractItem)that).getID();
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
        if (parent instanceof Project) {
            this.parent = (Project)parent;        
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
