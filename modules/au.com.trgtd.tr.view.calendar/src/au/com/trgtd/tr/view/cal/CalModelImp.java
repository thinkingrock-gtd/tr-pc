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
package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.model.CalEvent;
import au.com.trgtd.tr.cal.model.CalEvent.Type;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.model.EventID;
import au.com.trgtd.tr.cal.model.EventIDFactory;
import au.com.trgtd.tr.cal.model.EventUtils;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.view.cal.dialog.ActionEditDialog;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.*;
import tr.model.action.Action;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;

/**
 * Calendar model implementation.
 *
 * @author Jeremy Moore
 */
public class CalModelImp implements CalModel {

    private static final Comparator<CalEvent> COMPARATOR = (CalEvent e1, CalEvent e2) -> {
        Action a1 = e1.getAction();
        Action a2 = e2.getAction();
        int c = a1.getTopic().compareTo(a2.getTopic());
        if (c == 0) {
            c = a1.getDescription().compareToIgnoreCase(a2.getDescription());
        }
        return c;
    };
    
    private static final char CHAR_DO_ASAP = '\u2605';
//  private static final char CHAR_INACTIVE = '\u2606';
    private static final char CHAR_DELEGATE = '\u261E';
    private static final char CHAR_SCHEDULE = '\u2637';

    private boolean showDone = false;

    public CalModelImp() {
    }

    @Override
    public boolean isShowDone() {
        return showDone;
    }

    @Override
    public void setShowDone(boolean showDone) {
        this.showDone = showDone;
    }

    @Override
    public List<CalEvent> getEventsScheduled() {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (action.isStateScheduled()) {
                if (showDone || !action.isDone()) {
                    ScheduledEvent scheduledEvent = new ScheduledEvent(action);
                    if (scheduledEvent.isValid()) {
                        events.add(scheduledEvent);
                    }
                }
            }
        }

        return events;
    }

    private Date incrementDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private List<Date> getDates(Date beg, Date end) {
        if (beg == null || end == null) {
            return Collections.emptyList();
        }
        List<Date> list = new ArrayList<>();
        Date d1 = DateUtils.clearTime(beg);
        Date d2 = DateUtils.clearTime(end);
        while (!d1.after(d2)) {
            list.add(d1);
            d1 = incrementDay(d1);
        }
        return list;
    }

    @Override
    public Map<Date, List<CalEvent>> getEventsMap() {
        Map<Date, List<CalEvent>> eventsMap = new HashMap<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!showDone && action.isDone()) {
                continue;
            }

            if (action.isStateScheduled()) {
                ScheduledEvent scheduledEvent = new ScheduledEvent(action);
                if (scheduledEvent.isValid()) {
                    for (Date date : getDates(scheduledEvent.getCalEventStart(), scheduledEvent.getCalEventEnd())) {
                        List<CalEvent> list = eventsMap.get(date);
                        if (null == list) {
                            list = new ArrayList<>();
                            eventsMap.put(date, list);
                        }
                        list.add(scheduledEvent);
                    }

                }
            } else if (action.isStateInactive()) {

            } else if (action.isStateASAP()) {
                DoASAPEvent event = new DoASAPEvent(action);
                if (event.isValid()) {
                    Date date = event.getCalEventStart();
                    List<CalEvent> list = eventsMap.get(date);
                    if (null == list) {
                        list = new ArrayList<>();
                        eventsMap.put(date, list);
                    }
                    list.add(event);
                }
            } else if (action.isStateDelegated()) {
                DelegatedEvent event = new DelegatedEvent(action);
                if (event.isValid()) {
                    Date date = event.getCalEventStart();
                    List<CalEvent> list = eventsMap.get(date);
                    if (null == list) {
                        list = new ArrayList<>();
                        eventsMap.put(date, list);
                    }
                    list.add(event);
                }
            }
        }

        for (List<CalEvent> list : eventsMap.values()) {
            Collections.sort(list);
        }

        return eventsMap;
    }

    @Override
    public List<CalEvent> getEventsScheduledTime(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (action.isStateScheduled()) {
                if (showDone || !action.isDone()) {
                    ScheduledEvent scheduledEvent = new ScheduledEvent(action);
                    if (scheduledEvent.isValid()
                            && scheduledEvent.isOn(day.getDate())
                            && scheduledEvent.hasDuration()) {
                        events.add(scheduledEvent);
                    }
                }
            }
        }
        return events;
    }

    @Override
    public List<CalEvent> getEventsScheduledAllDay(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!action.isStateScheduled()) {
                continue;
            }
            if (action.isDone() && !showDone) {
                continue;
            }
            ScheduledEvent scheduledEvent = new ScheduledEvent(action);
            if (scheduledEvent.isValid()
                    && scheduledEvent.isOn(day.getDate())
                    && !scheduledEvent.hasDuration()) {
                events.add(scheduledEvent);
            }
        }
        events.sort(COMPARATOR);
        return events;
    }


    @Override
    public List<CalEvent> getEventsDelegatedFollowupOn(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!action.isStateDelegated()) {
                continue;
            }
            if (action.isDone() && !showDone) {
                continue;
            }
            DelegatedItem delegatedItem = new DelegatedItem(action);
            if (delegatedItem.isFollowupOn(day.getDate())) {
                events.add(new DelegatedEvent(action));
            }
        }
        return events;
    }

    @Override
    public List<CalEvent> getEventsDelegatedFollowupBefore(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!action.isStateDelegated()) {
                continue;
            }
            if (action.isDone()) {
                continue;
            }
            DelegatedItem delegatedItem = new DelegatedItem(action);
            if (delegatedItem.isFollowupBefore(day.getDate())) {
                events.add(new DelegatedEvent(action));
            }
        }
        return events;
    }

//    public List<CalEvent> getEventsDelegatedFollowup(Day day) {
//        List<CalEvent> events = new ArrayList<>();
//        events.addAll(this.getEventsDelegatedFollowupOn(day));
//        events.addAll(this.getEventsDelegatedFollowupBefore(day));
//        return events;
//    }

//    @Override
//    public List<CalEvent> getEventsDelegatedStartOn(Day day) {
//        List<CalEvent> events = new ArrayList<>();
//        for (Action action : Services.instance.getAllActions()) {
//            if (!action.isStateDelegated()) {
//                continue;
//            }
//            if (action.isDone() && !showDone) {
//                continue;
//            }
//            DelegatedItem delegatedItem = new DelegatedItem(action);
//            if (delegatedItem.isStartOn(day.getDate())) {
//                events.add(new DelegatedEvent(action));
//            }
//        }
//        return events;
//    }
//    @Override
//    public List<CalEvent> getEventsDelegatedStartBefore(Day day) {
//        List<CalEvent> events = new ArrayList<>();
//        for (Action action : Services.instance.getAllActions()) {
//            if (!action.isStateDelegated()) {
//                continue;
//            }
//            if (action.isDone() && !showDone) {
//                continue;
//            }
//            DelegatedItem delegatedItem = new DelegatedItem(action);
//            if (delegatedItem.isStartBefore(day.getDate())) {
//                events.add(new DelegatedEvent(action));
//            }
//        }
//        return events;
//    }
//    @Override
//    public List<CalEvent> getEventsDelegatedDueOn(Day day) {
//        List<CalEvent> events = new ArrayList<>();
//        for (Action action : Services.instance.getAllActions()) {
//            if (!action.isStateDelegated()) {
//                continue;
//            }
//            if (action.isDone() && !showDone) {
//                continue;
//            }
//            DelegatedItem delegatedItem = new DelegatedItem(action);
//            if (delegatedItem.isDueOn(day.getDate())) {
//                events.add(new DelegatedEvent(action));
//            }
//        }
//        return events;
//    }
//    @Override
//    public List<CalEvent> getEventsDelegatedOverdue(Day day) {
//        List<CalEvent> events = new ArrayList<>();
//        for (Action action : Services.instance.getAllActions()) {
//            if (!action.isStateDelegated()) {
//                continue;
//            }
//          if (action.isDone() && !showDone) {
//                continue;
//            }
//            DelegatedItem delegatedItem = new DelegatedItem(action);
//            if (delegatedItem.isDueBefore(day.getDate())) {
//                events.add(new DelegatedEvent(action));
//            }
//        }
//        return events;
//    }
    @Override
    public List<CalEvent> getEventsDoASAPDueOn(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!action.isStateASAP()) {
                continue;
            }
            if (action.isDone() && !showDone) {
                continue;
            }
            ASAPItem asapItem = new ASAPItem(action);
            if (asapItem.isDueOn(day.getDate())) {
                events.add(new DoASAPEvent(action));
            }
        }
//        events.sort(COMPARATOR);
        return events;
    }

    @Override
    public List<CalEvent> getEventsDoASAPOverdue(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!action.isStateASAP()) {
                continue;
            }
            if (action.isDone()) {
                continue;
            }
            ASAPItem asapItem = new ASAPItem(action);
            if (asapItem.isOverdueOn(day.getDate())) {
                events.add(new DoASAPEvent(action));
            }
        }
//        events.sort(COMPARATOR);        
        return events;
    }

    @Override
    public List<CalEvent> getEventsDoASAPStartOn(Day day) {
        List<CalEvent> events = new ArrayList<>();
        for (Action action : Services.instance.getAllActions()) {
            if (!action.isStateASAP()) {
                continue;
            }
            if (action.isDone() && !showDone) {
                continue;
            }
            ASAPItem asapItem = new ASAPItem(action);
            if (asapItem.isStartOn(day.getDate())) {
                events.add(new DoASAPEvent(action));
            }
        }
//        events.sort(COMPARATOR);
        return events;
    }

//    @Override
//    public List<CalEvent> getEventsDoASAPStartBefore(Day day) {
//        List<CalEvent> events = new ArrayList<>();
//        for (Action action : Services.instance.getAllActions()) {
//            if (!action.isStateASAP()) {
//                continue;
//            }
//            if (action.isDone() && !showDone) {
//                continue;
//            }
//            ASAPItem asapItem = new ASAPItem(action);
//            if (asapItem.isStartBefore(day.getDate())) {
//                events.add(new DoASAPEvent(action));
//            }
//        }
//        return events;
//    }

    @Override
    public void add(List<CalEvent> events) {
    }

    @Override
    public void add(CalEvent event) {
    }

    @Override
    public void remove(List<CalEvent> events) {
    }

    @Override
    public void remove(CalEvent event) {
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
    }

    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
    }

    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
    }

    @Override
    public List<CalEvent> getEventsDelegatedStartOn(Day day) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CalEvent> getEventsDelegatedStartBefore(Day day) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CalEvent> getEventsDelegatedDueOn(Day day) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CalEvent> getEventsDelegatedOverdue(Day day) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<CalEvent> getEventsDoASAPStartBefore(Day day) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* Inner class for Do ASAP action list item. */
    public static class ASAPItem {

        private final Action action;

        public ASAPItem(Action action) {
            this.action = action;
        }

        public Action getAction() {
            return action;
        }

        public boolean isDueOn(Date date) {
            Date dueDate = action.getDueDate();
            if (dueDate == null) {
                return false;
            }
            return DateUtils.isSameDay(dueDate, date);
        }

        public boolean isOverdueOn(Date date) {
            Date dueDate = action.getDueDate();
            if (dueDate == null) {
                return false;
            }
            return DateUtils.isBeforeDay(dueDate, date);
        }

        public boolean isStartOn(Date date) {
            Date startDate = action.getStartDate();
            if (startDate == null) {
                return false;
            }
            return DateUtils.isSameDay(startDate, date);
        }

        public boolean isStartBefore(Date date) {
            Date startDate = action.getStartDate();
            if (startDate == null) {
                return false;
            }
            return DateUtils.isBeforeDay(startDate, date);
        }

        @Override
        public String toString() {
            return action.getDescription();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this == object) {
                return true;
            }
            if (getClass() != object.getClass()) {
                return false;
            }
            return action.equals(((ASAPItem) object).action);
        }

        @Override
        public int hashCode() {
            return action.hashCode();
        }
    }

    /* Inner class for delegated action list item. */
    public static class DelegatedItem {

        private final Action action;
        private final ActionStateDelegated state;

        public DelegatedItem(Action action) {
            this.action = action;
            this.state = (ActionStateDelegated) action.getState();
        }

        public Action getAction() {
            return action;
        }

        public boolean hasFollowupDate() {
            return state.getDate() != null;
        }

        public boolean hasDueDate() {
            return action.getDueDate() != null;
        }

        public boolean hasStartDate() {
            return action.getStartDate() != null;
        }

        public boolean isFollowupOn(Date date) {
            return hasFollowupDate() && DateUtils.isSameDay(state.getDate(), date);
        }

        public boolean isFollowupBefore(Date date) {
            return hasFollowupDate() && DateUtils.isBeforeDay(state.getDate(), date);
        }

        public boolean isDueOn(Date date) {
            return hasDueDate() && DateUtils.isSameDay(action.getDueDate(), date);
        }

        public boolean isDueBefore(Date date) {
            return hasDueDate() && DateUtils.isBeforeDay(action.getDueDate(), date);
        }

        public boolean isStartOn(Date date) {
            return hasStartDate() && DateUtils.isSameDay(action.getStartDate(), date);
        }

        public boolean isStartBefore(Date date) {
            return hasStartDate() && DateUtils.isBeforeDay(action.getStartDate(), date);
        }

        @Override
        public String toString() {
            return action.getDescription();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (this == object) {
                return true;
            }
            if (getClass() != object.getClass()) {
                return false;
            }
            return action.equals(((DelegatedItem) object).action);
        }

        @Override
        public int hashCode() {
            return action.hashCode();
        }
    }

    /* Inner class for scheduled action implementation or CalEvent. */
    public class ScheduledEvent extends AbstractEvent implements CalEvent {

        private final ActionStateScheduled state;

        public ScheduledEvent(Action action) {
            super(action, Type.Scheduled);
            this.state = (ActionStateScheduled) action.getState();
        }

        public boolean isValid() {
            return state.getDate() != null;
        }

        public boolean isOn(Date date) {
            return EventUtils.isOn(this, date);
        }

        public boolean hasDuration() {
            return state.getDurationHours() > 0 || state.getDurationMinutes() > 0;
        }

        @Override
        public Date getCalEventStart() {
            return state.getDate();
        }

        @Override
        public Date getCalEventEnd() {
            Calendar cal = Calendar.getInstance();
            cal.setTime(state.getDate());
            cal.add(Calendar.HOUR_OF_DAY, state.getDurationHours());
            cal.add(Calendar.MINUTE, state.getDurationMinutes());
            return cal.getTime();
        }

        @Override
        public String getCalEventText() {
            return CHAR_SCHEDULE + super.getCalEventText();
        }
    }

    /* Inner class for delegated action list item. */
    public static class DelegatedEvent extends AbstractEvent implements CalEvent {

        private final ActionStateDelegated state;

        public DelegatedEvent(Action action) {
            super(action, Type.Delegated);
            this.state = (ActionStateDelegated) action.getState();
        }

        public boolean isValid() {
            return state.getDate() != null;
        }

        public boolean isOn(Date date) {
            return DateUtils.isSameDay(state.getDate(), date);
        }

        @Override
        public Date getCalEventStart() {
            return DateUtils.getStart(state.getDate());
        }

        @Override
        public Date getCalEventEnd() {
            return DateUtils.getEnd(state.getDate());
        }

        @Override
        public String getCalEventText() {
            return CHAR_DELEGATE + super.getCalEventText();
        }
    }

    /* Inner class for Do ASAP action list item. */
    public static class DoASAPEvent extends AbstractEvent implements CalEvent {

        private final ActionStateASAP state;

        public DoASAPEvent(Action action) {
            super(action, CalEvent.Type.DoASAP);
            this.state = (ActionStateASAP) action.getState();
        }

        public boolean isValid() {
            return action.getDueDate() != null;
        }

        public boolean isOn(Date date) {
            return DateUtils.isSameDay(action.getDueDate(), date);
        }

        @Override
        public Date getCalEventStart() {
            return DateUtils.getStart(action.getDueDate());
        }

        @Override
        public Date getCalEventEnd() {
            return DateUtils.getEnd(action.getDueDate());
        }

        @Override
        public String getCalEventText() {
            return CHAR_DO_ASAP + super.getCalEventText();
        }
    }

    public static abstract class AbstractEvent implements CalEvent {

        protected final Action action;
        protected final Type type;

        public AbstractEvent(Action action, Type type) {
            this.action = action;
            this.type = type;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public int compareTo(CalEvent that) {
            Integer thisType = this.getType().ordinal();
            Integer thatType = that.getType().ordinal();
            int order = thisType.compareTo(thatType);
            if (order != 0) {
                return order;
            }
            return this.getCalEventText().compareToIgnoreCase(that.getCalEventText());
        }

        @Override
        public Action getAction() {
            return action;
        }

        @Override
        public String toString() {
            return action.getDescription();
        }

        @Override
        public EventID getCalEventID() {
            return EventIDFactory.create(action.getID());
        }

        @Override
        public boolean isDone() {
            return action.isDone();
        }

        @Override
        public void setCalEventStart(Date date) {
        }

        @Override
        public void setCalEventEnd(Date date) {
        }

        @Override
        public String getCalEventText() {
            return action.getDescription();
        }

        @Override
        public void setCalEventText(String text) {
        }

        @Override
        public Color getCalEventBg() {
            return action.getTopic().getBackground();
        }

        @Override
        public Color getCalEventFg() {
            return action.getTopic().getForeground();
        }

        @Override
        public void setCalEventBg(Color color) {
        }

        @Override
        public void setCalEventFg(Color color) {
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener l) {
        }

        @Override
        public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener l) {
        }

        @Override
        public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        }

        @Override
        public void edit() {
            ActionEditDialog dialog = new ActionEditDialog(action);
            dialog.showModifyDialog();
        }
    }

}
