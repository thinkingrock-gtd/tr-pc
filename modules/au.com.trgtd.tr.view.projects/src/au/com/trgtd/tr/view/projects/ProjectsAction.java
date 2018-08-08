/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.resource.Resource;
import au.com.trgtd.tr.appl.InitialAction;
import java.awt.EventQueue;
import java.util.logging.Logger;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
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
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
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
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the initial action identifier. */
    @Override
    public String getID() {
        return "projects";
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = (Data) DataLookup.instance().lookup(Data.class);
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
            }
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
