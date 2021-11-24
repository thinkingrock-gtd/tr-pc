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
package au.com.trgtd.tr.view.actns.screens;

import au.com.trgtd.tr.swing.StyledString;
import tr.model.criteria.Value;

/**
 * Wraps a criteria value and provides some style attributes such as colour.
 *
 * @author Jeremy Moore
 */
public final class StyledValue extends StyledString {
    
    /** The value. */
    public final Value value;
    
    /**
     * Constructs a new instance for a given value.
     */
    public StyledValue(Value value) {
        super(value == null ? "" : value.getName());
        this.value = value;
    }
    
}
