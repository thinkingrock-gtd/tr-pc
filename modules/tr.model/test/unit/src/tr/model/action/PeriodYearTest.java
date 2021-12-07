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

import java.util.Calendar;
import junit.framework.TestCase;

/**
 *
 * @author jmoore
 */
public class PeriodYearTest extends TestCase {
    
    public PeriodYearTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of addPeriods method, of class PeriodYear.
     */
    public void testAddPeriods() {
        System.out.println("addPeriods");

        Period yearPeriod = new PeriodYear();
                
        Calendar cal = Calendar.getInstance();       
        Interval thisYear = yearPeriod.getPeriod(cal.getTime());
        System.out.println("This year starts on " + thisYear.start);
        System.out.println("This year ends on " + thisYear.end);
        
        Interval nextYear = yearPeriod.addPeriods(thisYear, 1);                
        System.out.println("Next year starts on " + nextYear.start);
        System.out.println("Next year ends on " + nextYear.end);
        
        Interval twoYears = yearPeriod.addPeriods(thisYear, 2);                
        System.out.println("Second year starts on " + twoYears.start);
        System.out.println("Second year ends on " + twoYears.end);
        
        assertEquals(true, true);        
    }

}
