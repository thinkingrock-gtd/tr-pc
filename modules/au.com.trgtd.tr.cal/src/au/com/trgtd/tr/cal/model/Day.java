package au.com.trgtd.tr.cal.model;

import au.com.trgtd.tr.cal.utils.DateUtils;
import java.util.Calendar;
import java.util.Date;

/**
 * Day wraps a date with no time values and provides convenience methods.
 * 
 * @author Jeremy Moore
 */
public final class Day {
    
    private final Date date;

    public Day(Date date) {
        if (null == date) {
            throw new IllegalArgumentException("Day date can not be null.");
        }
        this.date = DateUtils.clearTime(date);
    }

    /** 
     * Gets the wrapped date value for the day. 
     * @return the date with all time values set to 0.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Add a number of days to this day and returns the result day.
     * @param n The number of days to add (negative to subtract).
     * @return The result day.
     */
    public Day addDays(int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, n);
        return new Day(c.getTime());        
    }
    
    public Day next() {
        return addDays(1);        
    }

    public Day prev() {
        return addDays(-1);        
    }

    public boolean before(Day day) {
        return date.before(day.date);
    }

    public boolean after(Day day) {
        return date.after(day.date);
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        return date.equals(((Day)object).date);
    }
    
    @Override
    public int hashCode() {
        return date.hashCode();
    }
    
}
