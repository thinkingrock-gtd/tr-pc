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
package tr.model.util;

import java.util.Vector;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
 
/**
 * Manager for generic observable items.
 *
 * @author Jeremy Moore
 */
public class Manager<T extends Observable> extends ObservableImpl implements Observer, Observable {
    
    /** Observable argument event base class. */
    public abstract class Event<T> {}
    
    /** Observable argument item added event. */
    public class EventAdd<T> extends Event {
        public final T item;
        public EventAdd(T item) {
            this.item = item;
        }
    }
    
    /** Observable argument item inserted event. */
    public class EventInsert<T> extends Event {
        public final T item;
        public final int posn;
        public EventInsert(T item, int posn) {
            this.item = item;
            this.posn = posn;
        }
    }
    
    /** Observable argument item removed event. */
    public class EventRemove<T> extends Event {
        public final T item;
        public EventRemove(T item) {
            this.item = item;
        }
    }
    
    /** Observable argument all items removed event. */
    public class EventRemoveAll<T> extends Event {}
    
    /** Observable argument items replaced event. */
    public class EventReplace<T> extends Event {
        public final T itemOld;
        public final T itemNew;
        public final int posn;
        public EventReplace(T itemOld, T itemNew, int posn) {
            this.itemOld = itemOld;
            this.itemNew = itemNew;
            this.posn = posn;
        }
    }
    
    private final Vector<T> items;
    
    /**
     * Creates a new instance.
     */
    public Manager() {
        items = new Vector<>();
    }
    
    /**
     * Adds an item.
     * @param item The item to add.
     */
    public final synchronized boolean add(T item) {
        if (item == null) {
            return false;
        }
        
        synchronized(this) {
            if (!items.add(item)) {
                return false;
            }
        }
        
        item.addObserver(this);
        
        notifyObservers(this, new EventAdd<>(item));
        
        return true;
    }
    
    /**
     * Inserts an item.
     * @param item The item to insert.
     * @param pos The position to insert.
     */
    public final synchronized void insert(T item, int pos) {
        if (item == null) {
            return;
        }
        
        synchronized(this) {
            items.add(pos, item);
        }
        
        item.addObserver(this);
        
        notifyObservers(this, new EventInsert<>(item, pos));
    }
    
    /**
     * Removes an item.
     * @param item The item to remove.
     */
    public final synchronized boolean remove(T item) {
        if (item == null) {
            return false;
        }
        
        synchronized(this) {
            if (!items.contains(item)) {
                return false;
            }
            if (!items.remove(item)) {
                return false;
            }
        }
        
        item.removeObserver(this);
        
        notifyObservers(this, new EventRemove<>(item));
        
        return true;
    }
    
    /**
     * Removes all items.
     */
    public final void removeAll() {
        
        synchronized(this) {
            for (T item : items) {
                item.removeObserver(this);
            }
            items.removeAllElements();
        }
        
        notifyObservers(this, new EventRemoveAll());
    }
    
    /**
     * Gets the item at the given index.
     * @param index The index.
     * @return The item at index.
     * @throws IndexOutOfBoundsException If index < 0 or index >= size().
     */
    public final T get(int index) throws IndexOutOfBoundsException {
//        synchronized(this) {
        return items.get(index);
//        }
    }
    
    /**
     * Sets the item at the given index.
     * @param index The index.
     * @param item The item to set.
     * @throws IndexOutOfBoundsException if index < 0 or index >= size().
     */
    public final void set(int index, T item) throws IndexOutOfBoundsException {
        
        T old;
        
        synchronized(this) {
            old = items.set(index, item);
            if (Utils.equal(item, old)) {
                return;
            }
            old.removeObserver(this);
            item.addObserver(this);
        }
        
        notifyObservers(this, new EventReplace<>(old, item, index));
    }
    
    /**
     * Returns a list of the items.
     * @return a List containing the items.
     */
    public final Vector<T> list() {
        synchronized(this) {
            return new Vector<>(items);
        }
    }
    
    /**
     * Returns the index of an item.
     * @return the index of the item.
     */
    public final int indexOf(T item) {
        synchronized(this) {
            return items.indexOf(item);
        }
    }
    
    /**
     * Gets the number of elements.
     * @return the number of elements.
     */
    public final int size() {
        synchronized(this) {
            return items.size();
        }
    }
    
    /**
     * Returns true if there are no elements, false otherwise.
     * @return true if there are no elements.
     */
    public final boolean isEmpty() {
        synchronized(this) {
            return items.isEmpty();
        }
    }

    /**
     * Implements Observer to pass on item changes to Observers.
     * @param observable The observable object.
     * @param arg The object argument.
     */
    public final void update(Observable observable, Object arg) {
        notifyObservers(observable, arg);
    }
    
    /**
     * Resets this object as an observer for all the items.
     */
    @Override
    public final void resetObservers() {
        synchronized(this) {
            for (Observable item : items) {
                item.addObserver(this);
                item.resetObservers();
            }
        }
    }

    // Changes to allow batch updates without notification until afterwards.
    private transient boolean silent;

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public void notifyObservers(Observable observable) {
        if (!silent) {
            super.notifyObservers(observable);
        }
    }

    @Override
    public void notifyObservers(Observable observable, Object object) {
        if (!silent) {
            super.notifyObservers(observable, object);
        }
    }

    public void fireDataChanged() {
        notifyObservers(this);
    }
    // End of Changes to allow batch updates.

}
