/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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
        
        List<Date> dates = new Vector<Date>(1);
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
