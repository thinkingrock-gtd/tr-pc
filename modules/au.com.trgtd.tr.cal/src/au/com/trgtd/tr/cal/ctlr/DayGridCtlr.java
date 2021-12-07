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
package au.com.trgtd.tr.cal.ctlr;

import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.model.EventID;
import au.com.trgtd.tr.cal.model.EventUtils;
import au.com.trgtd.tr.cal.view.DayGridPanel;
import au.com.trgtd.tr.cal.view.EventPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Controller for day grid panel with calendar event model.
 * Maintains a map of eventID to eventPanel for each event occurring on a given 
 * day. When the calendar event model or date is changed, the map is updated and
 * the dayGridPanel is notified.
 * 
 * @author Jeremy Moore
 */
public final class DayGridCtlr {

    private final Map<EventID, EventPanel> eventMap;    
    private final CalModel calModel;
    private final DateCtlr dateCtlr;
    private final DayGridPanel dayGridPanel;
    private final int firstHr;
    private final int lastHr;

    /**
     * Constructor.
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     * @param firstHr The first hour of the day.
     * @param lastHr The last hour of the day.
     */
    public DayGridCtlr(CalModel calModel, DateCtlr dateCtlr, int firstHr, int lastHr) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }        
        this.calModel = calModel;        
        this.dateCtlr = dateCtlr;
        this.firstHr = firstHr;
        this.lastHr = lastHr;
        this.dayGridPanel = new DayGridPanel(firstHr, lastHr, dateCtlr, this);
        this.eventMap = new HashMap<>();        
        this.initEventMap();
        this.calModel.addPropertyChangeListener(CalModel.PROP_INSERT, pclInsert);
        this.calModel.addPropertyChangeListener(CalModel.PROP_REMOVE, pclRemove);        
        this.dateCtlr.addPropertyChangeListener(pclDate);        
        this.dayGridPanel.resetEvents(getEventPanels());
    }
  
    public DayGridPanel getPanel() {
        return dayGridPanel;
    }
    
    /**
     * Gets the end of day date based on the last hour.
     * @return a date set to the last millisecond of the last hour of the day. 
     */
    public Date getEndOfDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(dateCtlr.getDate());
        c.set(Calendar.HOUR_OF_DAY, lastHr);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }
    
    /**
     * Gets the start of day date based on the first hour.
     * @return a date set to the first hour of the day. 
     */
    public Date getStartOfDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(dateCtlr.getDate());
        c.set(Calendar.HOUR_OF_DAY, firstHr);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    /**
     * Adds a new event to the calendar model.
     * @param event The new event.
     */
    public void add(CalEvent event) {
        if (null != event) {
            calModel.add(event);
        }
    }

    /**
     * Removes an existing event from the calendar model.
     * @param event The event to remove.
     */
    public void remove(CalEvent event) {
        if (null != event) {
            calModel.remove(event);   
        }
    }
    
    /**
     * Get the event panels for events occurring on the day.
     * @return a collection of event panels for events occurring on the day.
     */
    public Collection<EventPanel> getEventPanels() {
        return Collections.unmodifiableCollection(eventMap.values());
    }
            
    private final PropertyChangeListener pclInsert = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            CalEvent event = (CalEvent)pce.getNewValue();
            if (EventUtils.isOn(event, dateCtlr.getDate())) {
                if (!eventMap.containsKey(event.getCalEventID())) {
                    EventPanel eventPanel = new EventPanel(dateCtlr.getDate(), event);
                    eventMap.put(event.getCalEventID(), eventPanel);                                
                    dayGridPanel.addEvent(eventPanel);
                }
            } 
        }        
    };

    private final PropertyChangeListener pclRemove = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            // remove event from day map 
            CalEvent event = (CalEvent)pce.getOldValue();
            EventPanel eventPanel = eventMap.remove(event.getCalEventID());                                
            if (null != eventPanel) {
                dayGridPanel.removeEvent(eventPanel);
            }
        }        
    };

    private final PropertyChangeListener pclDate = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            initEventMap();
            dayGridPanel.resetEvents(getEventPanels());
        }        
    };
    
    private void initEventMap() {
        eventMap.clear();
        for (CalEvent event : calModel.getEventsScheduledTime(dateCtlr.getDay())) {
            eventMap.put(event.getCalEventID(), new EventPanel(dateCtlr.getDate(), event));            
        }
    }
        
}
