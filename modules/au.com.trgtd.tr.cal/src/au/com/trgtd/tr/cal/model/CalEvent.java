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

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.Date;
import tr.model.action.Action;

/**
 * Calendar event interface.
 * 
 * @author jeremy moore
 */
public interface CalEvent extends Comparable<CalEvent> {

    public String PROP_CAL_EVENT_TEXT  = "cal-event-text";
    public String PROP_CAL_EVENT_START = "cal-event-start";
    public String PROP_CAL_EVENT_END   = "cal-event-end";
    public String PROP_CAL_EVENT_BG    = "cal-event-bg";
    public String PROP_CAL_EVENT_FG    = "cal-event-fg";    
    
    public static enum Type {
        Scheduled,
        DoASAP,
        Inactive,
        Delegated
    }
    
    /**
     * Get the action.
     * @return The action.
     */
    public Action getAction();

    
    public Type getType();
    
    /**
     * Get the id for this calendar event.
     * @return The calendar event id.
     */
    public EventID getCalEventID();
    
    
    /**
     * Determine whether the event is done.
     * @return true if done, otherwise false.
     */
    public boolean isDone();    
    
    /**
     * Get the start date and time of this calendar event.
     * @return The start date and time.
     */
    public Date getCalEventStart();

    /**
     * Set the start date and time of this calendar event.
     * This method should fire a property change event when the value is changed.
     * @param date The start date and time.
     */
    public void setCalEventStart(Date date);
    
    /**
     * Get the end date and time of this calendar event.
     * @return The end date and time.
     */
    public Date getCalEventEnd();

    /**
     * Set the end date and time of this calendar event. 
     * This method should fire a property change event when the value is changed.
     * @param date The end date and time.
     */
    public void setCalEventEnd(Date date);

    /**
     * Get the text (summary) for this calendar event.
     * @return The event summary.
     */
    public String getCalEventText();

    /**
     * Set the text (summary) for this calendar event.
     * This method should fire a property change event when the value is changed.
     * @param text The event text.
     */
    public void setCalEventText(String text);
    
    /**
     * Gets the background color.
     * @return the color.
     */
    public Color getCalEventBg();
    
    /**
     * Gets the foreground color.
     * @return the color.
     */
    public Color getCalEventFg();
        
    /**
     * Sets the background color.
     * @param color the color.
     */
    public void setCalEventBg(Color color);
    
    /**
     * Sets the foreground color.
     * @param color the color.
     */
    public void setCalEventFg(Color color);
    
    /**
     * Add a PropertyChangeListener for all properties.
     * @param l The listener
     */
    public void addPropertyChangeListener(PropertyChangeListener l);
    
    /**
     * Add a PropertyChangeListener for a specific property.
     * @param prop The property name.
     * @param l The listener
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l);
    
    /**
     * Remove a PropertyChangeListener for all properties.
     * @param l The listener
     */
    public void removePropertyChangeListener(PropertyChangeListener l);
    
    /**
     * Remove a PropertyChangeListener for a specific property.
     * @param prop The property name.
     * @param l The listener
     */
    public void removePropertyChangeListener(String prop, PropertyChangeListener l);
    
    
    public void edit();
    
}
