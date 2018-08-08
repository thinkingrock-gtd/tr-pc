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
package au.com.trgtd.tr.calendar.ical4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;
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
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Due;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import au.com.trgtd.tr.util.DateUtils;

/**
 * Wrapper of iCal4j for creating ICalendar files.
 *
 * @author Jeremy Moore
 */
public class ICal4JWrapper {

    private static final Logger LOG = Logger.getLogger("au.com.trgtd.tr.calendar.ical4j.ICal4JWrapper");
    public final TimeZoneRegistry registry;    
    public final Calendar calendar;
    public final TimeZone timezone;
    public final VTimeZone vtimezone;    

    /**
     * Constructs a new instance.
     */
    public ICal4JWrapper(String tzid) {
        registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        
        calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        
        if (tzid == null || tzid.trim().length() == 0) {
            timezone = null;
            vtimezone = null;
            LOG.info("No time zone specified.");
        } else {
            timezone = registry.getTimeZone(tzid);
            if (timezone == null) {
                vtimezone = null;
                LOG.warning("Time zone not recognised.");
            } else {
                vtimezone = timezone.getVTimeZone();                
                calendar.getComponents().add(vtimezone);
                LOG.info("Time zone added: " + timezone.getDisplayName());
            }
        }
    }

    /**
     * Creates an all day event.
     * @param uid The event UID prefix.
     * @param date The event date.
     * @param summary The event summary.
     * @param description The event description.
     * @param location The event location.
     */
    public void createAllDayEvent(String uid, java.util.Date date, String summary, String description, String location, Integer priority) {
        assert(uid != null);
        assert(date != null);
        assert(summary != null);
        
        VEvent vevent = new VEvent(new Date(DateUtils.clearTime(date)), summary);

        vevent.getProperties().add(new Uid(uid));

        if (description != null) {
            vevent.getProperties().add(new Description(description));
        }
        if (location != null) {
            vevent.getProperties().add(new Location(location));
        }
        if (priority != null) {            
            vevent.getProperties().add(new Priority(priority));
        }        
        if (vtimezone != null) {            
            vevent.getProperties().add(vtimezone.getTimeZoneId());
        }

        try {
            vevent.validate();
            calendar.getComponents().add(vevent);
        } catch (ValidationException ex) {
            LOG.severe("ValidationException for Event : " + vevent.toString() + "\n" + ex.getMessage());
            ex.printStackTrace(System.err);
        }

//      try {
//          calendar.validate();
//      } catch (ValidationException ex) {
//          ex.printStackTrace(System.err);
//      }

//      log.info(calendar);
    }

    /**
     * Creates a specific time event.
     * @param uid The event UID prefix.
     * @param date The event date.
     * @param summary The event description.
     * @param description The event description.
     * @param location The event location.
     * @param hrs The event duration hours.
     * @param mns The event duration minutes.
     */
    public void createSpecificTimeEvent(String uid, java.util.Date date, 
            String desc, String notes, String location, int hrs, int mns, 
            Integer priority) {

        assert(uid != null);
        assert(desc != null);
        assert(date != null);
        assert(DateUtils.hasTime(date));

        Dur dur = (hrs == 0 && mns == 0) ? new Dur(0, 0, 15, 0) : new Dur(0, hrs, mns, 0);

        VEvent vevent = new VEvent(new DateTime(date), dur, desc);
        vevent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE_TIME);
        vevent.getProperties().add(new Uid(uid));
        if (notes != null) {
            vevent.getProperties().add(new Description(notes));
        }
        if (location != null) {
            vevent.getProperties().add(new Location(location));
        }
        if (priority != null) {
            vevent.getProperties().add(new Priority(priority));            
        }                
        if (vtimezone != null) {            
            vevent.getProperties().add(vtimezone.getTimeZoneId());
        }

        try {
            vevent.validate();
            calendar.getComponents().add(vevent);
        } catch (ValidationException ex) {
            LOG.severe("ValidationException for Event : " + vevent.toString() + "\n" + ex.getMessage());
            ex.printStackTrace(System.err);
        }
        
//      try {
//          calendar.validate();
//      } catch (ValidationException ex) {
//          ex.printStackTrace(System.err);
//      }

//      log.info(calendar);
    }

    public void createToDo(String uid, String desc, String notes, String location, Integer priority) {
        createToDo(uid, null, null, desc, notes, location, priority);
    }
    
    public void createToDo(String uid, java.util.Date start, java.util.Date due, String desc, String notes, String location, Integer priority) {
        assert(uid != null);
        assert(desc != null);

        VToDo vtodo = new VToDo();         
        vtodo.getProperties().add(new Summary(desc));            
        vtodo.getProperties().add(new Uid(uid));     
        if (start != null) {
            DtStart dtStartProperty;
            if (DateUtils.hasTime(start)) {
                dtStartProperty = new DtStart(new DateTime(start));
                if (timezone != null) {
                    dtStartProperty.setTimeZone(timezone);
                }
            } else {
                dtStartProperty = new DtStart(new Date(DateUtils.clearTime(start)));
            }
            vtodo.getProperties().add(dtStartProperty);
        }
        if (due != null) {
            Due dueProperty;
            if (DateUtils.hasTime(due)) {
                dueProperty = new Due(new DateTime(due));
                if (timezone != null) {
                    dueProperty.setTimeZone(timezone);
                }
            } else {
                dueProperty = new Due(new Date(DateUtils.clearTime(due)));
            }
            vtodo.getProperties().add(dueProperty);
        }
        if (notes != null) {
            vtodo.getProperties().add(new Description(notes));
        }
        if (location != null) {
            vtodo.getProperties().add(new Location(location));
        }                
        if (priority != null) {
            vtodo.getProperties().add(new Priority(priority));                        
        }                
        if (vtimezone != null) {            
            vtodo.getProperties().add(vtimezone.getTimeZoneId());
        }

        try {
            vtodo.validate();
            calendar.getComponents().add(vtodo);
        } catch (ValidationException ex) {
            LOG.severe("ValidationException for ToDO : " + vtodo.toString() + "\n" + ex.getMessage());
            ex.printStackTrace(System.err);
        }

//      try {
//          calendar.validate();
//      } catch (ValidationException ex) {
//          ex.printStackTrace(System.err);
//      }

//      log.info(calendar);
    }
    
//    private Date getDate(java.util.Date date) {
//        java.util.Calendar cal = java.util.Calendar.getInstance();
//        if (timezone != null) {
//            cal.setTimeZone(timezone);
//        }
//        cal.setTime(date);
//        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
//        cal.set(java.util.Calendar.MINUTE, 0);
//        cal.set(java.util.Calendar.SECOND, 0);
//        cal.set(java.util.Calendar.MILLISECOND, 0);
//        return new Date(cal.getTime());
//    }
//
//    private DateTime getDateTime(java.util.Date date) {
//        java.util.Calendar cal = java.util.Calendar.getInstance();
//        if (timezone != null) {
//            cal.setTimeZone(timezone);
//        }
//        cal.setTime(date);
//        cal.set(java.util.Calendar.SECOND, 0);
//        cal.set(java.util.Calendar.MILLISECOND, 0);
//        DateTime datetime = new DateTime(cal.getTime());
//        if (timezone != null) {
//            datetime.setTimeZone(timezone);
//        }
//        return datetime;
//    }
    
    public void validate() throws ValidationException {
        calendar.validate();
    }
    
    /**
     * Write out the ICalendar to the output file.
     * @throws Exception.
     */
    public void write(File outfile) throws Exception {
        if (calendar.getComponents().size() > 0) {
            String encoding = "UTF-8";            
            Writer writer = new OutputStreamWriter(new FileOutputStream(outfile), encoding);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, writer);
            writer.close();
        }
    }
    
}
