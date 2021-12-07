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
