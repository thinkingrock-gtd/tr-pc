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
package au.com.trgtd.tr.calendar.ical4j.impl;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.calendar.ical4j.ICal4JWrapper;
import au.com.trgtd.tr.calendar.ical4j.impl.prefs.Options;
import au.com.trgtd.tr.calendar.ical4j.impl.prefs.OptionsPanel;
import au.com.trgtd.tr.calendar.prefs.CalendarPrefs;
import au.com.trgtd.tr.calendar.spi.CalendarSynchronizer;
import au.com.trgtd.tr.calendar.spi.CalendarSynchronizerOptions;
import java.io.File;
import java.util.Date;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.project.Project;
import tr.model.thought.Thought;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.util.UtilsFile;

/**
 * Generate ICalendar files for the data model.
 *
 * @author Jeremy Moore
 */
public class ICalendarSynchronizer implements CalendarSynchronizer {

    private static final String UID_SUFFIX = "@trgtd.com.au";
    private static final String DELEGATED_TO = NbBundle.getMessage(ICalendarSynchronizer.class, "Delegated_to");
    private static final String FILENAME = "ThinkingRock";
    private static final String FILEEXTN = "ics";
    private static final String PROJECT_FILENAME = "Projects";
    private static final String FUTURE_FILENAME = "FutureProjects";
    private static ICal4JWrapper actionICal;
    private static ICal4JWrapper projectICal;
    private static ICal4JWrapper futureICal;
    private static File actionFile;
    private static File projectFile;
    private static File futureFile;
    private static CalendarSynchronizerOptions options;

    @Override
    public String getID() {
        return "au.com.trgtd.tr.calendar.synchronizer.default";
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "synchronizer.name");
    }

    @Override
    public void syncToCalendar() {
        syncToCalendar(null);
    }

    @Override
    public void syncToCalendar(String filename) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        File dir = new File(Options.getICalendarPath());
        if (!dir.isDirectory()) {
            System.out.println("Calendar output folder does not exist: " + Options.getICalendarPath());
            return;
        }
        if (filename == null) {
            filename = FILENAME + "." + FILEEXTN;
        }
        process(data, dir, filename, Options.getTimeZoneID());
    }

    @Override
    public void syncFromCalendar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CalendarSynchronizerOptions getOptions() {
        if (options == null) {
            options = new OptionsPanel();
        }
        return options;
    }

    /**
     * Generate an iCalendar for the data and file path.
     * @param data The data model.
     * @param path The output file path (including filename).
     */
    private static void process(Data data, File dir, String filename, String tzid) {
        try {
            System.out.print("Generating ICalendar files ... ");
            initialise(dir, filename, tzid);
            process(data);
            finalise();
            System.out.println("done");
        } catch (Exception ex) {
            System.out.println();
            ex.printStackTrace(System.err);
        }
    }

    /* Initialise the output file. */
    private static void initialise(File folder, String filename, String tzid) throws Exception {

        actionFile = new File(folder, filename);
        actionICal = new ICal4JWrapper(tzid);

        String name = UtilsFile.removeExtension(filename);
        if (name == null || name.trim().length() == 0) {
            name = FILENAME;
        }
        String extn = UtilsFile.getExtension(filename);
        if (extn == null || extn.trim().length() == 0) {
            extn = FILEEXTN;
        }
        projectFile = null;
        projectICal = null;
        if (CalendarPrefs.isSyncProjects()) {
            if (CalendarPrefs.isSyncProjectsSeparate()) {
                projectFile = new File(folder, name + PROJECT_FILENAME + "." + extn);
                projectICal = new ICal4JWrapper(tzid);
            } else {
                projectFile = actionFile;
                projectICal = actionICal;
            }
        }
        futureFile = null;
        futureICal = null;
        if (CalendarPrefs.isSyncFutureProjects()) {
            if (CalendarPrefs.isSyncFutureProjectsSeparate()) {
                futureFile = new File(folder, name + FUTURE_FILENAME + "." + extn);
                futureICal = new ICal4JWrapper(tzid);
            } else {
                futureFile = actionFile;
                futureICal = actionICal;
            }
        }
    }

    /* Finalise the output file. */
    private static void finalise() throws Exception {
        actionICal.write(actionFile);
        if (projectFile != null) {
            projectICal.write(projectFile);
        }
        if (futureFile != null) {
            futureICal.write(futureFile);
        }
    }

    // Process the data.
    private static void process(Data data) {

        // process top level projects
        for (Project project : data.getRootProjects().getChildren(Project.class)) {
            process(project, projectICal, actionICal);
        }

        // process single actions
        for (Action action : data.getRootActions().getChildren(Action.class)) {
            process(action, actionICal);
        }

        // process future projects if specified in user preferences
        if (CalendarPrefs.isSyncFutureProjects()) {
            for (Project project : data.getRootFutures().getChildren(Project.class)) {
                process(project, futureICal, null);
            }
        }
    }

    /* process a project (recursively). */
    private static void process(Project project, ICal4JWrapper iCalProject, ICal4JWrapper iCalAction) {
        if (project.isDone()) {
            return;
        }

        if (iCalProject != null) {
            Date dueDate = DateUtils.clearTime(project.getDueDate());
            if (dueDate != null) {
                String uid = getUID(project.getID());
                String descr = project.getDescription();
                Integer priority = getPriority(project);
                Date startDate = DateUtils.clearTime(project.getStartDate());
                StringBuilder sb = new StringBuilder();
                if (startDate != null) {
                    sb.append("Start: ").append(Constants.DATE_FORMAT_FIXED.format(startDate)).append("\r\n");
                }
                String notes = sb.toString() + getNotes(project);
                iCalProject.createAllDayEvent(uid, dueDate, descr, notes, null, priority);
            }
        }

        if (iCalAction != null) {
            for (Item child : project.getChildren()) {
                if (child instanceof Action action) {
                    process(action, iCalAction);
                } else if (child instanceof Project prj) {
                    process(prj, iCalProject, iCalAction);
                }
            }
        }
    }

    /* Process an action. */
    private static void process(Action action, ICal4JWrapper ical) {
        if (action.isDone()) {
            return;
        }
        switch (action.getState().getType()) {
            case INACTIVE: {
                processInactive(action, ical);
                break;
            }
            case DOASAP: {
                processDoASAP(action, ical);
                break;
            }
            case DELEGATED: {
                processDelegated(action, ical);
                break;
            }
            case SCHEDULED: {
                processScheduled(action, ical);
                break;
            }
        }
    }

    private static void processInactive(Action action, ICal4JWrapper ical) {
        if (!CalendarPrefs.isSyncInactive()) {
            return;
        }
        Date actionDate = DateUtils.clearTime(action.getActionDate());
        if (actionDate == null) {
            return;
        }
        String uid = getUID(action.getID());
        String descr = action.getDescription();
        String context = getContext(action);
        Integer priority = getPriority(action);
        Date startDate = DateUtils.clearTime(action.getStartDate());
        Date dueDate = DateUtils.clearTime(action.getDueDate());

        if (CalendarPrefs.isInactiveAsTodo()) {
            ical.createToDo(uid, startDate, dueDate, descr, getNotes(action), context, priority);
        } else {
            StringBuilder sb = new StringBuilder();
            if (startDate != null) {
                sb.append("Start: ").append(Constants.DATE_FORMAT_FIXED.format(startDate)).append("\r\n");
            }
            if (dueDate != null) {
                sb.append("Due: ").append(Constants.DATE_FORMAT_FIXED.format(dueDate)).append("\r\n");
            }
            String notes = sb.toString() + getNotes(action);
            ical.createAllDayEvent(uid, actionDate, descr, notes, context, priority);
        }
    }

    private static void processDoASAP(Action action, ICal4JWrapper ical) {
        String uid = getUID(action.getID());
        String descr = action.getDescription();
        String context = getContext(action);
        Integer priority = getPriority(action);
        String notes = getNotes(action);
        Date start = DateUtils.clearTime(action.getStartDate());
        Date due = DateUtils.clearTime(action.getDueDate());

        if (due == null) {
            if (CalendarPrefs.isSyncDoasapNoDueDate()) {
                ical.createToDo(uid, start, due, descr, notes, context, priority);
            }
        } else {
            if (CalendarPrefs.isSyncDoasapDueDate()) {
                if (CalendarPrefs.isDoasapDueAsTodo()) {
                    ical.createToDo(uid, start, due, descr, notes, context, priority);
                } else {
                    ical.createAllDayEvent(uid, due, descr, notes, context, priority);
                }
            }
        }
    }

    private static void processDelegated(Action action, ICal4JWrapper ical) {

        Date actionDate = action.getActionDate();
        if (actionDate == null) {
            if (!CalendarPrefs.isSyncDelegatedNoDate()) {
                return;
            }
            // export as To Do with no date
            ActionStateDelegated state = (ActionStateDelegated) action.getState();
            String uid = getUID(action.getID());
            String descr = action.getDescription() + "  " + DELEGATED_TO + ": " + state.getTo();
            String context = getContext(action);
            Integer priority = getPriority(action);
            String notes = getNotes(action);
            ical.createToDo(uid, null, null, descr, notes, context, priority);
        } else { // (actionDate != null)
            if (!CalendarPrefs.isSyncDelegated()) {
                return;
            }
            ActionStateDelegated state = (ActionStateDelegated) action.getState();
            String uid = getUID(action.getID());
            String descr = action.getDescription() + "  " + DELEGATED_TO + ": " + state.getTo();
            String context = getContext(action);
            Integer priority = getPriority(action);
            String notes = getNotes(action);

            if (CalendarPrefs.isDelegatedAsTodo()) {
                ical.createToDo(uid, actionDate, actionDate, descr, notes, context, priority);
            } else {
                ical.createAllDayEvent(uid, actionDate, descr, notes, context, priority);
            }
        }
    }

    private static void processScheduled(Action action, ICal4JWrapper ical) {
        if (!CalendarPrefs.isSyncScheduled()) {
            return;
        }
        ActionStateScheduled state = (ActionStateScheduled) action.getState();
        Date scheduled = state.getDate();
        if (scheduled == null) {
            return;
        }
        int hours = state.getDurationHours();
        int mins = state.getDurationMinutes();
        // if no duration, default to 15 minutes
        if (hours < 1 && mins < 1) {
            hours = 0;
            mins = 15;
        }
        String uid = getUID(action.getID());
        String descr = action.getDescription();
        String context = getContext(action);
        Integer priority = getPriority(action);
        String notes = getNotes(action);

        if (DateUtils.hasTime(scheduled)) {
            ical.createSpecificTimeEvent(uid, scheduled, descr, notes, context, hours, mins, priority);
        } else {
            ical.createAllDayEvent(uid, scheduled, descr, notes, context, priority);
        }
    }

    private static String getUID(int id) {
        return id + UID_SUFFIX;
    }

    private static String getContext(Action action) {
        return action.getContext().getName();
    }

    private static Integer getPriority(Action action) {
        return action.getPriority() == null ? null : action.getPriority().getMappedValue();
    }

    private static Integer getPriority(Project project) {
        return project.getPriority() == null ? null : project.getPriority().getMappedValue();
    }

    private static String getNotes(Action action) {
        StringBuilder notes = new StringBuilder();
        notes.append(getContext(action));
        if (action.isSingleAction()) {
            Thought thought = action.getThought();
            if (thought != null) {
                notes.append(" {").append(thought.getDescription()).append("}");
            }
        } else {
            Project project = (Project) action.getParent();
            if (project != null) {
                notes.append(" [").append(project.getDescription()).append("]");
            }
        }
        notes.append("\r\n");
        notes.append(action.getNotes());
        return notes.toString();
    }

    private static String getNotes(Project project) {
        StringBuilder notes = new StringBuilder();
        Project parent = (Project) project.getParent();
        if (parent != null) {
            notes.append("[").append(parent.getDescription()).append("]\r\n");
        }
        notes.append(project.getNotes());
        return notes.toString();
    }
}
