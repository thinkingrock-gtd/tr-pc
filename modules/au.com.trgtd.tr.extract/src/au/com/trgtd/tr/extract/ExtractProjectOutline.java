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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.extract.Extract.FormatType;
import java.util.logging.Level;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.criteria.Value;
import tr.model.project.Project;

/**
 * Extract projects outline data as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractProjectOutline {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final DateFormat DFN = Constants.DATE_FORMAT_FIXED;
    private static final DateFormat DFT = Constants.DATE_TIME_FORMAT_FIXED;
    private static int index;
    private static boolean topDoneAll;
    private static boolean topDoneValue;
    private static boolean subDoneAll;
    private static boolean subDoneValue;
    private static boolean topTopicAll;
    private static String topTopicValue;
    private static boolean actions;

    /**
     * Extract ThinkingRock projects to an XML file.
     * @param xmlfile The extract file.
     * @param topTopic The topic name or "all" for top level projects.
     * @param topDone The done state ("all", "done" or "todo") for top level 
     * projects. 
     * @param subDone The done state ("all", "done" or "todo") for sub-projects
     * and actions. 
     * @param projects The project extract.
     * @param includeActions Whether or not to include Actions.
     * @param sort The sort type.
     */
    public static void process(File xmlfile, List<Project> projects, String topTopic,
            String topDone, String subDone, boolean includeActions, String sort) {
        
        index = 0;
        topTopicAll = topTopic.equals("all");
        topTopicValue = topTopic;
        topDoneAll = topDone.equals("all");
        topDoneValue = topDone.equals("done");
        subDoneAll = subDone.equals("all");
        subDoneValue = subDone.equals("done");
        actions = includeActions;

        if (sort != null) {
            switch (sort) {
                case "descr":
                    Collections.sort(projects, descrComparator);
                    break;
                case "priority":
                    Collections.sort(projects, priorityComparator);
                    break;
                case "date":                
                    Collections.sort(projects, dueDateComparator);
                    break;
                case "topic":
                    Collections.sort(projects, topicComparator);
                    break;
            }
        }
        
        try {
            Writer out = initialise(xmlfile);
            process(out, projects);
            finalise(out);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Extracting data failed: {0}", ex.getMessage());
        }
    }
    
    private static final Comparator<Project> descrComparator = (Project p1, Project p2) ->
            p1.getDescription().compareToIgnoreCase(p2.getDescription());
    
    private static final Comparator<Project> priorityComparator = (Project p1, Project p2) -> {
        String s1 = ExtractUtils.getPriorityIndex(p1.getPriority());
        String s2 = ExtractUtils.getPriorityIndex(p2.getPriority());
        int r = compareWithNulls(s1, s2, false);
        if (r == 0) {
            return p1.getDescription().compareToIgnoreCase(p2.getDescription());
        }
        return r;
    };
    private static final Comparator<Project> dueDateComparator = (Project p1, Project p2) -> {
        int r = compareWithNulls(p1.getDueDate(), p2.getDueDate(), false);
        if (r == 0) {
            return p1.getDescription().compareToIgnoreCase(p2.getDescription());
        }
        return r;
    };
    
    private static final Comparator<Project> topicComparator = (Project p1, Project p2) -> {
        int r = compareWithNulls(p1.getTopic(), p2.getTopic(), false);
        if (r == 0) {
            return p1.getDescription().compareToIgnoreCase(p2.getDescription());
        }
        return r;
    };    

    public static int compareWithNulls(Comparable c1, Comparable c2, boolean isNullFirst) {
        if (c1 == c2) {
            return 0;
        }
        if (c1 == null) {
            return isNullFirst ? -1 : 1;
        }
        if (c2 == null) {
            return isNullFirst ? 1 : -1;
        }
        return c1.compareTo(c2);
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
        out.write("<data>\r\n");
        return out;
    }

    /* Finalise the output XML file stream, etc. */
    private static void finalise(Writer out) throws Exception {
        out.write("</data>\r\n");
        out.flush();
        out.close();
    }

    /**
     * Extract ThinkingRock actions using a writer.
     * @param data The data.
     * @param out The writer.
     */
    private static void process(Writer out, List<Project> projects) throws Exception {
        LOG.info("Extracting projects ... ");
        for (Project project : projects) {
            extractProject(project, out, 0);
        }
        LOG.info("Extracting projects ... done");
    }

    private static boolean filtered(Project project, int level) {
        if (level == 0) {
            if (!topTopicAll && !topTopicValue.equals(project.getTopic().getName())) {
                return true;
            }
            if (!topDoneAll && topDoneValue != project.isDone()) {
                return true;
            }
        } else { // (level != 0)
            if (!subDoneAll && subDoneValue != project.isDone()) {
                return true;
            }
        }
        return false;
    }

    private static boolean filtered(Action action) {
        if (!actions) {
            return true;
        }
        return !subDoneAll && subDoneValue != action.isDone();
    }

    /* Extract a project (and its children) to XML. */
    public static void extractProject(Project project, Writer out, int level) throws Exception {
        if (filtered(project, level)) {
            return;
        }
        out.write("<item>\r\n");
        out.write("<index>" + index++ + "</index>\r\n");
        out.write("<level>" + level + "</level>\r\n");
        out.write("<type>P</type>\r\n");
        out.write("<indent>" + getIndent(level) + "</indent>\r\n");
        out.write("<check>" + (project.isDone() ? "\u2611" : "\u2610") + "</check>\r\n");
        out.write("<descr>" + escape(project.getDescription()) + "</descr>\r\n");
        out.write("<start-date>" + (project.getStartDate() == null ? "" : " " + NbBundle.getMessage(ExtractProjectOutline.class, "Start") + ": " + DFN.format(project.getStartDate())) + "</start-date>\r\n");
        out.write("<due-date>" + (project.getDueDate() == null ? "" : " " + NbBundle.getMessage(ExtractProjectOutline.class, "Due") + ": " + DFN.format(project.getDueDate())) + "</due-date>\r\n");
        out.write("<priority>" + getPriority(project.getPriority()) + "</priority>\r\n");
        out.write("<notes>" + escape(project.getNotes().trim()) + "</notes>\r\n");
        out.write("<done>" + project.isDone() + "</done>\r\n");
        out.write("<topic>" + escape(project.getTopic().getName()) + "</topic>\r\n");
        out.write("</item>\r\n");

        for (Item item : project.getChildren()) {
            if (item instanceof Action action) {
                extractAction(action, out, level + 1);
            } else if (item instanceof Project project1) {
                extractProject(project1, out, level + 1);
            }
        }

        out.write("<item>\r\n");
        out.write("<index>" + index++ + "</index>\r\n");
        out.write("<type>B</type>\r\n");
        out.write("<indent> </indent>\r\n");
        out.write("</item>\r\n");

    }

    public static void extractAction(Action action, Writer out, int level) throws Exception {
        if (filtered(action)) {
            return;
        }
        out.write("<item>\r\n");
        out.write("<index>" + index++ + "</index>\r\n");
        out.write("<level>" + level + "</level>\r\n");
        out.write("<type>A</type>\r\n");
        out.write("<indent>" + getIndent(level) + "</indent>\r\n");
        out.write("<check>" + (action.isDone() ? "\u2611" : "\u2610") + "</check>\r\n");
        out.write("<descr>" + escape(action.getDescription()) + "</descr>\r\n");
        out.write("<state>" + getState(action) + "</state>\r\n");
        out.write("<notes>" + escape(action.getNotes().trim()) + "</notes>\r\n");
        out.write("<done>" + action.isDone() + "</done>\r\n");
        out.write("</item>\r\n");
    }

    private static synchronized String getState(Action a) {
        StringBuilder sb = new StringBuilder();
        if (a.isStateASAP()) {
            sb.append("\u2605");
            if (a.getDueDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectOutline.class, "Due"))
                        .append(": ").append(DFN.format(a.getDueDate()));
        } else if (a.isStateDelegated()) {
            ActionStateDelegated s = (ActionStateDelegated) a.getState();
            sb.append("\u261E");
            if (s.getTo() != null)
                sb.append(" ").append(escape(s.getTo()));
            if (s.getDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectOutline.class, "Followup_Abbrev"))
                        .append(": ").append(DFN.format(s.getDate()));
             if (a.getDueDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectOutline.class, "Due"))
                        .append(": ").append(DFN.format(a.getDueDate()));
        } else if (a.isStateScheduled()) {
            ActionStateScheduled s = (ActionStateScheduled) a.getState();
            sb.append(s.getRecurrence() == null ? "\u2637" : "\u27F3");
            Date d = s.getDate();
            if (d != null) {
                sb.append(" ");
                sb.append(hasTime(d) ? DFT.format(s.getDate()) : DFN.format(s.getDate()));
            }
            if (s.getDurationHours() > 0 || s.getDurationMinutes() > 0) {
                sb.append(" ");
                if (s.getDurationHours() > 0) {
                    sb.append(s.getDurationHours()).append("h");
                }
                if (s.getDurationMinutes() > 0) {
                    sb.append(s.getDurationMinutes()).append("m");
                }
            }
        } else if (a.isStateInactive()) {
            sb.append("\u2606");
            if (a.getStartDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectOutline.class, "Start"))
                        .append(": ").append(DFN.format(a.getStartDate()));
            if (a.getDueDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectOutline.class, "Due"))
                        .append(": ").append(DFN.format(a.getDueDate()));
        }
        return sb.length() == 0 ? "" : sb.toString();
    }

    private static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR)   > 0 ||
               calendar.get(Calendar.MINUTE) > 0 ||
               calendar.get(Calendar.SECOND) > 0;
    }

    private static String getPriority(Value priority) {
        return priority == null ? "" : "\u2690" + escape(priority.getName());
    }

    private static String getIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    /*
     * Escapes the characters in a String using XML entities.  For example:
     * "bread" & "butter" => &quot;bread&quot; &amp; &quot;butter&quot;.
     * @param string The string.
     * @return The escaped string.
     */
    private static String escape(String string) {
        return FormatType.XML.escape(string);
    }
}
