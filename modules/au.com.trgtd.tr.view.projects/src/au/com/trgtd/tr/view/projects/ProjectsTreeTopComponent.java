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

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JToolBar;
import org.openide.awt.Toolbar;
import org.openide.cookies.ViewCookie;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.project.Project;
import tr.model.project.ProjectProjects;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.CollapseAction;
import au.com.trgtd.tr.view.ExpandAction;
import au.com.trgtd.tr.view.ReprocessAction;
import au.com.trgtd.tr.view.ToggleHideDoneAction;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.projects.goals.ToggleShowGoalsAction;
import java.awt.BorderLayout;
import javax.swing.ActionMap;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultEditorKit;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;

/**
 * Top component for the review projects tree.
 *
 * @author Jeremy Moore
 */
public class ProjectsTreeTopComponent extends TopComponent 
        implements ExplorerManager.Provider, Lookup.Provider, LookupListener {

    private static final Logger LOG = Logger.getLogger("tr.view.projects");
    private static final String PREFERRED_ID = "ProjectsTopComponent";
    private static ProjectsTreeTopComponent instance;
    private final ExplorerManager manager = new ExplorerManager();
    private JScrollPane projectsPane;
    private boolean initialised;
    private Lookup.Result dataResult;
    private Lookup.Result itemResult;
    private JToolBar toolbar;

    /** Constructs a new instance. */
    protected ProjectsTreeTopComponent() {
        setName(getText("CTL_ProjectsTopComponent"));
        setToolTipText(getText("TTT_ProjectsTopComponent"));
        setIcon(Icons.Projects.getImage());
        initComponents();
        initialise();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        projectsPane = new BeanTreeView();
        add(projectsPane, BorderLayout.CENTER);
    }

    private void initialise() {
        if (initialised) {
            return;
        }

        add(getToolbar(), GUIPrefs.getBorderLayoutButtonsPosition());

        // data lookup listener to force re-initialisation if data changes
        if (dataResult == null) {
            dataResult = DataLookup.instance().lookupResult(Data.class);
            dataResult.addLookupListener((LookupEvent lookupEvent) -> {
                initialised = false;
            });
        }

        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        manager.setRootContext(new ProjectRootNode((ProjectProjects) data.getRootProjects(), false));
        manager.getRootContext().setDisplayName(getText("CTL_ProjectsNode"));

        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(manager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(manager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(manager));
        map.put("delete", ExplorerUtils.actionDelete(manager, true));

        try {
            associateLookup(ExplorerUtils.createLookup(manager, map));
        } catch (IllegalStateException ex) {
            // already associated - ignore
        }

        initialised = true;
    }

    // Mantis:772
    private JToolBar getToolbar() {
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(AddActionAction.class),
            SystemAction.get(AddProjectAction.class),
            SystemAction.get(ReprocessAction.class),
            SystemAction.get(ProjectiseAction.class),
            SystemAction.get(ToggleHideDoneAction.class),
            SystemAction.get(ToggleShowGoalsAction.class),
            SystemAction.get(ExpandAction.class),
            SystemAction.get(CollapseAction.class)
        };
        toolbar = SystemAction.createToolbarPresenter(actions);
        toolbar.setUI((new Toolbar()).getUI());
        toolbar.setFloatable(false);
        toolbar.setOrientation(GUIPrefs.getToolBarOrientation());

        Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;
        for (Component component : toolbar.getComponents()) {
            if (component instanceof AbstractButton) {
                component.setPreferredSize(buttonSize);
                component.setMinimumSize(buttonSize);
                component.setMaximumSize(buttonSize);
                component.setSize(buttonSize);
            }
        }
        toolbar.setBorder(ViewUtils.BORDER_TOOLBAR);
        return toolbar;
    }

    @Override
    public void componentOpened() {
        super.componentOpened();

        initialise();
    }

    private void addItemListener() {
        itemResult = getLookup().lookupResult(Item.class);
        itemResult.addLookupListener(this);
        itemResult.allInstances();
    }

    private void removeItemListener() {
        if (itemResult != null) {
            itemResult.removeLookupListener(this);
            itemResult = null;
        }
    }

    @Override
    public void componentActivated() {
        super.componentActivated();

        initialise();

        ExplorerUtils.activateActions(manager, true);

        addItemListener();

        // If there is no selected node then select the root node, otherwise
        // make sure the selected project or action is viewed in the editor.
        Node[] selectedNodes = manager.getSelectedNodes();
        if (selectedNodes == null || selectedNodes.length == 0) {
            try {
                manager.setSelectedNodes(new Node[]{manager.getRootContext()});
                takeFocus();
            } catch (Exception ex) {
            }
        } else if (selectedNodes[0] instanceof ProjectNode projectNode) {
            EventQueue.invokeLater(() -> {
                EditorTopComponent.findInstance().view(projectNode);
                takeFocus();
            });
        } else if (selectedNodes[0] instanceof ActionNode actionNode) {
            EventQueue.invokeLater(() -> {
                EditorTopComponent.findInstance().view(actionNode);
                takeFocus();
            });
        }
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();

        ExplorerUtils.activateActions(manager, false);

        removeItemListener();
    }

    /**
     * Handle changed node selection by calling the editor to view the selected
     * project or action node.
     */
    public void resultChanged(LookupEvent lookupEvent) {
        EventQueue.invokeLater(() -> {
            Node[] nodes = manager.getSelectedNodes();
            Node node = nodes.length > 0 ? nodes[0] : null;
            EditorTopComponent.findInstance().view(node);
        });
    }

    public void clearSelection() {
        try {
            manager.setSelectedNodes(new Node[0]);
        } catch (Exception ex) {
        }
    }

    private String getText(String key) {
        return NbBundle.getMessage(ProjectsTreeTopComponent.class, key);
    }

    public TreeView getTreeView() {
        return (TreeView) projectsPane;
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized ProjectsTreeTopComponent getDefault() {
        if (instance == null) {
            instance = new ProjectsTreeTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ProjectsTopComponent instance. Never call {@link #getDefault}
     * directly!
     */
    public static synchronized ProjectsTreeTopComponent findInstance() {
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.projects");
    }

    /**
     * Sets the show done option.
     * @param b The option.
     */
    public void setShowDone(boolean b) {
        ProjectRootNode rootNode = (ProjectRootNode) manager.getRootContext();
        rootNode.setShowDone(b);
    }

    public boolean isSelected(Action action) {
        Node[] nodes = manager.getSelectedNodes();
        if (nodes != null && nodes.length > 0) {
            if (nodes[0] instanceof ActionNode actionNode) {
                return Utils.equal(actionNode.action, action);
            }
        }
        return false;
    }

    /**
     * Selects the node for the given action if possible.
     * @param action The action.
     */
    public synchronized void select(Action action) {
        if (!isOpened()) {
            LOG.info("NOT OPENED");
            return;
        }
        if (action.isSingleAction()) {
            LOG.info("SINGLE ACTION");
            return;
        }

        ProjectNode rootNode = (ProjectNode) manager.getRootContext();

        Node node = rootNode.find(action);
        if (node == null) {
            LOG.info("ACTION NOT FOUND");
            return;
        }

        requestVisible();

        try {
            manager.setExploredContextAndSelection(node, new Node[]{node});
            ViewCookie cookie = node.getCookie(ViewCookie.class);
            if (cookie != null) {
                cookie.view();
            }
        } catch (Exception ex) {
            LOG.severe("Action node could not be selected in the tree.");
        }
    }

    /**
     * Selects the node for the given project if possible.
     * @param project The project.
     */
    public synchronized void select(Project project) {
        if (!isOpened()) {
            LOG.info("NOT OPENED");
            return;
        }

        ProjectNode rootNode = (ProjectNode) manager.getRootContext();

        Node node = rootNode.find(project);
        if (node == null) {
            LOG.info("PROJECT NOT FOUND");
            return;
        }

        requestVisible();

        try {
            manager.setExploredContextAndSelection(node, new Node[]{node});

            ViewCookie cookie = node.getCookie(ViewCookie.class);
            if (cookie != null) {
                cookie.view();
            }
        } catch (Exception ex) {
            LOG.severe("Project node could not be selected in the tree.");
        }
    }

    public void edit(Project project) {
        if (!isOpened()) {
            return;
        }
        ProjectNode rootNode = (ProjectNode) manager.getRootContext();
        Node node = rootNode.find(project);
        if (node == null) {
            return;
        }

        requestVisible();

        try {
            manager.setSelectedNodes(new Node[]{node});
            EditCookie cookie = node.getCookie(EditCookie.class);
            if (cookie != null) {
                cookie.edit();
            }
        } catch (Exception ex) {
            LOG.severe("Project node could not be selected in the tree.");
        }
    }

    public void edit(Action action) {
        if (!isOpened()) {
            return;
        }
        ProjectNode rootNode = (ProjectNode) manager.getRootContext();
        Node node = rootNode.find(action);
        if (node == null) {
            return;
        }

        requestVisible();

        try {
            manager.setSelectedNodes(new Node[]{node});
            EditCookie cookie = node.getCookie(EditCookie.class);
            if (cookie != null) {
                cookie.edit();
            }
        } catch (Exception ex) {
            LOG.severe("Action node could not be selected in the tree.");
        }
    }

    private void takeFocus() {
        getTreeView().requestFocusInWindow();
    }

}
