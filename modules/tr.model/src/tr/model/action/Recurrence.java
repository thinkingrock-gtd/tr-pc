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
package tr.model.action;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.topic.Topic;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Utils;
import java.beans.PropertyChangeSupport;
import tr.model.Item.Notable;

/**
 * Recurrence object for scheduled actions.
 *
 * @author Jeremy Moore
 */
public final class Recurrence extends ObservableImpl implements Notable {

    private static final Logger LOG = Logger.getLogger("tr.recurrence");
    
    // Properties Start
    
    private final int id;       // unique identifier

    private Project project;    // project of reccurrence

    private String description; // recurrent actions description value

    private Topic topic;        // recurrent actions topic value

    private Context context;    // recurrent actions context value

    private Value time;         // recurrent actions time value

    private Value energy;       // recurrent actions energy value

    private Value priority;     // recurrent actions priority value

    private byte scheduleHours; // recurrent actions schedule hour value

    private byte scheduleMins;  // recurrent actions schedule minute value

    private byte durationHours; // recurrent actions duration hours value

    private byte durationMins;  // recurrent actions duration minutes value

    private byte basisID;       // 0=default, 1=start-date, 2=done-date

    private Date startDate;     // start date (entered by user)

    /* @deprecated Use period */
    private byte periodID;      // 0=default, 1=weekday, 2=day, 3=week, 4=month, 5=year

    private int frequency;      // frequency of period

    private int advanceNbr;     // number of occurences to generate in advance.

    private Integer endNbr;     // number of occurences in series in total.

    private Date endDate;       // end date

    private int genNbr;         // generated number of occurrences.

    private Date genToDate;     // generated to date

    /** @since 2.0 */
    private String success;     // recurrent actions successful outcome value

    /** @since 2.0 */
    private String notes;       // recurrent actions notes value

    /** @since 2.0.1 */
    private Period period;      // reccurrence period

    /** @since 2.0.1.1 */
    private Boolean calendarItem; // include in calendar export

    // Properties End   

    /** Recurrence properties. */
    public enum Property {
        ID,
        PROJECT,
        DESCRIPTION,
        TOPIC,
        CONTEXT,
        TIME,
        ENERGY,
        PRIORITY,
        SCHEDULED_HOURS,
        SCHEDULED_MINS,
        DURATION_HOURS,
        DURATION_MINS,
        BASIS_ID,
        START_DATE,
        FREQUENCY,
        ADVANCE,
        END_NBR,
        END_DATE,
        GEN_NBR,
        GEN_TO_DATE,
        SUCCESS,
        NOTES,
        PERIOD;
    }


//  public static final String PROP_NOTES = Notable.PROP_NOTES;

    private transient PropertyChangeSupport propertyChangeSupport;

    private PropertyChangeSupport getPropertyChangeSupport() {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        return propertyChangeSupport;
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(property, listener);
    }

    /**
     * Constructs a new instance.
     */
    public Recurrence(int id) {
        this.id = id;
    }

    /**
     * Constructs a new instance for the given action.
     */
    public Recurrence(int id, Action action) {
        this(id);
        setProject((Project) action.getParent());
        setDescription(action.getDescription());
        setSuccess(action.getSuccess());
        setContext(action.getContext());
        setTopic(action.getTopic());
        setTime(action.getTime());
        setEnergy(action.getEnergy());
        setPriority(action.getPriority());
        setNotes(action.getNotes());
        if (action.isStateScheduled()) {
            ActionStateScheduled state = (ActionStateScheduled) action.getState();
            Date schdDate = state.getDate();
            if (schdDate != null) {
                setStartDate(new Date(schdDate.getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(schdDate);
                setScheduleHours(calendar.get(Calendar.HOUR_OF_DAY));
                setScheduleMins(calendar.get(Calendar.MINUTE));
            }
            setDurationHours(state.getDurationHours());
            setDurationMins(state.getDurationMinutes());
            setFrequency(1);
        }
    }

    /**
     * Gets the ID number.
     * @return the ID number.
     */
    public int getID() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        if (this.frequency != frequency) {
            this.frequency = frequency;
            notifyObservers(this);
        }
    }

    public int getGenNbr() {
        return genNbr;
    }

    public void setGenNbr(int genNbr) {
        this.genNbr = genNbr;
    }

    public Period getPeriod() {
        if (period == null) {
            switch (periodID) {
                case PeriodWeekday.ID: {
                    period = new PeriodWeekday();
                    break;
                }
                case PeriodDay.ID: {
                    period = new PeriodDay();
                    break;
                }
                case PeriodWeek.ID: {
                    period = new PeriodWeek();
                    break;
                }
                case PeriodMonth.ID: {
                    period = new PeriodMonth();
                    break;
                }
                case PeriodYear.ID: {
                    period = new PeriodYear();
                    break;
                }
                default: {
                    period = new PeriodWeek();
                }
            }
        }
        return period;
    }

    public void setPeriod(Period period) {
        if (!Utils.equal(this.period, period)) {
            this.period = period;
            notifyObservers(this);
        }
    }

    public Basis getBasis() {
        return Basis.fromID(basisID);
    }

    public void setBasis(Basis basis) {
        if (basis == null) {
            basis = Basis.fromID((byte) 0);
        }
        if (this.basisID != basis.ID) {
            this.basisID = basis.ID;
            notifyObservers(this);
        }
    }

    public int getAdvanceNbr() {
        return advanceNbr;
    }

    public void setAdvanceNbr(int advanceNbr) {
        if (!Utils.equal(this.advanceNbr, advanceNbr)) {
            this.advanceNbr = advanceNbr;
            notifyObservers(this);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (!Utils.equal(this.startDate, startDate)) {
            this.startDate = startDate;
            notifyObservers(this);
        }
    }

    /**
     * Sets the schedule hours.
     * @param hours The hours value.
     */
    public void setScheduleHours(int hours) {
        if (scheduleHours != (byte) hours) {
            scheduleHours = (byte) hours;
            notifyObservers(this);
        }
    }

    /**
     * Gets the schedule hours.
     * @return The hours value.
     */
    public int getScheduleHours() {
        return scheduleHours;
    }

    /**
     * Sets the schedule minutes.
     * @param minutes The minutes value.
     */
    public void setScheduleMins(int minutes) {
        if (scheduleMins != (byte) minutes) {
            scheduleMins = (byte) minutes;
            notifyObservers(this);
        }
    }

    /**
     * Gets the schedule minutes.
     * @return The minutes value.
     */
    public int getScheduleMins() {
        return scheduleMins;
    }

    /**
     * Sets the duration hours.
     * @param hours The hours value.
     */
    public void setDurationHours(int hours) {
        if (durationHours != (byte) hours) {
            durationHours = (byte) hours;
            notifyObservers(this);
        }
    }

    /**
     * Gets the duration hours.
     * @return The hours value.
     */
    public int getDurationHours() {
        return durationHours;
    }

    /**
     * Sets the duration minutes.
     * @param minutes The minutes value.
     */
    public void setDurationMins(int minutes) {
        if (durationMins != (byte) minutes) {
            durationMins = (byte) minutes;
            notifyObservers(this);
        }
    }

    /**
     * Gets the duration minutes.
     * @return The minutes value.
     */
    public int getDurationMins() {
        return durationMins;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (!Utils.equal(this.endDate, endDate)) {
            this.endDate = endDate;
            if (this.endDate != null) {
                this.endNbr = null;
            }
            notifyObservers(this);
        }
    }

    public Integer getEndNbr() {
        if (endNbr == null) {
            return null;
        }
        return endNbr < 2 ? 2 : endNbr;
    }

    public void setEndNbr(Integer endNbr) {
        if (!Utils.equal(this.endNbr, endNbr)) {
            this.endNbr = endNbr;
            if (this.endNbr != null) {
                this.endDate = null;
            }
            notifyObservers(this);
        }
    }

    /** Gets the generated to date. */
    public Date getGenToDate() {
        return genToDate;
    }

    /**
     * Generate the next recurrent action for a scheduled action which is done.
     * @param scheduledAction The done scheduled action.
     */
    public void generateSubsequent(Action scheduledAction) {
        LOG.fine("Begin generateForDoneDateBasis(...)");

        if (getBasis() != Basis.DONE_DATE) {
            LOG.warning("Called for recurrence with basis not done-date.");
            return;
        }
        if (!scheduledAction.isDone()) {
            LOG.warning("Called for action that is not done.");
            return;
        }
        if (!scheduledAction.isStateScheduled()) {
            LOG.warning("Called for action with state not scheduled.");
            return;
        }
        // use (genNbr+1) since first instance is not generated
        if (getEndNbr() != null && getEndNbr() - (genNbr + 1) < 1) {
            LOG.warning("End number already reached.");
            return;
        }
        
        endDate = DateUtils.getEnd(endDate); // make sure end of day of end date
        
        if (endDate != null && Calendar.getInstance().after(endDate)) {
            LOG.warning("End date already reached.");
            return;
        }
        if (getData() == null) {
            return;
        }

        ActionStateScheduled saState = (ActionStateScheduled) scheduledAction.getState();
        if (saState.getDate() == null) {
            LOG.severe("State is null.");
            return;
        }

        Calendar calSchdDate = Calendar.getInstance();
        calSchdDate.setTime(saState.getDate());

        Calendar calStartDate = Calendar.getInstance();
        calStartDate.setTime(scheduledAction.getDoneDate());

        calStartDate.set(Calendar.HOUR_OF_DAY, getScheduleHours());
        calStartDate.set(Calendar.MINUTE, getScheduleMins());
        calStartDate.set(Calendar.SECOND, 0);
        calStartDate.set(Calendar.MILLISECOND, 0);

        Date nextDate = calculateNextDate(calStartDate, getPeriod(), getFrequency());
        if (nextDate == null) {
            return;
        }

        if (endDate != null && nextDate.after(endDate)) {
            return;
        }

        createRecurrenceAction(nextDate);

        // update original action
        saState.setRecurrence(null);

        // update this recurrence
        genNbr = genNbr + 1;

        setGenToDate(nextDate);

        notifyObservers(this);

        LOG.fine("End generateForDoneDateBasis(...)");
    }

    private Date calculateNextDate(Calendar start, Period period, int frequency) {
        switch (period.getID()) {
            case PeriodWeekday.ID:
                return calculateNextWeekDay(start, frequency);
            case PeriodDay.ID:
                start.add(Calendar.DAY_OF_YEAR, frequency);
                return start.getTime();
            case PeriodWeek.ID:
                start.add(Calendar.WEEK_OF_YEAR, frequency);
                return start.getTime();
            case PeriodMonth.ID:
                start.add(Calendar.MONTH, frequency);
                return start.getTime();
            case PeriodYear.ID:
                start.add(Calendar.YEAR, frequency);
                return start.getTime();
            default:
                return null;
        }
    }

    private Date calculateNextWeekDay(Calendar start, int weekdays) {
        int days = 0;
        switch (start.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                days = weekdays + ( ((weekdays + 0) / 5) * 2);
                break;
            case Calendar.TUESDAY:
                days = weekdays + ( ((weekdays + 1) / 5) * 2);
                break;
            case Calendar.WEDNESDAY:
                days = weekdays + ( ((weekdays + 2) / 5) * 2);
                break;
            case Calendar.THURSDAY:
                days = weekdays + ( ((weekdays + 3) / 5) * 2);
                break;
            case Calendar.FRIDAY:
                days = weekdays + ( ((weekdays + 4) / 5) * 2);
                break;
            case Calendar.SATURDAY:
                days = weekdays + ( ((weekdays - 1) / 5) * 2) + 1;
                break;
            case Calendar.SUNDAY:
                days = weekdays + ( ((weekdays - 1) / 5) * 2);
                break;
        }
        start.add(Calendar.DAY_OF_YEAR, days);
        return start.getTime();
    }

    private Data getData() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data instance could not be obtained.");
        }
        return data;
    }

    private Date getStartDateTime() {
        assert (startDate != null);

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR_OF_DAY, scheduleHours);
        c.set(Calendar.MINUTE, scheduleMins);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    private Intervals getIntervals() {
        return new Intervals(getPeriod(), frequency, startDate);
    }

    /**
     * Calculates the termination date.  This is either:
     * - the termination endDate if there is one, else
     * - the end of the termination interval (from termination number of intervals)
     * - null if there is no termination date or termination number of intervals.
     */
    private Date calculateEndDate() {
        // termination end date
        if (endDate != null) {
            return DateUtils.getEnd(endDate);
        }
        // termination number of periods        
        if (getEndNbr() != null) {
            return getTerminationInterval(getEndNbr()).end;
        }
        // termination never
        return DateUtils.MAX_DATE;
    }

    private Date getDateOfLatestAction(Date defaultDate) {
        Date date = defaultDate;
        Project p = getProject();
        for (Action a : p.getChildren(Action.class)) {
            Recurrence r = a.getRecurrence();
            if (r != null && r.id == this.id) {
                ActionStateScheduled s = (ActionStateScheduled) a.getState();
                Date schduledDate = s.getDate();
                if (schduledDate != null) {
                    if (date == null || date.before(schduledDate)) {
                        date = schduledDate;
                    }
                }
            }
        }
        return date;
    }

    /**
     * Generate occurrences from generated to date.
     * Assumes that genToDate is not null and is the last generated-to-date for
     * the current start-date, period and frequence.
     */
    public void generateFromGenToDate() {
        LOG.fine("Begin generateFromGenToDate()");

        assert (getBasis() == Basis.START_DATE);

        if (genToDate == null) {
            LOG.warning("Generated-to-date is null, using date of last action.");
            genToDate = getDateOfLatestAction(startDate);
            if (genToDate == null) {
                LOG.warning("Date of last action could not be determined.");
                return;
            }
        }

        Date start = DateUtils.getEnd(genToDate);

        Date end = calculateEndDate();

        Intervals intervals = getIntervals();

        Interval startInterval = intervals.getInterval(start);

        Interval endInterval = getLastAdvanceInterval(intervals);

        generate(intervals, startInterval, endInterval, start, end, false);

        LOG.fine("End generateFromGenToDate()");
    }

    private void generate(Intervals intervals,
            Interval startInterval,
            Interval endInterval,
            Date start,
            Date end,
            boolean knownStartDateAction) {

        Interval interval = startInterval;

        while (!interval.after(endInterval)) {

            LOG.log(Level.FINE, "Generate for interval from: {0} to: {1}", new Object[]{interval.start, interval.end});

            for (Date date : getIntervalDates(interval, intervals)) {

                if (date.after(end)) {
                    // TODO: flag recurrency all finished - don't try again.
                    return;
                }

                if (date.equals(start)) {

                    if (!knownStartDateAction && !existsStartDateAction(start)) {

                        createRecurrenceAction(date);

                        setGenToDate(date);
                    }

                } else if (date.after(start)) {

                    createRecurrenceAction(date);

                    setGenToDate(date);
                }
            }

            interval = intervals.getNextInterval(interval);
        }
    }

    private Interval getLastAdvanceInterval(Intervals intervals) {

        Date today = Calendar.getInstance().getTime();

        Interval interval = intervals.getInterval(today);

        for (int i = 0; i < advanceNbr; i++) {
            interval = intervals.getNextInterval(interval);
        }

        return interval;
    }

    private Interval getTerminationInterval(int endNbr) {
        Intervals intervals = getIntervals();
        Interval interval = intervals.getFirstInterval();
        for (int i = 0; i < (endNbr - 1); i++) {
            interval = intervals.getNextInterval(interval);
        }
        return interval;
    }

    public static Date getTerminationEndDate(Intervals intervals, int endNbr) {
        Interval interval = intervals.getFirstInterval();
        for (int i = 0; i < (endNbr - 1); i++) {
            interval = intervals.getNextInterval(interval);
        }
        return interval.end;
    }

    private List<Date> getIntervalDates(Interval interval, Intervals intervals) {
        return intervals.getSelectedDates(interval, getStartDateTime());
    }

    /* Generate a recurrence action for the given date.*/
    private void createRecurrenceAction(Date date) {
        LOG.log(Level.FINE, "Creating action for: {0}", date);

        Data data = getData();
        if (data == null) {
            return;
        }

        Action newAction = new Action(data);
        newAction.setContext(this.getContext());
        newAction.setDescription(this.getDescription());
        newAction.setSuccess(this.getSuccess());
        newAction.setDone(false);
        newAction.setEnergy(this.getEnergy());
        newAction.setNotes("");
        newAction.setPriority(this.getPriority());
        newAction.setTime(this.getTime());
        newAction.setTopic(this.getTopic());
        newAction.setNotes(this.getNotes());

        ActionStateScheduled state = new ActionStateScheduled();
        state.setDate(date);


        // BEG Mantis 0001907 fix (Recurrency process using default values)
        state.setSchdHour(this.getScheduleHours());
        state.setSchdMinute(this.getScheduleMins());
        // END Mantis 0001907 fix.


        state.setDurationHours(this.getDurationHours());
        state.setDurationMins(this.getDurationMins());
        state.setRecurrence(this);
        newAction.setState(state);

        getProject().add(newAction);
    }

//    /* Determines whether or not a recurrence action exists for a given date. */
//    private boolean knownStartDateAction(Date date) {
//        if (date == null) {
//            return false;
//        }
//        Data data = getData();
//        if (data == null) {
//            return false;
//        }
//
//        Date compareDate = DateUtils.clearTime(date);
//
//        for (Action action : getProject().getChildren(Action.class)) {
//
//            if (action.isStateScheduled()) {
//
//                ActionStateScheduled state = (ActionStateScheduled) action.getState();
//
//                Recurrence recurrence = state.getRecurrence();
//
//                if (recurrence != null && recurrence.id == this.id) {
//
//                    if (DateUtils.clearTime(state.getDate()).equals(compareDate)) {
//                        return true;
//                    }
//
//                }
//            }
//        }
//
//        return false;
//    }
    
    /* Determines whether or not a recurrence action exists for the start date. */
    private boolean existsStartDateAction(Date startDate) {
        if (startDate == null) {
            return false;
        }

        Date compareDate = DateUtils.clearTime(startDate);

        for (Action action : getProject().getChildren(Action.class)) {

            if (action.isStateScheduled()) {

                ActionStateScheduled state = (ActionStateScheduled) action.getState();

                Recurrence recurrence = state.getRecurrence();

                if (recurrence != null && recurrence.id == this.id) {

                    if (DateUtils.clearTime(state.getDate()).equals(compareDate)) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

    
    /**
     * Removes all occurrences scheduled after the start date.
     */
    public void removeOccurrencesAfterStartDate() {
        LOG.fine("Start.");

        if (startDate == null) {
            LOG.warning("Called for recurrence with null start-date.");
            return;
        }

        removeOccurrencesAfter(startDate);
    }

    /** Removes all occurrences scheduled after the given date. */
    private void removeOccurrencesAfter(Date date) {
        LOG.log(Level.FINE, "Removing recurrent actions after: {0}", date);

        if (!date.before(DateUtils.MAX_DATE)) {
            return;
        }

        Data data = getData();
        if (data == null) {
            return;
        }

        if (date == null) {
            LOG.warning("Called with null date.");
            return;
        }

        Date endOfDate = DateUtils.getEnd(date);

        for (Action action : getProject().getChildren(Action.class)) {

            ActionState state = action.getState();
            if (state instanceof ActionStateScheduled schdState) {

                Recurrence recurrence = schdState.getRecurrence();
                if (recurrence != null && recurrence.id == this.id) {

                    Date schdDate = schdState.getDate();
                    if (schdDate != null && schdDate.after(endOfDate)) {
                        LOG.log(Level.FINE, "Removing action; Scheduled: {0} Description: {1}", new Object[]{schdDate, action.getDescription()});

                        action.removeFromParent();
                    }
                }
            }
        }
    }

    public Date getNextGenToDate() {
        Date start = getStartDateTime();
        Date end = calculateEndDate();
        Intervals intervals = new Intervals(getPeriod(), frequency, start);
        Interval endInterval = getLastAdvanceInterval(intervals);
        return end.before(endInterval.end) ? end : endInterval.end;
    }

    
    
    /** 
     * Generates recurrent actions from the start date.  
     * The recurrence action is used to avoid creating a duplicate start date 
     * action when creating recurrence. This is needed because this action may
     * not yet be part of the series (i.e. it may not have the recurrence object
     * attached yet).
     * @param recurrenceAction The recurrence action.
     */
    public void generateFromStartDate(Action recurrenceAction) {
        assert (getBasis() == Basis.START_DATE);
        assert (startDate != null);                
        
        boolean knownStartDateAction = false;

        // see if the action is scheduled on the recurrence start date
        if (recurrenceAction.isStateScheduled()) {
            ActionStateScheduled state = (ActionStateScheduled)recurrenceAction.getState();             
            Date scheduledDay = DateUtils.clearTime(state.getDate());
            Date startDateDay = DateUtils.clearTime(startDate);            
            if (scheduledDay.equals(startDateDay)) {
                knownStartDateAction = true;                
            }
        }        
        
        generateFromStartDate(knownStartDateAction);
    }
    
    /** 
     * Generates recurrent actions from the start date.
     * Assumes that we are not creating recurrence, and therefore there does not
     * exist a start action that is not yet part of the series yet.
     */
    public void generateFromStartDate() {
        assert (getBasis() == Basis.START_DATE);
        assert (startDate != null);                
        
        generateFromStartDate(false);                
    }    
    
    /** 
     * Generates recurrent actions from the start date.
     * @param knownStartDateAction This should be true if there is an action on 
     * the recurrence start date that is not yet part of the series.
     */
    private void generateFromStartDate(boolean knownStartDateAction) {
        Date start = getStartDateTime();
        Date end = calculateEndDate();
        Intervals intervals = new Intervals(getPeriod(), frequency, start);
        Interval startInterval = intervals.getFirstInterval();
        Interval endInterval = getLastAdvanceInterval(intervals);
        generate(intervals, startInterval, endInterval, start, end, knownStartDateAction);
    }

    public void updateTermination() {
        LOG.fine("Recurrence.updateTermination()");

        assert (getBasis() == Basis.START_DATE);

        if (genToDate == null) {
            LOG.warning("Called for recurrence with null generated-to-date.");
            return;
        }
        if (startDate == null) {
            LOG.warning("Called for recurrence with null start-date.");
            return;
        }
        if (getPeriod() == null) {
            LOG.warning("Called for recurrence with null period.");
            return;
        }
        if (frequency < 1) {
            LOG.warning("Called for recurrence with frequency less than 1.");
            return;
        }
        if (advanceNbr < 1) {
            LOG.warning("Called for recurrence with advance less than 1.");
            return;
        }
        if (getData() == null) {
            return;
        }

        removeOccurrencesAfter(calculateEndDate());

        generateFromGenToDate();
    }

    /**
     * Update recurrence actions with recurrence action values.
     * @param fromDate The given schedule date lower limit. 
     */
    public void updateRecurrenceActions(List<Property> properties, Date fromDate) {
        LOG.fine("Start.");

        Data data = getData();
        if (data == null) {
            return;
        }

        fromDate = DateUtils.getStart(fromDate);

        for (Action action : getProject().getChildren(Action.class)) {
            Recurrence recurrence = action.getRecurrence();
            if (recurrence == null || recurrence.id != id) {
                continue;
            }
            ActionStateScheduled state = (ActionStateScheduled) action.getState();
            if (state == null) {
                continue;
            }
            Date schdDate = state.getDate();
            if (schdDate != null && !schdDate.before(fromDate)) {
                updateAction(action, state, properties);
            }
        }
    }

    /**
     * Update all recurrence actions with the recurrence action values. 
     */
    public void updateRecurrenceActions(List<Property> properties) {
        LOG.fine("Start.");

        Data data = getData();
        if (data == null) {
            return;
        }

        for (Action action : getProject().getChildren(Action.class)) {
            Recurrence recurrence = action.getRecurrence();
            if (recurrence == null || recurrence.id != id) {
                continue;
            }
            ActionStateScheduled state = (ActionStateScheduled) action.getState();
            if (state == null) {
                continue;
            }
            updateAction(action, state, properties);
        }
    }

    private void updateAction(Action action, ActionStateScheduled state, List<Property> properties) {
        if (properties.contains(Property.SCHEDULED_HOURS) || properties.contains(Property.SCHEDULED_MINS)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(state.getDate());
            cal.set(Calendar.HOUR_OF_DAY, this.getScheduleHours());
            cal.set(Calendar.MINUTE, this.getScheduleMins());
            state.setDate(cal.getTime());
        }
        if (properties.contains(Property.DURATION_HOURS) || properties.contains(Property.DURATION_MINS)) {
            state.setDurationHours(this.getDurationHours());
            state.setDurationMins(this.getDurationMins());
        }
        if (properties.contains(Property.DESCRIPTION)) {
            action.setDescription(this.getDescription());
        }
        if (properties.contains(Property.SUCCESS)) {
            action.setSuccess(this.getSuccess());
        }
        if (properties.contains(Property.TOPIC)) {
            action.setTopic(this.getTopic());
        }
        if (properties.contains(Property.CONTEXT)) {
            action.setContext(this.getContext());
        }
        if (properties.contains(Property.TIME)) {
            action.setTime(this.getTime());
        }
        if (properties.contains(Property.ENERGY)) {
            action.setEnergy(this.getEnergy());
        }
        if (properties.contains(Property.PRIORITY)) {
            action.setPriority(this.getPriority());
        }
        if (properties.contains(Property.NOTES)) {
            action.setNotes(this.getNotes());
        }
    }

    /**
     * Move recurrence actions from a given project with sheduled date on or 
     * after a given date.
     * @param fromDate The given schedule date lower limit. 
     * @param fromProject The project to move actions from.
     */
    public void moveRecurrenceActions(Date fromDate, Project fromProject) {
        LOG.fine("Start.");

        Data data = getData();
        if (data == null) {
            return;
        }

        fromDate = DateUtils.getStart(fromDate);
        fromProject = (fromProject == null) ? data.getRootActions() : fromProject;

        if (fromProject.equals(getProject())) {
            return;
        }

        for (Action action : fromProject.getChildren(Action.class)) {
            if (action.isStateScheduled()) {
                ActionStateScheduled state = (ActionStateScheduled) action.getState();
                Recurrence recurrence = state.getRecurrence();
                if (recurrence != null && recurrence.id == id) {
                    Date schdDate = state.getDate();
                    if (schdDate != null && !schdDate.before(fromDate)) {
                        action.removeFromParent();
                        getProject().add(action);
                    }
                }
            }
        }
    }

    /**
     * Move recurrence actions from a given project to the recurrence project.
     * @param fromProject The project to move actoins from.
     */
    public void moveRecurrenceActions(Project fromProject) {
        LOG.fine("Begin");

        Data data = getData();
        if (data == null) {
            return;
        }

        fromProject = (fromProject == null) ? data.getRootActions() : fromProject;

        if (fromProject.equals(getProject())) {
            return;
        }
        for (Action action : fromProject.getChildren(Action.class)) {
            Recurrence recurrence = action.getRecurrence();
            if (recurrence != null && recurrence.id == id) {
                action.removeFromParent();
                getProject().add(action);
            }
        }
        LOG.fine("End");
    }

    public void setGenToDate(Date genToDate) {
        if (!Utils.equal(this.genToDate, genToDate)) {
            this.genToDate = genToDate;
            notifyObservers(this);
        }
    }

    /**
     * Recurrence basis enumeration. Generated dates are either determined from
     * a start date or from the done date of the previous action.
     */
    public static enum Basis {

        START_DATE((byte) 1), DONE_DATE((byte) 2);

        private Basis(byte id) {
            ID = id;
        }
        public final byte ID;

        public static Basis fromID(byte id) {
            switch (id) {
                case 1:
                    return START_DATE;
                case 2:
                    return DONE_DATE;
                default:
                    return START_DATE;
            }
        }
    }

    public void setDescription(String description) {
        if (!Utils.equal(this.description, description)) {
            this.description = description;
            notifyObservers(this);
        }
    }

    public void setTopic(Topic topic) {
        if (!Utils.equal(this.topic, topic)) {
            this.topic = topic;
            notifyObservers(this);
        }
    }

    public void setContext(Context context) {
        if (!Utils.equal(this.context, context)) {
            this.context = context;
            notifyObservers(this);
        }
    }

    public void setTime(Value time) {
        if (!Utils.equal(this.time, time)) {
            this.time = time;
            notifyObservers(this);
        }
    }

    public void setEnergy(Value energy) {
        if (!Utils.equal(this.energy, energy)) {
            this.energy = energy;
            notifyObservers(this);
        }
    }

    public void setPriority(Value priority) {
        if (!Utils.equal(this.priority, priority)) {
            this.priority = priority;
            notifyObservers(this);
        }
    }

    public String getDescription() {
        return description;
    }

    public Topic getTopic() {
        return topic;
    }

    public Context getContext() {
        return context;
    }

    public Value getTime() {
        return time;
    }

    public Value getEnergy() {
        return energy;
    }

    public Value getPriority() {
        return priority;
    }

    /**
     * Gets the notes.
     * @return the notes.
     */
    public String getNotes() {
        return (notes == null) ? "" : notes;
    }

    /**
     * Sets the notes.
     * @param notes The notes.
     */
//    public void setNotes(String notes) {
//        if (Utils.equal(this.notes, notes)) {
//            return;
//        }
//
//        this.notes = notes;
//        notifyObservers(this);
//    }
    public void setNotes(String notes) {
        if (Utils.equal(this.notes, notes)) {
            return;
        }

        String oldValue = this.notes;

        this.notes = notes;

        notifyObservers(this);

        getPropertyChangeSupport().firePropertyChange(PROP_NOTES, oldValue, notes);
    }

    /**
     * Gets the successful outcome.
     * @return the successful outcome value.
     */
    public String getSuccess() {
        return (success == null) ? "" : success;
    }

    /**
     * Sets the successful outcome.
     * @param success The successful outcome value.
     */
    public void setSuccess(String success) {
        if (Utils.equal(this.success, success)) {
            return;
        }

        this.success = success;
        notifyObservers(this);
    }

    /**
     * Sets the recurrence project. 
     * @param project The project.
     */
    public void setProject(Project project) {
        if (Utils.equal(this.project, project)) {
            return;
        }
        this.project = project;

        notifyObservers(this);
    }

    /**
     * Gets the recurrence project.
     * @return The recurrence project.
     */
    public Project getProject() {
        if (project == null) {
            return getData().getRootActions();
        }
        return project;
    }
    
    public Boolean isCalendarItem() {
        return calendarItem;
    }

    public void setCalendarItem(Boolean b) {
        this.calendarItem = b;
    }
    
}
