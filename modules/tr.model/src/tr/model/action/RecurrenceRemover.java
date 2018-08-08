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
package tr.model.action;

import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.project.Project;
import au.com.trgtd.tr.util.DateUtils;

/**
 * Static methods for removing recurrence definitions from actions.
 *
 * @author Jeremy Moore
 */
public class RecurrenceRemover extends Thread {

    private static final Logger LOG = Logger.getLogger("tr.model.action");

    private RecurrenceRemover() {
    }

    /**
     * Removes a specific recurrence definition from all actions.
     * @param recurrenceID The ID of the recurrence to remove.
     * @param data The data model.
     */
    public static void remove(Data data, int recurrenceID) {
        new RemoverThread(data, recurrenceID).start();
    }

    /**
     * Removes a specific recurrence definition from all actions before a given
     * date.
     * @param recurrenceID The ID of the recurrence to remove.
     * @param data The data model.
     * @param beforeDate The upper limit date.
     */
    public static void remove(Data data, int recurrenceID, Date beforeDate) {
        if (beforeDate == null) {
            throw new IllegalArgumentException("Null date.");
        }
        new RemoverThread(data, recurrenceID, beforeDate).start();
    }

    /**
     * Removes all recurrence definitions from all actions in separate thread.
     * @param data The data model.
     */
    public static void removeAll(Data data) {
        new RemoverThread(data).start();
    }

    private static class RemoverThread extends Thread {

        private final Data data;
        private final boolean removeAll;
        private final int recurrenceID;
        private final Date beforeDate;

        public RemoverThread(Data data) {
            this.data = data;
            this.removeAll = true;
            this.recurrenceID = 0;
            this.beforeDate = DateUtils.MAX_DATE;
        }

        public RemoverThread(Data data, int recurrenceID) {
            this.data = data;
            this.recurrenceID = recurrenceID;
            this.removeAll = false;
            this.beforeDate = DateUtils.MAX_DATE;
        }

        public RemoverThread(Data data, int recurrenceID, Date beforeDate) {
            this.data = data;
            this.recurrenceID = recurrenceID;
            this.removeAll = false;
            this.beforeDate = (beforeDate == null ? DateUtils.MAX_DATE : beforeDate);
        }

        @Override
        public void run() {
            if (data == null) {
                LOG.severe("Data instance was not provided.");
                return;
            }
            process(data.getRootActions());
            process(data.getRootProjects());
        }

        private void process(Project project) {
            for (Iterator<Action> i = project.iterator(Action.class); i.hasNext();) {
                process(i.next());
            }
            for (Iterator<Project> i = project.iterator(Project.class); i.hasNext();) {
                process(i.next());
            }
        }

        private void process(Action action) {
            if (!action.isStateScheduled()) {
                return;
            }
            ActionStateScheduled state = (ActionStateScheduled) action.getState();
            if (state == null) {
                return;
            }
            Recurrence recurrence = state.getRecurrence();
            if (recurrence == null) {
                return;
            }
            if (beforeDate == null) {
                return;
            }
            Date date = state.getDate(); 
            if (date != null && !date.before(beforeDate)) {
                return;
            }
            if (removeAll || recurrence.getID() == recurrenceID) {
                state.setRecurrence(null);
            }
        }
    }
}
