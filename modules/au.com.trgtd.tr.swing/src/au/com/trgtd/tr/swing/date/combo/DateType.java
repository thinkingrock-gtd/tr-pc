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

import java.io.Serializable;

/**
 * Date type enumeration.
 *
 * @author Jeremy Moore
 */
public enum DateType implements Serializable {
    
    NONE(0), DAYS(1), MS(2), CHOOSE(3), FIXED(4), START_OF_WEEK(5), END_OF_WEEK(6);
    
    public final int id;
    
    private DateType(int id) {
        this.id = id;
    }
    
    public static DateType getDateType(int id) {
        if (id == NONE.id) return NONE;
        if (id == DAYS.id) return DAYS;
        if (id == MS.id) return MS;
        if (id == CHOOSE.id) return CHOOSE;
        if (id == FIXED.id) return FIXED;
        if (id == START_OF_WEEK.id) return START_OF_WEEK;
        if (id == END_OF_WEEK.id) return END_OF_WEEK;
        return NONE;
    }
    
    @Override
    public String toString() {
        return name();
    }
    
}
