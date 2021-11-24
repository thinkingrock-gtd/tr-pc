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
package au.com.trgtd.tr.prefs.recurrence;

import au.com.trgtd.tr.appl.Constants;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User preferences for recurrent scheduled actions.
 *
 * @author Jeremy Moore
 */
public class RecurrencePrefs {

    private static final Logger LOG = Logger.getLogger("tr.prefs.recurrence");
    
    private static final Preferences PREFS = Constants.getPrefs("recurrence");      

    private static final String KEY_NBR_FUTURE_WEEKDAY = "nbr.future.weekday";
    private static final String KEY_NBR_FUTURE_DAY = "nbr.future.day";
    private static final String KEY_NBR_FUTURE_WEEK = "nbr.future.week";
    private static final String KEY_NBR_FUTURE_MONTH = "nbr.future.month";
    private static final String KEY_NBR_FUTURE_YEAR = "nbr.future.year";

    private static final int DEF_NBR_FUTURE_WEEKDAY = 5;
    private static final int DEF_NBR_FUTURE_DAY = 7;
    private static final int DEF_NBR_FUTURE_WEEK = 4;
    private static final int DEF_NBR_FUTURE_MONTH = 1;
    private static final int DEF_NBR_FUTURE_YEAR = 1;

    /**
     * Gets the preference value for the number of future occurrences to generate 
     * for weekday recurrence. 
     * @return The value.
     */
    public static final int getNbrFutureWeekday() {
        return PREFS.getInt(KEY_NBR_FUTURE_WEEKDAY, DEF_NBR_FUTURE_WEEKDAY);
    }

    /**
     * Gets the preference value for the number of future occurrences to generate 
     * for day recurrence. 
     * @return The value.
     */
    public static final int getNbrFutureDay() {
        return PREFS.getInt(KEY_NBR_FUTURE_DAY, DEF_NBR_FUTURE_DAY);
    }
    
    /**
     * Gets the preference value for the number of future occurrences to generate 
     * for week recurrence. 
     * @return The value.
     */
    public static final int getNbrFutureWeek() {
        return PREFS.getInt(KEY_NBR_FUTURE_WEEK, DEF_NBR_FUTURE_WEEK);
    }
    
    /**
     * Gets the preference value for the number of future occurrences to generate 
     * for month recurrence. 
     * @return The value.
     */
    public static final int getNbrFutureMonth() {
        return PREFS.getInt(KEY_NBR_FUTURE_MONTH, DEF_NBR_FUTURE_MONTH);
    }
    
    /**
     * Gets the preference value for the number of future occurrences to generate 
     * for year recurrence. 
     * @return The value.
     */
    public static final int getNbrFutureYear() {
        return PREFS.getInt(KEY_NBR_FUTURE_YEAR, DEF_NBR_FUTURE_YEAR);
    }
 
    /**
     * Sets the preference value for the number of future occurrences to generate 
     * for weekday recurrence. 
     * @param value
     */
    public static final void setNbrFutureWeekday(int value) {
        PREFS.putInt(KEY_NBR_FUTURE_WEEKDAY, value);
        flush();
    }    
    
    /**
     * Sets the preference value for the number of future occurrences to generate 
     * for day recurrence. 
     * @param value.
     */
    public static final void setNbrFutureDay(int value) {
        PREFS.putInt(KEY_NBR_FUTURE_DAY, value);
        flush();
    }    
  
    /**
     * Sets the preference value for the number of future occurrences to generate 
     * for weekday recurrence. 
     * @param value.
     */
    public static final void setNbrFutureWeek(int value) {
        PREFS.putInt(KEY_NBR_FUTURE_WEEK, value);
        flush();
    }    
    
    /**
     * Sets the preference value for the number of future occurrences to generate 
     * for month recurrence. 
     * @param value.
     */
    public static final void setNbrFutureMonth(int value) {
        PREFS.putInt(KEY_NBR_FUTURE_MONTH, value);
        flush();
    }    

    /**
     * Sets the preference value for the number of future occurrences to generate 
     * for year recurrence. 
     * @param value.
     */
    public static final void setNbrFutureYear(int value) {
        PREFS.putInt(KEY_NBR_FUTURE_YEAR, value);
        flush();
    }    
    
    private static void flush() {
        try {
            PREFS.flush();
        } catch (BackingStoreException ex) {
            LOG.log(Level.SEVERE, "Projects preferences error. {0}", ex.getMessage());
        }
    }
    
}
