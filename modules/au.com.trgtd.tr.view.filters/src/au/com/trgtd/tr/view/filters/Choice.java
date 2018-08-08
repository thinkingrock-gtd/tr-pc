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
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.filters;

import java.io.Serializable;

/**
 * Choice base class.
 *
 * @author Jeremy Moore
 */
public abstract class Choice implements Serializable, Comparable<Choice> {
    
    /** Constructs a new instance. */
    public Choice() {}
    
    public abstract String getLabel();
    
    public abstract String getID();
    
    public String toString() {
        return getLabel();
    }
    
    public int compareTo(Choice choice) {
        if (choice.equals(this)) {
            return 0;            
        }        
        if (choice instanceof ChoiceAll) {
            return 1; // after the All choice
        }
        if (choice instanceof ChoiceMultiple) {
            return 1; // after the Multiple choice
        }
        if (choice instanceof ChoiceMultipleEdit) {
            return 1; // after the MultipleEdit choice
        }
        return toString().compareToIgnoreCase(choice.toString());
    }
    
    public boolean equals(Object object) {
        return object instanceof Choice && getID().equals(((Choice)object).getID());
    }
    
    
}
