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
import java.util.List;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.criteria.ValueIDsProviderEnergy;
import au.com.trgtd.tr.extract.criteria.ValueIDsProviderPriority;
import au.com.trgtd.tr.extract.criteria.ValueIDsProviderTime;
import java.util.logging.Level;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateInactive;
import tr.model.action.ActionStateScheduled;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.thought.Thought;

/**
 * Extract an action as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractAction {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final String TEXT_NONE = NbBundle.getMessage(ExtractAction.class, "none");
    private static final String TEXT_NOBODY = NbBundle.getMessage(ExtractAction.class, "nobody");
    private static final DateFormat DFN = Constants.DATE_FORMAT_FIXED;
    private static final DateFormat DFT = Constants.DATE_TIME_FORMAT_FIXED;
    private static List<Integer> timeIDs;
    private static List<Integer> energyIDs;
    private static List<Integer> priorityIDs;
    

    /**
     * Extract ThinkingRock action to an XML file.
     * @param data The data.
     * @param file The extract file.
     */
    public static void process(Action action, File xmlfile) {
        timeIDs = ValueIDsProviderTime.instance.getIDs();
        energyIDs = ValueIDsProviderEnergy.instance.getIDs();
        priorityIDs = ValueIDsProviderPriority.instance.getIDs();
        try {
            Writer out = initialise(xmlfile);
            process(action, out);
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
     * Extract ThinkingRock actions using a writer.
     * @param data The data.
     * @param out The writer.
     */
    public static void process(Action action, Writer out) {
        try {
            LOG.info("Extracting action ... ");
            processData(action, out);
            LOG.info("Extracting action ... done");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Extracting action failed: {0}", ex.getMessage());
        }

    }

    private static void processData(Action action, Writer out) throws Exception {
        out.write("<actions>\r\n");
        extractAction(action, out);
        out.write("</actions>\r\n");
    }

    public static void extractAction(Action action, Writer out) throws Exception {
        out.write("<action>\r\n");

        switch (action.getState().getType()) {
            case DOASAP: {
                out.write("<symbol>\u2605</symbol>\r\n");
                break;
            }
            case DELEGATED: {
                out.write("<symbol>\u261E</symbol>\r\n");
                break;
            }
            case SCHEDULED: {
                if (action.getRecurrence() == null) {
                    out.write("<symbol>\u2637</symbol>\r\n");
                } else {
                    out.write("<symbol>\u27F3</symbol>\r\n");
                }
                break;
            }
            case INACTIVE: {
                out.write("<symbol>\u2606</symbol>\r\n");
                break;
            }
        }

        out.write("<created>" + DFN.format(action.getCreated()) + "</created>\r\n");
        Thought thought = action.getThought();
        if (thought != null) {
            out.write("<thought>" + escape(action.getThought().getDescription()) + "</thought>\r\n");
        }
        Project parent = (Project) action.getParent();
        if (parent == null || parent.isRoot()) {
            out.write("<project></project>\r\n");
            out.write("<project-path></project-path>\r\n");
        } else {
            out.write("<project>" + escape(parent.getDescription()) + "</project>\r\n");
            out.write("<project-path>" + escape(getProjectPath(parent, "")) + "</project-path>\r\n");
        }
        out.write("<descr>" + escape(action.getDescription()) + "</descr>\r\n");
        out.write("<success>" + escape(action.getSuccess()) + "</success>\r\n");
        out.write("<topic>" + escape(action.getTopic().getName()) + "</topic>\r\n");
                
        String contextName = action.getContext().getName().trim();
        if (!contextName.startsWith("@")) {
            contextName = "@" + contextName;
        }        
        out.write("<context>" + escape(contextName) + "</context>\r\n");

        Value time = action.getTime();
        if (time == null) {
//          out.write("<time>None</time>\r\n");
            out.write("<time>" + TEXT_NONE + "</time>\r\n");
            out.write("<time-idx>" + Integer.MAX_VALUE + "</time-idx>\r\n");
        } else {
            out.write("<time>" + escape(time.getName()) + "</time>\r\n");
            out.write("<time-idx>" + ValueIDsProviderTime.instance.getIDs().indexOf(time.getID()) + "</time-idx>\r\n");
        }
        Value energy = action.getEnergy();
        if (energy == null) {
//          out.write("<energy>None</energy>\r\n");
            out.write("<energy>" + TEXT_NONE + "</energy>\r\n");
            out.write("<energy-idx>" + Integer.MAX_VALUE + "</energy-idx>\r\n");
        } else {
            out.write("<energy>" + escape(energy.getName()) + "</energy>\r\n");
            out.write("<energy-idx>" + ValueIDsProviderEnergy.instance.getIDs().indexOf(energy.getID()) + "</energy-idx>\r\n");
        }
        Value priority = action.getPriority();
        if (priority == null) {
//          out.write("<priority>None</priority>\r\n");
            out.write("<priority>" + TEXT_NONE + "</priority>\r\n");
            out.write("<priority-idx>" + Integer.MAX_VALUE + "</priority-idx>\r\n");
        } else {
            out.write("<priority>" + escape(priority.getName()) + "</priority>\r\n");
            out.write("<priority-idx>" + ValueIDsProviderPriority.instance.getIDs().indexOf(priority.getID()) + "</priority-idx>\r\n");
        }

        Date actionDate = action.getActionDate();
        out.write("<action-date-idx>" + (actionDate == null ? Long.MAX_VALUE : actionDate.getTime()) + "</action-date-idx>\r\n");
        if (action.isStateScheduled()) {
            out.write("<action-date>" + (actionDate == null ? "" : DFT.format(actionDate)) + "</action-date>\r\n");
        } else {
            out.write("<action-date>" + (actionDate == null ? "" : DFN.format(actionDate)) + "</action-date>\r\n");
        }

        Date dueDate = action.getDueDate();
        out.write("<due-date-idx>" + (dueDate == null ? Long.MAX_VALUE : dueDate.getTime()) + "</due-date-idx>\r\n");
        out.write("<due-date>" + (dueDate == null ? "" : DFN.format(dueDate)) + "</due-date>\r\n");

        extractActionState(action, out);

        out.write("<notes>" + escape(action.getNotes().trim()) + "</notes>\r\n");
        out.write("<done>" + action.isDone() + "</done>\r\n");

        Date doneDate = action.getDoneDate();
        if (doneDate != null) {
            out.write("<done_date>" + DFN.format(doneDate) + "</done_date>\r\n");
            out.write("<done-date-idx>" + (doneDate == null ? Long.MAX_VALUE : doneDate.getTime()) + "</done-date-idx>\r\n");
        }

        out.write("</action>\r\n");
    }

    private static void extractActionState(Action action, Writer out) throws Exception {

        ActionState state = action.getState();

        if (state instanceof ActionStateScheduled s) {
            out.write("<state>");
            out.write("<type>SCHEDULED</type>");
            out.write("<created>" + DFN.format(s.getCreated()) + "</created>");
            if (hasTime(s.getDate())) {
                out.write("<schd-date>" + (s.getDate() == null ? "" : DFT.format(s.getDate())) + "</schd-date>");
            } else {
                out.write("<schd-date>" + (s.getDate() == null ? "" : DFN.format(s.getDate())) + "</schd-date>");
            }
            out.write("<schd-date-idx>" + (s.getDate() == null ? Long.MAX_VALUE : s.getDate().getTime()) + "</schd-date-idx>");
            if (s.getDurationHours() > 0 || s.getDurationMinutes() > 0) {
                StringBuilder sb = new StringBuilder();
                if (s.getDurationHours() > 0) {
                    sb.append(s.getDurationHours()).append("h");
                }
                if (s.getDurationMinutes() > 0) {
                    sb.append(s.getDurationMinutes()).append("m");
                }
                out.write("<duration>" + sb.toString() + "</duration>\r\n");
            }
            out.write("</state>\r\n");
        } else {
            if (state instanceof ActionStateASAP s) {
                out.write("<state>");
                out.write("<type>DOASAP</type>");
                out.write("<created>" + DFN.format(s.getCreated()) + "</created>");
                out.write("</state>\r\n");
            } else if (state instanceof ActionStateDelegated s) {
                out.write("<state>");
                out.write("<type>DELEGATED</type>");
                out.write("<created>" + DFN.format(s.getCreated()) + "</created>");                
//              String to = s.getTo() == null || s.getTo().trim().length() == 0 ? "Nobody" : s.getTo();
                String to = s.getTo() == null || s.getTo().trim().length() == 0 ? TEXT_NOBODY : s.getTo();
                out.write("<to>" + escape(to) + "</to>");
                out.write("<followup-date>" + (s.getDate() == null ? "" : DFN.format(s.getDate())) + "</followup-date>");
                out.write("<followup-date-idx>" + (s.getDate() == null ? Long.MAX_VALUE : s.getDate().getTime()) + "</followup-date-idx>");
                out.write("</state>\r\n");
            } else if (state instanceof ActionStateInactive s) {
                out.write("<state>");
                out.write("<type>INACTIVE</type>");
                out.write("<created>" + DFN.format(s.getCreated()) + "</created>");
                out.write("</state>\r\n");
            }
            Date startDate = action.getStartDate();
            out.write("<start-date>" + (startDate == null ? "" : DFN.format(startDate)) + "</start-date>\r\n");
            Date dueDate = action.getDueDate();
            out.write("<due-date>" + (dueDate == null ? "" : DFN.format(dueDate)) + "</due-date>\r\n");
        }
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

    private static String getProjectPath(Project project, String path) {
        if (path.length() > 0) {
            path = project.getDescription() + "/" + path;
        } else {
            path = project.getDescription();
        }
        Object object = project.getParent();
        if (object instanceof Project parent) {
            if (!parent.isRoot()) {
                return getProjectPath(parent, path);
            }
        }
        return path;
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
