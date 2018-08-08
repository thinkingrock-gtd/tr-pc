/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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
