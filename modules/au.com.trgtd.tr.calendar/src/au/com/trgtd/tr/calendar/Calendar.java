/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
