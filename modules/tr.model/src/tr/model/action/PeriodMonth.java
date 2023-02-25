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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.openide.util.NbBundle;

/**
 * Month period.
 *
 * @author Jeremy Moore
 */
public class PeriodMonth extends Period {

    public static final byte ID = 4;
    public static final String BUNDLE_KEY = "period.month";
    private Option option;
    private List<Integer> selectedDays;
    private OnTheNth onTheNth;
    private OnTheDay onTheDay;

    public enum Option {
        Each(),
        OnThe()
    }

    public enum OnTheNth {
        First(1),
        Second(2),
        Third(3),
        Fourth(4),
        Last(Integer.MAX_VALUE);
        public final int n;
        private OnTheNth(int n) {
            this.n = n;
        }
        public String toString() {
            switch (n) {
                case 1: return NbBundle.getMessage(getClass(), "first");
                case 2: return NbBundle.getMessage(getClass(), "second");
                case 3: return NbBundle.getMessage(getClass(), "third");
                case 4: return NbBundle.getMessage(getClass(), "fourth");
                default: return NbBundle.getMessage(getClass(), "last");
            }
        }
    }

    public enum OnTheDay {
        Monday(Calendar.MONDAY),
        Tuesday(Calendar.TUESDAY),
        Wednesday(Calendar.WEDNESDAY),
        Thursday(Calendar.THURSDAY),
        Friday(Calendar.FRIDAY),
        Saturday(Calendar.SATURDAY),
        Sunday(Calendar.SUNDAY);
        public final int dayOfWeek;
        private OnTheDay(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
        public String toString() {
            return NbBundle.getMessage(getClass(), name().toLowerCase());
        }
    }

    public PeriodMonth() {
        this.setOption(Option.Each);
        this.selectedDays = new Vector<>();
    }

    public PeriodMonth(List<Integer> daysOfMonth) {
        this.setOption(Option.Each);
        this.selectedDays = daysOfMonth;
    }

    public PeriodMonth(OnTheNth nth, OnTheDay day) {
        this();
        this.setOption(Option.OnThe);
        this.setOnTheNth(nth);
        this.setOnTheDay(day);
    }

    /**
     * Creates a clone.
     */
    public Period clone() {
        PeriodMonth clone = new PeriodMonth();
        clone.option = this.option;
        clone.selectedDays = new Vector<>(selectedDays);
        clone.onTheDay = this.onTheDay;
        clone.onTheNth = this.onTheNth;
        return clone;
    }

    public final byte getID() {
        return ID;
    }

    public final String getBundleKey() {
        return BUNDLE_KEY;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PeriodMonth other) {
            if (option != other.option) {
                return false;
            }
            if (option == Option.Each) {
                return selectedDays.equals(other.selectedDays);
            }
            if (option == Option.OnThe) {
                return onTheNth == other.onTheNth && onTheDay == other.onTheDay;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.option != null ? this.option.hashCode() : 0);
        hash = 23 * hash + (this.selectedDays != null ? this.selectedDays.hashCode() : 0);
        hash = 23 * hash + (this.onTheNth != null ? this.onTheNth.hashCode() : 0);
        hash = 23 * hash + (this.onTheDay != null ? this.onTheDay.hashCode() : 0);
        return hash;
    }

    public List<Integer> getSelectedDays() {
        Collections.sort(selectedDays);        
        return new Vector<>(selectedDays);
    }

    public String getSelectedDaysText() {
        if (selectedDays.isEmpty()) {
            return getMsg("period.day") + " ...";
        }
        
        Collections.sort(selectedDays);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedDays.size(); i++) {
            sb.append(i > 0 ? "," : "").append(selectedDays.get(i));
        }
        
        return sb.toString();
    }

    public void setSelectedDays(List<Integer> days) {
        selectedDays.clear();
        if (days != null) {
            for (Integer day : days) {
                select(day);
            }
        }
    }

    public boolean isSelected(Integer dayOfMonth) {
        synchronized (this) {
            return selectedDays.contains(dayOfMonth);
        }
    }

    public void select(Integer dayOfMonth) {
        synchronized (this) {
            if (!isSelected(dayOfMonth)) {
                selectedDays.add(dayOfMonth);
            }
        }
    }

    public void deselected(Integer dayOfMonth) {
        synchronized (this) {
            selectedDays.remove(dayOfMonth);
        }
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
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

    /**
     * Calculates the start date of the period a given date is in.
     * @param date The given date.
     * @return The first day of the month of date with time values cleared.
     */
    private Date calculateStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Calculates the end date of the period a given date is in.
     * @param date The given date.
     * @return The last day of the month that the date is in, with time set to
     * the last millisecond of that day.
     */
    private Date calculateEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();        
        calendar.setTime(date);        
        calendar.set(Calendar.DAY_OF_MONTH, 1); // first day of month
        calendar.set(Calendar.HOUR_OF_DAY, 0);  // 0 hour
        calendar.set(Calendar.MINUTE, 0);       // 0 minute
        calendar.set(Calendar.SECOND, 0);       // 0 second
        calendar.set(Calendar.MILLISECOND, 0);  // 0 millisecond   
        calendar.add(Calendar.MONTH, 1);        // add 1 month        
        calendar.add(Calendar.MILLISECOND, -1); // minus 1 millisecond
        return calendar.getTime();
    }

    // This will probably not work too well with days at the end of the month 
    // (i.e. 28, 29 February and 30, 31 of other months).
    private Date addMonths(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    public List<Date> getSelectedDates(Date startDate, Date templateDate) {
        assert (startDate != null);
        assert (templateDate != null);

        List<Date> dates = new Vector<>();

        Calendar templateCal = Calendar.getInstance();
        templateCal.setTime(templateDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        if (this.option == PeriodMonth.Option.Each) {
            for (Integer day : this.selectedDays) {
                cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.HOUR_OF_DAY, templateCal.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, templateCal.get(Calendar.MINUTE));
                cal.set(Calendar.SECOND, templateCal.get(Calendar.SECOND));
                cal.set(Calendar.MILLISECOND, templateCal.get(Calendar.MILLISECOND));
                dates.add(cal.getTime());
            }
        } else {
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
        }

        return dates;
    }

    @Override
    public void initialise(Date startDate) {
        option = Option.Each;
        selectedDays = new Vector<>();
        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            selectedDays.add(calendar.get(Calendar.DAY_OF_MONTH));
        }
        onTheNth = OnTheNth.First;
        onTheDay = OnTheDay.Monday;
    }

    @Override
    public int getDefaultAdvanceNbr() {
        return RecurrencePrefs.getNbrFutureMonth();
    }

    @Override
    public PeriodType getType() {
        return PeriodType.MONTH;
    }

    @Override
    public Interval getPeriod(Date date) {
        return new Interval(calculateStartDate(date), calculateEndDate(date));
    }

    @Override
    public Interval addPeriods(Interval interval, int n) {
        return getPeriod(addMonths(interval.start, n));
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

}
