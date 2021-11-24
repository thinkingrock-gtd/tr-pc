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
package au.com.trgtd.tr.calendar;

import au.com.trgtd.tr.calendar.prefs.CalendarPrefs;
import au.com.trgtd.tr.calendar.spi.CalendarSynchronizer;
import org.openide.util.Lookup;

/** 
 * Calendar

 * @author Jeremy Moore
 */
public class Calendar {

    public static void syncToCalendar() {
        CalendarSynchronizer synchronizer = getSynchronizer();
        if (synchronizer != null) {
            synchronizer.syncToCalendar();
        }
    }    

    public static void syncToCalendar(String icsFile) {
        CalendarSynchronizer synchronizer = getSynchronizer();
        if (synchronizer != null) {
            synchronizer.syncToCalendar(icsFile);
        }
    }    
    
    public static void syncFromCalendar() {
        CalendarSynchronizer synchronizer = getSynchronizer();
        if (synchronizer != null) {
            synchronizer.syncFromCalendar();
        }
    }    
    
    private static CalendarSynchronizer getSynchronizer() {
        String id = CalendarPrefs.getSynchronizerID();
        if (id == null) {
            return null;
        }        
        for (CalendarSynchronizer synchronizer : lookup.allInstances()) {
            if (id.equals(synchronizer.getID())) {
                return synchronizer;
            }
        }
        return null;        
    }
    
    private static final Lookup.Result<CalendarSynchronizer> lookup 
            = Lookup.getDefault().lookupResult(CalendarSynchronizer.class);
        
}
