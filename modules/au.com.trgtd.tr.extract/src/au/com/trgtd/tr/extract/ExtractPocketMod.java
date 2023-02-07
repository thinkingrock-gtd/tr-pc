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
package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.appl.Constants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.extract.Extract.FormatType;
import tr.model.Data;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.context.Context;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import au.com.trgtd.tr.util.DateUtils;
import java.util.logging.Level;

/**
 * Extract data as XML for PocketMod report.
 *
 * @author Jeremy Moore
 */
public class ExtractPocketMod {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final DateFormat DFN = Constants.DATE_FORMAT_FIXED;
    private static final DateFormat DFT = Constants.DATE_TIME_FORMAT_FIXED;
    private static final DateFormat DFTime = new SimpleDateFormat("HH:mm");
    private static final DateFormat DFDay = new SimpleDateFormat("EEE");
    private static final DateFormat DFDayTime = new SimpleDateFormat("EEE HH:mm");
    private static final int PAGES = 8;
    private static final int LINES_PER_PAGE = 32;
    private static final String PAGE_ID_DoASAPDue = "doasap-due";
    private static final String PAGE_ID_DoASAPNoDue = "doasap-no-due";
    private static final String PAGE_ID_DoASAPAll = "doasap-all";
    private static final String PAGE_ID_Today = "today";
    private static final String PAGE_ID_Scheduled = "scheduled";
    private static final String PAGE_ID_Delegated = "delegated";
    private static final String PAGE_ID_ThisWeek = "this-week";
    private static final String PAGE_ID_Overdue = "overdue";
    private static final String PAGE_ID_NewThoughts = "new-thoughts";
    private static final String PAGE_ID_NewThoughtsSpaced = "new-thoughts-spaced";
    private static final String PAGE_ID_Blank = "blank";
    private static final String PAGE_ID_Thoughts = "thoughts";
    private static final String CHECKBOX = "\u2610";
    private static Data data;
    private static Date todayStart;
    private static Date todayEnd;
    private static Date weekEnd;
    private static boolean inclCriteria;
    private static boolean inclProject;    
    
    private static class Page {

        public final String[] lines = new String[LINES_PER_PAGE];
    }

    /**
     * Extract ThinkingRock actions to an XML file.
     * @param pData The data.
     * @param file The extract file.
     * @param IDs of pages to print
     */
    public static void process(Data data, File xmlfile, String[] pageIDs, 
            Date today, boolean includeCriteria, boolean includeProject) 
    {
        inclCriteria = includeCriteria;
        inclProject = includeProject;
        try {
            Writer out = initialise(xmlfile);
            process(data, out, pageIDs, today);
            finalise(out);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Extracting data failed: {0}", ex.getMessage());
        }
    }

    /* Initialise the output XML file stream, etc. */
    private static Writer initialise(File xmlfile) throws Exception {
        if (xmlfile.exists()) {
            xmlfile.delete();
        }
        OutputStream fout = new FileOutputStream(xmlfile);
        OutputStream bout = new BufferedOutputStream(fout);
        OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        out.write("<pages>\r\n");
        return out;
    }

    /* Finalise the output XML file stream, etc. */
    private static void finalise(Writer out) throws Exception {
        out.write("</pages>\r\n");
        out.flush();
        out.close();
    }

    /**
     * Extract ThinkingRock actions using action writer.
     * @param data The data.
     * @param out The writer.
     */
    public static void process(Data pData, Writer out, String[] pageIDs, Date today) {
        if (pData == null) {
            return;
        }
        data = pData;
        todayStart = DateUtils.getStart(today == null ? new Date() : today);
        todayEnd = DateUtils.getEnd(todayStart);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayEnd);
        calendar.add(Calendar.DAY_OF_YEAR, 6);
        weekEnd = calendar.getTime();
        try {
            LOG.info("Extracting data for PocketMod report ... ");
            processData(out, pageIDs, today);
            LOG.info("Extracting completed");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            LOG.log(Level.SEVERE, "Extracting failed: {0}", ex.getMessage());
        }
    }

    private static void processData(Writer out, String[] pageIDs, Date today) throws Exception {
        Page[] pages = new Page[PAGES];

        Vector<String> linesDoASAPDue = null;
        Vector<String> linesDoASAPNoDue = null;
        Vector<String> linesDoASAPAll = null;
        Vector<String> linesToday = null;
        Vector<String> linesScheduled = null;
        Vector<String> linesDelegated = null;
        Vector<String> linesThisWeek = null;
        Vector<String> linesOverdue = null;
        Vector<String> linesNewThoughts = null;
        Vector<String> linesNewThoughtsSpaced = null;
        Vector<String> linesBlank = null;
        Vector<String> linesThoughts = null;

        for (int i = 0; i < PAGES; i++) {

            pages[i] = new Page();

            if (pageIDs[i].startsWith(PAGE_ID_DoASAPDue)) {
                if (linesDoASAPDue == null) {
                    linesDoASAPDue = new Vector<>();
                    populateLinesDoASAPDue(linesDoASAPDue);
                }
                String title = org.openide.util.NbBundle.getMessage(ExtractPocketMod.class, "Do_ASAP_Due") + " " + getWeekText(true);
                populatePage(linesDoASAPDue, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_DoASAPNoDue)) {
                if (linesDoASAPNoDue == null) {
                    linesDoASAPNoDue = new Vector<>();
                    populateLinesDoASAPNoDue(linesDoASAPNoDue);
                }
                String title = org.openide.util.NbBundle.getMessage(ExtractPocketMod.class, "Do_ASAP_No_Due_Date");
                populatePage(linesDoASAPNoDue, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_DoASAPAll)) {
                if (linesDoASAPAll == null) {
                    linesDoASAPAll = new Vector<>();
                    populateLinesDoASAPAll(linesDoASAPAll);
                }
                String title = org.openide.util.NbBundle.getMessage(ExtractPocketMod.class, "Do_ASAP_ALL");
                populatePage(linesDoASAPAll, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_Today)) {
                if (linesToday == null) {
                    linesToday = new Vector<>();
                    populateLinesToday(linesToday);
                }
                String title = org.openide.util.NbBundle.getMessage(ExtractPocketMod.class, "Today") + " " + DFN.format(todayStart);
                populatePage(linesToday, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_Scheduled)) {
                if (linesScheduled == null) {
                    linesScheduled = new Vector<>();
                    populateLinesScheduled(linesScheduled);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "Scheduled") + " " + getWeekText(true);
                populatePage(linesScheduled, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_Delegated)) {
                if (linesDelegated == null) {
                    linesDelegated = new Vector<>();
                    populateLinesDelegated(linesDelegated);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "Delegated") + " " + getWeekText(true);
                populatePage(linesDelegated, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_ThisWeek)) {
                if (linesThisWeek == null) {
                    linesThisWeek = new Vector<>();
                    populateLinesThisWeek(linesThisWeek);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "This_Week") + " " + getWeekText(false);
                populatePage(linesThisWeek, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].startsWith(PAGE_ID_Overdue)) {
                if (linesOverdue == null) {
                    linesOverdue = new Vector<>();
                    populateLinesOverdue(linesOverdue);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "Overdue");
                populatePage(linesOverdue, pages[i], getPageNumber(pageIDs[i]), title, true);
            } else if (pageIDs[i].equals(PAGE_ID_NewThoughts)) {
                if (linesNewThoughts == null) {
                    linesNewThoughts = new Vector<>();
                    populateLinesNewThoughts(linesNewThoughts);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "New_Thoughts");
                populatePage(linesNewThoughts, pages[i], 1, title, false);
            } else if (pageIDs[i].equals(PAGE_ID_NewThoughtsSpaced)) {
                if (linesNewThoughtsSpaced == null) {
                    linesNewThoughtsSpaced = new Vector<>();
                    populateLinesNewThoughtsSpaced(linesNewThoughtsSpaced);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "New_Thoughts_(spaced)");
                populatePage(linesNewThoughtsSpaced, pages[i], 1, title, false);
            } else if (pageIDs[i].equals(PAGE_ID_Blank)) {
                if (linesBlank == null) {
                    linesBlank = new Vector<>();
                    populateLinesBlank(linesBlank);
                }
                populatePage(linesBlank, pages[i], 1, null, false);
            } else if (pageIDs[i].startsWith(PAGE_ID_Thoughts)) {
                if (linesThoughts == null) {
                    linesThoughts = new Vector<>();
                    populateLinesThoughts(linesThoughts);
                }
                String title = NbBundle.getMessage(ExtractPocketMod.class, "Thoughts");
                populatePage(linesThoughts, pages[i], getPageNumber(pageIDs[i]), title, true);
            }
        }

        // write XML output 
        for (int pageIndex = 0; pageIndex < PAGES; pageIndex++) {
            Page page = pages[pageIndex];
            for (int lineIndex = 0; lineIndex < LINES_PER_PAGE; lineIndex++) {
                String tag = "p" + (pageIndex + 1) + "l" + (lineIndex + 1);
                out.write("<" + tag + ">" + page.lines[lineIndex] + "</" + tag + ">\r\n");
            }
        }
    }

    private static String getWeekText(boolean includeToday) {
        if (includeToday) {
            return DFN.format(todayStart) + " - " + DFN.format(weekEnd);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 1);
            return DFN.format(cal.getTime()) + " - " + DFN.format(weekEnd);
        }
    }

    private static int getPageNumber(String pageID) {
        if (pageID == null) {
            return 1;
        }
        int lastDashIndex = pageID.lastIndexOf("-");
        if (lastDashIndex == -1) {
            return 1;
        }
        try {
            return Integer.parseInt(pageID.substring(lastDashIndex + 1));
        } catch (Exception ex) {
            return 1;
        }
    }

    // DoASAP, with due date this week, order by due-date, include context
    private static void populateLinesDoASAPDue(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterDoASAPDue.instance);
        processSingleActions(actions, ActionFilterDoASAPDue.instance);
        Collections.sort(actions, DueDateComparator.instance);
        for (Action a : actions) {
//            lines.add(checkbox() + dueDate(action) + descr(action) + context(action) + topic(action));
            lines.add(checkbox() + ExtractUtils.getState(a, DFDay, DFDayTime) + " " + descr(a) + context(a) + topic(a));
            addOptionalLines(lines, a);        
        }
    }

    // DoASAP, without due date, grouped by context
    private static void populateLinesDoASAPNoDue(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterDoASAPNoDue.instance);
        processSingleActions(actions, ActionFilterDoASAPNoDue.instance);
        Collections.sort(actions, ContextComparator.instance);
        Context contextGroup = null;
        for (Action a : actions) {
            if (contextGroup != a.getContext()) {
                contextGroup = a.getContext();
                lines.add(i(contextGroup.getName()));
            }
            lines.add(checkbox() + descr(a) + topic(a));
            addOptionalLines(lines, a);        
        }
    }
    // DoASAP, all, grouped by context
    private static void populateLinesDoASAPAll(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterDoASAP.instance);
        processSingleActions(actions, ActionFilterDoASAP.instance);
        Collections.sort(actions, ContextComparator.instance);
        Context contextGroup = null;
        for (Action a : actions) {
            if (contextGroup != a.getContext()) {
                contextGroup = a.getContext();
                lines.add(i(contextGroup.getName()));
            }
            lines.add(checkbox() + dueDate(a) + descr(a) + topic(a));
            addOptionalLines(lines, a);        }
    }

    // Actions scheduled this week
    private static void populateLinesScheduled(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterScheduled.instance);
        processSingleActions(actions, ActionFilterScheduled.instance);
        Collections.sort(actions, ScheduledComparator.instance);
        for (Action action : actions) {
//            lines.add(checkbox() + scheduledDate(action) + descr(action) + context(action) + topic(action));
            lines.add(checkbox() + ExtractUtils.getState(action, DFDay, DFDayTime) + " " + descr(action) + context(action) + topic(action));
            addOptionalLines(lines, action);            
        }
    }

    // Actions delegated this week (order by Delegated-to, follow-up date) show followup date
    private static void populateLinesDelegated(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterDelegated.instance);
        processSingleActions(actions, ActionFilterDelegated.instance);
        Collections.sort(actions, DelegatedComparator.instance);
        for (Action action : actions) {
//            lines.add(checkbox() + followUpDate(action) + dueDateWithLabel(action) + descr(action) + context(action) + topic(action));
            lines.add(checkbox() + ExtractUtils.getState(action, DFDay, DFDayTime) + " " + descr(action) + context(action) + topic(action));
            addOptionalLines(lines, action);
        }
    }

    private static void addOptionalLines(Vector<String> lines, Action action) {
        if (inclProject) {
            Project parent = (Project) action.getParent();
            if (parent == null || parent.isRoot()) {
                String thought = action.getThought() == null ? "" : action.getThought().getDescription().trim();
                if (thought.length() > 0) {
                    lines.add("   {" + thought + "}");                   
               }
            } else {
                lines.add("   [" + ExtractUtils.getProjectPath(parent, "") + "]");
            }
        }        
        if (inclCriteria) {
            String criteria = ExtractUtils.getCriteria(action).trim();
            if (criteria.length() > 0) {
                lines.add("    " + criteria);                
            }
        }        
    }
    
    /* Today: 
     * - list Scheduled linesToday (order time), DoAsap due linesToday (order desc),
     *   Delegated followup linesToday (order Delegated-to)
     */
    private static void populateLinesToday(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterToday.instance);
        processSingleActions(actions, ActionFilterToday.instance);
        Collections.sort(actions, TodayComparator.instance);
        for (Action a : actions) {
            if (a.isStateScheduled()) {
//              lines.add(checkbox() + actionDate(action, true) + descr(action) + context(action) + topic(action));  
                ActionStateScheduled s = (ActionStateScheduled) a.getState();
                String duration = ExtractUtils.getDuration(s);
                if (!duration.equals("")) {
                    duration = " " + duration;
                }
                String time = s.getDate() == null ? "" : " " + DFTime.format(s.getDate());
                lines.add(checkbox() + ExtractUtils.getSymbol(a) + " " + time + duration + " " + descr(a) + context(a) + topic(a));
            } else if (a.isStateDelegated()) {
                ActionStateDelegated s = (ActionStateDelegated) a.getState();
                String to = s.getTo() == null || s.getTo().trim().length() == 0 ? "" : " " + s.getTo().trim();
                lines.add(checkbox() + ExtractUtils.getSymbol(a) + escape(to) + " " + descr(a) + context(a) + topic(a));
            } else {
                lines.add(checkbox() + ExtractUtils.getSymbol(a) + " " + descr(a) + context(a) + topic(a));
            }
            addOptionalLines(lines, a);                    
        }
    }

    // All Actions this week: (order by action-date) show symbol - include inactive [show due date]
    private static void populateLinesThisWeek(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterThisWeek.instance);
        processSingleActions(actions, ActionFilterThisWeek.instance);
        Collections.sort(actions, ActionDateComparator.instance);
        for (Action a : actions) {
//          lines.add(checkbox() + actionDate(action) + descr(action) + context(action) + topic(action));
            lines.add(checkbox() + ExtractUtils.getState(a, DFDay, DFDayTime) + " " + descr(a) + context(a) + topic(a));
            addOptionalLines(lines, a);                    
        }
    }

    // All Actions linesOverdue: (include Scheduled)
    private static void populateLinesOverdue(Vector<String> lines) {
        Vector<Action> actions = new Vector<>();
        processProjects(actions, ActionFilterOverdue.instance);
        processSingleActions(actions, ActionFilterOverdue.instance);
        Collections.sort(actions, ActionDateComparator.instance);
        for (Action a : actions) {
//          lines.add(checkbox() + actionDate(action) + descr(action) + context(action) + topic(action));
            lines.add(checkbox() + ExtractUtils.getState(a) + " " + descr(a) + context(a) + topic(a));
            addOptionalLines(lines, a);                    
        }
    }

    private static void populateLinesNewThoughts(Vector<String> lines) {
        for (int i = 0; i < LINES_PER_PAGE; i++) {
            lines.add(checkbox() + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
        }
    }

    private static void populateLinesNewThoughtsSpaced(Vector<String> lines) {
        for (int i = 0; i < LINES_PER_PAGE; i++) {
            if (i % 2 == 0) {
                lines.add(" ");
            } else {
                lines.add(checkbox() + "_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _");
            }
        }
    }

    private static void populateLinesBlank(Vector<String> lines) {
        for (int i = 0; i < LINES_PER_PAGE; i++) {
            lines.add("");
        }
    }
    // Thoughts
    private static void populateLinesThoughts(Vector<String> lines) {
        Vector<Thought> thoughts = new Vector<>();
        processThoughts(thoughts);
        for (Thought thought : thoughts) {
            lines.add(checkbox() + " " + escape(thought.getDescription()) + " " + topic(thought.getTopic()));
        }
    }

    private interface ActionFilter {

        public abstract boolean accept(Action action);
    }

    // Do ASAP actions
    private static class ActionFilterDoASAP implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterDoASAP();

        public boolean accept(Action a) {
            if (a == null || a.isDone() || !a.isStateASAP()) {
                return false;
            }
            return true;
        }
    }

    // Do ASAP actions with no due date
    private static class ActionFilterDoASAPNoDue implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterDoASAPNoDue();

        public boolean accept(Action a) {
            if (a == null || a.isDone() || !a.isStateASAP()) {
                return false;
            }
            return a.getDueDate() == null;
        }
    }

    // Do ASAP actions due this week
    private static class ActionFilterDoASAPDue implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterDoASAPDue();

        public boolean accept(Action a) {
            if (a == null || a.isDone() || !a.isStateASAP()) {
                return false;
            }
            Date due = a.getDueDate();
            return due != null && !due.before(todayStart) && !due.after(weekEnd);
        }
    }

    // actions scheduled this week
    private static class ActionFilterScheduled implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterScheduled();

        public boolean accept(Action a) {
            if (a == null || a.isDone() || !a.isStateScheduled()) {
                return false;
            }
            Date date = ((ActionStateScheduled) a.getState()).getDate();
            return date != null && !date.before(todayStart) && !date.after(weekEnd);
        }
    }

    // actions delegated with followup this week
    private static class ActionFilterDelegated implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterDelegated();

        public boolean accept(Action a) {
            if (a == null || a.isDone() || !a.isStateDelegated()) {
                return false;
            }
            Date date = ((ActionStateDelegated) a.getState()).getDate();
            return date != null && !date.before(todayStart) && !date.after(weekEnd);
        }
    }
    // Actions overdue
    private static class ActionFilterOverdue implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterOverdue();

        public boolean accept(Action a) {
            if (a == null || a.isDone()) {
                return false;
            }
            return a.getActionDate() != null && a.getActionDate().before(todayStart);
        }
    }
    // Actions overdue
    private static class ActionFilterThisWeek implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterThisWeek();

        public boolean accept(Action a) {
            if (a == null || a.isDone()) {
                return false;
            }
            return a.getActionDate() != null && !a.getActionDate().before(todayEnd) && !a.getActionDate().after(weekEnd);
        }
    }

    // Actions today
    private static class ActionFilterToday implements ActionFilter {

        public static final ActionFilter instance = new ActionFilterToday();

        public boolean accept(Action a) {
            if (a == null || a.isDone()) {
                return false;
            }
            return a.getActionDate() != null && !a.getActionDate().before(todayStart) && !a.getActionDate().after(todayEnd);
        }
    }

    private static class ContextComparator implements Comparator<Action> {

        public static final Comparator<Action> instance = new ContextComparator();

        public int compare(Action a1, Action a2) {
            int r = a1.getContext().compareTo(a2.getContext());
            if (r != 0) {
                return r;
            }
            r = compareWithNulls(DateUtils.clearTime(a1.getActionDate()), DateUtils.clearTime(a2.getActionDate()));
            if (r != 0) {
                return r;
            }
            return a1.getDescription().compareToIgnoreCase(a2.getDescription());
        }
    }

    private static class DueDateComparator implements Comparator<Action> {

        public static final Comparator<Action> instance = new DueDateComparator();

        public int compare(Action a1, Action a2) {
            return compareWithNulls(a1.getDueDate(), a2.getDueDate());
        }
    }

    private static class ScheduledComparator implements Comparator<Action> {

        public static final Comparator<Action> instance = new ScheduledComparator();

        public int compare(Action a1, Action a2) {
            Date d1 = ((ActionStateScheduled) a1.getState()).getDate();
            Date d2 = ((ActionStateScheduled) a2.getState()).getDate();
            return compareWithNulls(d1, d2);
        }
    }

    private static class DelegatedComparator implements Comparator<Action> {

        public static final Comparator<Action> instance = new DelegatedComparator();

        public int compare(Action a1, Action a2) {
            Date d1 = ((ActionStateDelegated) a1.getState()).getDate();
            Date d2 = ((ActionStateDelegated) a2.getState()).getDate();
            return compareWithNulls(d1, d2);
        }
    }

    private static class ActionDateComparator implements Comparator<Action> {

        public static final Comparator<Action> instance = new ActionDateComparator();

        public int compare(Action a1, Action a2) {
            return compareWithNulls(a1.getActionDate(), a2.getActionDate());
        }
    }

    private static class TodayComparator implements Comparator<Action> {

        public static final Comparator<Action> instance = new ActionDateComparator();

        public int compare(Action a1, Action a2) {
            if (a1.isStateScheduled() && a2.isStateScheduled()) {
                Date d1 = ((ActionStateScheduled) a1.getState()).getDate();
                Date d2 = ((ActionStateScheduled) a2.getState()).getDate();
                return compareWithNulls(d1, d2);
            }
            if (a1.isStateScheduled()) {
                return -1;
            }
            if (a2.isStateScheduled()) {
                return 1;
            }
            return a1.getDescription().compareToIgnoreCase(a2.getDescription());
        }
    }

    private static int compareWithNulls(Date date1, Date date2) {
        if (date1 == date2) {
            return 0;
        }

        if (date1 == null) {
            return -1;
        }

        if (date2 == null) {
            return 1;
        }

        return date1.compareTo(date2);

    }

    private static void populatePage(Vector<String> lines, Page page, int pageNo, String title, boolean showPageNo) {
        assert (lines != null);
        assert (page != null);
        assert (pageNo > 0);

        // title line
        if (showPageNo) {
            page.lines[0] = (title == null ? "" : escape(b(title + " (" + pageNo + ")")));
        } else {
            page.lines[0] = (title == null ? "" : escape(b(title)));
        }

        // detail lines
        int lineIndex = (pageNo - 1) * (LINES_PER_PAGE - 1);

        for (int i = 1; i < LINES_PER_PAGE; i++) {
            page.lines[i] = (lineIndex < lines.size() ? escape(lines.get(lineIndex)) : "");
            lineIndex++;
        }
    }

    private static void processProjects(List<Action> actions, ActionFilter filter) {
        for (Project project : data.getRootProjects().getChildren(Project.class)) {
            processProject(project, actions, filter);
        }
    }

    private static void processSingleActions(List<Action> actions, ActionFilter filter) {
        processProject(data.getRootActions(), actions, filter);
    }

    private static void processProject(Project project, List<Action> actions, ActionFilter filter) {
        for (Project subproject : project.getChildren(Project.class)) {
            processProject(subproject, actions, filter);
        }
        for (Action action : project.getChildren(Action.class)) {
            if (filter.accept(action)) {
                actions.add(action);
            }
        }
    }

    private static void processThoughts(List<Thought> thoughts) {
        for (Thought thought : data.getThoughtManager().list()) {
            if (!thought.isProcessed()) {
                thoughts.add(thought);
            }
        }
    }

    private static String checkbox() {
        return CHECKBOX + " ";
    }

    private static String actionDate(Action action) {
        return actionDate(action, false);
    }

    private static String actionDate(Action action, boolean includeTime) {
        Date date = action.getActionDate();
        if (includeTime) {
            return date == null ? "" : DFT.format(date) + " ";
        } else {
            return date == null ? "" : DFN.format(date) + " ";
        }
    }

    private static String followUpDate(Action action) {
        if (!action.isStateDelegated()) {
            return "";
        }
        Date date = ((ActionStateDelegated) action.getState()).getDate();
        return date == null ? "" : DFN.format(date) + " ";
    }

    private static String dueDate(Action action) {
        if (action.isStateScheduled()) {
            return "";
        }
        Date date = action.getDueDate();
        return date == null ? "" : DFN.format(date) + " ";
    }

    private static String dueDateWithLabel(Action action) {
        if (action.isStateScheduled()) {
            return "";
        }
        Date date = action.getDueDate();
        return date == null ? "" : NbBundle.getMessage(ExtractPocketMod.class, "Due") + DFN.format(date) + " ";
    }

    private static String scheduledDate(Action action) {
        if (!action.isStateScheduled()) {
            return "";
        }
        Date date = ((ActionStateScheduled) action.getState()).getDate();
        return date == null ? "" : DFN.format(date) + " ";
    }

    private static String duration(Action action) {
        if (!action.isStateScheduled()) {
            return "";
        }
        ActionStateScheduled s = (ActionStateScheduled) action.getState();
        int h = s.getDurationHours();
        int m = s.getDurationMinutes();
        if (h < 1 && m < 1) {
            return "";
        }
        return (h > 0 ? h + "h" : "") + (m > 0 ? m + "m" : "") + " ";
    }

    private static String context(Action action) {
        return action == null ? "" : i(action.getContext().getName()) + " ";
    }

    private static String topic(Action action) {
        return action == null ? "" : topic(action.getTopic());
    }

    private static String topic(Topic topic) {
        return topic == null ? "" : i(u(topic.getName())) + " ";
    }

    private static String descr(Action action) {
        return action == null ? "" : action.getDescription() + " ";
    }

    private static String escape(String string) {
        return FormatType.XML.escape(string);
    }

    private static String i(String string) {
        return "<I>" + string + "</I>";
    }

    private static String b(String string) {
        return "<B>" + string + "</B>";
    }

    private static String u(String string) {
        return "<U>" + string + "</U>";
    }
}
