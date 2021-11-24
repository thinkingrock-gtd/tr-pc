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
package au.com.trgtd.tr.swing.time;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import junit.framework.*;

/**
 *
 * @author jmoore
 */
public class SpinnerCycleNumberTest extends TestCase {
    
    public SpinnerCycleNumberTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test.
     */
    public void testRun() {
        System.out.println("isValidValue");
        
        JFrame frame = new JFrame("Test SpinnerCycleNumber");
        frame.setSize(400, 400);
        
        SpinnerCycleNumber s1 = new SpinnerCycleNumber(0, 0, 23, 1);
        s1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                SpinnerCycleNumber s = (SpinnerCycleNumber)evt.getSource();
                System.out.println("HOUR: " + s.getVal() );
            }
        });
        s1.setVal(12);
        
        SpinnerCycleNumber s2 = new SpinnerCycleNumber(0, 0, 55, 5);
        s2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                SpinnerCycleNumber s = (SpinnerCycleNumber)evt.getSource();
                System.out.println("MINUTE: " + s.getVal() );
            }
        });
        
        s2.setVal(30);
        
        
        frame.add(s1, BorderLayout.NORTH);
        frame.add(s2, BorderLayout.SOUTH);
        frame.setVisible(true);
        
    }
    
}
