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

package tr.model;

import java.io.File;
import java.io.FileOutputStream;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

/**
 * Wrapper of iCal4j for creating ICalendar files.
 *
 * @author Jeremy Moore
 */
public class ICal4JWrapper {
    
//  private static Log log = LogFactory.getLog(Calendar.class);
    
    private final Calendar calendar;
    private final TimeZoneRegistry registry;
    private final TimeZone timezone;
    
    /**
     * Constructs a new instance.
     */
    public ICal4JWrapper(String tzid) {
        calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);        
        
        registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        timezone = registry.getTimeZone(tzid);
    }
    
    /**
     * Creates an all day event.
     * @param uid The event UID prefix.
     * @param date The event date.
     * @param desc The event description.
     */
    public void createAllDayEvent(String uid, java.util.Date date, String desc, String notes, String location) {
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        
        Date d = new Date(cal.getTime());
        
        VEvent vevent = new VEvent(d, desc);
        vevent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
        vevent.getProperties().add(new Uid(uid));
        
        if (notes != null) {
            vevent.getProperties().add(new Description(notes));
        }
        if (location != null) {
            vevent.getProperties().add(new Location(location));
        }
        if (timezone != null) {
            vevent.getProperties().add(timezone.getVTimeZone().getTimeZoneId());
        }
        
        calendar.getComponents().add(vevent);
        
        try {
            calendar.validate();
        } catch (ValidationException ex) {
            ex.printStackTrace(System.err);
        }
        
//	log.info(calendar);
    }
    
    /**
     * Creates a specific time event.
     * @param uid The event UID prefix.
     * @param date The event date.
     * @param desc The event description.
     * @param notes The event notes.
     * @param location The event location.
     * @param hrs The event duration hours.
     * @param mns The event duration minutes.
     */
    public void createSpecificTimeEvent(String uid, java.util.Date date,
            String desc, String notes, String location, int hrs, int mns) {
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        
        DateTime dt = new DateTime(cal.getTime());
        if (timezone != null) {
            dt.setTimeZone(timezone);
        }
        
        Dur dur = (hrs == 0 && mns == 0) ? new Dur(0, 0, 0, 1) : new Dur(0, hrs, mns, 0);
        
        VEvent vevent = new VEvent(dt, dur, desc);
        vevent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE_TIME);
        vevent.getProperties().add(new Uid(uid));
        if (notes != null) {
            vevent.getProperties().add(new Description(notes));
        }
        if (location != null) {
            vevent.getProperties().add(new Location(location));
        }
        calendar.getComponents().add(vevent);
        
        try {
            calendar.validate();
        } catch (ValidationException ex) {
            ex.printStackTrace(System.err);
        }
        
//	log.info(calendar);
    }
    
    public void createToDo(String uid, String desc, String notes, String location) {
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        
        VToDo vtodo = new VToDo(new Date(cal.getTime()), new Dur(0, 0, 0, 1), desc);
        vtodo.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
        vtodo.getProperties().add(new Uid(uid));
        if (notes != null) {
            vtodo.getProperties().add(new Description(notes));
        }
        if (location != null) {
            vtodo.getProperties().add(new Location(location));
        }
        if (timezone != null) {
            vtodo.getProperties().add(timezone.getVTimeZone().getTimeZoneId());
        }
        
        calendar.getComponents().add(vtodo);
        
        try {
            calendar.validate();
        } catch (ValidationException ex) {
            ex.printStackTrace(System.err);
        }
        
//	log.info(calendar);
    }
    
    /**
     * Write out the ICalendar to the output file.
     * @throws Exception.
     */
    public void write(File outfile) throws Exception {
        if (calendar.getComponents().size() > 0) {
            FileOutputStream fout = new FileOutputStream(outfile);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
            fout.close();
        }
    }
    
}
