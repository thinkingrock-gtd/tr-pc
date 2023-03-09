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

import au.com.trgtd.tr.prefs.projects.ProjectsPrefs;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.*;
import au.com.trgtd.tr.view.contexts.ContextChangeAction;
import au.com.trgtd.tr.view.projects.PostponeActionAction.Periods;
import au.com.trgtd.tr.view.topics.TopicChangeAction;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.text.StringEscapeUtils;
import org.openide.actions.DeleteAction;
import org.openide.actions.*;
import org.openide.cookies.ViewCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.windows.WindowManager;
import tr.model.Criteria;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.*;
import tr.model.context.Context;
import tr.model.context.ContextChangeCookie;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.project.ProjectSingleActions;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import tr.model.topic.TopicChangeCookie;

/**
 * Node for a TR action.
 *
 * @author Jeremy Moore
 */
public class ActionNode extends AbstractNode implements EditCookie,
        ProjectiseCookie, ReprocessCookie, ActionFinder, TransferNode,
        AddActionCookie, AddProjectCookie, ViewCookie, PrintCookie,
        SetDoneCookie, PostponeActionCookie, // DeleteCookie,
        CriteriaChangeCookie, ProjectChangeCookie, ChangeStatusCookie {

    private static final Logger LOG = Logger.getLogger("tr.view.projects");
    public final Action action;
    private IconChangeListener icl;
    private NameChangeListener ncl;
    private boolean showDone;

    /** Constructs a new instance. */
    public ActionNode(Action action) {
        super(Children.LEAF, Lookups.singleton(action));
        this.action = action;
        addListeners();
    }

    private void addListeners() {
        icl = new IconChangeListener();
        action.addPropertyChangeListener(Action.PROP_STATE, icl);
        ncl = new NameChangeListener();
        action.addPropertyChangeListener(Action.PROP_DONE, ncl);
        action.addPropertyChangeListener(Action.PROP_DESCR, ncl);
        action.addPropertyChangeListener(Action.PROP_TOPIC, ncl);
    }

    private void removeListeners() {
        action.removePropertyChangeListener(Action.PROP_STATE, icl);
        icl = null;
        action.removePropertyChangeListener(Action.PROP_DONE, ncl);
        action.removePropertyChangeListener(Action.PROP_DESCR, ncl);
        action.removePropertyChangeListener(Action.PROP_TOPIC, ncl);
        ncl = null;
    }

    @Override
    public boolean isHidden() {
        return !showDone && action.isDone();
    }

    public void setShowDone(boolean showDone) {
        this.showDone = showDone;
        this.fireCookieChange();
    }

    @Override
    public String getName() {
        return action.getDescription();
    }

    @Override
    public String toString() {
        return action.getDescription();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    private String escapeHTML(String str) {
        str = StringEscapeUtils.escapeXml10(str);
        return str.replace("&apos;", "'");
    }

    @Override
    public String getHtmlDisplayName() {
        String name = escapeHTML(getName());
        String color = au.com.trgtd.tr.util.HTML.format(action.getTopic().getForeground());
        if (action.isDone()) {
            return "<s><font color='" + color + "'>" + name + "</font></s>";
        } else {
            return "<font color='" + color + "'>" + name + "</font>";
        }
    }

    @Override
    public boolean canCut() {
        return true;
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        action.removeFromParent();
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        if (action.isSingleAction()) {
            return new javax.swing.Action[]{
                        SystemAction.get(EditAction.class),
                        null,
                        SystemAction.get(CopyAction.class),
                        SystemAction.get(CutAction.class),
                        null,
                        SystemAction.get(DeleteAction.class),
                        null,
                        SystemAction.get(MoveUpAction.class),
                        SystemAction.get(MoveDownAction.class),
                        null,
                        SystemAction.get(ReprocessAction.class),
                        SystemAction.get(ProjectiseAction.class),
                        null,
                        SystemAction.get(ContextChangeAction.class),
                        SystemAction.get(CriteriaChangeAction.class),
                        SystemAction.get(TopicChangeAction.class),
                        SystemAction.get(ChangeStatusAction.class),
                        SystemAction.get(ProjectChangeAction.class),
                        null,
                        SystemAction.get(AddActionAction.class),
                        null,
                        SystemAction.get(PostponeActionAction.class),
                        SystemAction.get(SetDoneAction.class),
                        null,
                        SystemAction.get(PrintAction.class),};
        } else {
            return new javax.swing.Action[]{
                        SystemAction.get(EditAction.class),
                        null,
                        SystemAction.get(CopyAction.class),
                        SystemAction.get(CutAction.class),
                        null,
                        SystemAction.get(DeleteAction.class),
                        null,
                        SystemAction.get(MoveUpAction.class),
                        SystemAction.get(MoveDownAction.class),
                        null,
                        SystemAction.get(ReprocessAction.class),
                        SystemAction.get(ProjectiseAction.class),
                        null,
                        SystemAction.get(ContextChangeAction.class),
                        SystemAction.get(CriteriaChangeAction.class),
                        SystemAction.get(TopicChangeAction.class),
                        SystemAction.get(ChangeStatusAction.class),
                        SystemAction.get(ProjectChangeAction.class),
                        null,
                        SystemAction.get(AddActionAction.class),
                        SystemAction.get(AddProjectAction.class),
                        null,
                        SystemAction.get(PostponeActionAction.class),
                        SystemAction.get(SetDoneAction.class),
                        null,
                        SystemAction.get(PrintAction.class),};
        }
    }

    @Override
    public javax.swing.Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == ViewCookie.class) {
            return this;
        }
//        if (clazz == DeleteCookie.class) {
//            return this;
//        }
        if (clazz == EditCookie.class) {
            return this;
        }
        if (clazz == AddActionCookie.class) {
            return this;
        }
        if (clazz == AddProjectCookie.class) {
            return this;
        }
        if (clazz == ReprocessCookie.class) {
            return this;
        }
        if (clazz == ProjectiseCookie.class) {
            return this;
        }
        if (clazz == PrintCookie.class) {
            return this;
        }
        if (clazz == TopicChangeCookie.class) {
            return action;
        }
        if (clazz == ContextChangeCookie.class) {
            return action;
        }
        if (clazz == SetDoneCookie.class) {
            return this;
        }
        if (clazz == PostponeActionCookie.class) {
            return this;
        }
        if (clazz == CriteriaChangeCookie.class) {
            return this;
        }
        if (clazz == ProjectChangeCookie.class) {
            return this;
        }
        if (clazz == ChangeStatusCookie.class) {
            return this;
        }
        return super.getCookie(clazz);
    }

    @Override
    public Image getIcon(int type) {
        Icon icon = action.getIcon(false);
        if (icon instanceof ImageIcon imageIcon) {
            return imageIcon.getImage();
        }
        return super.getIcon(type);
    }

    @Override
    public boolean isRecurrent() {
        return action.getRecurrence() != null;
    }

    /**
     * Reprocess the action as a thought.
     * @param action The action to reprocess.
     */
    @Override
    public void reprocess() {
        if (action == null) {
            return;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        // delete the action
        removeListeners();

        action.removeFromParent();

        fireNodeDestroyed(); // added 08/04/08

        // add the new thought to the front of the thoughts
        Thought thought = new Thought(data.getNextID());
        thought.setDescription(action.getDescription());
        thought.setTopic(action.getTopic());
        thought.setAction(action);
        data.getThoughtManager().insert(thought, 0);

        try {
            // hack to fix hanging - probably due to a windowing issue.
            Thread.sleep(400);
        } catch (InterruptedException ex) {
        }

    }

    /**
     * Converts an action to be the first action of a new project.
     * @param action The action to convert.
     */
    @Override
    public void projectise() {
        if (action == null) {
            return;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        Node parentNode = getParentNode();

        // get parent of action
        Project parent = (Project) action.getParent();
        int index = parent.indexOf(action);

        // delete the action
        removeListeners();

        action.removeFromParent();

        fireNodeDestroyed();

        // create a new project
        Project project = new Project(data);
        project.setThought(action.getThought());
        project.setDescription(action.getDescription());
        project.setTopic(action.getTopic());
        project.setPriority(action.getPriority());
        project.setDueDate(action.getDueDate());
        project.setNotes(action.getNotes());
        if (parent.isRoot()) {
//          if (ProjectsPrefs.isSequencing()) {
//              project.setSequencing(true);
//              project.setSequenceType(Sequencing.INTO_SUBPROJECTS);
//          }
            project.setSequencing(ProjectsPrefs.isSequencing());
            project.setSeqIncludeProjects(ProjectsPrefs.getAutoSeqIncludeSubprojects());
            project.setSeqIncludeScheduled(ProjectsPrefs.getAutoSeqIncludeScheduled());
            project.setSeqIncludeDelegated(ProjectsPrefs.getAutoSeqIncludeDelegated());
        } else {
            project.setSequencing(parent.isSequencing());
//          project.setSequenceType(parent.getSequenceType());
            project.setSeqIncludeProjects(parent.isSeqIncludeProjects());
            project.setSeqIncludeScheduled(parent.isSeqIncludeScheduled());
            project.setSeqIncludeDelegated(parent.isSeqIncludeDelegated());
        }

        project.add(action);

        if (parent instanceof ProjectSingleActions) {
            // add to root projects
            data.getRootProjects().add(project);

            // activate projects tree top component and select new project
            ProjectsTreeTopComponent pt = ProjectsTreeTopComponent.getDefault();
            if (!pt.isOpened()) {
                WindowManager wm = WindowManager.getDefault();
                pt = (ProjectsTreeTopComponent) wm.findTopComponent("RAProjectsTopComponent");
            }
            if (pt != null && pt.isOpened()) {
                pt.requestActive();
                Node rootNode = pt.getExplorerManager().getRootContext();
                if (rootNode instanceof ProjectNode projectNode) {
                    projectNode.select(project);
                }
            }
        } else {
            // insert new project into the tree at the same index as action was
            parent.add(index, project);

            // try to select new project
            if (parentNode instanceof ProjectNode projectNode) {
                projectNode.select(project);
            }
        }
    }

    /**
     * Finds the node of the given action if possible.
     * @param action The action
     * @return the node of the action or null if one can not be found.
     */
    @Override
    public Node find(Action findAction) {
        return action.getID() == findAction.getID() ? this : null;
    }

    @Override
    public Transferable clipboardCut() throws IOException {
        setTransferType(DnDConstants.ACTION_MOVE);

        return super.clipboardCut();
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        setTransferType(DnDConstants.ACTION_COPY);

        return super.clipboardCopy();
    }

    private int transferType;

    /**
     * Sets the transfer action.
     * @param type The transfer type which should be DndConstants.ACTION_COPY or
     * DnDConstants.ACTION_MOVE.
     */
    @Override
    public void setTransferType(int type) {
        transferType = type;
    }

    /**
     * Determines whether a move transfer type has been set.
     * @return true iff the type value of the latest setTransferAction() was
     * DnDConstants.ACTION_MOVE.
     */
    @Override
    public boolean isMoveTransfer() {
        return (transferType & DnDConstants.ACTION_MOVE) != 0;
    }

    /**
     * Determines whether a copy transfer type has been set.
     * @return true if the type value of the latest setTransferAction() was
     * DnDConstants.ACTION_COPY.
     */
    @Override
    public boolean isCopyTransfer() {
        return (transferType & DnDConstants.ACTION_COPY) != 0;
    }

    @Override
    public void edit() {
        EditorTopComponent.findInstance().edit(this);
    }

    public void changeTopic(Topic topic) {
        action.setTopic(topic);
    }

    public void changeContext(Context context) {
        action.setContext(context);
    }

    /** Calls parent project to add an action. */
    @Override
    public void addAction() {
        Node parentNode = getParentNode();
        if (parentNode instanceof AddActionCookie addActionCookie) {
            addActionCookie.addAction(action);
        }
    }

    /** Should not be called - doesn't do anything. */
    @Override
    public void addAction(Action sibling) {
    }

    /** Calls parent project to add a project. */
    @Override
    public void addProject() {
        Node parentNode = getParentNode();
        if (parentNode instanceof AddProjectCookie addProjectCookie) {
            addProjectCookie.addProject(action);
        }
    }

    /** Should not be called - doesn't do anything. */
    @Override
    public void addProject(Action sibling) {
    }

    @Override
    public boolean canAddAction() {
        Node parentNode = getParentNode();
        if (parentNode instanceof AddActionCookie addActionCookie) {
            return addActionCookie.canAddAction();
        }
        return false;
    }

    @Override
    public boolean canAddProject() {
        Node parentNode = getParentNode();
        if (parentNode instanceof AddActionCookie) {
            return ((AddProjectCookie) parentNode).canAddProject();
        }
        return false;
    }

    @Override
    public void view() {
        EditorTopComponent.findInstance().view(this);
    }

    @Override
    public void changeStatus(ActionState state) {
        action.setState(state);
    }

    private class IconChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            setName(action.getDescription()); // name colour - inactive grey
            fireIconChange();                 // state icon change
        }
    }

    private class NameChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            setName(action.getDescription()); // name change
        }
    }

    @Override
    public void print() {
        try {
            ReportImpl report = new ReportImpl();
            report.process(action);
        } catch (Exception ex) {
        }
    }

    @Override
    public void setDone() {
        if (action != null) {
            action.setDone(true);
        }
    }

    private Date add(Date date, int n, Periods periods) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        switch (periods) {
            case DAYS: {
                calendar.add(Calendar.DAY_OF_YEAR, n);
                break;
            }
            case WEEKS: {
                calendar.add(Calendar.WEEK_OF_YEAR, n);
                break;
            }
            case MONTHS: {
                calendar.add(Calendar.MONTH, n);
                break;
            }
            case YEARS: {
                calendar.add(Calendar.YEAR, n);
                break;
            }
        }
        return calendar.getTime();
    }

    @Override
    public void postponeAction(int n, Periods periods) {
        switch (action.getState().getType()) {
            case DOASAP: {
                action.setDueDate(add(action.getDueDate(), n, periods));
                break;
            }
            case INACTIVE: {
                action.setStartDate(add(action.getStartDate(), n, periods));
                break;
            }
            case SCHEDULED: {
                ActionStateScheduled state = (ActionStateScheduled) action.getState();
                state.setDate(add(state.getDate(), n, periods));
                break;
            }
            case DELEGATED: {
                ActionStateDelegated state = (ActionStateDelegated) action.getState();
                state.setDate(add(state.getDate(), n, periods));
                break;
            }
        }
    }

    @Override
    public void postponeAction(Date date) {
        switch (action.getState().getType()) {
            case DOASAP: {
                action.setDueDate(date);
                break;
            }
            case INACTIVE: {
                action.setStartDate(date);
                break;
            }
            case SCHEDULED: {
                ActionStateScheduled state = (ActionStateScheduled) action.getState();
                state.setDate(date);
                break;
            }
            case DELEGATED: {
                ActionStateDelegated state = (ActionStateDelegated) action.getState();
                state.setDate(date);
                break;
            }
        }
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        action.removeFromParent();
    }

    @Override
    public void changeCriteria(Criteria criteria, Value value) {
        if (criteria == Criteria.Time) {
            action.setTime(value);
        } else if (criteria == Criteria.Energy) {
            action.setEnergy(value);
        }
        if (criteria == Criteria.Priority) {
            action.setPriority(value);
        }
    }

    @Override
    public void changeProject(Project project) {
        // ignore move to same project
        if (Utils.equal(action.getParent(), project)) {
            return;
        }
        // if action is recurrenct then remove recurrence definition
        Recurrence r = action.getRecurrence();
        if (r != null && !r.getProject().equals(project)) {
            ((ActionStateScheduled)action.getState()).setRecurrence(null);
        }

        if (project == null) {
            // move to single actions
            Data data = DataLookup.instance().lookup(Data.class);
            if (data != null) {
                action.removeFromParent();
                data.getRootActions().add(action);
            }
        } else { 
            // move to given project
            action.removeFromParent();
            project.add(action);
        }
    }
}
