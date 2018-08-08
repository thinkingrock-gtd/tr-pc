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

import java.util.Vector;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * Multiple choice.
 *
 * @author Jeremy Moore
 */
public class ChoiceMultiple extends Choice {
    
    public static final String ID = "multiple";
    
    private Vector<Choice> chosen;
    
    public Vector<Choice> getChosen() {
        return (chosen == null) ? new Vector<Choice>() : chosen;
    }
    
    public void setChosen(Vector<Choice> chosen) {
        this.chosen = chosen;
    }
    
    public String getID() {
        return ID;
    }
    
    public String getLabel() {
        if (Utilities.isWindows()) {
            return NbBundle.getMessage(ChoiceMultiple.class, "windows-multiple");
        } else {
            return NbBundle.getMessage(ChoiceMultiple.class, "filter-multiple");
        }
    }
    
    public boolean equals(Object object) {
        return object instanceof ChoiceMultiple;
    }
    
}
