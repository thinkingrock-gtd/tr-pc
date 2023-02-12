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
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.data.FileFilterImpl;
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.text.StringEscapeUtils;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateInactive;
import tr.model.action.ActionStateScheduled;
import tr.model.action.Period;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodWeek;
import tr.model.action.PeriodYear;
import tr.model.action.Recurrence;
import tr.model.criteria.Value;
import tr.model.project.Project;

/**
 * Export a project hierarchy.
 *
 * @author Jeremy Moore
 */
public class ExportProject {

    private static final Logger LOG = Logger.getLogger("tr.export.project");
    private static final int VERSION_MAJOR = 1;
    private static final int VERSION_MINOR = 0;
    private static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR;
    private static final String EXTN = "trp";
    private static final String EXTN_NAME = NbBundle.getMessage(ExportProject.class, "project.file.extension.name");

    public ExportProject() {
    }

    /**
     * Export a project template to an XML file.
     * @param project the project.
     */
    public void export(Project project) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(NbBundle.getMessage(ExportProject.class, "export.project.file.chooser.title"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilterImpl(EXTN_NAME, new String[]{EXTN}, true);
        chooser.setFileFilter(filter);
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showDialog(p, NbBundle.getMessage(ExportProject.class, "export.project.file.chooser.button"));
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String path = chooser.getSelectedFile().getPath();
        String extn = FileUtil.getExtension(path);
        if (extn == null || extn.length() == 0) {
            path += "." + EXTN;
        }
        File outfile = new File(path);
        if (outfile.exists()) {
            String t = NbBundle.getMessage(ExportProject.class, "export.project.file.chooser.title");
            String m = NbBundle.getMessage(ExportProject.class, "confirm.replace.file");
            int r = JOptionPane.showConfirmDialog(p, m, t, JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) {
                return;
            }
            outfile.delete();
        }

        try {
            Writer writer = initialise(outfile);
            process(project, writer);
            finalise(writer);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Export project failed: {0}", ex.getMessage());
        }
    }

    /* Initialise the output XML file stream, etc. */
    private Writer initialise(File xmlfile) throws Exception {
        if (xmlfile.exists()) {
            xmlfile.delete();
        }
        OutputStream fout = new FileOutputStream(xmlfile);
        OutputStream bout = new BufferedOutputStream(fout);
        OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        out.write("<trproject version=\"" + VERSION + "\">\r\n");
        return out;
    }

    /* Finalise the output XML file stream, etc. */
    private void finalise(Writer out) throws Exception {
        out.write("</trproject>\r\n");
        out.flush();
        out.close();
    }

    private void process(Project project, Writer out) {
        try {
            LOG.info("Export project ... ");
            out.write("<project>\r\n");
            writeProject(project, out);
            out.write("</project>\r\n");
            LOG.info("Export project ... done");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Export project failed: {0}", ex.getMessage());
        }
    }

    /* Extract a project (and its children) to XML. */
    public void writeProject(Project project, Writer out) throws Exception {
        out.write("<id>" + project.getID() + "</id>\r\n");
        out.write("<created>" + project.getCreated().getTime() + "</created>\r\n");
        if (project.getThought() != null) {
            out.write("<thought>" + escape(project.getThought().getDescription()) + "</thought>\r\n");
        }
        if (project.getParent() != null) {
            out.write("<parent-id>" + project.getParent().getID() + "</parent-id>\r\n");
        }
        out.write("<topic>" + escape(project.getTopic().getName()) + "</topic>\r\n");
        out.write("<descr>" + escape(project.getDescription()) + "</descr>\r\n");
        out.write("<purpose>" + escape(project.getPurpose()) + "</purpose>\r\n");
        out.write("<success>" + escape(project.getVision()) + "</success>\r\n");
        out.write("<brainstorming>" + escape(project.getBrainstorming()) + "</brainstorming>\r\n");
        out.write("<organising>" + escape(project.getOrganising()) + "</organising>\r\n");
        out.write("<notes>" + escape(project.getNotes().trim()) + "</notes>\r\n");
        out.write("<done>" + project.isDone() + "</done>\r\n");
        if (project.isDone()) {
            out.write("<done-date>" + project.getDoneDate().getTime() + "</done-date>\r\n");
        }
        if (project.getDueDate() != null) {
            out.write("<due-date>" + project.getDueDate().getTime() + "</due-date>\r\n");
        }
        if (project.getStartDate() != null) {
            out.write("<start-date>" + project.getStartDate().getTime() + "</start-date>\r\n");
        }
        if (project.getPriority() != null) {
            out.write("<priority>" + escape(project.getPriority().getName()) + "</priority>\r\n");
        }
        out.write("<sequence>" + project.isSequencing() + "</sequence>\r\n");
        out.write("<seq-projects>" + project.isSeqIncludeProjects() + "</seq-projects>\r\n");
        out.write("<seq-scheduled>" + project.isSeqIncludeScheduled() + "</seq-scheduled>\r\n");
        out.write("<seq-delegated>" + project.isSeqIncludeDelegated() + "</seq-delegated>\r\n");

        // children;
        for (Item child : project.getChildren()) {
            if (child instanceof Project prj) {
                out.write("<child type='project'>\r\n");
                writeProject(prj, out);
                out.write("</child>\r\n");
            } else if (child instanceof Action action) {
                out.write("<child type='action'>\r\n");
                writeAction(action, out);
                out.write("</child>\r\n");
            }
        }
    }

    public void writeAction(Action action, Writer out) throws Exception {
        out.write("<id>" + action.getID() + "</id>\r\n");
        out.write("<created>" + action.getCreated().getTime() + "</created>\r\n");
        if (action.getThought() != null) {
            out.write("<thought>" + escape(action.getThought().getDescription()) + "</thought>\r\n");
        }
        if (action.getParent() != null) {
            out.write("<parent-id>" + action.getParent().getID() + "</parent-id>\r\n");
        }
        out.write("<descr>" + escape(action.getDescription()) + "</descr>\r\n");
        out.write("<success>" + escape(action.getSuccess()) + "</success>\r\n");
        out.write("<topic>" + escape(action.getTopic().getName()) + "</topic>\r\n");
        out.write("<context>" + escape(action.getContext().getName()) + "</context>\r\n");
        out.write("<notes>" + escape(action.getNotes().trim()) + "</notes>\r\n");
        Value time = action.getTime();
        if (time != null) {
            out.write("<time>" + escape(time.getName()) + "</time>\r\n");
        }
        Value energy = action.getEnergy();
        if (energy != null) {
            out.write("<energy>" + escape(energy.getName()) + "</energy>\r\n");
        }
        Value priority = action.getPriority();
        if (priority != null) {
            out.write("<priority>" + escape(priority.getName()) + "</priority>\r\n");
        }
        out.write("<done>" + action.isDone() + "</done>\r\n");
        if (action.isDone()) {
            out.write("<done-date>" + action.getDoneDate().getTime() + "</done-date>\r\n");
        }
        if (action.getStartDate() != null) {
            out.write("<start-date>" + action.getStartDate().getTime() + "</start-date>\r\n");
        }
        if (action.getDueDate() != null) {
            out.write("<due-date>" + action.getDueDate().getTime() + "</due-date>\r\n");
        }
        // state
        switch (action.getState().getType()) {
            case DELEGATED: {
                writeStateDelegated((ActionStateDelegated) action.getState(), out);
                break;
            }
            case SCHEDULED: {
                writeStateScheduled((ActionStateScheduled) action.getState(), out);
                break;
            }
            case INACTIVE: {
                writeStateInactive((ActionStateInactive) action.getState(), out);
                break;
            }
            case DOASAP: {
                writeStateDoASAP((ActionStateASAP) action.getState(), out);
                break;
            }
            default:
        }
    }

    private void writeStateInactive(ActionStateInactive s, Writer out) throws Exception {
        out.write("<state type='INACTIVE'>");
        out.write("</state>");
    }

    private void writeStateDoASAP(ActionStateASAP s, Writer out) throws Exception {
        out.write("<state type='DOASAP'>");
        out.write("</state>");
    }

    private void writeStateDelegated(ActionStateDelegated s, Writer out) throws Exception {
        out.write("<state type='DELEGATED'>");
        if (s.hasDelegateValue()) {
            out.write("<delegate-id>" + s.getActorID() + "</delegate-id>\r\n");
        }
        out.write("<delegate>" + escape(s.getTo()) + "</delegate>\r\n");
        if (s.getDate() != null) {
            out.write("<followup>" + s.getDate().getTime() + "</followup>\r\n");
        }
        out.write("</state>");
    }

    private void writeStateScheduled(ActionStateScheduled s, Writer out) throws Exception {
        out.write("<state type='SCHEDULED'>");
        if (s.getDate() != null) {
            out.write("<date>" + s.getDate().getTime() + "</date>\r\n");
        }
        out.write("<duration-hrs>" + s.getDurationHours() + "</duration-hrs>\r\n");
        out.write("<duration-mins>" + s.getDurationMinutes() + "</duration-mins>\r\n");
        if (s.getRecurrence() != null) {
            out.write("<recurrence>\r\n");
            writeRecurrence(s.getRecurrence(), out);
            out.write("</recurrence>\r\n");
        }
        out.write("</state>");
    }

    private void writeRecurrence(Recurrence r, Writer out) throws Exception {
        out.write("<id>" + r.getID() + "</id>\r\n");
        out.write("<descr>" + r.getDescription() + "</descr>\r\n");
        out.write("<topic>" + escape(r.getTopic().getName()) + "</topic>\r\n");
        out.write("<context>" + escape(r.getContext().getName()) + "</context>\r\n");
        Value time = r.getTime();
        if (time != null) {
            out.write("<time>" + escape(time.getName()) + "</time>\r\n");
        }
        Value energy = r.getEnergy();
        if (energy != null) {
            out.write("<energy>" + escape(energy.getName()) + "</energy>\r\n");
        }
        Value priority = r.getPriority();
        if (priority != null) {
            out.write("<priority>" + escape(priority.getName()) + "</priority>\r\n");
        }
        out.write("<schedule-hrs>" + r.getScheduleHours() + "</schedule-hrs>\r\n");
        out.write("<schedule-mins>" + r.getScheduleMins() + "</schedule-mins>\r\n");
        out.write("<duration-hrs>" + r.getDurationHours() + "</duration-hrs>\r\n");
        out.write("<duration-mins>" + r.getDurationMins() + "</duration-mins>\r\n");
        if (r.getBasis() != null) {
            out.write("<basis-id>" + r.getBasis().ID + "</basis-id>\r\n");
        }
        if (r.getStartDate() != null) {
            out.write("<start-date>" + r.getStartDate().getTime() + "</start-date>\r\n");
        }
        out.write("<frequency>" + r.getFrequency() + "</frequency>\r\n");
        out.write("<advance-nbr>" + r.getAdvanceNbr() + "</advance-nbr>\r\n");
        out.write("<end-nbr>" + r.getEndNbr() + "</end-nbr>\r\n");
        if (r.getEndDate() != null) {
            out.write("<end-date>" + r.getEndDate().getTime() + "</end-date>\r\n");
        }
        out.write("<gen-nbr>" + r.getGenNbr() + "</gen-nbr>\r\n");
        if (r.getGenToDate() != null) {
            out.write("<gen-to-date>" + r.getGenToDate().getTime() + "</gen-to-date>\r\n");
        }
        out.write("<success>" + r.getSuccess() + "</success>\r\n");
        out.write("<notes>" + r.getNotes() + "</notes>\r\n");

        if (r.getPeriod() != null) {
            out.write("<period type='" + r.getPeriod().getType().ID +"'>\r\n");
            writePeriod(r.getPeriod(), out);
            out.write("</period>\r\n");
        }
        if (r.isCalendarItem() != null) {
            out.write("<is-calendar-item>" + r.isCalendarItem() + "</is-calendar-item>\r\n");
        }
    }

    private void writePeriod(Period p, Writer out) throws Exception {
        switch (p.getType()) {
            case WEEKDAY: {
                return;
            }
            case DAY: {
                return;
            }
            case WEEK: {
                out.write("<days>\r\n");
                boolean first = true;
                for (Integer day : ((PeriodWeek)p).getSelectedDays()) {
                    if (first) {
                        out.write(day.toString());                       
                        first = false;
                    } else {
                        out.write("," + day.toString());
                    }
                }
                out.write("</days>\r\n");
                return;
            }
            case MONTH: {
                PeriodMonth pm = (PeriodMonth)p;
                if (pm.getOption() != null) {
                    out.write("<option>" + pm.getOption().name() + "</option>\r\n");
                }
                out.write("<days>\r\n");
                boolean first = true;
                for (Integer day : pm.getSelectedDays()) {
                    if (first) {
                        out.write(day.toString());
                        first = false;
                    } else {
                        out.write("," + day.toString());
                    }
                }
                out.write("</days>\r\n");
                if (pm.getOnTheNth() != null) {
                    out.write("<on-the-nth>" + pm.getOnTheNth().name() + "</on-the-nth>\r\n");
                }
                if (pm.getOnTheDay() != null) {
                    out.write("<on-the-day>" + pm.getOnTheDay().name() + "</on-the-day>\r\n");
                }
                return;
            }
            case YEAR: {
                PeriodYear py = (PeriodYear)p;
                out.write("<months>\r\n");
                boolean first = true;
                for (Integer month : py.getSelectedMonths()) {
                    if (first) {
                        out.write(month.toString());
                        first = false;
                    } else {
                        out.write("," + month.toString());
                    }
                }
                out.write("</months>\r\n");
                out.write("<is-on-the-selected>" + py.isOnTheSelected() + "</is-on-the-selected>\r\n");
                if (py.getOnTheNth() != null) {
                    out.write("<on-the-nth>" + py.getOnTheNth().name() + "</on-the-nth>\r\n");
                }
                if (py.getOnTheDay() != null) {
                    out.write("<on-the-day>" + py.getOnTheDay().name() + "</on-the-day>\r\n");
                }
                return;
            }
        }
    }

    /*
     * Escapes the characters in a String using XML entities.  For example:
     * "bread" & "butter" => &quot;bread&quot; &amp; &quot;butter&quot;.
     * @param string The string.
     * @return The escaped string.
     */
    private String escape(String string) {
        return StringEscapeUtils.escapeXml10(string);
    }
}
