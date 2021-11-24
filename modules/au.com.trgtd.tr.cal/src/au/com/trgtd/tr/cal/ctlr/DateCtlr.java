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

import au.com.trgtd.tr.cal.model.Day;
import au.com.trgtd.tr.cal.utils.DateUtils;
import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Date;

/**
 * Date selection controller.  
 * 
 * @author Jeremy Moore
 */
public class DateCtlr {

    /** Default singleton instance with current date and time. */
    public final static DateCtlr DEFAULT = new DateCtlr();

    public final static String PROP_DATE = "date";    
    
    private final PropertyChangeSupport pcs;
    private Date date; 

    public DateCtlr() {
        this.pcs = new PropertyChangeSupport(this);
        this.date= new Date();
    }

    public DateCtlr(Date date) {
        this.pcs = new PropertyChangeSupport(this);
        this.date= date;
    }

    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(date);
        return calendar;        
    }
    
    public void setDate(Date newDate) {
        if (DateUtils.isSameDay(this.date, newDate)) {
            return;
        }
        Date oldDate = this.date;
        this.date = newDate;
        this.pcs.firePropertyChange(PROP_DATE, oldDate, newDate);        
    }

    /**
     * Fires a date change event to force views to update.  
     */
    public void fireChange() {
        this.pcs.firePropertyChange(PROP_DATE, null, date);        
    }
    
    public Date getDate() {
        return date;        
    }
   
    public Day getDay() {
        return new Day(date);        
    }

    public Day getMonthStart() {
        Calendar c = getCalendar();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new Day(c.getTime());        
    }
    
    public Day getMonthEnd() {
        Calendar c = getCalendar();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new Day(c.getTime());        
    }

    public Day getWeekStart() {
        Calendar c = getCalendar();   
        int difference = c.get(Calendar.DAY_OF_WEEK) - getFirstDayOfWeek();                    
        if (difference == -1) {
            // first day of week is Monday and day is Sunday
            difference = 6;
        }        
        c.add(Calendar.DAY_OF_YEAR, -difference);        
        return new Day(c.getTime());
    }

    public Day getWeekEnd() {
//      Calendar c = Calendar.getInstance();
        Calendar c = getCalendar();   
        c.setTime(getWeekStart().getDate());
        c.add(Calendar.DAY_OF_YEAR, 6);
        return new Day(c.getTime());        
    }
    
    /**
     * Set the day of the month of the current date.
     * @param day The day number.
     */
    public void setDayOfMonth(int day) {
        Calendar c = getCalendar();   
        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (day > maxDay || day < 1) {
            return;
        }        
        c.set(Calendar.DAY_OF_MONTH, day);
        setDate(c.getTime());        
    }

    /**
     * Set the month of the current date.
     * @param month The month number.
     */
    public void setMonth(int month) {
        if (month < Calendar.JANUARY || month > Calendar.DECEMBER) {
            return;
        }
        Calendar c = getCalendar();   
        c.set(Calendar.DAY_OF_MONTH, month);
        setDate(c.getTime());        
    }
    
    /**
     * Set the year of the current date.
     * @param year The year number.
     */
    public void setYear(int year) {
        Calendar c = getCalendar();   
        c.set(Calendar.YEAR, year);
        setDate(c.getTime());        
    }
    
    /** 
     * Gets the first day of the week calendar setting.
     * @return either Calendar.SUNDAY or Calendar.MONDAY.
     */
    public int getFirstDayOfWeek() {
        return DatesPrefs.getFirstDayOfWeek();        
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }   
    
}
