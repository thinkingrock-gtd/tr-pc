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

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import java.util.Collections;
import java.util.logging.Logger;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.project.Project;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.Window;
import au.com.trgtd.tr.view.actn.ActionPanel;
import au.com.trgtd.tr.view.project.ProjectPanel;
import au.com.trgtd.tr.view.projects.goals.ProjectGoalNode;
import au.com.trgtd.tr.view.goals.ProjectGoalPanel;
import au.com.trgtd.tr.view.projects.goals.ProjectGoalsNode;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.openide.awt.Toolbar;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import tr.model.goals.ctrl.GoalCtrl;

/**
 * Top component window for the editing an action or a project.
 *
 * @author Jeremy Moore
 */
public final class EditorTopComponent extends Window implements Lookup.Provider {

    private static final Logger LOG = Logger.getLogger("tr.view.projects");
    private static final String PREFERRED_ID = "EditorTopComponent";
    private static EditorTopComponent instance;
    private final InstanceContent content = new InstanceContent();
    private final Lookup lookup = new AbstractLookup(content);
    private boolean initialised;
    private ActionPanel actionPanel;
    private ProjectPanel projectPanel;
    private ProjectGoalPanel goalPanel;
    private Object viewModel;
    private JPanel emptyPanel;
    private JToolBar toolbar;
    private enum Editing {Action, Project, Goal};
    private Editing editing;

    /* Constructs the instance. */
    private EditorTopComponent() {
        initComponents();
    }

    private void initComponents() {
        emptyPanel = new JPanel();
        emptyPanel.setBackground(ViewUtils.COLOR_PANEL_BG);
        setLayout(new BorderLayout());
    }
    
    private JToolBar getToolbar() {
        if (toolbar != null) {
            return toolbar;
        }
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(au.com.trgtd.tr.view.EditAction.class)
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

    private void initialise() {
        if (initialised) {
            return;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            LOG.severe("Data model could not be found.");
            return;
        }
        projectPanel = new ProjectPanel();
        actionPanel = new ActionPanel();
        goalPanel = new ProjectGoalPanel();
        initialised = true;
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        Data data = DataLookup.instance().lookup(Data.class);
        if (data != null) {
            view(data.getRootProjects());
        }
    }

    /**
     * Determines whether a given action is currently shown.
     * @param action The given action.
     * @return true if the given action is currently shown.
     */
    public boolean isViewing(Action action) {
        if (action == null) {
            return false;
        }
        if (actionPanel == null) {
            return false;
        }
        if (editing != Editing.Action) {
            return false;
        }
        return action.equals(actionPanel.getModel());
    }

    /**
     * Determines whether a given project is currently shown.
     * @param action The given project.
     * @return true if the given project is currently shown.
     */
    public boolean isViewing(Project project) {
        if (project == null) {
            return false;
        }
        if (projectPanel == null) {
            return false;
        }
        if (editing != Editing.Project) {
            return false;
        }
        return project.equals(projectPanel.getModel());
    }

    /**
     * Determines whether a given project is currently shown.
     * @param action The given project.
     * @return true if the given project is currently shown.
     */
    public boolean isViewing(GoalCtrl goal) {
        if (goal == null) {
            return false;
        }
        if (goalPanel == null) {
            return false;
        }
        if (editing != Editing.Goal) {
            return false;
        }
        return goal.equals(goalPanel.getModel());
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized EditorTopComponent getDefault() {
        if (instance == null) {
            instance = new EditorTopComponent();
            instance.initialise();
        }
        return instance;
    }

    /**
     * Obtain the instance. Never call {@link #getDefault} directly!
     */
    public static synchronized EditorTopComponent findInstance() {
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    /** Gets the identifier. */
    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }

    /**
     * Edit an action.
     * @param action The action to edit.
     */
    public void edit(ActionNode actionNode) {
        if (actionPanel == null) {
            return;
        }

        requestActive();

        view(actionNode);

        actionPanel.edit();
    }

    /**
     * Start editing a project.
     * @param project The project to edit.
     */
    public void edit(ProjectNode projectNode) {
        if (projectPanel == null) {
            return;
        }

        requestActive();

        view(projectNode);

        projectPanel.edit();
    }

    /**
     * View a node.
     * @param node The node to view.
     */
    public void view(Node node) {
        if (node instanceof ActionNode actionNode) {
            view(actionNode.action);
            content.set(Collections.singleton(actionNode), null);
        } else if (node instanceof ProjectNode projectNode) {
            view(projectNode.project);
            content.set(Collections.singleton(projectNode), null);
        } else if (node instanceof ProjectGoalsNode projectGoalsNode) {
            initModel();
            clearView();
            setIcon(projectGoalsNode.getIcon(0));
            setName(projectGoalsNode.getName());
            content.set(Collections.singleton(node), null);
        } else if (node instanceof ProjectGoalNode goalNode) {
            view(goalNode.goalCtrl);
            content.set(Collections.singleton(goalNode), null);
        }
    }

    private void initModel() {
        if (viewModel == null) {
            return;
        }
        if (viewModel instanceof Action action) {
            action.removePropertyChangeListener(Action.PROP_STATE, actionStateListener);
        }
        if (viewModel instanceof GoalCtrl goal) {
            goal.removePropertyChangeListener(GoalCtrl.PROP_LEVEL, goalLevelListener);
        }
        viewModel = null;
        editing = null;
    }

    private void initModel(Action action) {
        assert(action != null);
        initModel();
        viewModel = action;
        editing = Editing.Action;
    }

    private void initModel(Project project) {
        assert(project != null);
        initModel();
        viewModel = project;
        editing = Editing.Project;
    }

    private void initModel(GoalCtrl goal) {
        assert(goal != null);
        initModel();
        viewModel = goal;
        editing = Editing.Goal;
        goal.addPropertyChangeListener(GoalCtrl.PROP_LEVEL, goalLevelListener);
    }

    /**
     * Clear the view.
     */
    private void clearView() {
        synchronized (this) {
            setName("");
            setToolTipText("");
            setIcon(null);
            removeAll();
            add(emptyPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    /* View a goal. */
    private void view(GoalCtrl goal) {
        assert (goal != null);

        if (!isOpened()) {
            return;
        }
        if (isViewing(goal)) {
            return;
        }

        synchronized (this) {
            initModel(goal);
            goalPanel.initModel(goal);
            setName(NbBundle.getMessage(getClass(), "goal"));
            setToolTipText("");
            setIcon(goal.getLevel().getGoalsIcon().icon.getImage());
            removeAll();
            add(getToolbar(), GUIPrefs.getBorderLayoutButtonsPosition());
            add(goalPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    /*
     * View a project.
     * @param project The project to view.
     */
    private void view(Project project) {
        assert (project != null);

        if (!isOpened()) {
            return;
        }
        if (isViewing(project)) {
            return;
        }

        synchronized (this) {
            initModel(project);
            setName(NbBundle.getMessage(getClass(), "CTL_ProjectTopComponent"));
            setToolTipText(NbBundle.getMessage(getClass(), "TTT_ProjectTopComponent"));
            setIcon(project.getIcon(false).getImage());
            if (project.isEditable()) {
                removeAll();
                projectPanel.initModel(project);
                add(projectPanel, BorderLayout.CENTER);
            } else {
                setName(project.getDescription());
                removeAll();
                add(emptyPanel, BorderLayout.CENTER);
            }
            revalidate();
            repaint();
        }
    }

    /**
     * View an action.
     * @param action The action to view.
     */
    private void view(final Action action) {
        if (!isOpened()) {
            return;
        }
        if (isViewing(action)) {
            return;
        }

        synchronized (this) {
            initModel(action);
            setName(NbBundle.getMessage(getClass(), "CTL_ActionTopComponent"));
            setToolTipText(NbBundle.getMessage(getClass(), "TTT_ActionTopComponent"));
            setIcon(action.getIcon(false).getImage());
            if (action != null && action.isEditable()) {
                removeAll();
                actionPanel.initModel(action);
                add(actionPanel, BorderLayout.CENTER);
            } else {
                removeAll();
                add(emptyPanel, BorderLayout.CENTER);
            }
            revalidate();
            repaint();
        }
    }

    private void refreshIcon(final Action action) {
        if (action != null) {
            EventQueue.invokeLater(() -> {
                setIcon(action.getIcon(false).getImage());
            });
        }
    }

    private void refreshIcon(final GoalCtrl goal) {
        if (goal != null) {
            EventQueue.invokeLater(() -> {
                setIcon(goal.getLevel().getGoalsIcon().icon.getImage());
            });
        }
    }

    private final PropertyChangeListener actionStateListener =
            new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (viewModel instanceof Action action) {
                        refreshIcon(action);
                    }
                }
            };

    private final PropertyChangeListener goalLevelListener =
            new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (viewModel instanceof GoalCtrl goalCtrl) {
                        refreshIcon(goalCtrl);
                    }
                }
            };

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.projects");
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void takeFocus() {
        ActionNode actionNode = lookup.lookup(ActionNode.class);
        if (actionNode != null) {
            actionPanel.requestFocusInWindow();
        } else {
            ProjectNode projectNode = lookup.lookup(ProjectNode.class);
            if (projectNode != null) {
                projectPanel.requestFocusInWindow();
            }
        }
    }
}
