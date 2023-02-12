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
import java.util.Date;
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
import tr.model.thought.Thought;

/**
 * Extract projects detail data as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractProjectDetail {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final DateFormat DFN = Constants.DATE_FORMAT_FIXED;
    private static final DateFormat DFT = Constants.DATE_TIME_FORMAT_FIXED;
    private static final String TEXT_NONE = NbBundle.getMessage(ExtractAction.class, "none");
    private static int index;
    private static boolean doneAll;
    private static boolean doneValue;
    private static boolean subDoneAll;
    private static boolean subDoneValue;
    private static boolean topTopicAll;
    private static String topTopicValue;
    private static boolean actions;

    /**
     * Extract ThinkingRock projects to an XML file.
     * @param file The extract file.
     * @param done The done state ("all", "done" or "todo"). 
     * @param project The project to extract.
     */
    public static void process(File xmlfile, Project project, String done) {
        index = 0;
        doneAll = done.equals("all");
        doneValue = done.equals("done");
        try {
            Writer out = initialise(xmlfile);
            process(out, project);
            finalise(out);
        } catch (Exception ex) {
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
     * Extract using a writer.
     * @param data The data.
     * @param out The writer.
     */
    private static void process(Writer out, Project project) throws Exception {
        LOG.info("Extracting projects ... ");
        extractProject(project, out, 0);
        LOG.info("Extracting projects ... done");
    }

    private static boolean filtered(Project project) {
        if (!doneAll && doneValue != project.isDone()) {
            return true;
        }
        return false;
    }

    private static boolean filtered(Action action) {
        if (!doneAll && doneValue != action.isDone()) {
            return true;
        }
        return false;
    }

    /* Extract a project (and its children) to XML. */
    public static void extractProject(Project project, Writer out, int level) throws Exception {
        if (filtered(project)) {
            return;
        }
        Thought thought = project.getThought();
        out.write("<item>\r\n");
        out.write("<index>" + index++ + "</index>\r\n");
        out.write("<type>P</type>\r\n");
        out.write("<indent>" + level + "</indent>\r\n");
        out.write("<check>" + (project.isDone() ? "\u2611" : "\u2610") + "</check>\r\n");
        out.write("<descr>" + escape(project.getDescription()) + "</descr>\r\n");
        out.write("<start-date>" + (project.getStartDate() == null ? "" : DFN.format(project.getStartDate())) + "</start-date>\r\n");
        out.write("<due-date>" + (project.getDueDate() == null ? "" : DFN.format(project.getDueDate())) + "</due-date>\r\n");
        out.write("<priority>" + getPriority(project.getPriority()) + "</priority>\r\n");
        out.write("<notes>" + escape(project.getNotes().trim()) + "</notes>\r\n");
//        out.write("<notes>" + escape(ExtractUtils.indent(project.getNotes(), level * 2)) + "</notes>\r\n");
        out.write("<done>" + project.isDone() + "</done>\r\n");
        out.write("<done-date>" + (project.getDoneDate() == null ? "" : DFN.format(project.getDoneDate())) + "</done-date>\r\n");
        out.write("<topic>" + project.getTopic().getName() + "</topic>\r\n");
        out.write("<created>" + DFN.format(project.getCreated()) + "</created>\r\n");
        out.write("<purpose>" + escape(project.getPurpose()) + "</purpose>\r\n");
        out.write("<success>" + escape(project.getVision()) + "</success>\r\n");
        out.write("<brainstorming>" + escape(project.getBrainstorming()) + "</brainstorming>\r\n");
        out.write("<organising>" + escape(project.getOrganising()) + "</organising>\r\n");
        out.write("<thought>" + (thought == null ? "" : escape(thought.getDescription())) + "</thought>\r\n");
        out.write("</item>\r\n");

        for (Item item : project.getChildren()) {
            if (item instanceof Action action) {
                extractAction(action, out, level + 1);
            } else if (item instanceof Project prj) {
                extractProject(prj, out, level + 1);
            }
        }

//        out.write("<item>\r\n");
//        out.write("<index>" + index++ + "</index>\r\n");
//        out.write("<type>B</type>\r\n");
//        out.write("<indent> </indent>\r\n");
//        out.write("</item>\r\n");

    }

    public static void extractAction(Action action, Writer out, int level) throws Exception {
        if (filtered(action)) {
            return;
        }
        Thought thought = action.getThought();

        out.write("<item>\r\n");
        out.write("<index>" + index++ + "</index>\r\n");
        out.write("<type>A</type>\r\n");
        out.write("<indent>" + level + "</indent>\r\n");
        out.write("<check>" + (action.isDone() ? "\u2611" : "\u2610") + "</check>\r\n");
        out.write("<descr>" + escape(action.getDescription()) + "</descr>\r\n");
        out.write("<state>" + getState(action) + "</state>\r\n");
        out.write("<notes>" + escape(action.getNotes().trim()) + "</notes>\r\n");
//        out.write("<notes>" + escape(ExtractUtils.indent(action.getNotes(), level * 2)) + "</notes>\r\n");
        out.write("<done>" + action.isDone() + "</done>\r\n");
        out.write("<created>" + DFN.format(action.getCreated()) + "</created>\r\n");
        out.write("<thought>" + (thought == null ? "" : escape(action.getThought().getDescription())) + "</thought>\r\n");
        out.write("<success>" + escape(action.getSuccess()) + "</success>\r\n");
        out.write("<topic>" + escape(action.getTopic().getName()) + "</topic>\r\n");
        String contextName = action.getContext().getName().trim();
        if (!contextName.startsWith("@")) {
            contextName = "@" + contextName;
        }
        out.write("<context>" + escape(contextName) + "</context>\r\n");
        Value time = action.getTime();
        if (time == null) {
            out.write("<time>" + TEXT_NONE + "</time>\r\n");
        } else {
            out.write("<time>" + escape(time.getName()) + "</time>\r\n");
        }
        Value energy = action.getEnergy();
        if (energy == null) {
            out.write("<energy>" + TEXT_NONE + "</energy>\r\n");
        } else {
            out.write("<energy>" + escape(energy.getName()) + "</energy>\r\n");
        }
        Value priority = action.getPriority();
        if (priority == null) {
            out.write("<priority>" + TEXT_NONE + "</priority>\r\n");
        } else {
            out.write("<priority>" + escape(priority.getName()) + "</priority>\r\n");
        }
        Date actionDate = action.getActionDate();
        if (action.isStateScheduled()) {
            out.write("<action-date>" + (actionDate == null ? "" : DFT.format(actionDate)) + "</action-date>\r\n");
        } else {
            out.write("<action-date>" + (actionDate == null ? "" : DFN.format(actionDate)) + "</action-date>\r\n");
        }
        out.write("<due-date>" + (action.getDueDate() == null ? "" : DFN.format(action.getDueDate())) + "</due-date>\r\n");
        out.write("<done_date>" + (action.getDoneDate() == null ? "" : DFN.format(action.getDoneDate())) + "</done_date>\r\n");

        out.write("</item>\r\n");
    }

    private static synchronized String getState(Action a) {
        StringBuilder sb = new StringBuilder();
        if (a.isStateASAP()) {
            sb.append("\u2605");
            if (a.getDueDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectDetail.class, "Due"))
                        .append(": ").append(DFN.format(a.getDueDate()));
        } else if (a.isStateDelegated()) {
            ActionStateDelegated s = (ActionStateDelegated) a.getState();
            sb.append("\u261E");
            if (s.getTo() != null)
                sb.append(" ").append(escape(s.getTo()));
            if (s.getDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectDetail.class, "Followup_Abbrev"))
                        .append(": ").append(DFN.format(s.getDate()));
            if (a.getDueDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectDetail.class, "Due"))
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
                        .append(NbBundle.getMessage(ExtractProjectDetail.class, "Start"))
                        .append(": ").append(DFN.format(a.getStartDate()));
            if (a.getDueDate() != null)
                sb.append(" ")
                        .append(NbBundle.getMessage(ExtractProjectDetail.class, "Due"))
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
        return calendar.get(Calendar.HOUR) > 0 ||
                calendar.get(Calendar.MINUTE) > 0 ||
                calendar.get(Calendar.SECOND) > 0;
    }

    private static String getPriority(Value priority) {
        return priority == null ? "" : "\u2690" + escape(priority.getName());
    }

//    private static final String getIndent(int level) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < level; i++) {
//            sb.append("  ");
//        }
//        return sb.toString();
//    }

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
