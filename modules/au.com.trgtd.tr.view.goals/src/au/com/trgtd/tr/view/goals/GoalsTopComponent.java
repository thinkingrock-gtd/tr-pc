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
package au.com.trgtd.tr.view.goals;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.view.*;
import au.com.trgtd.tr.view.goals.projects.ToggleShowProjectsAction;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.swing.outline.Outline;
import org.openide.actions.DeleteAction;
import org.openide.awt.Toolbar;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.GoalsCtrl;
import tr.model.goals.ctrl.GoalsCtrlLookup;

/**
 * Goals top component.
 */
public final class GoalsTopComponent extends Window 
        implements ExplorerManager.Provider, Lookup.Provider {

    private final static String PREFERRED_ID = "GoalsTopComponent";
    private final static String ICON_PATH = "au/com/trgtd/tr/view/goals/resource/Goal.png";
    private static GoalsTopComponent instance;
    private final GoalsCtrl goalsCtrl = GoalsCtrlLookup.getGoalsCtrl();
    private final ExplorerManager manager;
    private final OutlineView outlineView;
    private final JToolBar toolbar;
    private final GoalNodeRoot rootNode;

    /**
     * Creates a new instance.
     */
    public GoalsTopComponent() {
        setName(NbBundle.getMessage(GoalsTopComponent.class, "CTL_GoalsTopComponent"));
        setToolTipText(NbBundle.getMessage(GoalsTopComponent.class, "TTT_GoalsTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        outlineView = new OutlineView();
        outlineView.setAllowedDragActions(DnDConstants.ACTION_COPY_OR_MOVE);

        rootNode = new GoalNodeRoot(outlineView, goalsCtrl.getRootGoal());

        manager = new ExplorerManager();
        manager.setRootContext(rootNode);

        GoalCtrl goal = manager.getExploredContext().getLookup().lookup(GoalCtrl.class);
        Property<PropertySupport.ReadOnly<?>>[] props = new Property[]{
            new PropertyAchieved(goal),
            new PropertyLevel(goal),
            new PropertyTopic(goal),};

        outlineView.setProperties(props);
        outlineView.setTreeSortable(false);
        outlineView.expandNode(rootNode);
        outlineView.getOutline().setColumnSelectionAllowed(false);
        outlineView.getOutline().setCellSelectionEnabled(false);
        outlineView.getOutline().setRowSelectionAllowed(true);

        Outline outline = outlineView.getOutline();
        outline.setRootVisible(true);
        outline.setAutoResizeMode(Outline.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        TableColumnModel tcm = outline.getColumnModel();
        tcm.setColumnSelectionAllowed(false);

        TableColumn tc = tcm.getColumn(0);
        tc.setHeaderValue(getName());
        tc.setMinWidth(150);
        tc.setPreferredWidth(250);
        tc.setResizable(true);

        tc = tcm.getColumn(1);
        tc.setPreferredWidth(68);
        tc.setMaxWidth(68);
        tc.setResizable(true);

        int w = 150;
        for (int c = 2; c < tcm.getColumnCount(); c++) {
            tc = tcm.getColumn(c);
            tc.setPreferredWidth(w);
            tc.setResizable(false);
        }

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

        toolbar = getToolBar();

        setLayout(new BorderLayout());
        add(outlineView, BorderLayout.CENTER);

        String position = GUIPrefs.getButtonsPosition();
        if (position.equals(GUIPrefs.BUTTONS_POSITION_TOP)) {
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(getToolBar(), BorderLayout.NORTH);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_BOTTOM)) {
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(getToolBar(), BorderLayout.SOUTH);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_LEFT)) {
            toolbar.setOrientation(JToolBar.VERTICAL);
            add(getToolBar(), BorderLayout.WEST);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_RIGHT)) {
            toolbar.setOrientation(JToolBar.VERTICAL);
            add(getToolBar(), BorderLayout.EAST);
        }

        goalsCtrl.addPropertyChangeListener(GoalsCtrl.PROP_GOALS_DATA, (PropertyChangeEvent evt) -> {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data != null) {
                GoalNodeRoot rootNode1 = new GoalNodeRoot(outlineView, goalsCtrl.getRootGoal());
                manager.setRootContext(rootNode1);
            }
        });
    }

    private JToolBar getToolBar() {
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(AddAction.class),
            SystemAction.get(EditAction.class),
            SystemAction.get(DeleteAction.class),
            null,
            SystemAction.get(ToggleShowProjectsAction.class),
            SystemAction.get(ExpandAction.class),
            SystemAction.get(CollapseAction.class)
        };
        JToolBar _toolbar = SystemAction.createToolbarPresenter(actions);
        _toolbar.setUI((new Toolbar()).getUI());
        _toolbar.setFloatable(false);

        Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;

        for (Component component : _toolbar.getComponents()) {
            if (component instanceof AbstractButton) {
                component.setPreferredSize(buttonSize);
                component.setMinimumSize(buttonSize);
                component.setMaximumSize(buttonSize);
                component.setSize(buttonSize);
            }
        }
        return _toolbar;
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        Node[] nodes = manager.getSelectedNodes();
        if (nodes.length == 0) {
            try {
                manager.setSelectedNodes(new Node[]{manager.getRootContext()});
            } catch (PropertyVetoException ex) {
            }
        }
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        rootNode.bump();
        ExplorerUtils.activateActions(manager, true);
    }

    @Override
    protected void componentDeactivated() {
        ExplorerUtils.activateActions(manager, false);
        super.componentDeactivated();
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. de-serialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized GoalsTopComponent getDefault() {
        if (instance == null) {
            instance = new GoalsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the LevelsTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GoalsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(GoalsTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof GoalsTopComponent goalsTopComponent) {
            return goalsTopComponent;
        }
        Logger.getLogger(GoalsTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
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

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.goals");
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public synchronized void select(GoalCtrl goalCtrl) {
        if (!isOpened()) {
        }

        Node node = rootNode.find(goalCtrl.getID());
        if (node == null) {
            return;
        }

        requestActive();

        try {
            manager.setExploredContext(node);
            manager.setSelectedNodes(new Node[] {node});
            SwingUtilities.invokeLater(() -> {
                outlineView.getOutline().requestFocusInWindow();
            });
        } catch (Exception ex) {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
