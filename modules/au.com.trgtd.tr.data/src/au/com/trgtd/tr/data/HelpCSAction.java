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

package au.com.trgtd.tr.data;

import au.com.trgtd.tr.resource.Resource;
import org.netbeans.api.javahelp.Help;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Action for context sensitive help.
 *
 * @author Jeremy Moore
 */
public final class HelpCSAction extends CallableSystemAction {
    
    public HelpCSAction() {
        super();
    }
    
    @Override
    protected String iconResource() {
        return Resource.CSHelp;
    }

    /** Perform the action to show help for the active screen. */
    @Override
    public void performAction() {
        TopComponent tc = WindowManager.getDefault().getRegistry().getActivated();
        if (tc == null) return;

        HelpCtx helpCtx = tc.getHelpCtx();
        if (helpCtx == null) return;
        
        Help help = (Help)Lookup.getDefault().lookup(Help.class);
        if (help == null) return;
        
        help.showHelp(helpCtx);
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(HelpCSAction.class, "CTL_HelpCSAction"); // No I18N
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}
