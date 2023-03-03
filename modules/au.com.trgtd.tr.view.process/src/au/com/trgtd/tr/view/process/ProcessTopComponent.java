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
package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.Constants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Collection;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.openide.awt.Toolbar;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.view.DeleteAction;
import au.com.trgtd.tr.view.Window;
import java.util.ArrayList;

/**
 * Top component for the process thoughts window.
 */
public final class ProcessTopComponent extends Window implements ProcessNodeProvider, Lookup.Provider {

    public static final String PREFERRED_ID = "ProcessTopComponent";
    private static ProcessTopComponent instance;
    private transient InstanceContent content;
    private transient ProcessPanel panel;
    private transient Lookup lookup;
    
    private ProcessTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ProcessTopComponent.class, "CTL_ProcessTopComponent"));
        setToolTipText(NbBundle.getMessage(ProcessTopComponent.class, "HINT_ProcessTopComponent"));
        setIcon(Icons.ProcessThoughts.getImage());     
    }

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    private JScrollPane scrollPane;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. de-serialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     * @return 
     */
    public static synchronized ProcessTopComponent getDefault() {
        if (instance == null) {
            instance = new ProcessTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the ProcessTopComponent instance. Never call {@link #getDefault} directly!
     * @return 
     */
    public static synchronized ProcessTopComponent findInstance() {
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    protected void componentClosed() {
        getInstanceContent().set(Collections.EMPTY_LIST, null);
    }

    private JToolBar getToolBar() {
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(ProcessAction.class),
            SystemAction.get(ProcessAddAction.class),
            SystemAction.get(DeleteAction.class)
        };       
        
        JToolBar toolbar = SystemAction.createToolbarPresenter(actions);
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

        return toolbar;
    }
    
    @Override
    protected void componentOpened() {
        provide(null);

        removeAll();

        JToolBar toolbar = getToolBar();
        
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

        panel = new ProcessPanel(this);

        scrollPane = new JScrollPane();
        scrollPane.setBorder(null);
        scrollPane.setViewportView(panel);
        add(scrollPane, BorderLayout.CENTER);

        panel.initModel();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();
        
        if (panel != null) {
            panel.takeFocus();            
        }
        
    }

    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }

//    /** replaces this in object stream */
//    public Object writeReplace() {
//        return new ResolvableHelper();
//    }
//
//    final static class ResolvableHelper implements Serializable {
//        private static final long serialVersionUID = 1L;
//        public Object readResolve() {
//            return ProcessTopComponent.getDefault();
//        }
//    }
    /** Refreshes the window. */
    public void refresh() {
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

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.process");
    }

    /**
     * Provides a process node on the global lookup so that actions are enabled.
     * @param processNode The process node or null to remove.
     */
    @Override
    public void provide(final ProcessNode processNode) {
        EventQueue.invokeLater(() -> {
            if (panel == null || processNode == null) {
                getInstanceContent().set(Collections.EMPTY_LIST, null);
            } else {
                Collection<ProcessNode> collection = new ArrayList<>();
                collection.add(processNode);
                getInstanceContent().set(collection, null);
            }
        });
    }
}
