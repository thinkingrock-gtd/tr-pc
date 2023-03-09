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
package au.com.trgtd.tr.view.contexts;

import au.com.trgtd.tr.appl.InitialAction;
import au.com.trgtd.tr.appl.InitialActionLookup;
import org.openide.modules.ModuleInstall;
import org.openide.util.actions.SystemAction;

/**
 * Contexts module installer.
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {
    
    /** Adds the contexts action to the lookup of initial actions. */
    public void restored() {
        InitialAction action = SystemAction.get(ContextsAction.class);
        InitialActionLookup.instance().add(action);
    }
    
}
