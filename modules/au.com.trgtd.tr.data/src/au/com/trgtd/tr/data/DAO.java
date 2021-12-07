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
package au.com.trgtd.tr.data;

/**
 * Data access object (DAO).
 *
 * @author Jeremy Moore
 * @param <D> The data type.
 */
public interface DAO<D> {

    /**
     * Gets the data transfer object.
     * @return the data transfer object.
     */
    public D getData();

    /**
     * Determines whether or not data exists in the persistent storage.
     * @return true if data exists in storage. 
     */
    public boolean hasPersistantData();

    /**
     * Saves data to persistent storage.
     * @throws java.lang.Exception
     */
    public void persist() throws Exception;

    /**
     * Restores data from persistent storage if possible.
     * @throws java.lang.Exception
     */
    public void restore() throws Exception;

    /**
     * Deletes the persistent data storage.
     */
    public void delete();

    /**
     * Resets to the initial state.
     */
    public void reset();
}
