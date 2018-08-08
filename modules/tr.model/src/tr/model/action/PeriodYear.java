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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.openide.util.NbBundle;
import tr.model.action.PeriodMonth.OnTheDay;
import tr.model.action.PeriodMonth.OnTheNth;

/**
 * Year period.
 *
 * @author Jeremy Moore
 */
public class PeriodYear extends Period {

    public static final byte ID = 5;
    public static final String BUNDLE_KEY = "period.year";
    public static final String[] MONTHS_TEXT = {
        NbBundle.getMessage(PeriodYear.class, "month.jan"),
        NbBundle.getMessage(PeriodYear.class, "month.feb"),
        NbBundle.getMessage(PeriodYear.class, "month.mar"),
        NbBundle.getMessage(PeriodYear.class, "month.apr"),
        NbBundle.getMessage(PeriodYear.class, "month.may"),
        NbBundle.getMessage(PeriodYear.class, "month.jun"),
        NbBundle.getMessage(PeriodYear.class, "month.jul"),
        NbBundle.getMessage(PeriodYear.class, "month.aug"),
        NbBundle.getMessage(PeriodYear.class, "month.sep"),
        NbBundle.getMessage(PeriodYear.class, "month.oct"),
        NbBundle.getMessage(PeriodYear.class, "month.nov"),
        NbBundle.getMessage(PeriodYear.class, "month.dec"),
    };
    private List<Integer> selectedMonths;
    private boolean onTheSelected;
    private PeriodMonth.OnTheNth onTheNth;
    private PeriodMonth.OnTheDay onTheDay;

    public PeriodYear() {
        this.selectedMonths = new Vector<Integer>();
        this.onTheSelected = false;
        this.onTheNth = OnTheNth.First;
        this.onTheDay = OnTheDay.Monday;
    }

    public PeriodYear(List<Integer> months) {
        this.selectedMonths = months;
        this.onTheSelected = false;
        this.onTheNth = OnTheNth.First;
        this.onTheDay = OnTheDay.Monday;
    }

    public PeriodYear(List<Integer> months, OnTheNth nth, OnTheDay day) {
        this.selectedMonths = months;
        this.onTheSelected = true;
        this.onTheNth = nth;
        this.onTheDay = day;
    }

    /**
     * Creates a clone.
     */
    public Period clone() {
        PeriodYear clone = new PeriodYear();
        clone.selectedMonths = new Vector<Integer>(selectedMonths);
        clone.onTheSelected = this.onTheSelected;
        clone.onTheNth = this.onTheNth;
        clone.onTheDay = this.onTheDay;
        return clone;
    }

    public final byte getID() {
        return ID;
    }

    public final String getBundleKey() {
        return BUNDLE_KEY;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.selectedMonths != null ? this.selectedMonths.hashCode() : 0);
        hash = 43 * hash + (this.onTheSelected ? 1 : 0);
        hash = 43 * hash + (this.onTheNth != null ? this.onTheNth.hashCode() : 0);
        hash = 43 * hash + (this.onTheDay != null ? this.onTheDay.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PeriodYear) {
            PeriodYear other = (PeriodYear) object;
            if (!selectedMonths.equals(other.selectedMonths)) {
                return false;
            }
            if (onTheSelected != other.onTheSelected) {
                return false;
            }
            if (onTheSelected) {
                return onTheNth == other.onTheNth && onTheDay == other.onTheDay;
            }
            return true;
        }
        return false;
    }

    public List<Integer> getSelectedMonths() {
        Collections.sort(selectedMonths);
        return new Vector<Integer>(selectedMonths);
    }

    public String getSelectedMonthsText() {
        if (selectedMonths.size() == 0) {
            return getMsg("period.month") + " ...";
        }

        Collections.sort(selectedMonths);

        StringBuffer sb = new StringBuffer();
        
        for (int i = 0; i < selectedMonths.size(); i++) {
            sb.append((i > 0 ? "," : "") + MONTHS_TEXT[selectedMonths.get(i)]);
        }

        return sb.toString();
    }

    public void setSelectedMonths(List<Integer> months) {
        selectedMonths.clear();
        if (months != null) {
            for (Integer month : months) {
                select(month);
            }
        }
    }

    public boolean isSelected(Integer month) {
        synchronized (this) {
            return selectedMonths.contains(month);
        }
    }

    public void select(Integer month) {
        synchronized (this) {
            if (!isSelected(month)) {
                selectedMonths.add(month);
            }
        }
    }

    public void deselected(Integer month) {
        synchronized (this) {
            selectedMonths.remove(month);
        }
    }

    public boolean isOnTheSelected() {
        return onTheSelected;
    }

    public void setOnTheSelected(boolean b) {
        this.onTheSelected = b;
    }

    public OnTheNth getOnTheNth() {
        return onTheNth;
    }

    public void setOnTheNth(OnTheNth onTheNth) {
        this.onTheNth = onTheNth;
    }

    public OnTheDay getOnTheDay() {
        return onTheDay;
    }

    public void setOnTheDay(OnTheDay onTheDay) {
        this.onTheDay = onTheDay;
    }

    /*
     * Calculates the start date of the period a given date is in.
     * @param date The given date.
     * @return The first day of the year of date with time values cleared.
     */
    private Date calculateStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * Calculates the end date of the fear period a given date is in.
     * @param date The given date.
     * @return The last day of the year that the date is in, with time set to
     * the last millisecond of that day.
     */
    private Date calculateEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);  // first day of year
        calendar.set(Calendar.HOUR_OF_DAY, 0);  // zero hour
        calendar.set(Calendar.MINUTE, 0);       // zero minute
        calendar.set(Calendar.SECOND, 0);       // zero second
        calendar.set(Calendar.MILLISECOND, 0);  // zero millisecond
        calendar.add(Calendar.YEAR, 1);         // add 1 year
        calendar.add(Calendar.MILLISECOND, -1); // minus 1 millisecond
        return calendar.getTime();
    }

    private Date addYears(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, n);
        return calendar.getTime();
    }

    public List<Date> getSelectedDates(Date startDate, Date templateDate) {
        assert (startDate != null);
        assert (templateDate != null);

        List<Date> dates = new Vector<Date>();

        Calendar templateCal = Calendar.getInstance();
        templateCal.setTime(templateDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        for (int month : this.selectedMonths) {

            cal.set(Calendar.MONTH, month);

            if (this.onTheSelected) {
                if (this.onTheNth == OnTheNth.Last) {
                    cal.set(Calendar.DAY_OF_WEEK, this.onTheDay.dayOfWeek);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
                    cal.set(Calendar.HOUR_OF_DAY, templateCal.get(Calendar.HOUR_OF_DAY));
                    cal.set(Calendar.MINUTE, templateCal.get(Calendar.MINUTE));
                    cal.set(Calendar.SECOND, templateCal.get(Calendar.SECOND));
                    cal.set(Calendar.MILLISECOND, templateCal.get(Calendar.MILLISECOND));
                    dates.add(cal.getTime());
                } else if (this.onTheNth != null) {
                    cal.set(Calendar.DAY_OF_WEEK, this.onTheDay.dayOfWeek);
                    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, this.onTheNth.n);
                    cal.set(Calendar.HOUR_OF_DAY, templateCal.get(Calendar.HOUR_OF_DAY));
                    cal.set(Calendar.MINUTE, templateCal.get(Calendar.MINUTE));
                    cal.set(Calendar.SECOND, templateCal.get(Calendar.SECOND));
                    cal.set(Calendar.MILLISECOND, templateCal.get(Calendar.MILLISECOND));
                    dates.add(cal.getTime());
                }
            } else {
                // base day of month on template
                cal.set(Calendar.DAY_OF_MONTH, templateCal.get(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.HOUR_OF_DAY, templateCal.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, templateCal.get(Calendar.MINUTE));
                cal.set(Calendar.SECOND, templateCal.get(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, templateCal.get(Calendar.MILLISECOND));
                dates.add(cal.getTime());
            }
        }

        return dates;
    }

    @Override
    public void initialise(Date startDate) {
        selectedMonths = new Vector<Integer>();
        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            selectedMonths.add(calendar.get(Calendar.MONTH));

        }
        onTheSelected = false;
        onTheNth = PeriodMonth.OnTheNth.First;
        onTheDay = PeriodMonth.OnTheDay.Monday;
    }

    @Override
    public int getDefaultAdvanceNbr() {
        return RecurrencePrefs.getNbrFutureYear();
    }

    @Override
    public PeriodType getType() {
        return PeriodType.YEAR;
    }

    @Override
    public Interval getPeriod(Date date) {
        return new Interval(calculateStartDate(date), calculateEndDate(date));
    }

    @Override
    public Interval addPeriods(Interval interval, int n) {
        return getPeriod(addYears(interval.start, n));
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

}
