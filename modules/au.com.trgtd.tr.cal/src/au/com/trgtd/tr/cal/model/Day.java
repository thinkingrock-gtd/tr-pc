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
package au.com.trgtd.tr.cal.model;

import au.com.trgtd.tr.cal.utils.DateUtils;
import java.util.Calendar;
import java.util.Date;

/**
 * Day wraps a date with no time values and provides convenience methods.
 * 
 * @author Jeremy Moore
 */
public final class Day {
    
    private final Date date;

    public Day(Date date) {
        if (null == date) {
            throw new IllegalArgumentException("Day date can not be null.");
        }
        this.date = DateUtils.clearTime(date);
    }

    /** 
     * Gets the wrapped date value for the day. 
     * @return the date with all time values set to 0.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Add a number of days to this day and returns the result day.
     * @param n The number of days to add (negative to subtract).
     * @return The result day.
     */
    public Day addDays(int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, n);
        return new Day(c.getTime());        
    }
    
    public Day next() {
        return addDays(1);        
    }

    public Day prev() {
        return addDays(-1);        
    }

    public boolean before(Day day) {
        return date.before(day.date);
    }

    public boolean after(Day day) {
        return date.after(day.date);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        return date.equals(((Day)object).date);
    }
    
    @Override
    public int hashCode() {
        return date.hashCode();
    }
    
}
