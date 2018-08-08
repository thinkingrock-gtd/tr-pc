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

package au.com.trgtd.tr.view.actn;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * ComboBoxModel for action states.
 */
public class StatusComboBoxModel extends DefaultComboBoxModel {
    
    private List<StatusEnum> states;
    
    /** 
     * Constructs a new instance.
     */
    public StatusComboBoxModel() {
        super();
        states = new Vector<StatusEnum>();
        states.add(StatusEnum.INACTIVE);
        states.add(StatusEnum.DO_ASAP);
        states.add(StatusEnum.SCHEDULED);
        states.add(StatusEnum.DELEGATED);
    }
        
    /**
     * Implement ListModel.getElementAt(int index).
     */
    public Object getElementAt(int index) {
        return states.get(index);
    }
    
    /**
     * Implement ListModel.getSize().
     */
    public int getSize() {
        return states.size();
    }

}
