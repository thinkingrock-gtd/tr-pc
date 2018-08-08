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
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data instance could not be obtained.");
            return;
        }
        
        done = new Vector<Integer>();
        
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
//      LOG.info("Processing action: " + action.getID() + " >>> " + action.getDescription());

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
