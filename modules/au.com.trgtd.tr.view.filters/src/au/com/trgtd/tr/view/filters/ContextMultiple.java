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
package au.com.trgtd.tr.view.filters;

import au.com.trgtd.tr.appl.Constants;
import java.io.Serializable;
import java.util.Vector;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.context.Context; 

/**
 * Context for multiple contexts.
 *
 * @author Jeremy Moore
 */
public class ContextMultiple extends Context implements MultipleItem<Context>, Serializable {
    
    public static final String ID = ChoiceMultiple.ID;
    
    private Vector<Context> chosen;
    
    public ContextMultiple() {
        super(Constants.ID_FILTER_CONTEXTS_MULTIPLE);
        if (Utilities.isWindows()) {
            setName(NbBundle.getMessage(ContextMultiple.class, "windows-multiple"));
        } else {
            setName(NbBundle.getMessage(ContextMultiple.class, "filter-multiple"));
        }
    }
    
    public Vector<Context> getChosen() {
        return (chosen == null) ? new Vector<>() : chosen;
    }
    
    public void setChosen(Vector<Context> chosen) {
        this.chosen = chosen;
    }
    
    public boolean equals(Object object) {
        return object instanceof ContextMultiple;
    }
    
}
