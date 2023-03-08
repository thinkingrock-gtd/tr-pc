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

import au.com.trgtd.tr.prefs.actions.ActionPrefs;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import java.util.Calendar;
import java.util.Date; 
import tr.model.action.ActionState.Type;

/**
 * Scheduled action state.
 *
 * @author Jeremy Moore
 */
public class ActionStateScheduled extends ActionState implements Observer {
    
    private Date date;
    private byte durationHours;
    private byte durationMinutes;
    private Recurrence recurrence;
    
    private transient Byte defTimeHr;
    private transient Byte defTimeMn;
    
    /** Constructs a new instance. */
    public ActionStateScheduled() {
        super();
        durationHours = (byte)ActionPrefs.getSchdDurHrs();
        durationMinutes = (byte)ActionPrefs.getSchdDurMns();        
        defTimeHr = (byte)ActionPrefs.getSchdTimeHr();
        defTimeMn = (byte)ActionPrefs.getSchdTimeMn();        
    }
    
    /**
     * Makes a copy of itself omitting recurrence. 
     * @return The copy.
     */
    @Override
    public ActionState copy() {
        ActionStateScheduled copy = new ActionStateScheduled();
        copy.created = this.created;
        copy.date = this.date;
        copy.durationHours = this.durationHours;
        copy.durationMinutes = this.durationMinutes;
        copy.recurrence = null;
        return copy;
    }
    
    /**
     * Sets the scheduled date.
     * @param newDate The date to set.
     */
    public void setDate(Date newDate) {
//        if (!Utils.equal(this.date, date) ) {
//            this.date = date;
//            notifyObservers(this);
//        } 
        
        if (Utils.equal(this.date, newDate) ) {
            return;
        } 

// 2012-05-11 Fix problem with default time
//        // Set default time fields if first time date is set.
//        if (this.date == null && defTimeHr != null && defTimeMn != null) {
//            newDate = DateUtils.setTimeFields(newDate, defTimeHr, defTimeMn);
//            defTimeHr = null;
//            defTimeMn = null;
//        }
            
        this.date = newDate;
            
        notifyObservers(this);
    }
    
    /**
     * Gets the schedule date.
     * @return The schedule date.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Sets the duration hours.
     * @param hours The hours value.
     */
    public void setDurationHours(int hours) {
        byte value = (byte)hours;
        if (value != durationHours) {
            this.durationHours = value;
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
        byte value = (byte)minutes;
        if (value != durationMinutes) {
            this.durationMinutes = value;
            notifyObservers(this);
        }
    }
    
    /**
     * Gets the duration minutes.
     * @return The minutes value.
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    
    /**
     * Gets the scheduled minute.
     * @return The minute value.
     */
    public int getSchdMinute() {
        if (null == date) {
            return defTimeMn == null ? 0 : defTimeMn;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }
    
    /**
     * Gets the scheduled hour.
     * @return The hour value.
     */
    public int getSchdHour() {
        if (null == date) {
            return defTimeHr == null ? 0 : defTimeHr;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }
    
    
    /**
     * Sets the scheduled minute.
     * @param minute The minute value.
     */
    public void setSchdMinute(int minute) {
        // 2012-05-11 remove dafault value
        defTimeMn = null;
        if (null == date) {
            return;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, minute);
        setDate(c.getTime());
    }
    
    /**
     * Sets the scheduled hour.
     * @param hour The hour value.
     */
    public void setSchdHour(int hour) {
        // 2012-05-11 remove dafault value
        defTimeHr = null;
        if (null == date) {
            return;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, hour);
        setDate(c.getTime());
    }
    
    /**
     * Sets or removes a recurrence.
     * @param recurrence The new recurrence or null to remove any existing
     * recurrence.
     */
    public void setRecurrence(Recurrence recurrence) {
        
        if (this.recurrence != null) {
            this.recurrence.removeObserver(this);            
        }        
        
        this.recurrence = recurrence;
        
        if (this.recurrence != null) {
            this.recurrence.addObserver(this);            
        }                
        
        notifyObservers(this);
    };
    
    /**
     * Gets the recurrence if recurrence is defined.
     * @return The recurrence object or null.
     */
    public Recurrence getRecurrence() {
        return recurrence;
    };
    
    /**
     * Overrides equals to compare this state and another object for equality.
     * @param object the object to compare with.
     * @return true if the object is an ActionStateScheduled instance, the
     * creation dates are equal and the schedule dates are equal.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        ActionStateScheduled that = (ActionStateScheduled)object;
        if (!Utils.equal(this.date, that.date)) {
            return false;
        }
        if (this.durationHours != that.durationHours) {
            return false;
        }
        if (this.durationMinutes != that.durationMinutes) {
            return false;
        }        
        return true;
    }
    
    /* Observable implementation. */
    /** Resets observing of recurrence. */
    @Override
    public void resetObservers() {
        if (recurrence != null) {
            recurrence.addObserver(this);
            recurrence.resetObservers();
        }
    }
    
    /* Observer implementation. */
    /**  Handle recurrence changes. */
    @Override
    public void update(Observable observable, Object object) {
        notifyObservers(this, object);
    }
    
    @Override
    public final ActionState.Type getType() {
        return Type.SCHEDULED;
    }
    
}
