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

package au.com.trgtd.tr.view.projects.actions;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Single actions tree lookup singleton.
 *
 * @author Jeremy Moore
 */
public class SingleActionsLookup extends ProxyLookup {
    
    private static final SingleActionsLookup instance = new SingleActionsLookup();
    
    private SingleActionsLookup() {
    }
    
    /**
     * Sets or removes the data instance.
     * @param ds The data store instance to set or null to remove.
     */
    public static void register(SingleActionsTopComponent satc) {
        if (satc == null) {
            instance.setLookups(new Lookup[] {});
        } else {
            instance.setLookups(new Lookup[] {Lookups.singleton(satc)});
        }
    }
        
    /**
     * Looks up the single actions tree.
     * @return The single actions tree or null.
     */
    public static SingleActionsTopComponent lookup() {        
        return (SingleActionsTopComponent)instance.lookup(
                SingleActionsTopComponent.class);
    }
    
}
