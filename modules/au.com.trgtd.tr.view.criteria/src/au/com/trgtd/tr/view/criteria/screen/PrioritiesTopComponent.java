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
package au.com.trgtd.tr.view.criteria.screen;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.EditAction;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.Window;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultEditorKit;
import net.miginfocom.swing.MigLayout;
import org.netbeans.swing.outline.Outline;
import org.openide.actions.DeleteAction;
import org.openide.actions.MoveDownAction;
import org.openide.actions.MoveUpAction;
import org.openide.awt.Toolbar;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Node.Property;
import org.openide.util.*;
import org.openide.util.actions.SystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Priority values top component.
 */
public final class PrioritiesTopComponent extends Window implements ExplorerManager.Provider {

    private final static String PREFERRED_ID = "PrioritiesTopComponent";
    private final static String ICON_PATH = "au/com/trgtd/tr/view/criteria/screen/priority.png";

    private static PrioritiesTopComponent instance;

    private final ExplorerManager explorerManager;
    private final OutlineView outlineView;
    private final JToolBar toolbar;
    private final JCheckBox usedCbx;

    /**
     * Creates a new instance.
     */
    public PrioritiesTopComponent() {
        setName(NbBundle.getMessage(PrioritiesTopComponent.class, "CTL_PrioritiesTopComponent"));
        setToolTipText(NbBundle.getMessage(PrioritiesTopComponent.class, "TTT_PrioritiesTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        outlineView = new OutlineView();
        explorerManager = new ExplorerManager();
        
        PriorityNodeRoot rootNode = new PriorityNodeRoot(new PriorityChildren(outlineView, explorerManager));
        
        explorerManager.setRootContext(rootNode);
        
        Property[] props = new Property[] {
            new PropertyMapped(null),
        };
        outlineView.setProperties(props);
        outlineView.setTreeSortable(false);
        outlineView.getOutline().setRootVisible(false);
        outlineView.getOutline().setAutoResizeMode(Outline.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        TableColumnModel tcm = outlineView.getOutline().getColumnModel();
        tcm.setColumnSelectionAllowed(false);

        TableColumn tc = tcm.getColumn(0);
        tc.setHeaderValue(getName());
        
        
//      ((JTable)outlineView.getOutline()).getTableHeader().setV.setBackground(Color.yellow);
        
        outlineView.setOpaque(true);
        outlineView.setBackground(Color.white);
        outlineView.getOutline().setBackground(Color.white);
        outlineView.getOutline().setShowGrid(false);
        outlineView.getOutline().setRowSelectionAllowed(true);
        
        
        
        // do not show headings
        JTableHeader header = ((JTable)outlineView.getOutline()).getTableHeader();
        header.setMaximumSize(new Dimension(0,0));
        header.setMinimumSize(new Dimension(0,0));
        header.setPreferredSize(new Dimension(0,0));
        
        
//        tc.setMinWidth(150);
//        tc.setPreferredWidth(250);
//        tc.setResizable(true);

//        tc = tcm.getColumn(1);
//        tc.setPreferredWidth(68);
//        tc.setResizable(false);
//
//        int w = 90;
//        for (int c = 2; c < tcm.getColumnCount(); c++) {
//            tc = tcm.getColumn(c);
//            tc.setPreferredWidth(w);
//            tc.setResizable(false);
//        }

        
        outlineView.expandNode(rootNode);
        
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

        
        final Data data = (Data) DataLookup.instance().lookup(Data.class);
        
        usedCbx = new JCheckBox(NbBundle.getMessage(getClass(), "use.priority.criteria"));
        usedCbx.addActionListener((ActionEvent ae) -> {
            data.getPriorityCriterion().setUse(usedCbx.isSelected());
        });
        usedCbx.setSelected(data.getPriorityCriterion().isUse());
        
        toolbar = getToolBar();

        JPanel toolbarPanel = getToolbarPanel();
        

        setLayout(new BorderLayout());
        add(outlineView, BorderLayout.CENTER);

        String position = GUIPrefs.getButtonsPosition();
        if (position.equals(GUIPrefs.BUTTONS_POSITION_TOP)) {
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(toolbarPanel, BorderLayout.NORTH);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_BOTTOM)) {
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(toolbarPanel, BorderLayout.SOUTH);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_LEFT)) {
            toolbar.setOrientation(JToolBar.VERTICAL);
            add(toolbarPanel, BorderLayout.WEST);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_RIGHT)) {
            toolbar.setOrientation(JToolBar.VERTICAL);
            add(toolbarPanel, BorderLayout.EAST);
        }

        // data lookup listener to force re-initialisation if data changes
        Lookup.Result<Data> dataResult = DataLookup.instance().lookupResult(Data.class);
        dataResult.addLookupListener((LookupEvent lookupEvent) -> {
            Data data1 = DataLookup.instance().lookup(Data.class);
            if (data1 != null) {
                explorerManager.setRootContext(new PriorityNodeRoot(new PriorityChildren(outlineView, explorerManager)));
            }
        });
        
        
        
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    private JToolBar getToolBar() {
        
        SystemAction moveDownAction = SystemAction.get(MoveDownAction.class);
        if (null != moveDownAction) {
            moveDownAction.setIcon(Icons.Down);
        }
        SystemAction moveUpAction = SystemAction.get(MoveUpAction.class);
        if (null != moveUpAction) {
            moveUpAction.setIcon(Icons.Up);
        }
        
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(PriorityAddAction.class),
            SystemAction.get(EditAction.class),
            SystemAction.get(DeleteAction.class),            
            moveUpAction,
            moveDownAction,
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

    private JPanel getToolbarPanel() {
        JPanel p = new JPanel(new MigLayout("", "0[]12[]0", "0[]0"));
        p.add(toolbar, "align left, aligny 50%");
        p.add(usedCbx, "align left, aligny 50%, wrap");
        p.setOpaque(true);
        p.setBackground(toolbar.getBackground());
        p.setBorder(ViewUtils.BORDER_TOOLBAR);
        return p;
    }    
    
    /** Gets default instance. Do not use directly.  */
    public static synchronized PrioritiesTopComponent getDefault() {
        if (instance == null) {
            instance = new PrioritiesTopComponent();
        }
        return instance;
    }

    /** Obtain the TopComponent instance. Never call {@link #getDefault} directly! */
    public static synchronized PrioritiesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(PrioritiesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof PrioritiesTopComponent comp) {
            return comp;
        }
        Logger.getLogger(PrioritiesTopComponent.class.getName()).warning(
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
        return new HelpCtx("tr.view.criteria");
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

}
