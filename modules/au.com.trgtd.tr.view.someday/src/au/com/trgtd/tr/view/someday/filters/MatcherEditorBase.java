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
package au.com.trgtd.tr.view.someday.filters;

import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import java.awt.Component;
import java.io.Serializable;
import tr.model.future.Future;

/**
 * Base class for items matcher editors.
 *
 * @author Jeremy Moore
 */
public abstract class MatcherEditorBase extends AbstractMatcherEditor<Future> {
    
    /** Default constructor. */
    public MatcherEditorBase() {}
    
    /** Gets the view label. */
    public abstract String getLabel();
    
    /** Gets the view component. */
    public abstract Component getComponent();
    
    /** Gets the serializable value. */
    public abstract Serializable getSerializable();
    
    /** Sets the serializable value. */
    public abstract void setSerializable(Serializable serializable);

    /** Resets the filter. */
    public abstract void reset();

    
}
