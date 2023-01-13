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
package au.com.trgtd.tr.calendar.ical4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.ValidationException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Jeremy Moore
 */
public class ICal4JWrapperTest {

    /**
     * Test of createAllDayEvent method, of class ICal4JWrapper.
     */
    @Test
    @Ignore // TODO #26 - reactivate after fixing flakyness
    public void testCreateAllDayEvent() {
        System.out.println("createAllDayEvent");

        String uid = "123456";
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 25);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.YEAR, 2009);
        Date date = c.getTime();
        String desc = "Christmas Day";
        String notes = null; // "Ho\nHo\nHo!\n"; // "";
        String location = null; // "1 Cold Place\nThe North Pole"; //"";
        Integer priority = 1;
        ICal4JWrapper instance = new ICal4JWrapper(null);
        instance.createAllDayEvent(uid, date, desc, notes, location, priority);

        try {
            instance.calendar.validate();
        } catch (ValidationException ex) {
            fail("Exception. " + ex.getMessage());
            ex.printStackTrace(System.err);
        }

        CalendarOutputter calendarOutputter = new CalendarOutputter();
        StringWriter stringWriter = new StringWriter();
        try {
            calendarOutputter.output(instance.calendar, stringWriter);
        } catch (Exception ex) {
            fail("Exception. " + ex.getMessage());
            ex.printStackTrace(System.err);
        }

        String actualOutput = stringWriter.getBuffer().toString();

        String expectOutput =
                "BEGIN:VCALENDAR\r\n" +
                "PRODID:-//Ben Fortuna//iCal4j 1.0//EN\r\n" +
                "VERSION:2.0\r\n" +
                "CALSCALE:GREGORIAN\r\n" +
                "BEGIN:VEVENT\r\n" +
                "DTSTAMP:99999999T0999999\r\n" +
                "DTSTART;VALUE=DATE:20091225\r\n" + // TODO #26 this is flaky, maybe dependent on time-zone (...20091224 in my laptop)
                "SUMMARY:Christmas Day\r\n" +
                "UID:123456\r\n" +
                "PRIORITY:1\r\n" +
                "END:VEVENT\r\n" +
                "END:VCALENDAR\r\n";

        int p = actualOutput.indexOf("DTSTAMP:");
        String s = actualOutput.substring(p + 8, p + 24);
        expectOutput = expectOutput.replace("99999999T0999999", s);

        try {
            FileWriter w1 = new FileWriter(File.createTempFile("tf1", "txt"));
            FileWriter w2 = new FileWriter(File.createTempFile("tf2", "txt"));
            w1.write(expectOutput);
            w2.write(actualOutput);
            w1.close();
            w2.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assertEquals(expectOutput.trim(), actualOutput.trim());
    }
//    /**
//     * Test of createSpecificTimeEvent method, of class ICal4JWrapper.
//     */
//    @Test
//    public void testCreateSpecificTimeEvent() {
//        System.out.println("createSpecificTimeEvent");
//        String uid = "";
//        Date date = null;
//        String desc = "";
//        String notes = "";
//        String location = "";
//        int hrs = 0;
//        int mns = 0;
//        ICal4JWrapper instance = null;
//        instance.createSpecificTimeEvent(uid, date, desc, notes, location, hrs, mns);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//

    /**
     * Test of createToDo method, of class ICal4JWrapper.
     */
    @Test
    public void testCreateToDo() {
        System.out.println("createToDo");
//        String uid = "";
//        String desc = "";
//        String notes = "";
//        String location = "";
//        ICal4JWrapper instance = null;
//        instance.createToDo(uid, desc, notes, location);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");

        String uid = "1000002";
        String desc = "Buy an iPhone cover";
        String notes = null;
        String location = null;
        Integer priority = 1;
        ICal4JWrapper instance = new ICal4JWrapper(null);
        instance.createToDo(uid, desc, notes, location, priority);

        try {
            instance.calendar.validate();
        } catch (ValidationException ex) {
            fail("Exception. " + ex.getMessage());
            ex.printStackTrace(System.err);
        }

        CalendarOutputter calendarOutputter = new CalendarOutputter();
        StringWriter stringWriter = new StringWriter();
        try {
            calendarOutputter.output(instance.calendar, stringWriter);
        } catch (Exception ex) {
            fail("Exception. " + ex.getMessage());
            ex.printStackTrace(System.err);
        }

        String actualOutput = stringWriter.getBuffer().toString();

        String expectOutput =
                "BEGIN:VCALENDAR\r\n" +
                "PRODID:-//Ben Fortuna//iCal4j 1.0//EN\r\n" +
                "VERSION:2.0\r\n" +
                "CALSCALE:GREGORIAN\r\n" +
                "BEGIN:VTODO\r\n" +
                "DTSTAMP:99999999T0999999\r\n" +
                "SUMMARY:Buy an iPhone cover\r\n" +
                "UID:1000002\r\n" +
                "PRIORITY:1\r\n" +
                "END:VTODO\r\n" +
                "END:VCALENDAR\r\n";

        int p = actualOutput.indexOf("DTSTAMP:");
        String s = actualOutput.substring(p + 8, p + 24);
        expectOutput = expectOutput.replace("99999999T0999999", s);

        try {
            FileWriter w1 = new FileWriter(new File("/Users/jmoore/temp/f1.txt"));
            FileWriter w2 = new FileWriter(new File("/Users/jmoore/temp/f2.txt"));
            w1.write(expectOutput);
            w2.write(actualOutput);
            w1.close();
            w2.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assertEquals(expectOutput.trim(), actualOutput.trim());

    }
//
//    /**
//     * Test of write method, of class ICal4JWrapper.
//     */
//    @Test
//    public void testWrite() throws Exception {
//        System.out.println("write");
//        File outfile = null;
//        ICal4JWrapper instance = null;
//        instance.write(outfile);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
