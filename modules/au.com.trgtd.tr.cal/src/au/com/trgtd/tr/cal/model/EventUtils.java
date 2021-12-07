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
import java.util.Date;

/**
 * Calendar event utilities.
 * 
 * @author Jeremy Moore
 */
public class EventUtils {
   
    /**
     * Get the end date and time of a calendar event on a given day.
     * First checks that the event occurs at least partially on the given day.
     * If the event ends after the day ends then the result is the end of the 
     * day, otherwise it is the end of the event.
     * @param event The event.
     * @param day The day date.
     * @return The end date and time on the given day or null if it does not 
     * occur on the day.
     */
    public static Date getEnd(CalEvent event, Date day) {
        Date dayStart = DateUtils.getStart(day);
        Date dayEnd = DateUtils.getEnd(day);
        Date evtStart = event.getCalEventStart();
        Date evtEnd = event.getCalEventEnd();
        // make sure event occurs on the day
        if (evtEnd.before(dayStart) || evtStart.after(dayEnd)) {
            return null;
        }
        // return event end for the day.
        return evtEnd.before(dayEnd) ? evtEnd : dayEnd;
    }
    
    /**
     * Get the start date and time of a calendar event on a given day.
     * First checks that the event occurs at least partially on the given day.
     * If the event starts before the day starts then the result is the start
     * of the day, otherwise it is the start of the event.
     * @param event The event.
     * @param day The day date.
     * @return The start date and time on the given day or null if it does not 
     * occur on the day.
     * 
     */
    public static Date getStart(CalEvent event, Date day) {
        Date dayStart = DateUtils.getStart(day);
        Date dayEnd = DateUtils.getEnd(day);
        Date evtStart = event.getCalEventStart();
        Date evtEnd = event.getCalEventEnd();
        // make sure event occurs on the day
        if (evtEnd.before(dayStart) || evtStart.after(dayEnd)) {
            return null;
        }
        // return event start for the day.
        return evtStart.after(dayStart) ? evtStart : dayStart;
    }
    
    /**
     * Determines whether or not a calendar event is on a given day.
     * @param event The event.
     * @param date the date of day.
     * @return true if (and only if) the event occurs on the day.
     */
    public static boolean isOn(CalEvent event, Date date) {
        Date dayStart = DateUtils.getStart(date);
        Date dayEnd = DateUtils.getEnd(date);
        Date evtStart = event.getCalEventStart();
        Date evtEnd = event.getCalEventEnd();
        return !evtEnd.before(dayStart) && !evtStart.after(dayEnd);
    }

    /**
     * Gets the duration in minutes of an event on a day.
     * @param event The event.
     * @param date the date of day.
     * @return The event duration in minutes for the day.
     */
    public static int getMins(CalEvent event, Date date) {
        Date evtStart = getStart(event, date);
        Date evtEnd = getEnd(event, date);
        if (evtStart == null || evtEnd == null) {
            return 0;
        }
        double msStart = evtStart.getTime();
        double msEnd = evtEnd.getTime();
        return (int)((msEnd - msStart) / DateUtils.MS_PER_MIN);        
    }
    
}
