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
package au.com.trgtd.tr.view.projects;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Projects tree lookup singleton.
 *
 * @author Jeremy Moore
 */
public class ProjectsTreeLookup extends ProxyLookup {
    
    private static final ProjectsTreeLookup instance = new ProjectsTreeLookup();
    
    private ProjectsTreeLookup() {}
    
    /**
     * Sets or removes the data instance.
     * @param ds The data store instance to set or null to remove.
     */
    public static void register(ProjectsTreeTopComponent pttc) {
        if (pttc == null) {
            instance.setLookups(new Lookup[] {});
        } else {
            instance.setLookups(new Lookup[] {Lookups.singleton(pttc)});
        }
    }
    
    /**
     * Looks up the registered projects tree.
     * @return The projects tree or null.
     */
    public static ProjectsTreeTopComponent lookup() {
        return instance.lookup(ProjectsTreeTopComponent.class);
    }
    
}
