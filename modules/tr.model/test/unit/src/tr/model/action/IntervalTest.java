/*
 * IntervalTest.java
 * JUnit based test
 *
 * Created on April 12, 2008, 2:06 PM
 */

package tr.model.action;

import java.util.Calendar;
import junit.framework.*;

/**
 * Test Interval class
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
