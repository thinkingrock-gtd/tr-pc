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

package au.com.trgtd.tr.view.project;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import tr.model.project.Sequencing;

/**
 * Sequencing ComboBoxModel.
 */ 
public class SequencingModel extends DefaultComboBoxModel {
    
    private List<Sequencing> types;
    
    /** Creates a new instance. */
    public SequencingModel() {
        super();
        types = new Vector<Sequencing>();
        types.add(Sequencing.INTO_SUBPROJECTS);
        types.add(Sequencing.OVER_SUBPROJECTS);
    }
    
    /**
     * Implement ListModel.getElementAt(int index).
     */
    @Override
    public Object getElementAt(int index) {
        return types.get(index);
    }
    
    /**
     * Implement ListModel.getSize().
     */
    @Override
    public int getSize() {
        return types.size();
    }
    
}
