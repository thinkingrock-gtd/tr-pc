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

import java.io.File;
import java.util.Iterator;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.project.Project;
import tr.model.thought.Thought;

/**
 * Generate an ICalendar file for the data model.
 *
 * @author Jeremy Moore
 */
public class ICalendar {
    
    private static final String UID_SUFFIX = "@thinkingrock.com.au";
    
    private static ICal4JWrapper ical;
    
    private static File outfile;
    
    /**
     * Generate an iCalendar for the data and file path.
     * @param data The data model.
     * @param path The output file path (including filename).
     */
    public static void process(Data data, String path, String tzid) {
        
        ical = new ICal4JWrapper(tzid);
        
        try {
            System.out.print("Generating ICalendar file ... ");
            initialise(path);
            process(data);
            finalise();
            System.out.println("done");
        } catch (Exception ex) {
            System.out.println();
            ex.printStackTrace(System.err);
        } 
    }
    
    /* Initialise the output file. */
    private static void initialise(String filename) throws Exception {
        outfile = new File(filename);
    }
    
    /* Finalise the output file. */
    private static void finalise() throws Exception {
        ical.write(outfile);
    }
    
    // Process the data.
    private static void process(Data data) {
        // process top level projects
        for (Iterator<Project> i = data.getRootProjects().iterator(Project.class); i.hasNext();) {
            process(data, i.next());
        }
        // process single actions
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext();) {
            process(data, i.next());
        }
    }
    
    /* process a project (recursively). */
    private static void process(Data data, Project project) {
        // process children
        for (Iterator<Item> i = project.iterator(Item.class); i.hasNext();) {
            Item child = i.next();
            if (child instanceof Action action) {
                process(data, action);
            } else if (child instanceof Project prj) {
                process(data, prj);
            }
        }
    }
    
    /* Process an action. */
    private static void process(Data data, Action action) {
        if (action.isDone()) return;
        
        String context = action.getContext().getName();
        
        StringBuilder notes = new StringBuilder();
        notes.append(context);
        if (action.isSingleAction()) {
            Thought thought = action.getThought();
            if (thought != null) {
                notes.append(" {").append(thought.getDescription()).append("}");
            }
        } else {
            Project project = (Project)action.getParent();
            if (project != null) {
                notes.append(" [").append(project.getDescription()).append("]");
            }
        }
        notes.append("\r\n");
        notes.append(action.getNotes());
        
        ActionState state = action.getState();
        if (state instanceof ActionStateASAP) {
            ical.createToDo(getUID(action.getID()), action.getDescription(), notes.toString(), context);
        } else if (state instanceof ActionStateDelegated asd) {
            if (asd.getDate()==null) return;
            String desc = action.getDescription() + " " + " " + org.openide.util.NbBundle.getMessage(ICalendar.class, "Delegated_to") + ": " + asd.getTo();
            ical.createAllDayEvent(getUID(action.getID()), asd.getDate(), desc, notes.toString(), context);
        } else if (state instanceof ActionStateScheduled ass) {
            if (ass.getDate() == null) return;
            
            ical.createSpecificTimeEvent(getUID(action.getID()), ass.getDate(),
                    action.getDescription(), notes.toString(), context,
                    ass.getDurationHours(), ass.getDurationMinutes());
        }
    }
    
    private static String getUID(int id) {
        return id + UID_SUFFIX;
    }
    
}
