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
package au.com.trgtd.tr.view.goals.levels;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.Window;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.dnd.DnDConstants;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.JToolBar;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.swing.outline.Outline;
import org.openide.actions.DeleteAction;
import org.openide.awt.Toolbar;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.actions.SystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.goals.ctrl.LevelCtrl;

/**
 * Goal levels top component.
 */
public final class LevelsTopComponent extends Window implements ExplorerManager.Provider {

    private final static String PREFERRED_ID = "LevelsTopComponent";
    private final static String ICON_PATH = "au/com/trgtd/tr/view/goals/resource/Level.png";

    private static LevelsTopComponent instance;

    private final ExplorerManager explorerManager;
    private final OutlineView outlineView;
    private final JToolBar toolbar;


    /**
     * Creates a new instance.
     */
    public LevelsTopComponent() {
        setName(NbBundle.getMessage(LevelsTopComponent.class, "CTL_LevelsTopComponent"));
        setToolTipText(NbBundle.getMessage(LevelsTopComponent.class, "TTT_LevelsTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        outlineView = new OutlineView();
        outlineView.setTreeSortable(false);

        LevelNodeRoot rootNode = new LevelNodeRoot(new LevelChildren(outlineView));

        explorerManager = new ExplorerManager();
        explorerManager.setRootContext(rootNode);
        explorerManager.getRootContext().setDisplayName("Levels");

        LevelCtrl level = explorerManager.getExploredContext().getLookup().lookup(LevelCtrl.class);

        Property<PropertySupport.ReadOnly<?>>[] props = new Property[] {
            new PropertyGoalsIcon(level),
            new PropertyCanHaveProjects(level),
            new PropertyCanHaveStartDate(level),
            new PropertyCanHaveEndDate(level),
            new PropertyCanHaveAccountability(level),
            new PropertyCanHaveBrainstorming(level),
            new PropertyCanHaveObstacles(level),
            new PropertyCanHaveRewards(level),
            new PropertyCanHaveSupport(level),
            new PropertyCanHaveVision(level),
        };

        outlineView.setProperties(props);

        outlineView.setAllowedDragActions(DnDConstants.ACTION_COPY_OR_MOVE);
        outlineView.getOutline().setRootVisible(true);
        outlineView.expandNode(rootNode);

        outlineView.getOutline().setAutoResizeMode(Outline.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        TableColumnModel tcm = outlineView.getOutline().getColumnModel();
        tcm.setColumnSelectionAllowed(false);

        TableColumn tc = tcm.getColumn(0);
        tc.setHeaderValue(getName());
        tc.setMinWidth(150);
        tc.setPreferredWidth(250);
        tc.setResizable(true);

        tc = tcm.getColumn(1);
        tc.setPreferredWidth(68);
        tc.setResizable(false);

        int w = 90;
        for (int c = 2; c < tcm.getColumnCount(); c++) {
            tc = tcm.getColumn(c);
            tc.setPreferredWidth(w);
            tc.setResizable(false);
        }

        ActionMap map = getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(explorerManager));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(explorerManager));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(explorerManager));
        map.put("delete", ExplorerUtils.actionDelete(explorerManager, true));
        try {
            associateLookup(ExplorerUtils.createLookup(explorerManager, map));
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

        // data lookup listener to force re-initialisation if data changes
        Lookup.Result<Data> dataResult = DataLookup.instance().lookupResult(Data.class);
        dataResult.addLookupListener((LookupEvent lookupEvent) -> {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data != null) {
                explorerManager.setRootContext(new LevelNodeRoot(new LevelChildren(outlineView)));
            }
        });
    }

    private JToolBar getToolBar() {
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(LevelAddAction.class),
            SystemAction.get(EditAction.class),
            SystemAction.get(DeleteAction.class)
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

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized LevelsTopComponent getDefault() {
        if (instance == null) {
            instance = new LevelsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the LevelsTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized LevelsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(LevelsTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof LevelsTopComponent levelsTopComponent) {
            return levelsTopComponent;
        }
        Logger.getLogger(LevelsTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        LevelsTopComponent singleton = LevelsTopComponent.getDefault();
        singleton.readPropertiesImpl(p);
        return singleton;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.levels");
    }

    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
