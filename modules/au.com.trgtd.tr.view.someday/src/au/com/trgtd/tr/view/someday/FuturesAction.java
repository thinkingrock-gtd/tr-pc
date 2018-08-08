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
package au.com.trgtd.tr.view.someday;

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
import au.com.trgtd.tr.view.someday.screen.SomedayTopComponent;
import au.com.trgtd.tr.view.someday.screen.SomedaysTopComponent;

/**
 * Action for the review future items feature.
 *
 * @author Jeremy Moore
 */
//public class FuturesAction extends AbstractAction implements InitialAction {
public class FuturesAction extends CallableSystemAction implements InitialAction {

    private static final Logger LOG = Logger.getLogger("tr.view.future");

    public FuturesAction() {
        super();
        enableDisable();
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }

    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_FutureAction");
    }

    @Override
    protected String iconResource() {
        return Resource.SomedayMaybes;
    }        
    
    private void enableDisable() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    /** Gets the initial action identifier. */
    public String getID() {
        return "future";
    }

    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                WindowUtils.closeWindows();

                TopComponent tcFutures = SomedaysTopComponent.findInstance();
                TopComponent tcFuture = SomedayTopComponent.findInstance();

                Mode mode = WindowManager.getDefault().findMode("futures");
                if (mode == null) {
                    LOG.severe("Futures mode was not found.");
                } else {
                    mode.dockInto(tcFutures);
                }

                mode = WindowManager.getDefault().findMode("future");
                if (mode == null) {
                    LOG.severe("Future mode was not found.");
                } else {
                    mode.dockInto(tcFuture);
                }

                tcFutures.open();
                tcFuture.open();

                tcFutures.requestActive();

            }
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.future");
    }
}
