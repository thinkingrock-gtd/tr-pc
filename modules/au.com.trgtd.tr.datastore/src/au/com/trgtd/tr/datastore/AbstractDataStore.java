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
package au.com.trgtd.tr.datastore;

import java.util.logging.Logger;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;

/**
 * Data store abstract superclass.
 *
 * @author Jeremy Moore
 */
public abstract class AbstractDataStore extends ObservableImpl implements DataStore, Observer {

    private static final Logger LOG = Logger.getLogger("tr.datastore");
    
    // The data
    private Data data;
    // Data loaded state
    private boolean loaded;

    /**
     * Protected constructor for singleton subclasses.
     */
    protected AbstractDataStore() {
    }

    /**
     * Gets the current working data.
     * @return the data.
     */
    @Override
    public Data getData() {
        return data;
    }

    /**
     * Sets the current working data.
     * @param data the data to set.
     */
    @Override
    public void setData(Data data) {
        LOG.fine("Start");

        if (this.data != null) {
            this.data.removeObserver(this);
        }

        this.data = data;

        DataLookup.instance().setData(data);

        if (data == null) {
            loaded = false;
        } else {
            loaded = true;
            data.resetObservers();
            data.addObserver(this);
        }

        notifyObservers(this);
    }

    /**
     * Loads data from persistent storage and sets it as the data.
     * @throws Exception if an exception occurs while loading the data.
     */
    @Override
    public abstract void load() throws Exception;

    /**
     * Stores the current working data to persistent storage.
     * @throws Exception if an exception occurs while storing the data.
     */
    @Override
    public abstract void store() throws Exception;

    /**
     * Sets the data changed state.
     * @param changed The new data changed state.
     */
    @Override
    public void setChanged(boolean changed) {
        if (data == null) {
            return;
        }
        if (data.hasChanged() != changed) {
            data.setChanged(changed);
        }
    }

    /**
     * Gets the data changed state.
     * @return true if the data has changed.
     */
    @Override
    public boolean hasChanged() {
        if (data == null) {
            return false;
        }
        return data.hasChanged();
    }

    /**
     * Determines whether the data is loaded.
     * @return true if the data is loaded.
     */
    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void update(Observable observable, Object arguement) {
        notifyObservers(observable, arguement);
    }
}
