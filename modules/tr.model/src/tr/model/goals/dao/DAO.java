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
package tr.model.goals.dao;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * DAO interface.
 */
public interface DAO {

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public boolean hasListeners(String propertyName);

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    public PropertyChangeListener[] getPropertyChangeListeners();

    public void firePropertyChange(PropertyChangeEvent evt);

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue);

    public void firePropertyChange(String propertyName, int oldValue, int newValue);

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue);

    public void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue);

    public void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue);

    public void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) ;



}
