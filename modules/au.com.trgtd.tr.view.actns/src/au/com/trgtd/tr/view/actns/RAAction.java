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
package au.com.trgtd.tr.view.actns;

import au.com.trgtd.tr.resource.Resource;
import au.com.trgtd.tr.appl.InitialAction;
import java.awt.EventQueue;
import java.util.List;
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
import au.com.trgtd.tr.view.actns.screens.*;
import au.com.trgtd.tr.view.actns.screens.ActionsScreen;
import au.com.trgtd.tr.view.actns.screens.dao.ScreensDAOProvider;
import au.com.trgtd.tr.view.projects.EditorTopComponent;
import au.com.trgtd.tr.view.projects.ProjectsTreeLookup;
import au.com.trgtd.tr.view.projects.ProjectsTreeTopComponent;
import au.com.trgtd.tr.view.projects.actions.SingleActionsLookup;
import au.com.trgtd.tr.view.projects.actions.SingleActionsTopComponent;

/**
 * Action for Review Actions.
 *
 * @author Jeremy Moore
 */
//public class RAAction extends AbstractAction implements InitialAction {
public class RAAction extends CallableSystemAction implements InitialAction {

    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    private static RAAction instance;
    private Lookup.Result<Data> result;

    /* Constructs a new instance. */
    public RAAction() {
//        super(NbBundle.getMessage(RAAction.class, "CTL_RAAction"));
        super();
//        putValue(SMALL_ICON, Resources.ICON_ACTIONS);
        enableDisable();
        result = DataLookup.instance().lookup(new Lookup.Template<Data>(Data.class));
        result.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                LOG.fine("DataLookup Data.class result changed.");
                enableDisable();
            }
        });
        instance = this;
    }

    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_RAAction");
    }

    @Override
    protected String iconResource() {
        return Resource.Actions;
    }

    public static RAAction getInstance() {
        if (instance == null) {
            instance = new RAAction();
        }
        return instance;
    }

    private void enableDisable() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the initial action identifier. */
    public String getID() {
        return "actions";
    }

    /**
     * Sets up windows for TR review actions.
     */
    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Data data = (Data) DataLookup.instance().lookup(Data.class);
                if (data == null) {
                    return;
                }
                WindowUtils.closeWindows();

                Mode mode = WindowManager.getDefault().findMode("ra-actions");
                if (mode == null) {
                    LOG.severe("Mode ra-actions was not found.");
                    return;
                }



//              boolean first = true;
                TopComponent tcActions1 = null;

                List<ActionsScreen> screens = ScreensDAOProvider.instance().provide().getData().getScreens().list();
                for (ActionsScreen s : screens) {
                    TopComponent tc = ReviewActionsTopComponent.createInstance(s);
                    mode.dockInto(tc);
                    tc.open();
//                  if (first) {
//                        tc.requestActive();
//                        first = false;
//                    }
                    if (tcActions1 == null) {
                        tcActions1 = tc;

                    }
                }

                ProjectsTreeTopComponent tcProjects = RAProjectsTreeTopComponent.findInstance();
                ProjectsTreeLookup.register(tcProjects);

                SingleActionsTopComponent tcSingleActions = RASingleActionsTopComponent.findInstance();
                SingleActionsLookup.register(tcSingleActions);

                TopComponent tcEditor = EditorTopComponent.findInstance();

                mode = WindowManager.getDefault().findMode("ra-projects");
                if (mode == null) {
                    LOG.severe("Mode ra-projects was not found.");
                } else {
                    mode.dockInto(tcProjects);
                    mode.dockInto(tcSingleActions);
                }
                mode = WindowManager.getDefault().findMode("ra-editor");
                if (mode == null) {
                    LOG.severe("Mode ra-editor was not found.");
                } else {
                    mode.dockInto(tcEditor);
                }

                tcProjects.open();
                tcSingleActions.open();
                tcEditor.open();

                if (tcActions1 != null) {
                    tcActions1.requestActive();
                    tcActions1.requestVisible();
                }
            }
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }    
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("prefs.actions.screens");
    }
}
