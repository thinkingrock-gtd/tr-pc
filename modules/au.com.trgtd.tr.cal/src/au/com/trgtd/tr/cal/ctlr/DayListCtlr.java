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
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.cal.view.AllDayPanel;
import au.com.trgtd.tr.cal.view.EventLabel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tr.model.topic.Topic;

/**
 * Controller for day list panel with calendar event model. Maintains a map of
 * eventID to eventLabel for each all day event on a given day. When the
 * calendar event model or date is changed, the map is updated and the
 * dayListPanel is notified.
 *
 * @author Jeremy Moore
 */
public final class DayListCtlr {

    private final Map<EventID, EventLabel> eventMap;
    private final CalModel calModel;
    private final DateCtlr dateCtlr;
    private final AllDayPanel dayListPanel;

    /**
     * Constructor.
     *
     * @param calModel The calendar model.
     * @param dateCtlr The date controller.
     */
    public DayListCtlr(CalModel calModel, DateCtlr dateCtlr) {
        if (null == calModel) {
            throw new IllegalArgumentException("Calendar model can not be null.");
        }
        if (null == dateCtlr) {
            throw new IllegalArgumentException("Date controller can not be null.");
        }
        this.calModel = calModel;
        this.dateCtlr = dateCtlr;
        this.dayListPanel = new AllDayPanel(dateCtlr);

        this.eventMap = new HashMap<>();
        this.initMap();

        this.calModel.addPropertyChangeListener(CalModel.PROP_INSERT, pclInsert);
        this.calModel.addPropertyChangeListener(CalModel.PROP_REMOVE, pclRemove);
        this.dateCtlr.addPropertyChangeListener(pclDate);
        this.dayListPanel.resetEvents(getEventLabels());
    }

    public AllDayPanel getPanel() {
        return dayListPanel;
    }

    /**
     * Get the event labels for events occurring on the day.
     *
     * @return a collection of event labels for events occurring on the day.
     */
    public Collection<EventLabel> getEventLabels() {

        List<EventLabel> list = new ArrayList<>(eventMap.values());
        list.sort((EventLabel l1, EventLabel l2) -> {
            Topic topic1 = l1.getEvent().getAction().getTopic();
            Topic topic2 = l2.getEvent().getAction().getTopic();
            int order = topic1.compareTo(topic2);
            if (order == 0) {
                order = l1.getEvent().compareTo(l2.getEvent());
            }
            return order;
        });
        return list;
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
            initMap();
            dayListPanel.resetEvents(getEventLabels());
        }
    };

    private void initMap() {
        eventMap.clear();
        for (CalEvent event : calModel.getEventsScheduledAllDay(dateCtlr.getDay())) {
            eventMap.put(event.getCalEventID(), new EventLabel(event));
        }
    }

}
