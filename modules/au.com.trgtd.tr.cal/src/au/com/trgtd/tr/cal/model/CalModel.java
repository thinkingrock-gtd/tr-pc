package au.com.trgtd.tr.cal.model;

import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Calendar model interface.
 * 
 * @author Jeremy Moore
 */
public interface CalModel {

    public String PROP_INSERT = "insert-cal-event";
    public String PROP_REMOVE = "remove-cal-event";

    /**
     * Gets all calendar events.
     * @return a list containing all events.
     */    
    public List<CalEvent> getCalEvents();

    /**
     * Gets all calendar events as a map from date to event list.
     * @return a map relating each date (that has events on it) to the list of
     * events on that date.
     */
    public Map<Date, List<CalEvent>> getCalEventsMap();

    /**
     * Gets the calendar events (excluding all-day events) for on a given day.
     * @return a list of calendar events which may be empty but never null.
     */    
    public List<CalEvent> getCalEvents(Day day);

    /**
     * Gets the "all-day" calendar events for a given day.
     * @return a list of calendar events which may be empty but never null.
     */    
    public List<CalEvent> getCalEventsAllDay(Day day);
    
    /**
     * Add a list of calendar events.
     * @param events The list of events to add.
     */    
    public void add(List<CalEvent> events);

    /**
     * Add a calendar events.
     * @param event The events to add.
     */
    public void add(CalEvent event);

    /**
     * Remove a list of calendar events.
     * @param events The list of events to remove.
     */
    public void remove(List<CalEvent> events);

    /**
     * Remove a calendar event.
     * @param event The event to remove.
     */
    public void remove(CalEvent event);

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
    
}
