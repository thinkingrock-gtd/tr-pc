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
 * Weekday period (ie not weekends).
 *
 * @author Jeremy Moore
 */
public class PeriodWeekday extends Period {

    public static final byte ID = 1;
    public static final String BUNDLE_KEY = "period.weekday";

    /**
     * Creates a clone.
     */
    public Period clone() {
        return new PeriodWeekday();
    }

    public final byte getID() {
        return ID;
    }

    public final String getBundleKey() {
        return BUNDLE_KEY;
    }

    /*
     * Calculates the start date of the period for a given date.
     * @param date The given date.
     * @return The given date if it is a weekday or the next Monday if the date
     * is a Saturday or Sunday. Time values are cleared.
     */
    private Date calculateStartDate(Date date) {
        assert (date != null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 2);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * Calculates the end date of the period for a given date.
     * @param date The given date.
     * @return The given date if it is a weekday or the next Monday if the date
     * is a Saturday or Sunday. The time is set to the last millisecond of the
     * day.
     */
    private Date calculateEndDate(Date date) {
        assert (date != null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 2);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private Date addWeekdays(Date date, int weekdays) {
        assert (date != null);
        assert (weekdays > 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int days = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
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

        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    private Date minusWeekdays(Date date, int weekdays) {
        assert (date != null);
        assert (weekdays > 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int days = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.FRIDAY:
                days = weekdays + ( ((weekdays + 0) / 5) * 2);
                break;
            case Calendar.THURSDAY:
                days = weekdays + ( ((weekdays + 1) / 5) * 2);
                break;
            case Calendar.WEDNESDAY:
                days = weekdays + ( ((weekdays + 2) / 5) * 2);
                break;
            case Calendar.TUESDAY:
                days = weekdays + ( ((weekdays + 3) / 5) * 2);
                break;
            case Calendar.MONDAY:
                days = weekdays + ( ((weekdays + 4) / 5) * 2);
                break;
            case Calendar.SUNDAY:
                days = weekdays + ( ((weekdays - 1) / 5) * 2) + 1;
                break;
            case Calendar.SATURDAY:
                days = weekdays + ( ((weekdays - 1) / 5) * 2);
                break;
        }

        calendar.add(Calendar.DAY_OF_YEAR, days * -1);
        return calendar.getTime();
    }

    public List<Date> getSelectedDates(Date startDate, Date templateDate) {
        assert (startDate != null);
        assert (templateDate != null);

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
    public boolean equals(Object object) {
        return object instanceof PeriodWeekday;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public void initialise(Date startDate) {
        // nothing to do.
    }

    @Override
    public int getDefaultAdvanceNbr() {
        return RecurrencePrefs.getNbrFutureWeekday();
    }

    @Override
    public PeriodType getType() {
        return PeriodType.WEEKDAY;
    }

    @Override
    public Interval getPeriod(Date date) {
        return new Interval(calculateStartDate(date), calculateEndDate(date));
    }

    @Override
    public Interval addPeriods(Interval interval, int n) {
        if (n > 0) {
            return getPeriod(addWeekdays(interval.start, n));
        }
        if (n < 0) {
            return getPeriod(minusWeekdays(interval.start, Math.abs(n)));
        }
        return getPeriod(interval.start);
    }
}
