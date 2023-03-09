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
package au.com.trgtd.tr.view.projects.actions;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Single actions tree lookup singleton.
 *
 * @author Jeremy Moore
 */
public class SingleActionsLookup extends ProxyLookup {
    
    private static final SingleActionsLookup instance = new SingleActionsLookup();
    
    private SingleActionsLookup() {
    }
    
    /**
     * Sets or removes the data instance.
     * @param ds The data store instance to set or null to remove.
     */
    public static void register(SingleActionsTopComponent satc) {
        if (satc == null) {
            instance.setLookups(new Lookup[] {});
        } else {
            instance.setLookups(new Lookup[] {Lookups.singleton(satc)});
        }
    }
        
    /**
     * Looks up the single actions tree.
     * @return The single actions tree or null.
     */
    public static SingleActionsTopComponent lookup() {        
        return instance.lookup(SingleActionsTopComponent.class);
    }
    
}
