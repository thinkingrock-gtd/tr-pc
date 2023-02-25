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
            if (child instanceof Action action) {
                processAction(action);
            } else if (child instanceof Project project1) {
                processProject(project1);
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
        if (state instanceof ActionStateDelegated asd) {
            asd.setDate(clearTime(asd.getDate()));
        } else if (state instanceof ActionStateScheduled ass) {
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
