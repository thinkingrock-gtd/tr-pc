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
package au.com.trgtd.tr.calendar.spi;

/**
 * Service provider interface for calendar application synchronizers.
 * 
 * @author Jeremy Moore
 */
public interface CalendarSynchronizer {

    /**
     * Gets the unique identifier of this service provider.
     * @return the identifier (e.g. au.com.trgtd.tr.calendar.synchronizer.outlook).
     */
    public String getID();
            
    /**
     * Gets the name of the synchronizer (e.g. Outlook Synchronizer). 
     * This will be displayed to the user in the user preferences. 
     * @return the name.
     */
    public String getName();

    /**
     * Synchronize out to the calendar application.
     */
    public void syncToCalendar();
    
    /**
     * Synchronize out to the calendar application.
     */
    public void syncToCalendar(String icsFile);

    /**
     * Synchronize in from the calendar application.
     */
    public void syncFromCalendar();
     
    /**
     * Get the synchronizer options.
     */
    public CalendarSynchronizerOptions getOptions();
    
}
