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

import au.com.trgtd.tr.resource.Resource;
import au.com.trgtd.tr.appl.InitialAction;
import java.awt.EventQueue;
import java.util.logging.Logger;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import au.com.trgtd.tr.view.projects.actions.SingleActionsLookup;
import au.com.trgtd.tr.view.projects.actions.SingleActionsTopComponent;
import au.com.trgtd.tr.view.projects.future.FutureTopComponent;
import au.com.trgtd.tr.view.projects.templates.TemplatesTopComponent;

/**
 * Action for TR Review Projects.
 */
public class ProjectsAction extends CallableSystemAction implements InitialAction {

    private static final Logger LOG = Logger.getLogger("tr.archive");

    /** Constructs a new instance. */
    public ProjectsAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ProjectsAction");
    }

    @Override
    protected String iconResource() {
        return Resource.Projects;
    }

    private void enableDisable() {
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the initial action identifier. */
    @Override
    public String getID() {
        return "projects";
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(() -> {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                return;
            }
            WindowUtils.closeWindows();

            ProjectsTreeTopComponent tcProjects = ProjectsTreeTopComponent.findInstance();
            ProjectsTreeLookup.register(tcProjects);

            SingleActionsTopComponent tcSingleActions = SingleActionsTopComponent.findInstance();
            SingleActionsLookup.register(tcSingleActions);

            TopComponent tcFuture = FutureTopComponent.findInstance();
            TopComponent tcTemplates = TemplatesTopComponent.findInstance();
            TopComponent tcEdit = EditorTopComponent.findInstance();

            Mode mode = WindowManager.getDefault().findMode("projects-tree");
            if (mode == null) {
                LOG.severe("Mode projects-tree was not found.");
            } else {
                mode.dockInto(tcProjects);
                mode.dockInto(tcSingleActions);
                mode.dockInto(tcFuture);
                mode.dockInto(tcTemplates);
            }

            mode = WindowManager.getDefault().findMode("projects-editor");
            if (mode == null) {
                LOG.severe("Mode projects-editor was not found.");
            } else {
                mode.dockInto(tcEdit);
                tcEdit.open();
            }

            tcProjects.open();
            tcSingleActions.open();
            tcFuture.open();
            tcTemplates.open();
            tcEdit.open();

            tcProjects.requestActive();
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.projects");
    }
}
