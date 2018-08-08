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
        items = new Vector<T>();
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
        
        notifyObservers(this, new EventAdd(item));
        
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
        
        notifyObservers(this, new EventInsert(item, pos));
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
        
        notifyObservers(this, new EventRemove(item));
        
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
        
        notifyObservers(this, new EventReplace(old, item, index));
    }
    
    /**
     * Returns a list of the items.
     * @return a List containing the items.
     */
    public final Vector<T> list() {
        synchronized(this) {
            return new Vector<T>(items);
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
