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

import tr.model.Data;
import au.com.trgtd.tr.util.Observable;

/**
 * Data store interface.
 *
 * @author Jeremy Moore
 */
public interface DataStore extends Observable {
    
    /**
     *  Gets the data file path preference.
     *  @return The data file path.
     */
    public String getPath();

    /**
     *  Sets the data file path preference.
     *  @param path The data file path.
     */
    public void setPath(String path);
    
    /**
     * Gets the current working data.
     * @return the data.
     */
    public Data getData();
    
    /**
     * Sets the current working data.
     * @param data the data to set.
     */
    public void setData(Data data);
    
    /**
     * Sets the changed state of the data.
     * @param changed The new changed state.
     */
    public void setChanged(boolean changed);
    
    /**
     * Gets the changed state of the data.
     * @return the data changed state.
     */
    public boolean hasChanged();
    
    /**
     * Determines whether data is loaded.
     * @return true if and only if data is loaded.
     */
    public boolean isLoaded();
    
    /**
     * Loads data from persistent storage and sets it as the current working
     * data.
     * @throws Exception if an exception occurs while loading the data.
     */
    public void load() throws Exception;
    
    /**
     * Stores the current working data to persistent storage.
     * @throws Exception if an exception occurs while storing the data.
     */
    public void store() throws Exception;
    
    /**
     * Starts the data store daemon.
     */
    public void startDaemon();
    
    /**
     * Stops the data store daemon.
     */
    public void stopDaemon();
    
    
}
