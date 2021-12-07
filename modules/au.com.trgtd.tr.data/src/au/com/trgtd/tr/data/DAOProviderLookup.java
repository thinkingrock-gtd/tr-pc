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

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * DAO provider lookup singleton.
 *
 * @author Jeremy Moore
 */
public class DAOProviderLookup extends ProxyLookup {

    private static final DAOProviderLookup instance = new DAOProviderLookup();

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static DAOProviderLookup instance() {
        return instance;
    }

    /** Private singleton constructor. */
    private DAOProviderLookup() {
    }

    /**
     * Sets or removes the data instance.
     * @param provider The data access object provider to set or null to remove.
     */
    public void setDAOProvider(DAOProvider provider) {
        if (provider == null) {
            setLookups(new Lookup[]{});
        } else {
            setLookups(new Lookup[]{Lookups.singleton(provider)});
        }
    }
}
