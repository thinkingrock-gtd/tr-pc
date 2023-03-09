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

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import au.com.trgtd.tr.extract.Extract.FormatType;
import java.util.logging.Level;
import tr.model.Data;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateInactive;
import tr.model.action.ActionStateScheduled;
import tr.model.context.Context;
import tr.model.future.Future;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * ExtractData data to an XML file to be used for XSLT processing.
 *
 * @author Jeremy Moore
 */
public class ExtractData {
    
    private static final Logger LOG = Logger.getLogger("tr.extract");    
    private static final DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssEEE dd MMM yyyy HH:mm:ss");
//    /* date format for date sort fields. */
//    private static final DateFormat dfsort = new SimpleDateFormat("yyyyMMddHHmmss");
    
    private static final String KEY_PREFIX_THOUGHT = "T";
    private static final String KEY_PREFIX_PROJECT = "P";
    private static final String KEY_PREFIX_ACTION = "A";
    private static final String KEY_PREFIX_TOPIC = "TPC";
    private static final String KEY_PREFIX_CONTEXT = "CTX";
    
    /* Lists for data extraction. */
    private static Vector<Project> projects;
    private static Vector<Action> actions;
    
    private static File xmlfile;
    private static Data data;
    
    /* Key generator class. */
    private final static class KeyGenerator {
        private int number = 0;
        private final String prefix;
        /**
         * Constructs a new instance with the given prefix.
         * @param prefix The prefix.
         */
        public KeyGenerator(String prefix) {
            this.prefix = prefix;
        }
        /**
         *  Get the next key.
         */
        public String next() {
            return prefix + number++;
        }
        /**
         *  Resets the key generator.
         */
        public void reset() {
            number = 0;
        }
    }
    
    private static final KeyGenerator thoughtKeyGenerator = new KeyGenerator(KEY_PREFIX_THOUGHT);
    private static final KeyGenerator topicKeyGenerator = new KeyGenerator(KEY_PREFIX_TOPIC);
    private static final KeyGenerator contextKeyGenerator = new KeyGenerator(KEY_PREFIX_CONTEXT);
    
    private static OutputStreamWriter out;
    
    private static FormatType formatType;
    
    /**
     * ExtractData ThinkingRock data to an XML file to be used for XSLT processing.
     *
     * @param data The data.
     * @param file The extract file.
     */
    public static void process(Data datamodel, File extractfile, FormatType format) {
        thoughtKeyGenerator.reset();
        topicKeyGenerator.reset();
        contextKeyGenerator.reset();
        
        data = datamodel;
        xmlfile = extractfile;
        formatType = format;
        
        // save datastore data
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds != null) {
            try {
                ds.store();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        
        try {
            LOG.info("Extracting data ... ");
            extract();
            output();
            LOG.info("Extracting data done");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Extracting data failed: {0}", ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    // EXTRACT
    
    /* ExtractData the data. */
    private static void extract() {
        extractProjects();
    }
    
    /* ExtractData projects. */
    private static void extractProjects() {
        projects = new Vector<>();
        actions = new Vector<>();
        
        // extract all top level projects
        for (Iterator<Project> i = data.getRootProjects().iterator(Project.class); i.hasNext();) {
            extractProject(i.next());
        }
    }
    
    /* ExtractData a project (recursively). */
    private static void extractProject(Project project) {
        
        projects.add(project);
        
        // extract project children
        for (Iterator<Item> i = project.iterator(Item.class); i.hasNext();) {
            Item child = i.next();
            if (child instanceof Action action) {
                extractAction(action);
            } else if (child instanceof Project prj) {
                extractProject(prj);
            }
        }
    }
    
    /* ExtractData an action. */
    private static void extractAction(Action action) {
        actions.add(action);
    }
    
    // OUTPUT
    
    /* Output the extracted data to an XML file. */
    private static void output() throws Exception {
        initialise();
        writeTopics();
        writeContexts();
        writeThoughts();
        writeInformationItems();
        writeFutureItems();
        writeSingleActions();
        writeActions();
        writeProjects();
        finalise();
    }
    
    /* Initialise the output XML file stream, etc. */
    private static void initialise() throws Exception {
        
        if (xmlfile.exists()) {
            xmlfile.delete();
        }
        
        OutputStream fout = new FileOutputStream(xmlfile);
        OutputStream bout = new BufferedOutputStream(fout);
        out = new OutputStreamWriter(bout, "UTF-8");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        out.write("<data>\r\n");
        
        Calendar calendar = Calendar.getInstance();
        out.write("<date>" + df.format(calendar.getTime()) + "</date>\r\n");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        out.write("<tomorrow>" + df.format(calendar.getTime()) + "</tomorrow>\r\n");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        out.write("<week>" + df.format(calendar.getTime()) + "</week>\r\n");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        out.write("<two-weeks>" + df.format(calendar.getTime()) + "</two-weeks>\r\n");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 21);
        out.write("<three-weeks>" + df.format(calendar.getTime()) + "</three-weeks>\r\n");
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 28);
        out.write("<four-weeks>" + df.format(calendar.getTime()) + "</four-weeks>\r\n");
    }
    
    /* Finalise the output XML file stream, etc. */
    private static void finalise() throws Exception {
        out.write("</data>\r\n");
        out.flush();
        out.close();
    }
    
    /* Write topics. */
    private static void writeTopics() throws Exception {
        out.write("<topics>\r\n");
        for (Topic topic : data.getTopicManager().list()) {
            topic.key = topicKeyGenerator.next();
            out.write("<topic key=\"" + topic.key + "\">\r\n");
            out.write("<name>" + escape(topic.getName()) + "</name>\r\n");
            out.write("<desc>" + escape(topic.getDescription()) + "</desc>\r\n");
            out.write("<fg>" + topic.getForeground().getRGB() + "</fg>\r\n");
            out.write("<bg>" + topic.getBackground().getRGB() + "</bg>\r\n");
            out.write("</topic>\r\n");
        }
        out.write("</topics>\r\n");
        
        // Set the default topic key to the managed default topic key
        if (!data.getTopicManager().isEmpty()) {
            Topic def = data.getTopicManager().get(0);
            Topic.getDefault().key = def.key;
        }
    }
    
    /* Write contexts. */
    private static void writeContexts() throws Exception {
        out.write("<contexts>\r\n");
        for (Context context : data.getContextManager().list()) {
            context.key = contextKeyGenerator.next();
            out.write("<context key=\"" + context.key + "\">\r\n");
            out.write("<name>" + escape(context.getName()) + "</name>\r\n");
            out.write("<desc>" + escape(context.getDescription()) + "</desc>\r\n");
            out.write("</context>\r\n");
        }
        out.write("</contexts>\r\n");
        
        // Set the default context key to the managed default context key
        if (!data.getContextManager().isEmpty()) {
            Context def = data.getContextManager().get(0);
            Context.getDefault().key = def.key;
        }
    }
    
    /* Write thoughts. */
    private static void writeThoughts() throws Exception {
        out.write("<thoughts>\r\n");
        for (Thought thought : data.getThoughtManager().list()) {
            thought.key = thoughtKeyGenerator.next();
            out.write("<thought key=\"" + thought.key + "\">\r\n");
            out.write("<created>" + df.format(thought.getCreated()) + "</created>\r\n");
            out.write("<desc>" + escape(thought.getDescription()) + "</desc>\r\n");
            out.write("<topic-key>" + thought.getTopic().key + "</topic-key>\r\n");
            out.write("</thought>\r\n");
        }
        out.write("</thoughts>\r\n");
    }
    
    /* Write information items. */
    private static void writeInformationItems() throws Exception {
        out.write("<infos>\r\n");
        for (Information info : data.getInformationManager().list()) {
            out.write("<info>\r\n");
            out.write("<created>" + df.format(info.getCreated()) + "</created>\r\n");
            out.write("<desc>" + escape(info.getDescription()) + "</desc>\r\n");
            out.write("<topic-key>" + info.getTopic().key + "</topic-key>\r\n");
            out.write("<location>" + info.getLocation() + "</location>\r\n");
            out.write("<notes>" + escape(info.getNotes().trim()) + "</notes>\r\n");
            out.write("</info>\r\n");
        }
        out.write("</infos>\r\n");
    }
    
    /* Write future items. */
    private static void writeFutureItems() throws Exception {
        out.write("<futures>\r\n");
        for (Future future : data.getFutureManager().list()) {
            out.write("<future>\r\n");
            out.write("<created>" + df.format(future.getCreated()) + "</created>\r\n");
            out.write("<desc>" + escape(future.getDescription()) + "</desc>\r\n");
            out.write("<notes>" + escape(future.getNotes()) + "</notes>\r\n");
            out.write("<tickle>" + (future.getTickle() == null ? "" : df.format(future.getTickle())) + "</tickle>\r\n");
            out.write("<topic-key>" + future.getTopic().key + "</topic-key>\r\n");
            if (future.getThought() != null) {
                out.write("<thought-key>" + future.getThought().key + "</thought-key>\r\n");
            }
            out.write("</future>\r\n");
        }
        out.write("</futures>\r\n");
    }
    
    /* Write single actions. */
    private static void writeSingleActions() throws Exception {
        out.write("<single_actions>\r\n");
        for (Iterator<Action> i = data.getRootActions().iterator(Action.class); i.hasNext();) {
            writeAction(i.next());
        }
        out.write("</single_actions>\r\n");
    }
    
    /* Write projects. */
    private static void writeProjects() throws Exception {
        out.write("<projects>\r\n");
        for (Project p : projects) {
            writeProject(p);
        }
        out.write("</projects>\r\n");
    }
    
    /* Write a project. */
    private static void writeProject(Project project) throws Exception {
        out.write("<project key=\"" + getKey(project) + "\">\r\n");
        out.write("<created>" + df.format(project.getCreated()) + "</created>\r\n");
        Thought thought = project.getThought();
        if (thought != null) {
            out.write("<thought-key>" + project.getThought().key + "</thought-key>\r\n");
        }
        Project parent = (Project)project.getParent();
        if (parent != null && parent.getID() != data.getRootProjects().getID()) {
            out.write("<parent-key>" + getKey(parent) + "</parent-key>\r\n");
        }
        
        String path = ExtractUtils.getProjectPath(project, "");
        out.write("<path>" + escape(path) + "</path>\r\n");
        
        out.write("<topic-key>" + project.getTopic().key + "</topic-key>\r\n");
        out.write("<desc>" + escape(project.getDescription()) + "</desc>\r\n");
        out.write("<purpose>" + escape(project.getPurpose()) + "</purpose>\r\n");
        out.write("<success>" + escape(project.getVision()) + "</success>\r\n");
        out.write("<brainstorming>" + escape(project.getBrainstorming()) + "</brainstorming>\r\n");
        out.write("<organising>" + escape(project.getOrganising()) + "</organising>\r\n");
        out.write("<notes>" + escape(project.getNotes()) + "</notes>\r\n");
        out.write("<done>" + project.isDone() + "</done>\r\n");
        if (project.getDoneDate() != null) {
            out.write("<done_date>" + df.format(project.getDoneDate()) + "</done_date>\r\n");
        }
        out.write("<start-date>" + (project.getStartDate() == null ? "" : df.format(project.getStartDate())) + "</start-date>\r\n");
        out.write("<due-date>" + (project.getDueDate() == null ? "" : df.format(project.getDueDate())) + "</due-date>\r\n");
        out.write("<children>\r\n");
        for (Iterator<Item> i = project.iterator(Item.class); i.hasNext(); ) {
            Item item = i.next();
            if (item instanceof Action) {
                out.write("<action key=\"" + getKey((Action)item) + "\"/>\r\n");
            } else if (item instanceof Project) {
                out.write("<project key=\"" + getKey((Project)item) + "\"/>\r\n");
            }
        }
        out.write("</children>\r\n");
        out.write("</project>\r\n");
    }
    
    /* Write actions. */
    private static void writeActions() throws Exception {
        out.write("<actions>\r\n");
        for (Action a : actions) {
            writeAction(a);
        }
        out.write("</actions>\r\n");
    }
    
    /* Write an action. */
    private static void writeAction(Action action) throws Exception {
        out.write("<action key=\"" + getKey(action) + "\">\r\n");
        out.write("<created>" + df.format(action.getCreated()) + "</created>\r\n");
        Thought thought = action.getThought();
        if (thought != null) {
            out.write("<thought-key>" + action.getThought().key + "</thought-key>\r\n");
        }
        Project parent = (Project)action.getParent();
        if (parent != null && parent.getID() != data.getRootProjects().getID() && parent.getID() != data.getRootActions().getID()) {
            out.write("<parent-key>" + getKey(parent) + "</parent-key>\r\n");
        }
        out.write("<desc>" + escape(action.getDescription()) + "</desc>\r\n");
        out.write("<success>" + escape(action.getSuccess()) + "</success>\r\n");
        out.write("<topic-key>" + action.getTopic().key + "</topic-key>\r\n");
        out.write("<context-key>" + action.getContext().key + "</context-key>\r\n");
        
        String time = action.getTime() == null ? "" : action.getTime().getName();
        out.write("<time>" + escape(time) + "</time>\r\n");
        String energy = action.getEnergy() == null ? "" : action.getEnergy().getName();
        out.write("<energy>" + escape(energy) + "</energy>\r\n");
        String priority = action.getPriority() == null ? "" : action.getPriority().getName();
        out.write("<priority>" + escape(priority) + "</priority>\r\n");
        
        Date actionDate = action.getActionDate();
        out.write("<action-date>" + (actionDate == null ? "" : df.format(actionDate)) + "</action-date>\r\n");
        
        writeActionState(action);
        
        out.write("<notes>" + escape(action.getNotes()) + "</notes>\r\n");
        out.write("<done>" + action.isDone() + "</done>\r\n");
        if (action.getDoneDate() != null) {
            out.write("<done_date>" + df.format(action.getDoneDate()) + "</done_date>\r\n");
        }
        
        
//////        // start: for actions screen report
//////        ActionState s = action.getState();
//////        if (s instanceof ActionStateDelegated) {
//////            out.write("<icon>D</icon>\r\n");  // for sort by the icon
//////            Date date = ((ActionStateDelegated)s).getDate();
//////            out.write("<action-date>" + (date==null ? "" : dfsort.format(date)) + "</action-date>\r\n");
//////            out.write("<delegate>" + escape(((ActionStateDelegated)s).getTo()) + "</delegate>\r\n");
//////        } else if (s instanceof ActionStateScheduled) {
//////            out.write("<icon>S</icon>\r\n");  // for sort by the icon
//////            Date date = ((ActionStateScheduled)s).getDate();
//////            out.write("<action-date>" + (date==null ? "" : dfsort.format(date)) + "</action-date>\r\n");
//////        } else if (s instanceof ActionStateInactive) {
//////            out.write("<icon>I</icon>\r\n");  // for sort by the icon
//////        } else if (s instanceof ActionStateASAP) {
//////            out.write("<icon>A</icon>\r\n");  // for sort by the icon
//////        }
//////        out.write("<context-name>" + escape(action.getContext().getName()) + "</context-name>\r\n");
//////        out.write("<topic-name>" + escape(action.getTopic().getName()) + "</topic-name>\r\n");
//////        
//////        if (parent != null && parent.getID() != data.getRootProjects().getID() && parent.getID() != data.getRootActions().getID()) {
//////            out.write("<from-icon>P</from-icon>\r\n");  // for sorting on icon
//////            out.write("<from-desc>" + escape(parent.getDescription()) + "</from-desc>\r\n");
//////            out.write("<from-topic-name>" + escape(parent.getTopic().getName()) + "</from-topic-name>\r\n");
//////        } else if (thought != null) {
//////            out.write("<from-icon>T</from-icon>\r\n");  // for sorting on icon
//////            out.write("<from-desc>" + escape(action.getThought().getDescription()) + "</from-desc>\r\n");
//////            out.write("<from-topic-name>" + escape(action.getThought().getTopic().getName()) + "</from-topic-name>\r\n");
//////        }
//////        // end: for actions screen report
        
        
        
        out.write("</action>\r\n");
    }
    
    /* Write an action state. */
    private static void writeActionState(Action action) throws Exception {
        
        ActionState state = action.getState();
        
        if (state instanceof ActionStateScheduled s) {
            out.write("<state type='SCHEDULED'>");
            out.write("<created>" + df.format(s.getCreated()) + "</created>");
            out.write("<date>" + (s.getDate() == null ? "" : df.format(s.getDate())) + "</date>");
            out.write("<duration-hrs>" + s.getDurationHours() + "</duration-hrs>\r\n");
            out.write("<duration-mns>" + s.getDurationMinutes() + "</duration-mns>\r\n");
            out.write("</state>\r\n");
        } else {
            if (state instanceof ActionStateASAP s) {
                out.write("<state type='ASAP'>");
                out.write("<created>" + df.format(s.getCreated()) + "</created>");
                out.write("</state>\r\n");
            } else if (state instanceof ActionStateDelegated s) {
                out.write("<state type='DELEGATED'>");
                out.write("<created>" + df.format(s.getCreated()) + "</created>");
                out.write("<to>" + escape(s.getTo()) + "</to>");
                out.write("<date>" + (s.getDate() == null ? "" : df.format(s.getDate())) + "</date>");
                out.write("</state>\r\n");
            } else if (state instanceof ActionStateInactive s) {
                out.write("<state type='INACTIVE'>");
                out.write("<created>" + df.format(s.getCreated()) + "</created>");
                out.write("</state>\r\n");
            }
            Date startDate = action.getStartDate();
            out.write("<start-date>" + (startDate == null ? "" : df.format(startDate)) + "</start-date>\r\n");
            Date dueDate = action.getDueDate();
            out.write("<due-date>" + (dueDate == null ? "" : df.format(dueDate)) + "</due-date>\r\n");
        }
    }
    
    private static String getKey(Project project) {
        return (project==null) ? "" : KEY_PREFIX_PROJECT + project.getID();
    }
    
    private static String getKey(Action action) {
        return (action==null) ? "" : KEY_PREFIX_ACTION + action.getID();
    }
    
    /*
     * Escapes the characters in a String using XML entities.  For example:
     * "bread" & "butter" => &quot;bread&quot; &amp; &quot;butter&quot;.
     * @param string The string.
     * @return The escaped string.
     */
    private static String escape(String string) {
        return formatType.escape(string);        
    }
    
}
