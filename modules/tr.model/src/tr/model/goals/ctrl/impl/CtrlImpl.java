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
package tr.model.goals.ctrl.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Controller base class.
 * 
 * @author Jeremy Moore
 */
public abstract class CtrlImpl {

    private static int count = 0;
    public final int n;

    public int getN() {
        return n;
    }
    




    private final Integer id;
    private final PropertyChangeSupport propertyChangeSupport;

    public CtrlImpl(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Controller must have an ID.");
        }
        this.id = id;
        this.propertyChangeSupport = new PropertyChangeSupport(this);




        n = ++count;
    }

    public int getID() {
        return id;
    }

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof CtrlImpl ctrlImpl) {
            return ctrlImpl.getID() == this.getID();
        }
        return false;
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public synchronized boolean hasListeners(String propertyName) {
        return propertyChangeSupport.hasListeners(propertyName);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }

    public void firePropertyChange(PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange(evt);
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

}
