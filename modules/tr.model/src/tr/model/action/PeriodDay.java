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

import au.com.trgtd.tr.prefs.recurrence.RecurrencePrefs;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Day period.
 *
 * @author Jeremy Moore
 */
public class PeriodDay extends Period {
    
    public static final byte ID = 2;
    public static final String BUNDLE_KEY = "period.day";
        
    /**
     * Creates a clone.
     */
    public Period clone() {
        return new PeriodDay();
    }    
    
    public final String getBundleKey() {
        return BUNDLE_KEY;
    }
    
    public final byte getID() {
        return ID;
    }
    
    /*
     * Calculates the start date of the period a given date is in.
     * @param date The given date.
     * @return The given date with time cleared.
     */
    private Date calculateStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /*
     * Calculates the end date of the period a given date is in.
     * @param date The given date.
     * @return The given date with time set to the last millisecond of the day.
     */
    private Date calculateEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    private Date addDays(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }    
    
    public List<Date> getSelectedDates(Date startDate, Date templateDate) {
        assert(startDate != null);
        assert(templateDate != null);
        
        Calendar templateCal = Calendar.getInstance();
        templateCal.setTime(templateDate);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, templateCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, templateCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, templateCal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, templateCal.get(Calendar.MILLISECOND));
        
        List<Date> dates = new Vector<>(1);
        dates.add(cal.getTime());
        return dates;
    }

    @Override
    public void initialise(Date startDate) {
        // nothing to do
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PeriodDay;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public int getDefaultAdvanceNbr() {
        return RecurrencePrefs.getNbrFutureDay();
    }

    @Override
    public PeriodType getType() {
        return PeriodType.DAY;
    }
    
    @Override
    public Interval getPeriod(Date date) {
        return new Interval(calculateStartDate(date), calculateEndDate(date));
    }

    @Override
    public Interval addPeriods(Interval interval, int n) {
        return getPeriod(addDays(interval.start, n));
    }
    
}
