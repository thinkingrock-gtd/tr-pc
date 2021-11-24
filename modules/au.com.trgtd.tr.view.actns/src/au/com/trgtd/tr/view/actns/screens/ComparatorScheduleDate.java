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

import java.util.Comparator;
import java.util.Date;
import org.jdesktop.swingx.calendar.DateUtils;
import au.com.trgtd.tr.swing.StyledDate;

/**
 * Comparator for schedule dates.
 *
 * @author Jeremy Moore
 */
public class ComparatorScheduleDate implements Comparator<StyledDate> {
    
    /**
     * Ordering is first by date <= today, then null, then date > today.
     */
    public int compare(StyledDate sd1, StyledDate sd2) {
        
        Date d1 = sd1.getDate();
        Date d2 = sd2.getDate();
        
        if (d1 == d2) {
            return 0;
        }
        
        if (d1 == null) {
            Date today = DateUtils.endOfDay(new Date());
            return (d2.after(today)) ? -1 : 1;
        }
        
        if (d2 == null) {
            Date today = DateUtils.endOfDay(new Date());
            return (d1.after(today)) ? 1 : -1;
        }
        
        return d1.compareTo(d2);
    }
    
}

