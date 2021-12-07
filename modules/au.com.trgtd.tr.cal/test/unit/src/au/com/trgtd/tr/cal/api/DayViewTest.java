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
package au.com.trgtd.tr.cal.api;

//import au.com.trgtd.tr.cal.test.TestFrame;
import javax.swing.SwingUtilities;
import org.junit.*;

public class DayViewTest {
    
    public DayViewTest() {
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
     * Test of setDayViewCtlr method, of class DayView.
     */
    @Test
    public void testDayView() {
        System.out.println("test day view");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                TestFrame.main(null);
            }
            
        });
    }

}
