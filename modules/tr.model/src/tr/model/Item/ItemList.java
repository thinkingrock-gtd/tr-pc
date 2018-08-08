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

import java.util.Iterator;

/**
 * Interface for classes of objects which are a list of items.  An item list is
 * an item and therefore can contain item lists as well as items.
 *
 * @author Jeremy Moore (jimoore@netscape.net.au)
 */
public interface ItemList extends Item {
    
    /**
     * Determines whether a given item can be added.
     * @param item the given item.
     * @return true if the item can be added.
     */
    public boolean canAdd(Item item);
    
    /**
     * Adds a given item if possible.
     * @param item The item to add.
     * @return true if the item was added.
     */
    public boolean add(Item item);
    
    /**
     * Adds a given item at the given index if possible.
     * @param index The index at which the item is to be inserted.
     * @param item The item to be added.
     */
    public void add(int index, Item item);
    
    /**
     * Removes the item at the given index.
     * @param index The given index.
     * @return The item that was removed.
     */
    public Item remove(int index);

    /**
     * Removes the item at the given index.
     * @param item the item to remove.
     * @return true if the item in the list and was removed.
     */
    public boolean remove(Item item);

    /**
     * Determines whether this list directly contains a given item.
     * @param item The item to look for.
     * @return true if this list directly contains the given item.
     */
    public boolean contains(Item item);

    /**
     * Determines whether this list contains a given item.
     * @param item The item to look for.
     * @param recurse Whether or not to recurse into sub-lists.
     * @return true if this list contains the given item.
     */
    public boolean contains(Item item, boolean recurse);

    /**
     * Determines whether this list contains an item defined by the given item
     * selector.
     * @param selector The item selector.
     * @param recurse Whether or not to recurse into sub-lists.
     * @return true if an item defined by the selector is found.
     */
    public boolean contains(ItemSelector selector, boolean recurse);
    
    /**
     * Gets an iterator over items in the list where the class of the item is 
     * the same as or a subclass of the given class.
     * @param <T> The item type.
     * @param clazz The given class.
     * @return the iterator containing all item instances of the given class or 
     * of a subclass of the given class.
     */
    public <T extends Item> Iterator<T> iterator(Class<T> clazz);
    
    /**
     * Gets the list size.
     * @return The number of items in the list.
     */
    public int size();
    
    /**
     * Returns the index of the given item.
     * @param item The item to get the index for.
     * @return The index of the given item or -1 if the item is not in the list.
     */
    public int indexOf(Item item);
        
}
