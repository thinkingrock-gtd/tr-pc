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
package tr.model;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Data lookup singleton.
 *
 * @author Jeremy Moore
 */
public class DataLookup extends ProxyLookup {
    
    private static final DataLookup INSTANCE = new DataLookup();

    /** Private singleton constructor. */
    private DataLookup() {
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static DataLookup instance() {
        return INSTANCE;
    }
    
    /**
     * Sets or removes the data instance.
     * @param data The data instance to set or null to remove.
     */
    public void setData(Data data) {
        if (data == null) {
            setLookups(new Lookup[] {});                        
        } else {
            setLookups(new Lookup[] {Lookups.singleton(data)});            
        }
    }
    
}
