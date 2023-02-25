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

import java.util.Iterator;
import tr.model.Item.Item;

import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateASAP;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.project.Project;

/**
 * Upgrade the data model from version 1 to version 2.
 *
 * @author Jeremy Moore
 */
public class DataUpgrade1to2 {
    
    /**
     * Upgrade the data from version 0 to version 1.
     * @param data The data.
     */
    public static void process(Data data) {        
        if (data.version == 1) {
            upgrade(data);            
            data.version = 2;
        }
    }
    
    // Upgrade the data.
    private static void upgrade(Data data) {
        System.out.print("Upgrading data from version 1 to version 2 ... ");
        
        processProjects(data);
        processSingleActions(data);
        processFutureItems(data);
        processInformationItems(data);
        
        System.out.println("Done");
    }
    
    // Process all projects.
    private static void processProjects(Data data) {
        // process all top level projects
        for (Iterator<Project> i = data.getRootProjects().iterator(Project.class); i.hasNext();) {
            processProject(i.next());
        }
    }
    
    /* Process a project and call recursively for sub-projects. */
    private static void processProject(Project project) {
        // convert notes HTML format to plain text        
        String notes = project.getNotes();
        if (isHTML(notes)) {
            project.setNotes(au.com.trgtd.tr.util.HTML.html2text(notes));
        }        
        // process children
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
        // convert notes HTML format to plain text
        String notes = action.getNotes();
        if (isHTML(notes)) {
            action.setNotes(au.com.trgtd.tr.util.HTML.html2text(notes));
        }
        // transfer due date from DoASAP state to Action.
        ActionState actionState = action.getState();
        if (actionState instanceof ActionStateASAP stateDoASAP) {
            action.setDueDate(stateDoASAP.getDueDate());
        }
    }
    
    // Process future items.
    private static void processFutureItems(Data data) {
        // convert notes HTML format to plain text
        for (Future future : data.getFutureManager().list()) {
            String notes = future.getNotes();
            if (isHTML(notes)) {
                future.setNotes(au.com.trgtd.tr.util.HTML.html2text(notes));
            }
        }
    }
    
    // Process information items.
    private static void processInformationItems(Data data) {
        // convert notes HTML format to plain text
        for (Information information : data.getInformationManager().list()) {
            String notes = information.getNotes();
            if (isHTML(notes)) {
                information.setNotes(au.com.trgtd.tr.util.HTML.html2text(notes));
            }
        }
    }
    
    private static boolean isHTML(String s) {
        if (s == null) {
            return false;
        }
        s = s.trim();
        return s.startsWith("<html>") || s.startsWith("<HTML>");
    }
    
}
