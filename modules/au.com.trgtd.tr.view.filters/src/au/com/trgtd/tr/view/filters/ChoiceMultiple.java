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

import java.util.Vector;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * Multiple choice.
 *
 * @author Jeremy Moore
 */
public class ChoiceMultiple extends Choice {
    
    public static final String ID = "multiple";
    
    private Vector<Choice> chosen;
    
    public Vector<Choice> getChosen() {
        return (chosen == null) ? new Vector<>() : chosen;
    }
    
    public void setChosen(Vector<Choice> chosen) {
        this.chosen = chosen;
    }
    
    public String getID() {
        return ID;
    }
    
    public String getLabel() {
        if (Utilities.isWindows()) {
            return NbBundle.getMessage(ChoiceMultiple.class, "windows-multiple");
        } else {
            return NbBundle.getMessage(ChoiceMultiple.class, "filter-multiple");
        }
    }
    
    public boolean equals(Object object) {
        return object instanceof ChoiceMultiple;
    }
    
}
