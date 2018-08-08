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

import tr.model.Item.Item;

import tr.model.action.Action;
import tr.model.action.Recurrence;
import tr.model.project.Project;

/**
 * Upgrade the data model from version 2 to version 3.
 * Initialises new recurrence period values.
 * @author Jeremy Moore
 */
public class DataUpgrade2to3 {

    private static int OLD_VERSION = 2;
    private static int NEW_VERSION = 3;

    /**
     * Upgrade the data from version 0 to version 1.
     * @param data The data.
     */
    public static void process(Data data) {
        if (data.version == OLD_VERSION) {
            upgrade(data);
            data.version = NEW_VERSION;
        }
    }

    // Upgrade the data.
    private static void upgrade(Data data) {
        System.out.print("Upgrading data from version " + OLD_VERSION + " to version " + NEW_VERSION + " ... ");

        processProjects(data);
        processSingleActions(data);

        System.out.println("Done");
    }

    // Process all projects.
    private static void processProjects(Data data) {
        // process all top level projects
        for (Project project : data.getRootProjects().getChildren(Project.class)) {
            processProject(project);
        }
    }

    /* Process a project and call recursively for sub-projects. */
    private static void processProject(Project project) {
        // process children
        for (Item child : project.getChildren()) {
            if (child instanceof Action) {
                processAction((Action) child);
            } else if (child instanceof Project) {
                processProject((Project) child);
            }
        }
    }

    // Process all single actions.
    private static void processSingleActions(Data data) {
        for (Action action : data.getRootActions().getChildren(Action.class)) {
            processAction(action);
        }
    }

    // Process an action.
    private static void processAction(Action action) {
        // initialise recurrence period values
        Recurrence recurrence = action.getRecurrence();
        if (recurrence == null) {
            return;
        }
        recurrence.getPeriod().initialise(recurrence.getStartDate());
    }
}
