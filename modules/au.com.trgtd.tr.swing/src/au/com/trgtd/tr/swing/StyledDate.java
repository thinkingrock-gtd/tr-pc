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
package au.com.trgtd.tr.swing;

import java.text.DateFormat;
import java.util.Date;

/**
 * Extends <code>StyledLabel</code> to provide styled date.
 *
 * @author Jeremy Moore
 */
public class StyledDate extends StyledString implements Comparable {
    
    private Date date;
    private boolean showTime;
    
    private final DateFormat dateformat;
    private final DateFormat datetimeformat;
    
    
    public StyledDate(DateFormat dateformat, DateFormat datetimeformat) {
        super();
        this.dateformat = dateformat;
        this.datetimeformat = datetimeformat;
    }
    
    /**
     * Sets the date.
     * @param date The date to set.
     */
    public synchronized void setDate(Date date) {
        this.date = date;
        if (date == null) {
            setString("");
            return;
        }
        try {
            setString(showTime ? datetimeformat.format(date) : dateformat.format(date));
        } catch (Exception ex) {
            setString("");
        }
    }
    
    /**
     * Gets the date.
     * @return the date value.
     */
    public synchronized Date getDate() {
        return date;
    }
    
    public void setShowTime(boolean b) {
        showTime = b;
        setDate(date);
    }
    
    /**
     * Override to provide date ordering by dates then nulls.
     * @param object The Object to compare to.
     * @return -1, 0, 1 if this.date is less than, equal to or greater than
     * o.date respectively.
     */
    @Override
    public int compareTo(Object object) {
        if ( ! (object instanceof StyledDate)) {
            return -1;
        }
        
        Date d1 = getDate();
        Date d2 = ((StyledDate)object).getDate();
        
        if (d1 == d2) return 0;
        if (d1 == null) return 1;  // d1 (null) > d2
        if (d2 == null) return -1; // d1 < d2 (null)
        
        return d1.compareTo(d2);
    }
    
}
