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
package tr.model.Item;

/**
 * Interface for classes of objects that can be done.
 *
 * @author Jeremy Moore
 */
public interface Doable {
    
    /**
     * Determine whether the item is done.
     * @return true iff the item is done.
     */
    public boolean isDone();

    /**
     * Determines whether setting the done state is permissable.
     * @param b The done state to consider.
     */
    public boolean canSetDone(boolean b);
    
    /**
     * Sets the done state.
     * @param b The new done state.
     */
    public void setDone(boolean b);
    
}
