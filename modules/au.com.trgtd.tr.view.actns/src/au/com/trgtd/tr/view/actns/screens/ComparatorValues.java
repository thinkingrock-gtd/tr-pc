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

package au.com.trgtd.tr.view.actns.screens;

import java.util.Comparator;
import tr.model.criteria.Value;

/**
 * Comparator for the time criteria.
 *
 * @author Jeremy Moore
 */
public class ComparatorValues implements Comparator<StyledValue> {
    
    private final ValueIDsProvider provider;
    
    public ComparatorValues(ValueIDsProvider provider) {
        this.provider = provider;
    }
    
    /**
     * Ordering is first by time values in time definition order, then nulls.
     */
    public int compare(StyledValue sv1, StyledValue sv2) {
        Value v1 = sv1.value;
        Value v2 = sv2.value;
        
        if (v1 == v2) return 0;
        if (v1 == null) return 1;
        if (v2 == null) return -1;
        
        int p1 = provider.getIDs().indexOf(v1.getID());
        int p2 = provider.getIDs().indexOf(v2.getID());
        
        if (p1 == -1) return 1;
        if (p2 == -1) return -1;
        
        return p1 - p2;
    }
    
}

