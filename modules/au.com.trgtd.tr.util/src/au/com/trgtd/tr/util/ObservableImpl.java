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
package au.com.trgtd.tr.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Observable implementation that tries to avoid memory leaks by using weak
 * references to observers.
 *
 * This class is part of an improved Observer design pattern implementation to
 * that of the java.util package.
 *
 * @author Jeremy Moore
 */
public class ObservableImpl implements Observable, Serializable {

    private static final long serialVersionUID = 1;

    private transient Map<Observer, Object> observers;

    /**
     * Constructs a default instance.
     */
    public ObservableImpl() {
    }

    /**
     * Adds an observer to the set of observers, provided that it is not the
     * same as some observer already in the set. The order in which
     * notifications will be delivered to multiple observers is not specified.
     *
     * @param observer The observer to add.
     */
    @Override
    public void addObserver(Observer observer) {
        if (observer == null) {
            return;
        }
        if (observers == null) {
            observers = Collections.synchronizedMap(new WeakHashMap<>());
        }
        observers.put(observer, null);
    }

    /**
     * Removes an observer from the set of observers of this object.
     *
     * @param observer The observer to remove.
     */
    @Override
    public void removeObserver(Observer observer) {
        if (observer == null || observers == null) {
            return;
        }
        observers.remove(observer);
    }

    /**
     * Removes all observers from the list.
     */
    @Override
    public void removeObservers() {
        if (observers == null) {
            return;
        }
        observers.clear();
    }

    /**
     * Override to reset all child observers.
     */
    @Override
    public void resetObservers() {
    }

    /**
     * Notify all observers. Each observer has its update method called with two
     * arguments: the observable and null.
     *
     * @param observable The observable.
     */
    public void notifyObservers(Observable observable) {
        notifyObservers(observable, null);
    }

    /**
     * Notify all observers. Each observer has its update method called with two
     * arguments: the observable and the object.
     *
     * @param observable The observable.
     * @param object The object or null.
     */
    public void notifyObservers(Observable observable, Object object) {
        if (observers == null) {
            return;
        }
        for (Observer observer : new HashSet<>(observers.keySet())) {
            observer.update(observable, object);
        }
    }
}
