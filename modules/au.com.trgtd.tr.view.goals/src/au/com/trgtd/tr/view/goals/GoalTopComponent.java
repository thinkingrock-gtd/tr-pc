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
import au.com.trgtd.tr.view.ViewUtils;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.logging.Logger;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import au.com.trgtd.tr.view.Window;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.AbstractButton;
import javax.swing.JToolBar;
import org.openide.awt.Toolbar;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;
import org.openide.util.actions.SystemAction;

/**
 * Top component for goal view.
 *
 * @author Jeremy Moore
 */
public final class GoalTopComponent extends Window implements LookupListener {

    private static final Logger LOG = Logger.getLogger("tr.view.goals");
    private static final String PREFERRED_ID = "GoalTopComponent";
    private static final String ICON_PATH = "au/com/trgtd/tr/view/goals/resource/Goal.png";
    private static GoalTopComponent instance;
    private ProjectGoalPanel panel;
    private Lookup.Result<GoalNode> result;
    private JToolBar toolbar;
    
    private GoalTopComponent() {
        setName(NbBundle.getMessage(GoalTopComponent.class, "CTL_GoalTopComponent"));
//      setToolTipText(NbBundle.getMessage(GoalTopComponent.class, "TTT_GoalTopComponent"));
        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        setLayout(new BorderLayout());
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();

        if (panel == null) {
            panel = new ProjectGoalPanel();
            removeAll();
            add(getToolbar(), BorderLayout.NORTH);
//          add(new JScrollPane(panel), BorderLayout.CENTER);
            add(panel, BorderLayout.CENTER);
        }
        panel.initModel(null);

//      GoalsTopComponent goalsTopComponent = GoalsTopComponent.findInstance();
//      result = goalsTopComponent.getLookup().lookup(new Lookup.Template(GoalNode.class));   
        result = Utilities.actionsGlobalContext().lookupResult(GoalNode.class);
        result.addLookupListener(this);
        result.allInstances();
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

    @Override
    protected void componentClosed() {
        super.componentClosed();
        result.removeLookupListener(this);
        result = null;
    }

    @Override
    protected void componentDeactivated() {
//        panel.deactivate();
        super.componentDeactivated();
    }

    @Override
    public synchronized void resultChanged(LookupEvent lookupEvent) {
        LOG.info("Starting");

        if (panel == null) {
            return;
        }

        Collection<? extends GoalNode> collection = result.allInstances();
        if (collection.isEmpty()) {
//            panel.initModel(null);
//            LOG.info("null");
        } else {
            GoalNode node = collection.iterator().next();
            panel.initModel(node.goalCtrl);
            LOG.info("initialising model");
        }
    }

    /** Start editing if possible. */
    public void edit() {
//        if (panel == null) {
//            return;
//        }
//        requestActive();
//        panel.edit();
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. de-serialization routines; otherwise you could get a
     * non-de-serialized instance.
     * To obtain the singleton instance, use {@link findInstance}.
     */
    public static synchronized GoalTopComponent getDefault() {
        if (instance == null) {
            instance = new GoalTopComponent();
        }
        return instance;
    }

    public static synchronized GoalTopComponent findInstance() {
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

//    /** replaces this in object stream */
//    public Object writeReplace() {
//        return new ResolvableHelper();
//    }
//
//    final static class ResolvableHelper implements Serializable {
//        private static final long serialVersionUID = 1L;
//        public Object readResolve() {
//            return ReferenceTopComponent.getDefault();
//        }
//    }

}
