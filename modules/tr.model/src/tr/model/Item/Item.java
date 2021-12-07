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

import java.util.Date;
import javax.swing.ImageIcon;
import tr.model.IDGenerator;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Interface for items that can be in item lists.
 *
 * @author Jeremy Moore
 */
public interface Item extends Observable, Observer, Comparable<Item> {
    
    /**
     * Gets the ID number.
     * @return the ID number.
     */
    public int getID();
    
    /**
     * Gets the creation date.
     * @return the creation date.
     */
    public Date getCreated();
    
    /**
     * Gets the description.
     * @return the description.
     */
    public String getDescription();
    
    /**
     * Sets the description.
     * @param description The new description.
     */
    public void setDescription(String description);
    
    /**
     * Gets the icon representing the item.
     * @param opened Whether or not the item is in an opened state.
     * @return The icon.
     */
    public ImageIcon getIcon(boolean opened);
    
    /**
     * Gets the parent item list.
     * @return The parent.
     */
    public ItemList getParent();
    
    /**
     * Sets the parent item list.
     * @param parent The new parent.
     */
    public void setParent(ItemList parent);
    
    /**
     * Makes a copy with a new ID.
     * @param idGenerator The id generator.
     * @return the copy.
     */
    public Item copy(IDGenerator idGenerator);
    
    /**
     * Removes this item from its parent.
     */
    public void removeFromParent();
    
    /**
     * Determines whether this item is directly or indirectly within a given
     * list.
     * @param list The given list.
     * @return true it this item is in the given list.
     */
    public boolean isWithin(ItemList list);
    
    /**
     * Determines whether the item is user editable.
     * @return true if the item is editable.
     */
    public boolean isEditable();
    
    
    public boolean isProject();

    public boolean isAction();

}

