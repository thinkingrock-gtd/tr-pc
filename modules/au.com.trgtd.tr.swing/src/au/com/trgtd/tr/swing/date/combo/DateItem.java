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
package au.com.trgtd.tr.swing.date.combo;

import au.com.trgtd.tr.prefs.dates.DatesPrefs;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.openide.util.NbBundle;

/**
 * Date item for date combo list.
 *
 * @author Jeremy Moore
 */
public class DateItem implements Serializable {

    public static final DateItem NONE = new DateItem(DateType.NONE, "", 0);
    public static final DateItem DATE_CHOOSER = new DateItem(DateType.CHOOSE, NbBundle.getMessage(DateItem.class, "select.date"), 0);
    public static final DateItem WEEKS_AGO_4 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "four.weeks.ago"), -28);
    public static final DateItem WEEKS_AGO_3 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "three.weeks.ago"), -21);
    public static final DateItem WEEKS_AGO_2 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "two.weeks.ago"), -14);
    public static final DateItem WEEKS_AGO_1 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "one.weeks.ago"), -7);
    public static final DateItem YESTERDAY = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "yesterday"), -1);
    public static final DateItem TODAY = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "today"), 0);
    public static final DateItem TOMORROW = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "tomorrow"), 1);
    public static final DateItem WEEKS_1 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "one.week"), 7);
    public static final DateItem WEEKS_2 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "two.weeks"), 14);
    public static final DateItem WEEKS_3 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "three.weeks"), 21);
    public static final DateItem WEEKS_4 = new DateItem(DateType.DAYS, NbBundle.getMessage(DateItem.class, "four.weeks"), 28);
    public static final DateItem EARLIEST = new DateItem(DateType.FIXED, NbBundle.getMessage(DateItem.class, "earliest"), Long.MIN_VALUE);
    public static final DateItem LATEST = new DateItem(DateType.FIXED, NbBundle.getMessage(DateItem.class, "latest"), Long.MAX_VALUE);
    public static final DateItem BEG_LAST_WEEK = new DateItem(DateType.START_OF_WEEK, NbBundle.getMessage(DateItem.class, "beg.last.week"), -1);
    public static final DateItem END_LAST_WEEK = new DateItem(DateType.END_OF_WEEK, NbBundle.getMessage(DateItem.class, "end.last.week"), -1);
    public static final DateItem BEG_THIS_WEEK = new DateItem(DateType.START_OF_WEEK, NbBundle.getMessage(DateItem.class, "beg.this.week"), 0);
    public static final DateItem END_THIS_WEEK = new DateItem(DateType.END_OF_WEEK, NbBundle.getMessage(DateItem.class, "end.this.week"), 0);
    public static final DateItem BEG_NEXT_WEEK = new DateItem(DateType.START_OF_WEEK, NbBundle.getMessage(DateItem.class, "beg.next.week"), 1);
    public static final DateItem END_NEXT_WEEK = new DateItem(DateType.END_OF_WEEK, NbBundle.getMessage(DateItem.class, "end.next.week"), 1);

    public static int getDaysToStartOfWeek() {
        return -1 * (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - DatesPrefs.getFirstDayOfWeek());
    }

    public static int getDaysToEndOfWeek() {
        return getDaysToStartOfWeek() + 6;
    }

    public DateType type;
    public String label;
    public long value;

    public DateItem(DateType type, String label, long value) {
        this.type = type;
        this.label = label;
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DateItem dateItem) {
            return (dateItem.type == type && dateItem.value == value);
        }
        return false;
    }

    public Date getDate() {
        switch (type) {
            case DAYS: {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, (int) value);
                return c.getTime();
            }
            case MS: {
                return new Date(value);
            }
            case FIXED: {
                return new Date(value);
            }
            case START_OF_WEEK: {
                long days = getDaysToStartOfWeek() + (value * 7);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, (int) days);
                return c.getTime();
            }
            case END_OF_WEEK: {
                long days = getDaysToEndOfWeek() + (value * 7);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_YEAR, (int) days);
                return c.getTime();
            }
            default: {
                return null;
            }
        }
    }
    
}
