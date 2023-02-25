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
import au.com.trgtd.tr.services.Services;
import java.awt.Component;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateInactive;
import tr.model.action.ActionStateScheduled;
import tr.model.action.PeriodDay;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodMonth.OnTheDay;
import tr.model.action.PeriodMonth.OnTheNth;
import tr.model.action.PeriodMonth.Option;
import tr.model.action.PeriodType;
import tr.model.action.PeriodWeek;
import tr.model.action.PeriodWeekday;
import tr.model.action.PeriodYear;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.context.Context;
import tr.model.criteria.Criterion;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.topic.Topic;

/**
 * Import a project hierarchy.

 * @author Jeremy Moore
 */
public class ImportProject {

    private static final Logger LOG = Logger.getLogger("tr.import.project");
    private static final String EXTN = "trp";
    private static final String EXTN_NAME = NbBundle.getMessage(ExportTemplate.class, "project.file.extension.name");
    private Map<String, Value> mapTime;
    private Map<String, Value> mapEnergy;
    private Map<String, Value> mapPriority;
    private String vStr;
    private Integer vInt;
    private List<Integer> vIntList;
    private Byte vByte;
    private Boolean vBool;
    private Date vDate;
    private Element element;

    /** Constructs a new instance. */
    public ImportProject() {
    }

    /**
     * Import a project from a project XML file.
     * @param targetProject The project to import into.
     */
    public void process(Project targetProject) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data was not available.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(NbBundle.getMessage(ExportTemplate.class, "import.project.title"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilterImpl(EXTN_NAME, new String[]{EXTN}, true);
        chooser.setFileFilter(filter);
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showDialog(p, NbBundle.getMessage(ExportTemplate.class, "import.project.button"));
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String path = chooser.getSelectedFile().getPath();
        File file = new File(path);
        if (!file.isFile() || !file.canRead()) {
            String t = NbBundle.getMessage(ImportProject.class, "import.project.title");
            String m = NbBundle.getMessage(ImportProject.class, "import.project.read.error");
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
            return;
        }

        mapTime = initialiseMap(data.getTimeCriterion());
        mapEnergy = initialiseMap(data.getEnergyCriterion());
        mapPriority = initialiseMap(data.getPriorityCriterion());

        Document dom = parse(file);
        if (dom == null) {
            String t = NbBundle.getMessage(ImportProject.class, "import.project.title");
            String m = NbBundle.getMessage(ImportProject.class, "import.project.parse.error");
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);
            return;
        }

        process(dom, data, targetProject);
    }

    private Map<String, Value> initialiseMap(Criterion criterion) {
        Map<String, Value> map = new HashMap<>();
        for (Value value : criterion.values.list()) {
            map.put(value.getName(), value);
        }
        return map;
    }

    // Parse the XML file and get the DOM object
    private Document parse(File file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(file);
        } catch (Exception ex) {
            return null;
        }
    }
    // Process the DOM and create the template project.

    private void process(Document dom, Data data, Project targetProject) {

        Element templateElement = dom.getDocumentElement();
        if (templateElement == null) {
            LOG.severe("Element root \"trproject\" was not found.");
            return;
        }

        Element projectElement = getChildElementByName(templateElement, "project");
        if (projectElement == null) {
            LOG.severe("Element top level \"project\" was not found.");
            return;
        }

        Project root = processProjectElement(projectElement, data, targetProject);
        if (root != null) {
            targetProject.add(root);
        }

    }

    private Project processProjectElement(Element projectElement, Data data, Project parent) {

        Project project = new Project(data);

        project.setParent(parent);

        vDate = getDate(projectElement, "created");
        if (vDate != null) {
            project.setCreated(vDate);
        }
        vStr = getStr(projectElement, "descr");
        if (vStr != null) {
            project.setDescription(vStr);
        }
        element = getChildElementByName(projectElement, "topic");
        if (element != null) {
            String topicName = element.getTextContent();
            Topic topic = getTopic(data, topicName);
            if (topic != null) {
                project.setTopic(topic);
            }
        }
        vStr = getStr(projectElement, "purpose");
        if (vStr != null) {
            project.setPurpose(vStr);
        }
        vStr = getStr(projectElement, "success");
        if (vStr != null) {
            project.setVision(vStr);
        }
        vStr = getStr(projectElement, "brainstorming");
        if (vStr != null) {
            project.setBrainstorming(vStr);
        }
        vStr = getStr(projectElement, "organising");
        if (vStr != null) {
            project.setOrganising(vStr);
        }
        vStr = getStr(projectElement, "notes");
        if (vStr != null) {
            project.setNotes(vStr);
        }
        vBool = getBool(projectElement, "done");
        if (vBool != null) {
            project.setDone(vBool);
            if (project.isDone()) {
                vDate = getDate(projectElement, "done-date");
                if (vDate != null) {
                    project.setDoneDate(vDate);
                }
            }
        }
        vDate = getDate(projectElement, "due-date");
        if (vDate != null) {
            project.setDueDate(vDate);
        }
        vDate = getDate(projectElement, "start-date");
        if (vDate != null) {
            project.setStartDate(vDate);
        }
        vStr = getStr(projectElement, "priority");
        if (vStr != null) {
            Value value = mapPriority.get(vStr);
            if (value != null) {
                project.setPriority(value);
            }
        }
        vBool = getBool(projectElement, "sequence");
        if (vBool != null) {
            project.setSequencing(vBool);
            if (project.isSequencing()) {
                vBool = getBool(projectElement, "seq-projects");
                if (vBool != null) {
                    project.setSeqIncludeProjects(vBool);
                }
                vBool = getBool(projectElement, "seq-scheduled");
                if (vBool != null) {
                    project.setSeqIncludeScheduled(vBool);
                }
                vBool = getBool(projectElement, "seq-delegated");
                if (vBool != null) {
                    project.setSeqIncludeDelegated(vBool);
                }
            }
        }
        for (Element childElement : getChildElementsByName(projectElement, "child")) {
            String type = childElement.getAttribute("type");
            if (type.equals("action")) {
                Action action = processActionElement(childElement, data, project);
                if (action != null) {
                    project.add(action);
                }
            } else if (type.equals("project")) {
                Project subproject = processProjectElement(childElement, data, project);
                if (subproject != null) {
                    project.add(subproject);
                }
            }
        }
        return project;
    }

    private Action processActionElement(Element elementAction, Data data, Project parent) {

        Action action = new Action(data);

        action.setParent(parent);

        vDate = getDate(elementAction, "created");
        if (vDate != null) {
            action.setCreated(vDate);
        }        
        vStr = getStr(elementAction, "descr");
        if (vStr != null) {
            action.setDescription(vStr);
        }
        vStr = getStr(elementAction, "success");
        if (vStr != null) {
            action.setSuccess(vStr);
        }
        element = getChildElementByName(elementAction, "topic");
        if (element != null) {
            String topicName = element.getTextContent();
            Topic topic = getTopic(data, topicName);
            if (topic != null) {
                action.setTopic(topic);
            }
        }
        element = getChildElementByName(elementAction, "context");
        if (element != null) {
            String contextName = element.getTextContent();
            Context context = getContext(data, contextName);
            if (context != null) {
                action.setContext(context);
            }
        }
        vStr = getStr(elementAction, "notes");
        if (vStr != null) {
            action.setNotes(vStr);
        }
        element = getChildElementByName(elementAction, "time");
        if (element != null) {
            Value value = mapTime.get(element.getTextContent());
            if (value != null) {
                action.setTime(value);
            }
        }
        element = getChildElementByName(elementAction, "energy");
        if (element != null) {
            Value value = mapEnergy.get(element.getTextContent());
            if (value != null) {
                action.setEnergy(value);
            }
        }
        element = getChildElementByName(elementAction, "priority");
        if (element != null) {
            Value value = mapPriority.get(element.getTextContent());
            if (value != null) {
                action.setPriority(value);
            }
        }
        vBool = getBool(elementAction, "done");
        if (vBool != null) {
            action.setDone(vBool);
            if (action.isDone()) {
                vDate = getDate(elementAction, "done-date");
                if (vDate != null) {
                    action.setDoneDate(vDate);
                }
            }
        }
        vDate = getDate(elementAction, "due-date");
        if (vDate != null) {
            action.setDueDate(vDate);
        }
        vDate = getDate(elementAction, "start-date");
        if (vDate != null) {
            action.setStartDate(vDate);
        }

        Element stateElement = getChildElementByName(elementAction, "state");
        if (stateElement != null) {
            String type = stateElement.getAttribute("type");
            if (type.equals("DOASAP")) {
                action.setState(new ActionStateASAP());
            } else if (type.equals("INACTIVE")) {
                action.setState(new ActionStateInactive());
            } else if (type.equals("DELEGATED")) {
                ActionStateDelegated state = new ActionStateDelegated();
                Actor delegate = null;
                Integer delegateID = getInt(stateElement, "delegate-id");
                String delegateName = getStr(stateElement, "delegate");
                if (delegateID != null) {
                    delegate = ActorUtils.instance().getActor(delegateID);
                }
                if (delegate == null) {
                    delegate = Services.instance.getActor(delegateName);
                }
                if (delegate != null) {
                    state.setActorID(delegate.getID());
                } else if (delegateName != null) {
                    state.setTo(delegateName);
                }
                vDate = getDate(stateElement, "followup");
                if (vDate != null) {
                    state.setDate(vDate);
                }
                action.setState(state);
            } else if (type.equals("SCHEDULED")) {
                ActionStateScheduled state = new ActionStateScheduled();
                vDate = getDate(stateElement, "date");
                if (vDate != null) {
                    state.setDate(vDate);
                }
                vInt = getInt(stateElement, "duration-hrs");
                if (vInt != null) {
                    state.setDurationHours(vInt);
                }
                vInt = getInt(stateElement, "duration-mins");
                if (vInt != null) {
                    state.setDurationMins(vInt);
                }
                Element recurrenceElement = getChildElementByName(stateElement, "recurrence");
                if (recurrenceElement != null) {
                    Recurrence recurrence = new Recurrence(data.getNextID());
                    recurrence.setProject(parent);
                    vStr = getStr(recurrenceElement, "descr");
                    if (vStr != null) {
                        recurrence.setDescription(vStr);
                    }
                    element = getChildElementByName(recurrenceElement, "topic");
                    if (element != null) {
                        String topicName = element.getTextContent();
                        Topic topic = getTopic(data, topicName);
                        if (topic != null) {
                            recurrence.setTopic(topic);
                        }
                    }
                    element = getChildElementByName(recurrenceElement, "context");
                    if (element != null) {
                        String contextName = element.getTextContent();
                        Context context = getContext(data, contextName);
                        if (context != null) {
                            recurrence.setContext(context);
                        }
                    }
                    element = getChildElementByName(recurrenceElement, "time");
                    if (element != null) {
                        Value value = mapTime.get(element.getTextContent());
                        if (value != null) {
                            recurrence.setTime(value);
                        }
                    }
                    element = getChildElementByName(recurrenceElement, "energy");
                    if (element != null) {
                        Value value = mapEnergy.get(element.getTextContent());
                        if (value != null) {
                            recurrence.setEnergy(value);
                        }
                    }
                    element = getChildElementByName(recurrenceElement, "priority");
                    if (element != null) {
                        Value value = mapPriority.get(element.getTextContent());
                        if (value != null) {
                            recurrence.setPriority(value);
                        }
                    }
                    vInt = getInt(recurrenceElement, "schedule-hrs");
                    if (vInt != null) {
                        recurrence.setScheduleHours(vInt);
                    }
                    vInt = getInt(recurrenceElement, "schedule-mins");
                    if (vInt != null) {
                        recurrence.setScheduleMins(vInt);
                    }
                    vInt = getInt(recurrenceElement, "duration-hrs");
                    if (vInt != null) {
                        recurrence.setDurationHours(vInt);
                    }
                    vInt = getInt(recurrenceElement, "duration-mins");
                    if (vInt != null) {
                        recurrence.setDurationMins(vInt);
                    }
                    vByte = getByte(recurrenceElement, "basis-id");
                    if (vByte != null) {
                        recurrence.setBasis(Basis.fromID(vByte));
                    }
                    vDate = getDate(recurrenceElement, "start-date");
                    if (vDate != null) {
                        recurrence.setStartDate(vDate);
                    }
                    vInt = getInt(recurrenceElement, "frequency");
                    if (vInt != null) {
                        recurrence.setFrequency(vInt);
                    }
                    vInt = getInt(recurrenceElement, "advance-nbr");
                    if (vInt != null) {
                        recurrence.setAdvanceNbr(vInt);
                    }
                    vInt = getInt(recurrenceElement, "end-nbr");
                    if (vInt != null) {
                        recurrence.setEndNbr(vInt);
                    }
                    vDate = getDate(recurrenceElement, "end-date");
                    if (vDate != null) {
                        recurrence.setEndDate(vDate);
                    }
                    vInt = getInt(recurrenceElement, "gen-nbr");
                    if (vInt != null) {
                        recurrence.setGenNbr(vInt);
                    }
                    vStr = getStr(recurrenceElement, "success");
                    if (vStr != null) {
                        recurrence.setSuccess(vStr);
                    }
                    vStr = getStr(recurrenceElement, "notes");
                    if (vStr != null) {
                        recurrence.setNotes(vStr);
                    }
                    Element periodElement = getChildElementByName(recurrenceElement, "period");
                    if (periodElement != null) {
                        Byte typeID = getByte(periodElement.getAttribute("type"));
                        if (typeID != null) {
                            PeriodType periodType = PeriodType.fromID(typeID);
                            switch (periodType) {
                                case WEEKDAY: {
                                    recurrence.setPeriod(new PeriodWeekday());
                                    break;
                                }
                                case DAY: {
                                    recurrence.setPeriod(new PeriodDay());
                                    break;
                                }
                                case WEEK: {
                                    vIntList = getIntList(periodElement, "days");
                                    if (vIntList == null) {
                                        recurrence.setPeriod(new PeriodWeek());
                                    } else {
                                        recurrence.setPeriod(new PeriodWeek(vIntList));
                                    }
                                    break;
                                }
                                case MONTH: {
                                    PeriodMonth pm = new PeriodMonth();
                                    vStr = getStr(periodElement, "option");
                                    if (vStr != null) {
                                        pm.setOption(Option.valueOf(vStr));
                                    }
                                    vIntList = getIntList(periodElement, "days");
                                    if (vIntList != null) {
                                        pm.setSelectedDays(vIntList);
                                    }
                                    vStr = getStr(periodElement, "on-the-nth");
                                    if (vStr != null) {
                                        pm.setOnTheNth(OnTheNth.valueOf(vStr));
                                    }
                                    vStr = getStr(periodElement, "on-the-day");
                                    if (vStr != null) {
                                        pm.setOnTheDay(OnTheDay.valueOf(vStr));
                                    }
                                    recurrence.setPeriod(pm);
                                    break;
                                }
                                case YEAR: {
                                    PeriodYear py = new PeriodYear();
                                    vIntList = getIntList(periodElement, "months");
                                    if (vIntList != null) {
                                        py.setSelectedMonths(vIntList);
                                    }
                                    vBool = getBool(periodElement, "is-on-the-selected");
                                    if (vBool != null) {
                                        py.setOnTheSelected(vBool);
                                    }
                                    vStr = getStr(periodElement, "on-the-nth");
                                    if (vStr != null) {
                                        py.setOnTheNth(OnTheNth.valueOf(vStr));
                                    }
                                    vStr = getStr(periodElement, "on-the-day");
                                    if (vStr != null) {
                                        py.setOnTheDay(OnTheDay.valueOf(vStr));
                                    }
                                    recurrence.setPeriod(py);
                                    break;
                                }
                            }
                        }
                    }
                    vBool = getBool(recurrenceElement, "is-calendar-item");
                    if (vBool != null) {
                        recurrence.setCalendarItem(vBool);
                    }
                    state.setRecurrence(recurrence);
                }
                action.setState(state);
            }
        }
        return action;
    }

    private Element getChildElementByName(Element element, String name) {

        NodeList nodeList = element.getChildNodes();
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i) instanceof Element childElement) {
                    if (childElement.getTagName().equals(name)) {
                        return childElement;
                    }
                }
            }
        }
        return null;
    }

    private List<Element> getChildElementsByName(Element element, String name) {
        List<Element> childElements = new Vector<>();
        NodeList nodeList = element.getChildNodes();
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i) instanceof Element childElement) {
                    if (childElement.getTagName().equals(name)) {
                        childElements.add(childElement);
                    }
                }
            }
        }
        return childElements;
    }

    private Topic getTopic(Data data, String topicName) {
        for (Topic topic : data.getTopicManager().list()) {
            if (topic.getName().equals(topicName)) {
                return topic;
            }
        }
        return null;
    }

    private Context getContext(Data data, String contextName) {
        for (Context context : data.getContextManager().list()) {
            if (context.getName().equals(contextName)) {
                return context;
            }
        }
        return null;
    }

    private String getStr(Element element, String name) {
        Element e = getChildElementByName(element, name);
        return e == null ? null : e.getTextContent();
    }

    private Integer getInt(Element element, String name) {
        Element e = getChildElementByName(element, name);
        if (e != null) {
            try {
                return Integer.parseInt(e.getTextContent());
            } catch (Exception x) {
            }
        }
        return null;
    }

    private List<Integer> getIntList(Element element, String name) {
        String s = getStr(element, name);
        if (s == null || s.trim().length() == 0) {
            return null;
        }
        List<Integer> list = new Vector<>();
        for (String n : s.trim().split(",")) {
            try {
                list.add(Integer.parseInt(n));
            } catch (Exception x) {
                return null;
            }
        }
        return list;
    }

    private Byte getByte(Element element, String name) {
        Element e = getChildElementByName(element, name);
        if (e != null) {
            try {
                return Byte.parseByte(e.getTextContent());
            } catch (Exception x) {
            }
        }
        return null;
    }

    private Byte getByte(String string) {
        try {
            return Byte.parseByte(string);
        } catch (Exception x) {
            return null;
        }
    }

    private Boolean getBool(Element element, String name) {
        Element e = getChildElementByName(element, name);
        if (e != null) {
            try {
                return Boolean.parseBoolean(e.getTextContent());
            } catch (Exception x) {
            }
        }
        return null;
    }

    private Date getDate(Element element, String name) {
        Element e = getChildElementByName(element, name);
        if (e != null) {
            try {
                return new Date(Long.parseLong(e.getTextContent()));
            } catch (Exception x) {
            }
        }
        return null;
    }

//    private Actor getDelegate(Element element, String name) {
//        String str = getStr(element, name);
//        return str == null ? null : Services.instance.getActor(str);
//    }

}
