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

import javax.swing.SpinnerNumberModel;

public class SpinnerCycleNumberModel extends SpinnerNumberModel {

    private final int min;
    private final int max;
    private final int step;
    
    public SpinnerCycleNumberModel(int value, int min, int max, int step) {
        super(value, min, max, step);
        this.min = min;
        this.max = max;
        this.step = step;
    }
    
    @Override
    public Object getNextValue() {
        int value = super.getNumber().intValue();
        
//      int step = super.getStepSize().intValue();
        int next = value + step;
//        int c = super.getMaximum().compareTo(new Integer(next));
//        if (c < 0) {
//            return super.getMinimum();
//        } else {
//            return super.getNextValue();
//        }
        if (next > max) {
            return min;
        } else {
            return next;
        }
    }
    
    
    @Override
    public Object getPreviousValue() {
        int value = super.getNumber().intValue();
//        int step = super.getStepSize().intValue();
        int next = value - step;
        
        
//        int c = super.getMinimum().compareTo(new Integer(next));
//        if (c > 0) {
//            return super.getMaximum();
//        } else {
//            return super.getPreviousValue();
//        }
        if (next < min) {
            return max;
        } else {
            return next;
        }
    }
    
}
