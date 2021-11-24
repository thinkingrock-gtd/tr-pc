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
package au.com.trgtd.tr.prefs.dates;

import au.com.trgtd.tr.appl.Constants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User preferences for dates.
 *
 * @author Jeremy Moore
 */
public class DatesPrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.dates");

    private static final Preferences PREFS = Constants.getPrefs("dates");      
    
    /* Week start day key. */
    public static final String KEY_WEEK_FIRST_DAY = "week.first.day";
    /* Date order key. */
    public static final String KEY_DATE_ORDER = "date.order";

    /** Week start day Sunday value. */
    public static final int SUNDAY = Calendar.SUNDAY;
    /** Week start day Monday value. */
    public static final int MONDAY = Calendar.MONDAY;

    /* Week start day default. */
    private static final int DEF_WEEK_FIRST_DAY = Calendar.getInstance().getFirstDayOfWeek();
    /** Date order for month first. */
    public static final int MMDDYY = 0;
    /** Date order for day first. */
    public static final int DDMMYY = 1;
    /** Date order for Japanese. */
    public static final int YYMMDD = 2;
    
    /* Date order default. */
    private static final int DEF_DATE_ORDER;
    static {
        final String country = Locale.getDefault().getCountry();
        if (country.equals(Locale.US.getCountry())) {
            DEF_DATE_ORDER = MMDDYY;
        } else if (country.equals(Locale.JAPAN.getCountry())) {
            DEF_DATE_ORDER = YYMMDD;            
        } else {
            DEF_DATE_ORDER = DDMMYY;                        
        }
    }
    
    /** Date format type for long format. */
    public static final int DF_LONG = 0;
    /** Date format type for short format. */
    public static final int DF_SHORT = 1;
    /** Date format type for medium format. */
    public static final int DF_MEDIUM = 2;
    
    /**
     * Date formats for the date picker and also for formatting dates, e.g.
     * DateFormats[MMDDYY][DF_LONG]
     */
    public static final DateFormat[][] DATE_FORMATS = new DateFormat[][] {
        {
            new SimpleDateFormat("EEE MMM d yyyy"), 
            new SimpleDateFormat("M/d/yy"), 
            new SimpleDateFormat("MM/dd/yyyy")
        },
        { 
            new SimpleDateFormat("EEE d MMM yyyy"), 
            new SimpleDateFormat("d/M/yy"), 
            new SimpleDateFormat("dd/MM/yyyy")
        },
        {
            DateFormat.getDateInstance(DateFormat.LONG, Locale.JAPANESE), 
            DateFormat.getDateInstance(DateFormat.SHORT, Locale.JAPANESE), 
            DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.JAPANESE)
        }
    };

    public static final Preferences getPrefs() {
        return PREFS;
    }

    /**
     * Format the given date in long (length) format using the date format
     * preferences.
     * @param date the date to format.
     * @return the formatted date.
     */
    public static final String formatLong(Date date) {
        return format(date, 0);
    }
    
    /**
     * Format the given date in medium (length) format using the date format
     * preferences.
     * @param date the date to format.
     * @return the formatted date.
     */
    public static final String formatMedium(Date date) {
        return format(date, 2);
    }
    
    /**
     * Format the given date in short (length) format using the date format
     * preferences.
     * @param date the date to format.
     * @return the formatted date.
     */
    public static final String formatShort(Date date) {
        return format(date, 1);
    }
    
    public static String format(Date date, int type) {
        return getDateFormat(type).format(date);
    }
    
    public static DateFormat getDateFormat(int type) throws IllegalArgumentException {
        if (type < 0 || type > 2) {
            throw new IllegalArgumentException("Argument type must be one of [0 = long, 1 = short, 2 = medium]");
        }
        int order = getDateOrder();
        return DATE_FORMATS[order][type];
    }
    
    /**
     * Gets the value for the first day of the week preference.
     * @see Calendar.getFirstDayOfWeek()
     * @return The value.
     */
    public static final int getFirstDayOfWeek() {
        return PREFS.getInt(KEY_WEEK_FIRST_DAY, DEF_WEEK_FIRST_DAY);
    }
    
    /**
     * Sets the value for the first day of the week preference.
     * @see Calendar.getFirstDayOfWeek()
     * @param value The value.
     */
    public static final void setFirstDayOfWeek(int value) {
        if (value == SUNDAY || value == MONDAY) {
            PREFS.putInt(KEY_WEEK_FIRST_DAY, value);
            flush();
        }
    }
        
    /**
     * Gets the value for the date order preference.
     * @return The value [DDMMYY|MMDDYY].
     */
    public static final int getDateOrder() {
        return PREFS.getInt(KEY_DATE_ORDER, DEF_DATE_ORDER);
    }
    
    /**
     * Sets the value for the date order preference.
     * @param value The value [DDMMYY|MMDDYY].
     */
    public static final void setDateOrder(int value) {
        if (value == DDMMYY || value == MMDDYY || value == YYMMDD) {
            PREFS.putInt(KEY_DATE_ORDER, value);
            flush();
        }
    }
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Dates preferences error. {0}", ex.getMessage());
        }
    }
    
}
