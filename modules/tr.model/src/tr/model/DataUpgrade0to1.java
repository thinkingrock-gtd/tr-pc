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
 
package tr.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import tr.model.Item.Item;

import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.project.Project;

/**
 * Upgrade the data model from version 0 to version 1.
 *
 * @author Jeremy Moore
 */
public class DataUpgrade0to1 {
    
    /**
     * Upgrade the data from version 0 to version 1.
     * @param data The data.
     */
    public static void process(Data data) {        
        if (data.getVersion() == 0) {
            upgrade(data);        
            data.version = 1;
        }        
    }
    
    // Upgrade the data.
    private static void upgrade(Data data) {
        System.out.print("Upgrading data from version 0 to version 1 ... ");

        processProjects(data);
        processSingleActions(data);

        System.out.println("Done");
    }
    
    // Process all projects.
    private static void processProjects(Data data) {
        // process all top level projects
        for (Iterator<Project> i = data.getRootProjects().iterator(Project.class); i.hasNext();) {
//            processProject((Project)i.next());
            processProject(i.next());
        }
    }
    
    /* Process a project and call recursively for sub-projects. */
    private static void processProject(Project project) {
        // process children
//        for (Iterator iter = project.iterator(); iter.hasNext();) {
        for (Iterator<Item> i = project.iterator(Item.class); i.hasNext();) {
            Item child = i.next();
            if (child instanceof Action) {
                processAction((Action)child);
            } else if (child instanceof Project) {
                processProject((Project)child);
            }
        }
    }
    
    // Process all single actions.
    private static void processSingleActions(Data data) {
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext();) {
            processAction(i.next());
        }
    }
    
    // Process an action.
    private static void processAction(Action action) {
        ActionState state = action.getState();
        if (state instanceof ActionStateDelegated) {
            ActionStateDelegated asd = (ActionStateDelegated)state;
            asd.setDate(clearTime(asd.getDate()));
        } else if (state instanceof ActionStateScheduled) {
            ActionStateScheduled ass = (ActionStateScheduled)state;
            ass.setDate(clearTime(ass.getDate()));
        }
    }
    
    private static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
}
