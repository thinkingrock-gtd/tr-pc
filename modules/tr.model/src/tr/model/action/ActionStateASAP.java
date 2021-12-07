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

import au.com.trgtd.tr.util.Utils;
import java.util.Calendar;
import java.util.Date;
import tr.model.action.ActionState.Type;

/**
 * Do ASAP action state.
 *
 * @author Jeremy Moore
 */
public class ActionStateASAP extends ActionState {
    
    /* @deprecated */
    private Date due;
    
    /** Constructs a new instance. */
    public ActionStateASAP() {
        super();
    }
    
    /**
     * Makes an exact copy of itself.
     * @return The copy.
     */
    @Override
    public ActionState copy() {
        ActionStateASAP copy = new ActionStateASAP();
        copy.created = this.created;
        copy.due = this.due;
        return copy;
    }
    
    /**
     * Gets the due date.
     * @return the due date value.
     * @deprecated
     */
    public Date getDueDate() {
        return due;
    }
    
    /**
     * Sets the due date value after clearing time values.
     * @param date The date to set.
     * @deprecated
     */
    public void setDueDate(Date due) {
        // remove time from date
        if (due != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(due);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            due = cal.getTime();
        }
        
        if (!Utils.equal(this.due, due)) {
            this.due = due;
            notifyObservers(this);
        }
    }
    
    @Override
    public final ActionState.Type getType() {
        return Type.DOASAP;
    }
    
}
