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
package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.ctlr.DayPanelCtlr;
import au.com.trgtd.tr.cal.view.DateChangePanel;
import au.com.trgtd.tr.cal.view.DatePanel;
import au.com.trgtd.tr.cal.view.DayOfMonthChooserPanel;
import au.com.trgtd.tr.cal.view.DayPanel;
import au.com.trgtd.tr.cal.view.Period;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.cal.tree.RootNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
import net.miginfocom.swing.MigLayout; 
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * Calendar day view top component.
 */
public final class DayTopComponent extends TopComponent
        implements ExplorerManager.Provider, Lookup.Provider, LookupListener {

    private final static String ICON_PATH = "au/com/trgtd/tr/view/cal/resource/day.png";
    private final ExplorerManager manager = new ExplorerManager();
    private final DateCtlr dateCtlr = DateCtlr.DEFAULT;
    private BeanTreeView treeView;
    private ShowHideDoneAction showDoneAction;

    /**
     * Creates a new instance.
     */
    public DayTopComponent() {
        setName(NbBundle.getMessage(DayTopComponent.class, "CTL_DayTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        this.initComponents();
    }

    private void initComponents() {
        CalModelImp calModel = new CalModelImp();

        showDoneAction = new ShowHideDoneAction(calModel, dateCtlr);
        
        RootNode rootNode = new RootNode(dateCtlr, calModel);
        manager.setRootContext(rootNode);
        
        treeView = new BeanTreeView();
        treeView.setRootVisible(false);
        treeView.setBackground(Color.WHITE);

        dateCtlr.addPropertyChangeListener((PropertyChangeEvent pce) -> {
            treeView.expandAll();
        });
        
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

        JPanel treePanel = new JPanel(new BorderLayout());
        Color COLOR_BORDER = new Color(8 * 26, 8 * 26, 8 * 26);
        Border treePanelBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, COLOR_BORDER),
                BorderFactory.createEmptyBorder(6, 0, 0, 0));
        treePanel.setBorder(treePanelBorder);
        treePanel.setBackground(ViewUtils.COLOR_PANEL_BG);
        treePanel.add(treeView, BorderLayout.CENTER);

        
        DayPanelCtlr dayPanelCtlr = new DayPanelCtlr(calModel, dateCtlr, 0, 23);        
        DayPanel dayPanel = dayPanelCtlr.getPanel();
        
        DatePanel dateDisplayPanel = new DatePanel(dateCtlr);
        dateDisplayPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DayOfMonthChooserPanel dayOfMonthPanel = new DayOfMonthChooserPanel(dateCtlr);
        dayOfMonthPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        DateChangePanel dateChangerPanel = new DateChangePanel(dateCtlr, Period.Day);
        dateChangerPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel leftPanel = new JPanel(new MigLayout("fill", "6[grow]6[]20", "8[]0[grow]6"));
        leftPanel.add(dateDisplayPanel, "alignx left, aligny top, growx");
        leftPanel.add(dayOfMonthPanel, "alignx right, wrap");
        leftPanel.add(treePanel, "spanx, spany, growx, growy, wrap");
        leftPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel rightNorthPanel = new JPanel(new MigLayout("", "0[grow]6[]0", "0[]0"));
        rightNorthPanel.add(getShowDoneButton(), "alignx right");
        rightNorthPanel.add(dateChangerPanel, "alignx right, wrap");
        rightNorthPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(rightNorthPanel, BorderLayout.NORTH);
        rightPanel.add(dayPanel, BorderLayout.CENTER);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 6));
        rightPanel.setBackground(ViewUtils.COLOR_PANEL_BG);

        setLayout(new GridLayout(1, 2));
        add(leftPanel);
        add(rightPanel);

        setBackground(ViewUtils.COLOR_PANEL_BG);
    }

    private Component getShowDoneButton() {
        JToggleButton button = new JToggleButton(showDoneAction);
        Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setText("");
        button.setFocusable(false);
        button.doClick();
        return button;
    }

    @Override
    protected void componentOpened() {
        dateCtlr.fireChange();
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
    }


    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return "DayTopComponent";
    }
}
