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
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.commons.text.StringEscapeUtils;
import org.openide.cookies.ViewCookie;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.nodes.NodeTransfer;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.ActionStateASAP;
import tr.model.action.ActionStateInactive;
import tr.model.action.ActionStateScheduled;
import tr.model.action.Recurrence;
import tr.model.project.Project;
import tr.model.topic.Topic;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.CollapseAction;
import au.com.trgtd.tr.view.CollapseCookie;
import au.com.trgtd.tr.view.ExpandAction;
import au.com.trgtd.tr.view.ExpandCookie;
import au.com.trgtd.tr.view.ToggleHideDoneAction;
import au.com.trgtd.tr.view.ToggleHideDoneCookie;
import au.com.trgtd.tr.view.projects.goals.ToggleShowGoalsAction;
import au.com.trgtd.tr.view.projects.goals.ToggleShowGoalsCookie;
import au.com.trgtd.tr.view.topics.TopicChangeAction;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.actions.PasteAction;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import tr.model.topic.TopicChangeCookie;

/**
 * Node for a TR project.
 *
 * @author Jeremy Moore
 */
public class ProjectNode extends AbstractNode implements Observer, EditCookie,
        ToggleHideDoneCookie, CollapseCookie, ExpandCookie, TransferNode,
        AddActionCookie, AddProjectCookie, ViewCookie, ActionFinder,
        ProjectFinder, PrintOutlineCookie, PrintDetailCookie,
        CreateTemplateCookie, ExportTemplateCookie, ExportProjectCookie, 
        ImportProjectCookie, ToggleShowGoalsCookie {

    private static final Logger LOG = Logger.getLogger("tr.view.projects");
    public final Project project;
    private final InstanceContent content;
    protected boolean showDone;

    /**
     * Constructs a new instance for a given project.
     * @param project The project.
     * @param showDone The initial value for showing done children.
     */
    public ProjectNode(Project project, boolean showDone) {
        this(new ProjectChildren(project, showDone), new InstanceContent());
    }

    /**
     * Constructs a new instance for a given project children.
     * @param children The project children.
     */
    public ProjectNode(ProjectChildren children) {
        this(children, new InstanceContent());
    }

    private ProjectNode(ProjectChildren children, InstanceContent content) {
        super(children, new AbstractLookup(content));
        this.project = children.project;
        this.project.addObserver(this);
        this.showDone = children.showDone;
        this.content = content;
        this.content.add(this);
        this.content.add(project);
    }

    @Override
    public String toString() {
        return project.getDescription();
    }

    @Override
    public String getName() {
        return project.getDescription();
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
        String fg = au.com.trgtd.tr.util.HTML.format(project.getTopic().getForeground());
        if (project.isDone()) {
            return "<s><font color='" + fg + "'>" + name + "</font></s>";
        } else {
            return "<font color='" + fg + "'>" + name + "</font>";
        }
    }

    private Image getIcon(int type, boolean opened) {
        Icon icon = project.getIcon(opened);
        if (icon instanceof ImageIcon imageIcon) {
            return imageIcon.getImage();
        }
        return super.getIcon(type);
    }

    @Override
    public Image getIcon(int type) {
        return getIcon(type, false);
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type, true);
    }
    public final static int MASK_ANY = DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT;

    @Override
    public PasteType getDropType(Transferable t, final int type, int index) {
        final Node dropNode = NodeTransfer.node(t, MASK_ANY);
        if (dropNode != null && dropNode instanceof TransferNode) {
            final TransferNode transferNode = (TransferNode) dropNode;
            final boolean isMove = isMoveTransfer(type) || transferNode.isMoveTransfer();
            final Action dropAction = dropNode.getLookup().lookup(Action.class);
            if (dropAction != null) {
                final Action transferAction = (isMove) ? dropAction : (Action) dropAction.copy(getData());
                if (transferAction != null && project.canAdd(transferAction)) {
                    return new PasteType() {

                        public Transferable paste() throws IOException {
                            if (isMove) {
                                move(transferAction, transferNode);
                            } else {
                                transferAction.setThought(null);
                                project.add(transferAction);
                            }
                            return null;
                        }
                    };
                }
            }
            Project p = dropNode.getLookup().lookup(Project.class);
            if (p != null) {
                final Project transferProject = (isMove) ? p : (Project) p.copy(getData());
                if (transferProject != null && project.canAdd(transferProject)) {
                    return new PasteType() {
                        @Override
                        public Transferable paste() throws IOException {
                            if (isMove) {
                                move(transferProject, transferNode);
                            } else {
                                transferProject.setThought(null);
                                project.add(transferProject);
                            }
                            return null;
                        }
                    };
                }
            }
        }
        return null;
    }

    protected void move(final Action tAction, final TransferNode tNode) {
        EventQueue.invokeLater(() -> {
            // check for recurrence
            Recurrence r = tAction.getRecurrence();
            if (r != null && !r.getProject().equals(project)) {
                String ad = tAction.getDescription();
                String pd = project.getDescription();
                String m = org.openide.util.NbBundle.getMessage(ProjectNode.class, "warning.move.recurrent.action", ad, pd);
                String t = org.openide.util.NbBundle.getMessage(ProjectNode.class, "confirm.move.recurrent.action");
                Component p = WindowManager.getDefault().getMainWindow();
                int opt = JOptionPane.showConfirmDialog(p, m, t, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opt == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                // remove recurrence from transfer action
                ((ActionStateScheduled) tAction.getState()).setRecurrence(null);
            }
            // do the move
            tAction.removeFromParent();
            project.add(tAction);
            tNode.setTransferType(DnDConstants.ACTION_COPY);
            select(tAction);
        });
    }

    protected void move(final Project transferProject, final TransferNode transferNode) {
        EventQueue.invokeLater(() -> {
            transferProject.removeFromParent();
            project.add(transferProject);
            transferNode.setTransferType(DnDConstants.ACTION_COPY);
            select(transferProject);
        });
    }

    /**
     * Select the node of the action in the tree if possible, otherwise clear
     * the node selection.
     * @param action The action of the node to select.
     */
    void select(final Action action) {
        EventQueue.invokeLater(() -> {
            select(find(action));
        });
    }

    /**
     * Select the node of the project in the tree if possible, otherwise clear
     * the node selection.
     * @param project The project of the node to select.
     */
    void select(final Project project) {
        EventQueue.invokeLater(() -> {
            select(find(project));
        });
    }

    private void select(Node node) {
        try {
            if (node == null) {
                getExplorerManager().setSelectedNodes(new Node[]{});
            } else {
                Node parentNode = node.getParentNode();
                if (parentNode != null) {
                    getTreeView().expandNode(parentNode);
                }
                getExplorerManager().setSelectedNodes(new Node[]{node});
            }
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void createPasteTypes(Transferable t, List<PasteType> s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_COPY, -1);
        if (paste != null) {
            s.add(paste);
        }
    }

    @Override
    public Cookie getCookie(Class type) {
        if (type == Index.class) {
            return ((ProjectChildren) getChildren()).getIndex();
        }
        if (type == ViewCookie.class) {
            return this;
        }
        if (type == EditCookie.class) {
            return this;
        }
        if (type == AddActionCookie.class) {
            return this;
        }
        if (type == AddProjectCookie.class) {
            return this;
        }
        if (type == ExpandCookie.class) {
            return this;
        }
        if (type == CollapseCookie.class) {
            return this;
        }
        if (type == ToggleHideDoneCookie.class) {
            return this;
        }
        if (type == TopicChangeCookie.class) {
            return project;
        }
        if (type == PrintOutlineCookie.class) {
            return this;
        }
        if (type == PrintDetailCookie.class) {
            return this;
        }
        if (type == ExportProjectCookie.class) {
            return this;
        }
        if (type == ImportProjectCookie.class) {
            return this;
        }
        if (type == ExportTemplateCookie.class) {
            return this;
        }
        if (type == ToggleShowGoalsCookie.class) {
            return this;
        }
        Children ch = getChildren();
        if (type.isInstance(ch)) {
            return (Cookie) ch;
        }
        return super.getCookie(type);
    }

    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[]{
                    SystemAction.get(EditAction.class),
                    null,
                    SystemAction.get(ExpandAction.class),
                    SystemAction.get(CollapseAction.class),
                    null,
                    SystemAction.get(ToggleHideDoneAction.class),
                    SystemAction.get(ToggleShowGoalsAction.class),
                    null,
                    SystemAction.get(AddActionAction.class),
                    SystemAction.get(AddProjectAction.class),
                    null,
                    SystemAction.get(CopyAction.class),
                    SystemAction.get(CutAction.class),
                    SystemAction.get(PasteAction.class),
                    null,
                    SystemAction.get(DeleteAction.class),
                    null,
                    SystemAction.get(MoveUpAction.class),
                    SystemAction.get(MoveDownAction.class),
                    null,
                    SystemAction.get(TopicChangeAction.class),
                    null,
                    SystemAction.get(PrintOutlineAction.class),
                    SystemAction.get(PrintDetailAction.class),
                    null,
                    SystemAction.get(CreateTemplateAction.class),
                    SystemAction.get(ExportTemplateAction.class),
                    null,
                    SystemAction.get(ExportProjectAction.class),
                    SystemAction.get(ImportProjectAction.class),};
    }

    @Override
    public javax.swing.Action getPreferredAction() {
        return SystemAction.get(EditAction.class);
    }

    @Override
    public boolean canCopy() {
        return true;
    }

    @Override
    public boolean canCut() {
        return true;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    public void destroy() throws IOException {
        delete();
    }

    @Override
    public boolean canRename() {
        return false;
    }

    /** Handle change to data model project by refreshing visible attributes. */
////public synchronized void update(Observable observable, Object object) {
    @Override
    public void update(Observable obs, Object arg) {
        if (project.equals(obs)) {
            // handle project update
            if (arg == null) { // project itself has changed
                // could be a description change
                setName(project.getDescription());
            }
        }
        fireIconChange();
        fireOpenedIconChange();
    }

//    /**
//     * Finds the node of the given object if possible.
//     * @param o The object
//     * @return the node of the object or null if one can not be found.
//     */
//    public Node find(Object o) {
//        if (project.equals(o)) return this;
//
//        Node result = null;
//        for (Node node : getChildren().getNodes()) {
//            if (node instanceof SearchableNode) {
//                result = ((SearchableNode)node).find(o);
//                if (result != null) break;
//            }
//        }
//        return result;
//    }
    /**
     * Finds the node of the given action if possible.
     * @param findAction The action to find.
     * @return the node of the action or null if it can not be found.
     */
    @Override
    public Node find(Action findAction) {
        Node result = null;
        for (Node node : getChildren().getNodes()) {
            if (node instanceof ActionFinder actionFinder) {
                result = actionFinder.find(findAction);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Finds the node of the given project if possible.
     * @param project The project
     * @return the node of the project or null if it can not be found.
     */
    @Override
    public Node find(Project findProject) {
        if (project.getID() == findProject.getID()) {
            return this;
        }
        Node result = null;
        for (Node node : getChildren().getNodes()) {
            if (node instanceof ProjectFinder projectFinder) {
                result = projectFinder.find(findProject);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    public synchronized void setShowDone(boolean showDone) {
        if (this.showDone == showDone) {
            return;
        }
        this.showDone = showDone;
        try {
            setChildren(new ProjectChildren(project, showDone));
        } catch (Exception ex) {
            LOG.warning(ex.getMessage());
        }
        fireCookieChange();
        if (!getTreeView().isExpanded(this)) {
            getTreeView().expandNode(this);
        }
    }

    public TreeView getTreeView() {
        TreeView tv = ProjectsTreeLookup.lookup().getTreeView();
        if (tv != null) {
            return tv;
        }
        ProjectsTreeTopComponent pttc = ProjectsTreeTopComponent.findInstance();
        return pttc == null ? null : pttc.getTreeView();
    }

    public ExplorerManager getExplorerManager() {
        ExplorerManager em = ProjectsTreeLookup.lookup().getExplorerManager();
        if (em != null) {
            return em;
        }
        ProjectsTreeTopComponent pttc = ProjectsTreeTopComponent.findInstance();
        return pttc == null ? null : pttc.getExplorerManager();
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
     * Sets the transfer type.
     * @param type The transfer type which should be DndConstants.ACTION_COPY or
     * DnDConstants.ACTION_MOVE.
     */
    @Override
    public void setTransferType(int type) {
        transferType = type;
    }

    /**
     * Determines whether a move transfer type has been set.
     * @return true if the action value of the latest setTransferAction() was
     * DnDConstants.ACTION_MOVE.
     */
    @Override
    public boolean isMoveTransfer() {
        return (transferType & DnDConstants.ACTION_MOVE) != 0;
    }

    protected boolean isMoveTransfer(int type) {
        return (type & DnDConstants.ACTION_MOVE) != 0;
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

    protected Data getData() {
        return DataLookup.instance().lookup(Data.class);
    }

    @Override
    public boolean canAddAction() {
        return true;
    }

    @Override
    public boolean canAddProject() {
        return true;
    }

    @Override
    public void addAction() {
        EventQueue.invokeLater(() -> {
            addNewAction(null);
        });
    }

    @Override
    public void addAction(final Action sibling) {
        EventQueue.invokeLater(() -> {
            addNewAction(sibling);
        });
    }

    private void addNewAction(Action sibling) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        Action newAction = new Action(data);
        newAction.setDescription(Resources.getText("NewActionDescr"));
        newAction.setTopic(project.getTopic());
        ProjectsPrefs.ActionState defState = ProjectsPrefs.getNewActionState();
        switch (defState) {
            case INACTIVE: {
                newAction.setState(new ActionStateInactive());
                break;
            }
            case DOASAP: {
                newAction.setState(new ActionStateASAP());
                break;
            }
            case SCHEDULED: {
                newAction.setState(new ActionStateScheduled());
                break;
            }
            case DELEGATED: {
                newAction.setState(new ActionStateScheduled());
                break;
            }
        }
        newAction.setPriority(project.getPriority());

        if (sibling == null) {
            if (!project.add(newAction)) {
                return;
            }
        } else {
            int index = project.indexOf(sibling);
            if (index == -1) {
                if (!project.add(newAction)) {
                    return;
                }
            } else {
                project.add(index + 1, newAction);
            }
        }

        // try to select the new action node
        try {
            Thread.sleep(500); // wait a bit for node to be created

        } catch (InterruptedException ex) {
        }

        Node node = find(newAction);
        if (node == null) {
            return;
        }
        try {
            getExplorerManager().setSelectedNodes(new Node[]{node});
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Node selection failed. {0}", ex.getMessage());
        }

        getTreeView().requestFocus();

        EditCookie cookie = node.getCookie(EditCookie.class);
        if (cookie != null) {
            cookie.edit();
        }
    }

    @Override
    public void addProject() {
        EventQueue.invokeLater(() -> {
            addNewProject(null);
        });
    }

    @Override
    public void addProject(final Action sibling) {
        EventQueue.invokeLater(() -> {
            addNewProject(sibling);
        });
    }

    private void addNewProject(Action sibling) {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        Project newProject = new Project(data);
        newProject.setDescription(Resources.getText("NewProjectDescr"));
        newProject.setTopic(project.getTopic());

        if (project.isRoot()) {
            newProject.setSequencing(ProjectsPrefs.isSequencing());
            newProject.setSeqIncludeProjects(ProjectsPrefs.getAutoSeqIncludeSubprojects());
            newProject.setSeqIncludeScheduled(ProjectsPrefs.getAutoSeqIncludeScheduled());
            newProject.setSeqIncludeDelegated(ProjectsPrefs.getAutoSeqIncludeDelegated());
        } else {
            newProject.setSequencing(project.isSequencing());
            newProject.setSeqIncludeProjects(project.isSeqIncludeProjects());
            newProject.setSeqIncludeScheduled(project.isSeqIncludeScheduled());
            newProject.setSeqIncludeDelegated(project.isSeqIncludeDelegated());
        }

        newProject.setPriority(project.getPriority());

//      project.add(newProject);
        if (sibling == null) {
            if (!project.add(newProject)) {
                return;
            }
        } else {
            int index = project.indexOf(sibling);
            if (index == -1) {
                if (!project.add(newProject)) {
                    return;
                }
            } else {
                project.add(index + 1, newProject);
            }
        }

        // try to select the new action node
        try {
            Thread.sleep(500); // wait a bit for node to be created
        } catch (InterruptedException ex) {
        }

        Node node = find(newProject);
        if (node == null) {
            return;
        }
        try {
            getExplorerManager().setSelectedNodes(new Node[]{node});
        } catch (Exception ex) {
            LOG.log(Level.INFO, "Node selection failed. {0}", ex.getMessage());
        }

        getTreeView().requestFocus();

        EditCookie cookie = node.getCookie(EditCookie.class);
        if (cookie != null) {
            cookie.edit();
        }
    }

    @Override
    public void edit() {
        EditorTopComponent.findInstance().edit(this);
    }

    public void changeTopic(Topic topic) {
        project.setTopic(topic);
    }

    @Override
    public void toggleHideDone() {
        setShowDone(!showDone);
    }

    @Override
    public void view() {
        EditorTopComponent.findInstance().view(this);
    }

    @Override
    public void printOutline() {
        try {
            au.com.trgtd.tr.report.project.outline.ReportImpl report =
                    new au.com.trgtd.tr.report.project.outline.ReportImpl();
            List<Project> projects = new Vector<>();
            projects.add(project);
            report.process(projects, showDone);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Report exception: {0}", ex.getMessage());
        }
    }

    @Override
    public void printDetail() {
        try {
            au.com.trgtd.tr.report.project.detail.ReportImpl report =
                    new au.com.trgtd.tr.report.project.detail.ReportImpl();
            report.process(project, showDone);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Report exception: {0}", ex.getMessage());
        }
    }

    @Override
    public void exportTemplate() {
        ExportTemplate exportTemplate = new ExportTemplate();
        exportTemplate.export(project);
    }

    public boolean canDelete() {
        return true;
    }

    public void delete() {
        project.removeFromParent();
    }

    @Override
    public void exportProject() {
        ExportProject exportProject = new ExportProject();
        exportProject.export(project);
    }

    @Override
    public void importProject() {
        ImportProject importProject = new ImportProject();
        importProject.process(project);
    }

    @Override
    public void expand() {
        TreeView view = getTreeView();
        if (view == null) {
            return;
        }
//        view.expandNode(this);
//        for (Node node : getChildren().getNodes()) {
//            ExpandCookie c = node.getCookie(ExpandCookie.class);
//            if (c != null) {
//                c.expand();
//            }
//        }
        expand(view, this);
    }

    private void expand(TreeView view, Node node) {
        if (node.isLeaf()) {
            return;
        }

        view.expandNode(node);

        for (Node subnode : node.getChildren().getNodes()) {
            if (!subnode.isLeaf()) {
                expand(view, subnode);
            }
        }
    }

    @Override
    public void collapse() {
        TreeView view = getTreeView();
        if (view == null) {
            return;
        }
//        for (Node node : getChildren().getNodes()) {
//            CollapseCookie c = node.getCookie(CollapseCookie.class);
//            if (c != null) {
//                c.collapse();
//            }
//        }
//        view.collapseNode(this);
        collapse(view, this);
    }

    private void collapse(TreeView view, Node node) {
        if (node.isLeaf()) {
            return;
        }
        for (Node subnode : node.getChildren().getNodes()) {
            if (!subnode.isLeaf()) {
                collapse(view, subnode);
            }
        }
        view.collapseNode(node);
    }

    @Override
    public void toggleShowGoals() {
        ProjectChildren children = (ProjectChildren) getChildren();

        setShowGoals(!children.isShowGoals());

//        TreeView view = getTreeView();
//        if (view != null) {
//            view.expandNode(this);
//            view.expandNode(children.goalsNode);
//        }
    }

    @Override
    public void setShowGoals(boolean show) {
        final ProjectChildren children = (ProjectChildren) getChildren();

        children.setShowGoals(show);

        for (Node node : children.getNodes()) {
            ToggleShowGoalsCookie c = node.getCookie(ToggleShowGoalsCookie.class);
            if (c != null) {
                c.setShowGoals(show);
            }
        }

        if (show) {
            final TreeView view = getTreeView();
            if (view != null) {
                SwingUtilities.invokeLater(() -> {
                    view.expandNode(children.goalsNode);
                });
                view.expandNode(this);
            }
        }

    }

    // Mantis ID: 0001959
    @Override
    public void createTemplate() {
        CreateTemplate createTemplate = new CreateTemplate();
        createTemplate.create(project);
    }
}
