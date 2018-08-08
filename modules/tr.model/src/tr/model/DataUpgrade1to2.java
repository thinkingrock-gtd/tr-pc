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
        // convert notes HTML format to plain text
        String notes = action.getNotes();
        if (isHTML(notes)) {
            action.setNotes(au.com.trgtd.tr.util.HTML.html2text(notes));
        }
        // transfer due date from DoASAP state to Action.
        ActionState actionState = action.getState();
        if (actionState instanceof ActionStateASAP) {
            ActionStateASAP stateDoASAP = (ActionStateASAP)actionState;
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
