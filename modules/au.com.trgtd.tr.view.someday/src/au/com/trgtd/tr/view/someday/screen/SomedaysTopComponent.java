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
package au.com.trgtd.tr.view.someday.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.Constants;
import ca.odell.glazedlists.matchers.MatcherEditor;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.openide.awt.Toolbar;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.openide.ErrorManager;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.future.Future;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.view.ReprocessAction;
import au.com.trgtd.tr.view.Window;
import au.com.trgtd.tr.view.someday.FutureAddAction;
import au.com.trgtd.tr.view.someday.FutureDeleteAction;
import au.com.trgtd.tr.view.someday.FutureEditAction;
import au.com.trgtd.tr.view.someday.TickleDateAction;

/**
 * Top component for the someday/maybe items table.
 *
 * @author Jeremy Moore
 */
public final class SomedaysTopComponent extends Window implements SomedayNodeProvider {
    
    public static final String PREFERRED_ID = "FuturesTopComponent";
    
    private static final long serialVersionUID = 1L;
    
    private static final Preferences PREFS = Constants.getPrefs("sdmb");
    
    private static final String PREFS_KEY_SHOW_FILTERS = "show.filters";
    private static final boolean PREFS_DEF_SHOW_FILTERS = true;
    
    private static SomedaysTopComponent instance;
    
    private SomedaysPanel.Preferences panelPrefs;
    
    private transient SomedayFilters filters;
    private transient SomedaysPanel panel;
    private transient InstanceContent content;
    private transient Lookup lookup;
    private transient JToolBar toolbar;
    private transient JPanel bodyPanel;
    private transient JToggleButton filtersButton;
    
    /* Singleton instance. */
    private SomedaysTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SomedaysTopComponent.class, "CTL_FuturesTopComponent"));
        setToolTipText(NbBundle.getMessage(SomedaysTopComponent.class, "TTT_FuturesTopComponent"));
        setIcon(Icons.SomedayMaybes.getImage());                    
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized SomedaysTopComponent getDefault() {
        if (instance == null) {
            instance = new SomedaysTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the CollectTopComponent instance. Never call {@link #getDefault}
     * directly!
     */
    public static synchronized SomedaysTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find futures component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SomedaysTopComponent comp) {
            return comp;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }
    
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }
    
    private JToolBar getToolBar() {
        if (toolbar == null) {
            SystemAction[] actions = new SystemAction[] {
                SystemAction.get(FutureAddAction.class),
                SystemAction.get(FutureEditAction.class),
                SystemAction.get(FutureDeleteAction.class),
                SystemAction.get(ReprocessAction.class),
                SystemAction.get(TickleDateAction.class)
            };
            toolbar = SystemAction.createToolbarPresenter(actions);
            filtersButton = new JToggleButton(new FiltersAction());
            toolbar.add(filtersButton, 0);
            toolbar.setUI((new Toolbar()).getUI());
            toolbar.setFloatable(false);
            
            Dimension buttonSize = Constants.TOOLBAR_BUTTON_SIZE;
            for (Component component : toolbar.getComponents()) {
                if (component instanceof AbstractButton) {
                    component.setPreferredSize(buttonSize);
                    component.setMinimumSize(buttonSize);
                    component.setMaximumSize(buttonSize);
                    component.setSize(buttonSize);
                }
            }
        }
//      toolbar.setBorder(ViewUtils.BORDER_TOOLBAR);
        return toolbar;
    }
    
    private class FiltersAction extends AbstractAction {
        public FiltersAction() {
            super("", Icons.FiltersView);
            putValue(SHORT_DESCRIPTION, NbBundle.getMessage(getClass(), "CTL_FiltersAction"));
        }
        public void actionPerformed(ActionEvent e) {
            if (filtersButton.isSelected()) {
                bodyPanel.add(getFilters().getComponent(), BorderLayout.NORTH);
            } else {
                bodyPanel.remove(getFilters().getComponent());
            }
            validate();
        }
    }
    
    private SomedayFilters getFilters() {
        if (filters == null) {
            filters = new SomedayFilters();
        }
        return filters;
    }
    
    private MatcherEditor getMatcherEditor() {
        return getFilters().getMatcherEditor();
    }
    
    @Override
    protected void componentOpened() {
        super.componentOpened();
        
        provide(null);
        
        removeAll();
        
        getToolBar();
        filtersButton.setSelected(false);
        String position = GUIPrefs.getButtonsPosition();
        if (position.equals(GUIPrefs.BUTTONS_POSITION_TOP)) {
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(toolbar, BorderLayout.NORTH);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_BOTTOM)) {
            toolbar.setOrientation(JToolBar.HORIZONTAL);
            add(toolbar, BorderLayout.SOUTH);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_LEFT)) {
            toolbar.setOrientation(JToolBar.VERTICAL);
            add(toolbar, BorderLayout.WEST);
        } else if (position.equals(GUIPrefs.BUTTONS_POSITION_RIGHT)) {
            toolbar.setOrientation(JToolBar.VERTICAL);
            add(toolbar, BorderLayout.EAST);
        }
        
        panel = new SomedaysPanel(this, getMatcherEditor());
        panel.setPreferences(panelPrefs);
        
        bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.add(panel, BorderLayout.CENTER);
        
        add(bodyPanel, BorderLayout.CENTER);
        
        // restore showFilters from preference
        boolean b = PREFS.getBoolean(PREFS_KEY_SHOW_FILTERS, PREFS_DEF_SHOW_FILTERS);
        filtersButton.setSelected(b);
        filtersButton.getAction().actionPerformed(null);
    }
    
    @Override
    protected void componentClosed() {
        panelPrefs = panel.getPreferences();
        
        // save show filters as a preference
        PREFS.putBoolean(PREFS_KEY_SHOW_FILTERS, filtersButton.isSelected());
        
        super.componentClosed();
    }
    
    private synchronized InstanceContent getInstanceContent() {
        if (content == null) {
            content = new InstanceContent();
        }
        return content;
    }
    
    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = new AbstractLookup(getInstanceContent());
        }
        return lookup;
    }
    
    public void provide(final Collection<SomedayNode> nodes) {
        EventQueue.invokeLater(() -> {
            if (panel == null || nodes == null) {
                getInstanceContent().set(Collections.EMPTY_LIST, null);
            } else {
                getInstanceContent().set(nodes, null);
            }
        });
    }

    public void select(Future future) {
        filters.reset();
        panel.select(future);
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.future");
    }
    
    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }
    
    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper(panel.getPreferences(), filters.getSerializable());
    }
    
    final static class ResolvableHelper implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final SomedaysPanel.Preferences prefs;
        private final Serializable rmes;
        
        public ResolvableHelper(SomedaysPanel.Preferences prefs, Serializable rmes) {
            this.prefs = prefs;
            this.rmes = rmes;
        }
        
        public Object readResolve() {
            SomedaysTopComponent result = SomedaysTopComponent.getDefault();
            result.panelPrefs = prefs;
            result.filters = new SomedayFilters();
            result.filters.setSerializable(rmes);
            return result;
        }
    }
    
}
