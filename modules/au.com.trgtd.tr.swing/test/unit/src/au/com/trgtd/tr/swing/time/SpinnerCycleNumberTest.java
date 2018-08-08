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
