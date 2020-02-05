package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.model.EventID;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.view.DayListPanel;
import au.com.trgtd.tr.cal.view.EventLabel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for day plan list panel with calendar event model.
 *
 * @author Jeremy Moore
 */
public final class DayPlanListCtlr {

    private final Map<EventID, EventLabel> eventMap;
    private final CalModel calModel;
    private final DateCtlr dateCtlr;
    private final DayListPanel dayListPanel;
    private final boolean isFirstDayOfWeek;

    /**
     * Constructor.
     *
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     * @param isFirstDayOfWeek Is this the first day of the week?
     */
    public DayPlanListCtlr(CalModel calModel, DateCtlr dateCtlr, boolean isFirstDayOfWeek) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }

        this.isFirstDayOfWeek = isFirstDayOfWeek;
        
        this.calModel = calModel;
        this.calModel.addPropertyChangeListener(CalModel.PROP_INSERT, pclInsert);
        this.calModel.addPropertyChangeListener(CalModel.PROP_REMOVE, pclRemove);
        
        this.dateCtlr = dateCtlr;
        this.dateCtlr.addPropertyChangeListener(pclDate);
                        
        this.eventMap = new HashMap<>();
        this.initEventMap();
        
        this.dayListPanel = new DayListPanel(dateCtlr);
        this.dayListPanel.resetEvents(getEventLabels());

    }

    public DayListPanel getPanel() {
        
        return dayListPanel;
    }

    /**
     * Get the event panels for events occurring on the day.
     *
     * @return a collection of event panels for events occurring on the day.
     */
    public Collection<EventLabel> getEventLabels() {
        return Collections.unmodifiableCollection(eventMap.values());
    }

    private final PropertyChangeListener pclInsert = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            CalEvent event = (CalEvent) pce.getNewValue();
            if (DateUtils.isSameDay(dateCtlr.getDate(), event.getCalEventStart())) {
                if (!eventMap.containsKey(event.getCalEventID())) {
                    EventLabel eventLabel = new EventLabel(event);
                    eventMap.put(event.getCalEventID(), eventLabel);
                    dayListPanel.addEvent(eventLabel);
                }
            }
        }
    };

    private final PropertyChangeListener pclRemove = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            // remove event from day map 
            CalEvent event = (CalEvent) pce.getOldValue();
            EventLabel eventLabel = eventMap.remove(event.getCalEventID());
            if (null != eventLabel) {
                dayListPanel.removeEvent(eventLabel);
            }
        }
    };

    private final PropertyChangeListener pclDate = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            initEventMap();
            dayListPanel.resetEvents(getEventLabels());
        }
    };

    private void initEventMap() {
        eventMap.clear();

        Day day = dateCtlr.getDay();
        
        List<List<CalEvent>> lists = new ArrayList<>();     
        
        if (isFirstDayOfWeek) {
            lists.add(calModel.getEventsDoASAPOverdue(day));
            lists.add(calModel.getEventsDoASAPStartBefore(day));
            lists.add(calModel.getEventsDelegatedOverdue(day));
            lists.add(calModel.getEventsDelegatedFollowupBefore(day));
            lists.add(calModel.getEventsDelegatedStartBefore(day));            
        }
        lists.add(calModel.getEventsDoASAPDueOn(day));
        lists.add(calModel.getEventsDoASAPStartOn(day));
        lists.add(calModel.getEventsDelegatedDueOn(day));
        lists.add(calModel.getEventsDelegatedFollowupOn(day));
        lists.add(calModel.getEventsDelegatedStartOn(day));

        for (List<CalEvent> list : lists) {
            for (CalEvent event : list) {
                eventMap.put(event.getCalEventID(), new EventLabel(event));
            }
        }
    }
}
