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
