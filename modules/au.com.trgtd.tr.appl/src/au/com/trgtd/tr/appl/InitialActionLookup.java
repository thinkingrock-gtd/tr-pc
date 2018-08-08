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

package au.com.trgtd.tr.appl;
 
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * Initial actions lookup singleton.
 *
 * @author Jeremy Moore
 */
public class InitialActionLookup extends ProxyLookup {
    
    private static final InitialActionLookup instance = new InitialActionLookup();

    private final InstanceContent ic;
    private final AbstractLookup al;
    
    /** Private singleton constructor. */
    private InitialActionLookup() {
        ic = new InstanceContent();
        al = new AbstractLookup(ic);
        setLookups(new Lookup[] {al});
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static InitialActionLookup instance() {
        return instance;
    }
    
    /**
     * Sets or removes the data instance.
     * @param data The data instance to set or null to remove.
     */
    public void add(InitialAction action) {
        if (action != null) {
            ic.add (action);
        }
        
//        List list = Arrays.asList(getLookups());
//        list.add(action);        
//        Lookup[] lookups = (Lookup[])list.toArray(new Lookup[] {});                
//        setLookups(lookups);
    }
    
}
