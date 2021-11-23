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

import org.openide.util.NbBundle;

/**
 * Period type enumeration.
 *
 * @author Jeremy Moore
 */
public enum PeriodType {

    WEEKDAY(PeriodWeekday.ID, "period.weekday"),
    DAY(PeriodDay.ID, "period.day"),
    WEEK(PeriodWeek.ID, "period.week"),
    MONTH(PeriodMonth.ID, "period.month"),
    YEAR(PeriodYear.ID, "period.year");
    
    /** The period identifier. */
    public final byte ID;
    
    /** The period name. */
    public final String NAME;

    /* Constructor */
    private PeriodType(byte id, String key) {
        ID = id;
        NAME = NbBundle.getMessage(Period.class, key);
    }

    /** 
     * Gets the period type from the given period ID.
     * @param id The period ID.
     * @return The period type.
     */
    public static final PeriodType fromID(byte id) {
        switch (id) {
            case PeriodWeekday.ID:
                return WEEKDAY;
            case PeriodDay.ID:
                return DAY;
            case PeriodWeek.ID:
                return WEEK;
            case PeriodMonth.ID:
                return MONTH;
            case PeriodYear.ID:
                return YEAR;
            default:
                return WEEK;
        }
    }
    
    @Override
    public String toString() {
        return NAME;
    }
    
}
