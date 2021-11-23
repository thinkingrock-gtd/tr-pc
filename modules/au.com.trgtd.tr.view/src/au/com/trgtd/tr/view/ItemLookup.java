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
package au.com.trgtd.tr.view;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * Lookup singleton for current Action and/or Project item.
 *
 * @author Jeremy Moore
 */
public class ItemLookup extends ProxyLookup {

    private static final ItemLookup instance = new ItemLookup();

    /** Private singleton constructor. */
    private ItemLookup() {
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static ItemLookup instance() {
        return instance;
    }

    /**
     * Sets or removes the current action. The action is put in the lookup.
     * @param action The action to set as current or null to clear.
     */
    public void setCurrent(Action action) {
        if (action == null) {
            setLookups(new Lookup[]{});
        } else {
//            Object[] items = new Object[] {action, action.getParent()};
//            setLookups(new Lookup[] {Lookups.fixed(items)});
            setLookups(new Lookup[]{Lookups.singleton(action)});
        }
    }

    /**
     * Sets or removes the current project. The project is put in the lookup.
     * @param project The project to set as current or null to clear.
     */
    public void setCurrent(Project project) {
        if (project == null) {
            setLookups(new Lookup[]{});
        } else {
            setLookups(new Lookup[]{Lookups.singleton(project)});
        }
    }
}
