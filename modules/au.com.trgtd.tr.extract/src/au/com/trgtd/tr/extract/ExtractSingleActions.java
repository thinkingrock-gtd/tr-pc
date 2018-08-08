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

import java.io.File;
import java.io.Writer;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.action.Action;

/**
 * Extract single actions data as XML.
 *
 * @author Jeremy Moore
 */
public class ExtractSingleActions {

    private static final Logger LOG = Logger.getLogger("tr.extract");
    private static int index;
    private static boolean doneAll;
    private static boolean doneValue;
    private static boolean topicAll;
    private static String topicValue;
    private static boolean contextAll;
    private static String contextValue;

    /**
     * Extract to an XML file.
     * @param file The extract file.
     * @param data The data model.
     * @param topic The topic name or "all".
     * @param context The context name or "all".
     * @param done The done state ("all", "done" or "todo"). 
     */
    public static void process(File xmlfile, Data data, String topic, String context, String done) {
        index = 0;
        topicAll = topic.equals("all");
        topicValue = topic;
        contextAll = context.equals("all");
        contextValue = context;
        doneAll = done.equals("all");
        doneValue = done.equals("done");
        try {
            Writer out = ExtractUtils.initialise(xmlfile);
            process(out, data);
            ExtractUtils.finalise(out);
        } catch (Exception ex) {
            LOG.severe("Extracting data failed: " + ex.getMessage());
        }
    }

    /**
     * Extract ThinkingRock actions using a writer.
     * @param data The data.
     * @param out The writer.
     */
    private static void process(Writer out, Data data) throws Exception {
        LOG.info("Extracting single actions ... ");
        for (Action action : data.getRootActions().getChildren(Action.class)) {
            extract(action, out);
        }
        LOG.info("Extracting single actions ... done");
    }

    private static void extract(Action action, Writer out) throws Exception {
        if (filtered(action)) {
            return;
        }
        out.write("<action>\r\n");
        out.write("<index>" + index++ + "</index>\r\n");
        out.write("<id>" + action.getID() + "</id>\r\n");
        out.write("<created>" + ExtractUtils.DFN.format(action.getCreated()) + "</created>\r\n");
        out.write("<thought>" + (action.getThought() == null ? "" : ExtractUtils.escape(action.getThought().getDescription())) + "</thought>\r\n");
        out.write("<check>" + (action.isDone() ? "\u2611" : "\u2610") + "</check>\r\n");
        out.write("<descr>" + ExtractUtils.escape(action.getDescription()) + "</descr>\r\n");
        out.write("<topic>" + ExtractUtils.getTopic(action) + "</topic>\r\n");
        out.write("<context>" + ExtractUtils.getContext(action) + "</context>\r\n");
        out.write("<state>" + ExtractUtils.getState(action) + "</state>\r\n");
        out.write("<notes>" + ExtractUtils.escape(action.getNotes().trim()) + "</notes>\r\n");
        out.write("<done>" + action.isDone() + "</done>\r\n");
        out.write("<success>" + ExtractUtils.escape(action.getSuccess()) + "</success>\r\n");
        out.write("<done_date>" + (action.getDoneDate() == null ? "" : ExtractUtils.DFN.format(action.getDoneDate())) + "</done_date>\r\n");
        out.write("<done-date-idx>" + (action.getDoneDate() == null ? Long.MAX_VALUE : action.getDoneDate().getTime()) + "</done-date-idx>\r\n");
        out.write("<start_date>" + (action.getStartDate() == null ? "" : ExtractUtils.DFN.format(action.getStartDate())) + "</start_date>\r\n");
        out.write("<start-date-idx>" + (action.getStartDate() == null ? Long.MAX_VALUE : action.getStartDate().getTime()) + "</start-date-idx>\r\n");
        out.write("<due_date>" + (action.getDueDate() == null ? "" : ExtractUtils.DFN.format(action.getDueDate())) + "</due_date>\r\n");
        out.write("<due-date-idx>" + (action.getDueDate() == null ? Long.MAX_VALUE : action.getDueDate().getTime()) + "</due-date-idx>\r\n");
        out.write("<action-date>" + ExtractUtils.getActionDate(action) + "</action-date>\r\n");
        out.write("<action-date-idx>" + ExtractUtils.getActionDateIndex(action) + "</action-date-idx>\r\n");
        out.write("<time>" + ExtractUtils.getTime(action.getTime()) + "</time>\r\n");
        out.write("<time-idx>" + ExtractUtils.getTimeIndex(action.getTime()) + "</time-idx>\r\n");
        out.write("<energy>" + ExtractUtils.getEnergy(action.getEnergy()) + "</energy>\r\n");
        out.write("<energy-idx>" + ExtractUtils.getEnergyIndex(action.getEnergy()) + "</energy-idx>\r\n");
        out.write("<priority>" + ExtractUtils.getPriority(action.getPriority()) + "</priority>\r\n");
        out.write("<priority-idx>" + ExtractUtils.getPriorityIndex(action.getPriority()) + "</priority-idx>\r\n");
        out.write("<criteria>" + ExtractUtils.getCriteria(action) + "</criteria>\r\n");
        out.write("</action>\r\n");
    }

    private static boolean filtered(Action action) {
        if (!doneAll && doneValue != action.isDone()) {
            return true;
        }
        if (!topicAll && !topicValue.equals(action.getTopic().getName())) {
            return true;
        }        
        if (!contextAll && !contextValue.equals(ExtractUtils.getContext(action))) {
            return true;
        }
        return false;
    }
}
