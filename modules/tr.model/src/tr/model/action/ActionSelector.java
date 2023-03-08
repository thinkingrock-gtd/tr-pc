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
package tr.model.action;

/**
 * Interface for named action selectors.
 *
 * @author Jeremy Moore
 */
public interface ActionSelector {
    
    /**
     * Returns the name of the action selector.
     * @return The name.
     */
    public String getName();
    
    /**
     * Determines whether the given project and action should be selected or not.
     * @param action The action
     * @return true iff the action should be selected.
     */
    public boolean isSelected(Action action);
    
}
