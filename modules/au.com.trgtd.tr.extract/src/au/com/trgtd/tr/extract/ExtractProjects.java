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
package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.appl.Constants;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import au.com.trgtd.tr.extract.Extract.FormatType;
import au.com.trgtd.tr.extract.criteria.ValueIDsProviderEnergy;
import au.com.trgtd.tr.extract.criteria.ValueIDsProviderPriority;
import au.com.trgtd.tr.extract.criteria.ValueIDsProviderTime;
import tr.model.Data;
import tr.model.action.Action;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.thought.Thought;

/**
 * Extract projects data as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractProjects {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static final DateFormat DFN = Constants.DATE_FORMAT_FIXED;
    private static final DateFormat DFT = Constants.DATE_TIME_FORMAT_FIXED;
    private static List<Integer> timeIDs;
    private static List<Integer> energyIDs;
    private static List<Integer> priorityIDs;

    /**
     * Extract ThinkingRock projects to an XML file.
     * @param data The data.
     * @param file The extract file.
     */
    public static void process(Data data, File xmlfile) {
        timeIDs = ValueIDsProviderTime.instance.getIDs();
        energyIDs = ValueIDsProviderEnergy.instance.getIDs();
        priorityIDs = ValueIDsProviderPriority.instance.getIDs();
        try {
            Writer out = initialise(xmlfile);
            process(data, out);
            finalise(out);
        } catch (Exception ex) {
            LOG.severe("Extracting data failed: " + ex.getMessage());
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
    public static void process(Data data, Writer out) {
        try {
            LOG.info("Extracting projects ... ");
            processData(data, out);
            LOG.info("Extracting projects ... done");
        } catch (Exception ex) {
            LOG.severe("Extracting projects failed: " + ex.getMessage());
        }

    }

    private static void processData(Data data, Writer out) throws Exception {
        out.write("<projects>\r\n");
        processProjects(data, out);
        out.write("</projects>\r\n");
    }

    private static void processProjects(Data data, Writer out) throws Exception {
        for (Project project : data.getRootProjects().getChildren(Project.class)) {
            extractProject(project, out);
        }
    }

    /* Extract a project (and its children) to XML. */
    public static void extractProject(Project project, Writer out) throws Exception {
        out.write("<project>\r\n");
        out.write("<created>" + DFN.format(project.getCreated()) + "</created>\r\n");
        Thought thought = project.getThought();
        if (thought != null) {
            out.write("<thought>" + escape(thought.getDescription()) + "</thought>\r\n");
        }
        out.write("<topic>" + escape(project.getTopic().getName()) + "</topic>\r\n");
        out.write("<desc>" + escape(project.getDescription()) + "</desc>\r\n");
        out.write("<purpose>" + escape(project.getPurpose()) + "</purpose>\r\n");
        out.write("<success>" + escape(project.getVision()) + "</success>\r\n");
        out.write("<brainstorming>" + escape(project.getBrainstorming()) + "</brainstorming>\r\n");
        out.write("<organising>" + escape(project.getOrganising()) + "</organising>\r\n");
        out.write("<notes>" + escape(project.getNotes().trim()) + "</notes>\r\n");
        out.write("<done>" + project.isDone() + "</done>\r\n");
        if (project.getDoneDate() != null) {
            out.write("<done_date>" + DFN.format(project.getDoneDate()) + "</done_date>\r\n");
        }

        Value priority = project.getPriority();
        if (priority == null) {
            out.write("<priority></priority>\r\n");
            out.write("<priority-idx>" + Integer.MAX_VALUE + "</priority-idx>\r\n");
        } else {
            out.write("<priority>" + escape(priority.getName()) + "</priority>\r\n");
            out.write("<priority-idx>" + ValueIDsProviderPriority.instance.getIDs().indexOf(priority.getID()) + "</priority-idx>\r\n");
        }
        
        Date startDate = project.getStartDate();
        out.write("<start-date-idx>" + (startDate == null ? Long.MAX_VALUE : startDate.getTime()) + "</start-date-idx>\r\n");
        out.write("<start-date>" + (startDate == null ? "" : DFN.format(startDate)) + "</start-date>\r\n");

        Date dueDate = project.getDueDate();
        out.write("<due-date-idx>" + (dueDate == null ? Long.MAX_VALUE : dueDate.getTime()) + "</due-date-idx>\r\n");
        out.write("<due-date>" + (dueDate == null ? "" : DFN.format(dueDate)) + "</due-date>\r\n");

        for (Action action : project.getChildren(Action.class)) {
            ExtractActions.extractAction(action, out);
        }
        for (Project subproject : project.getChildren(Project.class)) {
            extractProject(subproject, out);
        }

        out.write("</project>\r\n");
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
