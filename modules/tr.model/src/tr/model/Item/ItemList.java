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
