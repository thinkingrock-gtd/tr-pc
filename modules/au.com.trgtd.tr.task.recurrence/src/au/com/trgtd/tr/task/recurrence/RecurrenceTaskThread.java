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
package au.com.trgtd.tr.task.recurrence;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.Recurrence;
import tr.model.project.Project;

/**
 * Recurrence task thread.
 *
 * @author Jeremy Moore
 */
final class RecurrenceTaskThread extends Thread {
    
    private static final Logger LOG = Logger.getLogger("tr.recurrence");
    private static final String NAME = NbBundle.getMessage(RecurrenceTaskThread.class, "recurrence.task");
    private List<Integer> done;
    
    /** Constructs a new instance. */
    public RecurrenceTaskThread() {
        super(NAME);
    }
    
    /** Run the background task. */
    @Override
    public void run() {
        LOG.info("Started.");
        
        process();
        
        LOG.info("Finished.");
    }
    
    private void process() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data instance could not be obtained.");
            return;
        }
        
        done = new Vector<>();
        
//      LOG.info("Processing single actions ... ");
        for (Action action : data.getRootActions().getChildren(Action.class)) {
            process(action);
        }
        
//      LOG.info("Processing top level projects ... ");
        for (Project project : data.getRootProjects().getChildren(Project.class)) {
            process(project);
        }
    }
    
    /* Process for the given project. */
    private void process(Project project) {
        
        // call recursively for sub-projects
        for (Project subproject : project.getChildren(Project.class)) {
            process(subproject);
        }
        
        // process actions
        for (Action action : project.getChildren(Action.class)) {
            process(action);
        }
    }
    
    /* Process the given action. */
    private void process(Action action) {
//      LOG.log(Level.INFO, "Processing action: {0} >>> {1}", new Object[]{action.getID(), action.getDescription()});

        Recurrence recurrence = action.getRecurrence();
        if (recurrence == null) {
            return;
        }        
        if (recurrence.getBasis() != Recurrence.Basis.START_DATE) {
            return;
        }
        if (done.contains(recurrence.getID())) {
            return;
        }
        
        recurrence.generateFromGenToDate();
        
        done.add(recurrence.getID());
    }
    
}
