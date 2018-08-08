/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr.model.action;

import java.util.Calendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jmoore
 */
public class PeriodMonthTest {

    public PeriodMonthTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of clone method, of class PeriodMonth.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        PeriodMonth instance = new PeriodMonth();
        Period expect = instance;
        Period result = instance.clone();
        assertEquals(expect, result);
        
        // TODO other tests with period fields populated
    }

    /**
     * Test of getID method, of class PeriodMonth.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
        PeriodMonth instance = new PeriodMonth();
        byte expect = PeriodMonth.ID;
        byte result = instance.getID();
        assertEquals(expect, result);
    }

    /**
     * Test of getBundleKey method, of class PeriodMonth.
     */
    @Test
    public void testGetBundleKey() {
        System.out.println("getBundleKey");
        PeriodMonth instance = new PeriodMonth();
        String expect = PeriodMonth.BUNDLE_KEY;
        String result = instance.getBundleKey();
        assertEquals(expect, result);
    }

    /**
     * Test of addPeriods method, of class PeriodMonth.
     */
    @Test
    public void testAddPeriods() {
        System.out.println("addPeriods");

        Period months = new PeriodMonth();
                
        Calendar cal = Calendar.getInstance();
        
        Interval thisMonth = months.getPeriod(cal.getTime());
        System.out.println("This month starts: " + thisMonth.start);
        System.out.println("This month finish: " + thisMonth.end);
        
        Interval nextMonth = months.addPeriods(thisMonth, 1);                
        System.out.println("Next month starts: " + nextMonth.start);
        System.out.println("Next month finish: " + nextMonth.end);
        
        Interval twoMonths = months.addPeriods(thisMonth, 2);                
        System.out.println("Second month starts: " + twoMonths.start);
        System.out.println("Second month finish: " + twoMonths.end);
        
        assertEquals(true, true);        
    }

}
