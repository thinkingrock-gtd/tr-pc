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
import junit.framework.*;

/**
 * Test Interval class
 *
 * Created on April 12, 2008, 2:06 PM
 *
 * @author Jeremy Moore
 */
public class IntervalTest extends TestCase {
    
    public IntervalTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected void tearDown() throws Exception {
    }

    /**
     * Test constructor, of class tr.model.action.Interval.
     */
    public void testConstructor() {
        System.out.println("constructor");
        
        try {            
            Interval instance = new Interval(null, null);            
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
        }

        
        
//        Interval instance = new Interval(calBeg.getTime(), calEnd.getTime());
//        
//        boolean expect = true;        
//        boolean result = instance.equals(instance);
//        
//        assertEquals(expect, result);

        // TODO: other equals tests with interval populated.
    }
    
    
    /**
     * Test of equals method, of class tr.model.action.Interval.
     */
    public void testEquals() {
        System.out.println("equals");
        
        Object object = null;
        
        Calendar calBeg = Calendar.getInstance();
        Calendar calEnd = Calendar.getInstance();        
        Interval instance = new Interval(calBeg.getTime(), calEnd.getTime());
        
        boolean expect = true;        
        boolean result = instance.equals(instance);
        
        assertEquals(expect, result);

        // TODO: other equals tests with interval populated.
    }
    
    
    
    
    
    
    
//    /**
//     * Test of after method, of class tr.model.action.Interval.
//     */
//    public void testAfter() {
//        System.out.println("after");
//        
//        Interval other = null;
//        Interval instance = null;
//        
//        boolean expResult = true;
//        boolean result = instance.after(other);
//        assertEquals(expResult, result);
//        
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
//    /**
//     * Test of before method, of class tr.model.action.Interval.
//     */
//    public void testBefore() {
//        System.out.println("before");
//        
//        Interval other = null;
//        Interval instance = null;
//        
//        boolean expResult = true;
//        boolean result = instance.before(other);
//        assertEquals(expResult, result);
//        
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
