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
package au.com.trgtd.tr.view.actn;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * ComboBoxModel for action states.
 */
public class StatusComboBoxModel extends DefaultComboBoxModel<StatusEnum> {
    
    private List<StatusEnum> states;
    
    /** 
     * Constructs a new instance.
     */
    public StatusComboBoxModel() {
        super();
        states = new Vector<>();
        states.add(StatusEnum.INACTIVE);
        states.add(StatusEnum.DO_ASAP);
        states.add(StatusEnum.SCHEDULED);
        states.add(StatusEnum.DELEGATED);
    }
        
    /**
     * Implement ListModel.getElementAt(int index).
     */
    public StatusEnum getElementAt(int index) {
        return states.get(index);
    }
    
    /**
     * Implement ListModel.getSize().
     */
    public int getSize() {
        return states.size();
    }

}
