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

