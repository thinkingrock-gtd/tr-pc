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
package au.com.trgtd.tr.view.collect.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.Constants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.JToolBar;
import org.openide.ErrorManager;
import org.openide.awt.Toolbar;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.thought.Thought;
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.view.ProcessAction;
import au.com.trgtd.tr.view.collect.DeleteThoughtAction;
import au.com.trgtd.tr.view.collect.EditThoughtAction;
import au.com.trgtd.tr.view.Window;
import au.com.trgtd.tr.view.collect.AddThoughtsAction;

/**
 * Top component for the collect thoughts window.
 */
public final class CollectThoughtsTopComponent extends Window implements ThoughtNodeProvider {

    public static final String PREFERRED_ID = "CollectThoughtsTopComponent";
    private static final long serialVersionUID = 1L;
    private static CollectThoughtsTopComponent instance;
    private transient CollectThoughtsPanel panel;
    private transient InstanceContent content;
    private transient Lookup lookup;
    private transient JToolBar toolbar;

    /* Singleton instance. */
    private CollectThoughtsTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(CollectThoughtsTopComponent.class, "CTL_CollectTopComponent"));
        setToolTipText(NbBundle.getMessage(CollectThoughtsTopComponent.class, "HINT_CollectTopComponent"));
        setIcon(Icons.Collect.getImage());            
    }

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());

    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. de-serialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     * @return 
     */
    public static synchronized CollectThoughtsTopComponent getDefault() {
        if (instance == null) {
            instance = new CollectThoughtsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the CollectTopComponent instance. Never call {@link #getDefault}
     * directly!
     * @return 
     */
    public static synchronized CollectThoughtsTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find collect thoughts component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CollectThoughtsTopComponent comp) {
            return comp;
        }
        ErrorManager.getDefault().log(ErrorManager.WARNING, "There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    private JToolBar getToolbar() {
        SystemAction[] actions = new SystemAction[]{
            SystemAction.get(AddThoughtsAction.class),
            SystemAction.get(EditThoughtAction.class),
            SystemAction.get(DeleteThoughtAction.class),
            null,
            SystemAction.get(ProcessAction.class),
            SystemAction.get(ProcessToReferenceAction.class),
            SystemAction.get(ProcessToSomedayAction.class)
        };
        toolbar = SystemAction.createToolbarPresenter(actions);
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
        if (GUIPrefs.getButtonsPosition().equals(GUIPrefs.BUTTONS_POSITION_TOP)) {
            add(getToolbar(), BorderLayout.NORTH);
        } else {
            add(getToolbar(), BorderLayout.SOUTH);
        }
        panel = new CollectThoughtsPanel(this);
        add(panel, BorderLayout.CENTER);
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
    public void provide(final Collection thoughtNodes) {

        EventQueue.invokeLater(() -> {
            if (panel == null || thoughtNodes == null) {
                getInstanceContent().set(Collections.EMPTY_LIST, null);
            } else {
                getInstanceContent().set(thoughtNodes, null);
            }
        });
    }

    /**
     * Selects the row for the given thought if possible.
     * @param thought The thought.
     */
    public synchronized void select(Thought thought) {
        if (!isOpened()) {
            return;
        }
        if (thought.isProcessed()) {
            return;
        }
        panel.select(thought);
    }


    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.collect");
    }

    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    final static class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return CollectThoughtsTopComponent.getDefault();
        }
    }
}
