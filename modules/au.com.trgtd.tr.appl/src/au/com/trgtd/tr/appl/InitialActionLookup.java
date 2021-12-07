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
package au.com.trgtd.tr.appl;
 
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * Initial actions lookup singleton.
 *
 * @author Jeremy Moore
 */
public class InitialActionLookup extends ProxyLookup {
    
    private static final InitialActionLookup instance = new InitialActionLookup();

    private final InstanceContent ic;
    private final AbstractLookup al;
    
    /** Private singleton constructor. */
    private InitialActionLookup() {
        ic = new InstanceContent();
        al = new AbstractLookup(ic);
        setLookups(new Lookup[] {al});
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static InitialActionLookup instance() {
        return instance;
    }
    
    /**
     * Sets or removes the data instance.
     * @param data The data instance to set or null to remove.
     */
    public void add(InitialAction action) {
        if (action != null) {
            ic.add (action);
        }
        
//        List list = Arrays.asList(getLookups());
//        list.add(action);        
//        Lookup[] lookups = (Lookup[])list.toArray(new Lookup[] {});                
//        setLookups(lookups);
    }
    
}
