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
package au.com.trgtd.tr.task.activation;

import au.com.trgtd.tr.appl.Constants;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateInactive;
import tr.model.future.Future;
import tr.model.project.Project;
import tr.model.thought.Thought;
import au.com.trgtd.tr.util.DateUtils;

/**
 * Activator thread.
 *
 * @author Jeremy Moore
 */
final class ActivatorTaskThread extends Thread {
    
    private static final Logger LOG = Logger.getLogger("tr.activation"); // No I18N
    private static final String NAME = NbBundle.getMessage(ActivatorTaskThread.class, "activation.task"); // No I18N
    private Data data;
    private Date endOfToday;
    
    /** Constructs a new instance. */
    public ActivatorTaskThread() {
        super(NAME);
    }
    
    /** Run the background task. */
    public void run() {
        LOG.info("Started."); // No I18N
        
        process();
        
        LOG.info("Finished."); // No I18N
    }
    
    private void process() {
        data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data instance could not be obtained."); // No I18N
            return;
        }
        
        endOfToday = DateUtils.getEnd(Calendar.getInstance().getTime());
        
        activateFutureItems();
        activateFutureProjects();
        activateInactiveActions();
    }
    
    /* Reprocess any future items where the tickle date has passed. */
    private void activateFutureItems() {
        for (Future future : data.getFutureManager().list()) {
            Date tickle = future.getTickle();
            if (tickle != null && !tickle.after(endOfToday)) {
                Action action = new Action(data);
                action.setDescription(future.getDescription());
                action.setNotes(future.getNotes());
                Thought thought = new Thought(data.getNextID());
                thought.setAction(action);
//              thought.setDescription(future.getDescription());
                String date = Constants.DATE_FORMAT_FIXED.format(future.getTickle());
                String desc = NbBundle.getMessage(getClass(), "tickled", date, future.getDescription()); // No I18N
                thought.setDescription(desc);
                thought.setTopic(future.getTopic());
                thought.setNotes(future.getNotes());
                data.getThoughtManager().insert(thought, 0);
                data.getFutureManager().remove(future);
            }
        }
    }
    
    /* Activate any future projects where the start date has passed. */
    private void activateFutureProjects() {
        for (Iterator<Project> i = data.getRootFutures().iterator(Project.class); i.hasNext();) {
            Project futureProject = i.next();
            Date startDate = futureProject.getStartDate();
            if (startDate != null && endOfToday.after(startDate)) {
                futureProject.removeFromParent();
                data.getRootProjects().add(futureProject);
            }
        }
    }
    
    /* Activate any inactive actions where the start date has passed. */
    private void activateInactiveActions() {
        // process single actions
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext();) {
            process(i.next());
        }
        // process top level projects
        for (Iterator<Project> i = data.getRootProjects().iterator(Project.class); i.hasNext();) {
            process(i.next());
        }
    }
    
    /* Process a given project. */
    private void process(Project project) {
        for (Item item : project.getChildren()) {
            if (item instanceof Project prj) {
                process(prj);
            } else if (item instanceof Action action) {
                process(action);
            }
        }
    }
    
    /* Process a given action. */
    private void process(Action action) {
        ActionState state = action.getState();
        if (state instanceof ActionStateInactive) {
            Date startDate = action.getStartDate();
            if (startDate != null && endOfToday.after(startDate)) {
                action.setState(new ActionStateASAP());
            }
        }
    }
    
}
