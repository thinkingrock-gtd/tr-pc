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
package au.com.trgtd.tr.view.delegates.screen;

import au.com.trgtd.tr.appl.Constants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
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
import au.com.trgtd.tr.prefs.ui.GUIPrefs;
import au.com.trgtd.tr.view.delegates.AddActorsAction;
import au.com.trgtd.tr.view.delegates.DeleteActorAction;
import au.com.trgtd.tr.view.delegates.EditActorAction;
import au.com.trgtd.tr.view.delegates.Resources;

/**
 * Top component for the actors window.
 *
 * @author Jeremy Moore
 */
public final class ActorsTopComponent extends TopComponent implements ActorNodeProvider {

    private static final long serialVersionUID = 1L;

    private static final String PREFERRED_ID = "ActorsTopComponent";

    private static ActorsTopComponent instance;

    private ActorPanel.Preferences panelPrefs;
    private ActorPanel panel;
    private InstanceContent content;
    private Lookup lookup;
    private JToolBar toolbar;

    /* Singleton instance. */
    private ActorsTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ActorsTopComponent.class, "CTL_ActorsTopComponent"));
        setToolTipText(NbBundle.getMessage(ActorsTopComponent.class, "TTT_ActorsTopComponent"));
        setIcon(Resources.DELEGATES_ICON.getImage());
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     * @return 
     */
    public static synchronized ActorsTopComponent getDefault() {
        if (instance == null) {
            instance = new ActorsTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the instance. Never call {@link #getDefault} directly!
     * @return 
     */
    public static synchronized ActorsTopComponent findInstance() {
        TopComponent tc = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (tc == null) {
            ErrorManager.getDefault().log(ErrorManager.WARNING, "Cannot find actors component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (tc instanceof ActorsTopComponent actorsTopComponent) {
            return actorsTopComponent;
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
                SystemAction.get(AddActorsAction.class),
                SystemAction.get(EditActorAction.class),
                SystemAction.get(DeleteActorAction.class)
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
        }
        return toolbar;
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        provide(null);
        removeAll();
        toolbar = getToolBar();
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
        panel = new ActorPanel(this);
        panel.setPreferences(panelPrefs);
        add(panel, BorderLayout.CENTER);
    }

    @Override
    protected void componentClosed() {
        panelPrefs = panel.getPreferences();
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

    @Override
    public void provide(final Collection<ActorNode> actorNodes) {
        EventQueue.invokeLater(() -> {
            if (panel == null || actorNodes == null) {
                getInstanceContent().set(new ArrayList<>(), null);
            } else {
                getInstanceContent().set(actorNodes, null);
            }
        });
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.delegates");
    }

    @Override
    public String preferredID() {
        return PREFERRED_ID;
    }

    /** replaces this in object stream
     * @return  */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper(panel.getPreferences());
    }

    final static class ResolvableHelper implements Serializable {
        private static final long serialVersionUID = 1L;

        private final ActorPanel.Preferences prefs;

        public ResolvableHelper(ActorPanel.Preferences prefs) {
            this.prefs = prefs;
        }

        public Object readResolve() {
            ActorsTopComponent dtc = ActorsTopComponent.getDefault();
            dtc.panelPrefs = prefs;
            return dtc;
        }
    }

}
