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
import tr.model.actor.Actor;
import tr.model.actor.ActorUtils;
import tr.model.context.Context;
import tr.model.criteria.Criterion;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.topic.Topic;

/**
 * Import project template.

 * @author Jeremy Moore
 */
public class ImportTemplate {

    private static final Logger LOG = Logger.getLogger("tr.view.projects.ImportTemplate");
    private static final String EXTN = "trpt";
    private static final String EXTN_NAME = NbBundle.getMessage(ExportTemplate.class, "template.file.extension.name");

    private Map<String, Value> mapTime;
    private Map<String, Value> mapEnergy;
    private Map<String, Value> mapPriority;    
    
    /** Constructs a new instance. */
    public ImportTemplate() {
    }

    /**
     * Import a project template from a project template XML file.
     */
    public void process() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data was not available.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(NbBundle.getMessage(ExportTemplate.class, "import.template.title"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilterImpl(EXTN_NAME, new String[]{EXTN}, true);
        chooser.setFileFilter(filter);
        Component p = WindowManager.getDefault().getMainWindow();
        int returnVal = chooser.showDialog(p, NbBundle.getMessage(ExportTemplate.class, "import.template.button"));
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String path = chooser.getSelectedFile().getPath();
        File file = new File(path);
        if (!file.isFile() || !file.canRead()) {
            String t = NbBundle.getMessage(ImportTemplate.class, "import.template.title");
            String m = NbBundle.getMessage(ImportTemplate.class, "import.template.read.error");
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);            
            return;
        }

        mapTime = initialiseMap(data.getTimeCriterion());
        mapEnergy = initialiseMap(data.getEnergyCriterion());
        mapPriority = initialiseMap(data.getPriorityCriterion());        
        
        Document dom = parse(file);
        if (dom == null) {
            String t = NbBundle.getMessage(ImportTemplate.class, "import.template.title");
            String m = NbBundle.getMessage(ImportTemplate.class, "import.template.parse.error");
            JOptionPane.showMessageDialog(p, m, t, JOptionPane.ERROR_MESSAGE);            
            return;
        }

        process(dom, data);
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
    private void process(Document dom, Data data) {

        Element templateElement = dom.getDocumentElement();
        if (templateElement == null) {
            LOG.severe("Element root \"template\" was not found.");
            return;
        }

        Element projectElement = getChildElementByName(templateElement, "project");
        if (projectElement == null) {
            LOG.severe("Element top level \"project\" was not found.");
            return;
        }

        Project root = processProjectElement(projectElement, data);
        if (root == null) {
            return;
        }

        data.getRootTemplates().add(root);
    }

    private Project processProjectElement(Element projectElement, Data data) {

        Project project = new Project(data);

        Element element = getChildElementByName(projectElement, "descr");
        if (element != null) {
            project.setDescription(element.getTextContent());
        }
        element = getChildElementByName(projectElement, "topic");
        if (element != null) {
            String topicName = element.getTextContent();
            Topic topic = getTopic(data, topicName);
            if (topic != null) {
                project.setTopic(topic);
            }
        }
        element = getChildElementByName(projectElement, "purpose");
        if (element != null) {
            project.setPurpose(element.getTextContent());
        }
        element = getChildElementByName(projectElement, "success");
        if (element != null) {
            project.setVision(element.getTextContent());
        }
        element = getChildElementByName(projectElement, "brainstorming");
        if (element != null) {
            project.setBrainstorming(element.getTextContent());
        }
        element = getChildElementByName(projectElement, "organising");
        if (element != null) {
            project.setOrganising(element.getTextContent());
        }
        element = getChildElementByName(projectElement, "notes");
        if (element != null) {
            project.setNotes(element.getTextContent());
        }

        element = getChildElementByName(projectElement, "sequence");
        if (element != null) {
            project.setSequencing(Boolean.parseBoolean(element.getTextContent()));
        }

        if (project.isSequencing()) {
            element = getChildElementByName(projectElement, "sequence-into");
            if (element != null) {
                if (Boolean.parseBoolean(element.getTextContent())) {
//                  project.setSequenceType(Sequencing.INTO_SUBPROJECTS);
                    project.setSeqIncludeProjects(true);
                } else {
//                  project.setSequenceType(Sequencing.OVER_SUBPROJECTS);
                    project.setSeqIncludeProjects(false);
                }
            }
        }
        
        element = getChildElementByName(projectElement, "priority");
        if (element != null) {
            Value value = mapPriority.get(element.getTextContent());
            if (value != null) {
                project.setPriority(value);
            }
        }

        for (Element childElement : getChildElementsByName(projectElement, "child")) {
            String type = childElement.getAttribute("type");
            if (type.equals("action")) {
                Action action = processActionElement(childElement, data);
                if (action != null) {
                    project.add(action);
                }
            } else if (type.equals("project")) {
                Project subproject = processProjectElement(childElement, data);
                if (subproject != null) {
                    project.add(subproject);
                }
            }
        }

        return project;
    }

    private Action processActionElement(Element elemAction, Data data) {

        Action action = new Action(data);

        Element element = getChildElementByName(elemAction, "descr");
        if (element != null) {
            action.setDescription(element.getTextContent());
        }
        element = getChildElementByName(elemAction, "topic");
        if (element != null) {
            String topicName = element.getTextContent();
            Topic topic = getTopic(data, topicName);
            if (topic != null) {
                action.setTopic(topic);
            }
        }
        element = getChildElementByName(elemAction, "context");
        if (element != null) {
            String contextName = element.getTextContent();
            Context context = getContext(data, contextName);
            if (context != null) {
                action.setContext(context);
            }
        }
        element = getChildElementByName(elemAction, "success");
        if (element != null) {
            action.setSuccess(element.getTextContent());
        }
        element = getChildElementByName(elemAction, "notes");
        if (element != null) {
            action.setNotes(element.getTextContent());
        }
        element = getChildElementByName(elemAction, "time");
        if (element != null) {
            Value value = mapTime.get(element.getTextContent());
            if (value != null) {
                action.setTime(value);
            }
        }
        element = getChildElementByName(elemAction, "energy");
        if (element != null) {
            Value value = mapEnergy.get(element.getTextContent());
            if (value != null) {
                action.setEnergy(value);
            }
        }
        element = getChildElementByName(elemAction, "priority");
        if (element != null) {
            Value value = mapPriority.get(element.getTextContent());
            if (value != null) {
                action.setPriority(value);
            }
        }
        element = getChildElementByName(elemAction, "state");
        if (element != null) {
            String type = element.getAttribute("type");
            if (type.equals("DELEGATED")) {
                ActionStateDelegated state = new ActionStateDelegated();
                Actor delegate = null;
                Integer delegateID = getInt(element, "delegate-id");
                String delegateName = getStr(element, "delegate");
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
                Date vDate = getDate(element, "followup");
                if (vDate != null) {
                    state.setDate(vDate);
                }
                action.setState(state);
            } else if (type.equals("SCHEDULED")) {
                ActionStateScheduled state = new ActionStateScheduled();
                action.setState(state);
            } else if (type.equals("DOASAP")) {
                ActionStateASAP state = new ActionStateASAP();
                action.setState(state);
            } else if (type.equals("INACTIVE")) {
                ActionStateInactive state = new ActionStateInactive();
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

}
