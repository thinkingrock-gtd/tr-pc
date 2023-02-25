/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
            if (child instanceof Action action) {
                processAction(action);
            } else if (child instanceof Project prj) {
                processProject(prj);
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
