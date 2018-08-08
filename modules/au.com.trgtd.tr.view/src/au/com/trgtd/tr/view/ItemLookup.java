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
package au.com.trgtd.tr.view;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * Lookup singleton for current Action and/or Project item.
 *
 * @author Jeremy Moore
 */
public class ItemLookup extends ProxyLookup {

    private static final ItemLookup instance = new ItemLookup();

    /** Private singleton constructor. */
    private ItemLookup() {
    }

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static ItemLookup instance() {
        return instance;
    }

    /**
     * Sets or removes the current action. The action is put in the lookup.
     * @param action The action to set as current or null to clear.
     */
    public void setCurrent(Action action) {
        if (action == null) {
            setLookups(new Lookup[]{});
        } else {
//            Object[] items = new Object[] {action, action.getParent()};
//            setLookups(new Lookup[] {Lookups.fixed(items)});
            setLookups(new Lookup[]{Lookups.singleton(action)});
        }
    }

    /**
     * Sets or removes the current project. The project is put in the lookup.
     * @param project The project to set as current or null to clear.
     */
    public void setCurrent(Project project) {
        if (project == null) {
            setLookups(new Lookup[]{});
        } else {
            setLookups(new Lookup[]{Lookups.singleton(project)});
        }
    }
}
