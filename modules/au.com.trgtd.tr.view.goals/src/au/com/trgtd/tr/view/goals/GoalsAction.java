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
 * Portions Copyright 2006-2012 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.view.goals;

import java.awt.EventQueue;
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
import java.util.logging.Logger;

/**
 * Action which shows the goals screen.
 *
 * @author Jeremy Moore
 */
public class GoalsAction extends CallableSystemAction {

    private static final Logger LOG = Logger.getLogger("tr.view.goals");

    public GoalsAction() {
        super();
        enableDisable();
        Lookup.Result<Data> r = DataLookup.instance().lookup(new Lookup.Template<Data>(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }
    
    @Override
    protected String iconResource() {
        return "au/com/trgtd/tr/view/goals/resource/Circle.png";
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_GoalsAction");
    }
    
    private void enableDisable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setEnabled(null != DataLookup.instance().lookup(Data.class));
            }
        });
    }
    
    /** Gets the action identifier. */
    public String getID() {
        return "goals";
    }
    
    @Override
    public void performAction() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = DataLookup.instance().lookup(Data.class);
                if (data == null) {
                    return;
                }
                
                WindowUtils.closeWindows();
                
//                TopComponent tc = GoalsTopComponent.findInstance();
//
//                Mode mode = WindowManager.getDefault().findMode("Setup");
//                if (mode != null) {
//                    mode.dockInto(tc);
//                }
//
//                tc.open();
//                tc.requestActive();




                TopComponent tcGoals = GoalsTopComponent.findInstance();
                TopComponent tcGoal = GoalTopComponent.findInstance();

                Mode mode = WindowManager.getDefault().findMode("goals");
                if (mode == null) {
                    LOG.severe("Goals mode was not found.");
                } else {
                    mode.dockInto(tcGoals);
                }

                mode = WindowManager.getDefault().findMode("goal");
                if (mode == null) {
                    LOG.severe("Goal mode was not found.");
                } else {
                    mode.dockInto(tcGoal);
                }

                tcGoals.open();
                tcGoal.open();
                tcGoals.requestActive();
            }
        });
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.view.goals");
    }
    
}
