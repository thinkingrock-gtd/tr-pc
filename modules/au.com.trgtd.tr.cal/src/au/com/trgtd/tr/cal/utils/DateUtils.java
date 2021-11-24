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
package au.com.trgtd.tr.cal.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * <p>Checks if two dates are in the same month.</p>
     * @param date1 The first date, not null
     * @param date2 The second date, not null
     * @return true if both dates are in the same month.
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return isSameMonth(c1, c2);
    }
    
    /**
     * <p>Checks if the dates of two calendars are in the same month.</p>
     * @param c1 The first calendar, not null.
     * @param c2 The second calendar, not null
     * @return true if both dates are in the same month.
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameMonth(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.ERA) == c2.get(Calendar.ERA));
    }
    
    /**
     * <p>Checks if two dates are on the same day (ignoring time).</p>
     * @param date1 The first date, not null
     * @param date2 The second date, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if two calendars represent the same day (ignoring time).</p>
     * @param c1  the first calendar, not null
     * @param c2  the second calendar, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) &&
                c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.ERA) == c2.get(Calendar.ERA));
    }
    
    /**
     * <p>Checks if a date is today.</p>
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }
    
    /**
     * <p>Checks if a calendar date is today.</p>
     * @param cal the calendar, not altered, not null
     * @return true if date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }
    
    /**
     * <p>Checks if the first date is before the second date ignoring time.</p>
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if the first calendar date is before the second calendar date ignoring time.</p>
     * @param c1 the first calendar, not altered, not null.
     * @param c2 the second calendar, not altered, not null.
     * @return true if c1 date is before c2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (c1.get(Calendar.ERA) < c2.get(Calendar.ERA)) return true;
        if (c1.get(Calendar.ERA) > c2.get(Calendar.ERA)) return false;
        if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) return true;
        if (c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)) return false;
        return c1.get(Calendar.DAY_OF_YEAR) < c2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * <p>Checks if the first date is after the second date ignoring time.</p>
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }
    
    /**
     * <p>Checks if the first calendar date is after the second calendar date ignoring time.</p>
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * <p>Checks if a date is after today and within a number of days in the future.</p>
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }
    
    /**
     * <p>Checks if a calendar date is after today and within a number of days in the future.</p>
     * @param cal the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && ! isAfterDay(cal, future));
    }
    
    /** 
     * Returns the given date with the time set to the start of the day.
     * @param date the given date.
     * @return the given date with time set to the start of the day.
     */
    public static Date getStart(Date date) {
        return clearTime(date);
    }
    
    /** Returns the given date with the time values cleared. */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }    

    /** Determines whether or not a date has any time values (hour, minute, 
     * seconds or millisecondsReturns the given date with the time values cleared. */

    /**
     * Determines whether or not a date has any time values.
     * @param date The date.
     * @return true if the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /** Returns the given date with time set to the end of the day */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /** 
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date. 
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }
    
    /** 
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date. 
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /** The maximum date possible. */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);




    public static final long MS_PER_MS = 1;
    public static final long MS_PER_SEC = 1000 * MS_PER_MS;
    public static final long MS_PER_MIN = 60 * MS_PER_SEC;
    public static final long MS_PER_HR = 60 * MS_PER_MIN;
    public static final long MS_PER_DAY = 24 * MS_PER_HR;
    
    public static final long MINS_PER_DAY = 24 * 60;
    

    /**
     * Gets the whole days in the given number of milliseconds.
     * @param ms
     * @return the number of days.
     */
    public static long getDays(final long ms) {
        return (ms < MS_PER_DAY) ? 0 : ms / MS_PER_DAY;
    }

    /**
     * Gets the number of hours in the given number of milliseconds (not
     * including whole days).
     * @param ms
     * @return the number of hours (not including whole days).
     */
    public static long getHrs(final long ms) {
        long remainder = ms;
        remainder -= getDays(ms) * MS_PER_DAY;
        return remainder / MS_PER_HR;
    }
    /**
     * Gets the number of minutes in the given number of milliseconds (not
     * including whole hours).
     * @param ms
     * @return the number of minutes (not including whole hours).
     */
    public static long getMins(final long ms) {
        long remainder = ms;
        remainder -= getDays(ms) * MS_PER_DAY;
        remainder -= getHrs(ms) * MS_PER_HR ;
        return remainder / MS_PER_MIN;
    }
    /**
     * Gets the number of seconds in the given number of milliseconds (not
     * including whole minutes).
     * @param ms
     * @return the number of seconds (not including whole minutes).
     */
    public static long getSecs(final long ms) {
        long remainder = ms;
        remainder -= getDays(ms) * MS_PER_DAY;
        remainder -= getHrs(ms) * MS_PER_HR ;
        remainder -= getMins(ms) * MS_PER_MIN ;
        return remainder / MS_PER_SEC;
    }
    /**
     * Gets the number of milliseconds for the given number of days, hours,
     * minutes and seconds.
     * @param days The number of days.
     * @param hrs The number of hours.
     * @param mins The number of minutes.
     * @param secs The number of seconds.
     * @return the number of milliseconds.
     */
    public static long getMilliseconds(long days, long hrs, long mins, long secs) {
        return (days * MS_PER_DAY) + (hrs * MS_PER_HR) + (mins * MS_PER_MIN) + (secs * MS_PER_SEC);
    }
     
    /**
     * Gets the time of day in minutes.
     * @param date The date.
     * @return The time of day in minutes.
     */ 
    public static int getTimeInMins(Date date) {
        if (null == date) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);        
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);        
        return (h * 60) + m;
    }


    
    
}
