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
package tr.model.criteria;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.util.Manager;

/**
 * Criterion is a named manager of criterion values.
 *
 * @author Jeremy Moore
 */
public class Criterion extends ObservableImpl implements Observable, Observer {
    
    private final String name;
    private boolean use;
    public final Manager<Value> values;
    
    public transient String key;
    
    /**
     * Constructs a new instance with the given name.
     * @param name The name.
     */
    public Criterion(String name) {
        super();
        this.name = name;
        values = new Manager<>();
        values.addObserver(this);
    }
        
    public boolean isUse() {
        return use;
    }

    /**
     * Sets the use value.
     */
    public void setUse(boolean use) {
        if (this.use == use) return;
        
        this.use = use;

        notifyObservers(this);        
    }

    public String getName() {
        return NbBundle.getMessage(Data.class, name);
    }

    /**
     * Resets observation of values.
     */
    @Override
    public void resetObservers() {
        values.resetObservers();
        values.addObserver(this);
    }
    
    /**
     * Passes on notification of changes in contained values.
     */
    @Override
    public void update(Observable observable, Object object) {
        notifyObservers(observable, object);
    }

    /**
     * Gets the display string.
     * @return The name.
     */
    @Override
    public String toString() {
        return getName();
    }
    
}
