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

package au.com.trgtd.tr.view.collect;

import au.com.trgtd.tr.resource.Resource;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.thought.Thought;

/**
 * Action to edit a thought.
 *
 * @author Jeremy Moore
 */
public class EditThoughtAction extends CookieAction {
    
    public EditThoughtAction() {
        super();
    }
    
    @Override
    protected String iconResource() {
        return Resource.ThoughtEdit;
    }    

    /** Gets the display name. */
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_EditThoughtAction");
    }
    
    /** Gets help context. */
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;        
    }
    
    public Class[] cookieClasses() {
        return new Class[] { Thought.class };
    }
    
    public int mode() {
        return MODE_EXACTLY_ONE;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    public void performAction(Node[] nodes) {
        EditThoughtCookie cookie = (EditThoughtCookie)nodes[0].getCookie(EditThoughtCookie.class);
        if (cookie != null) {
            cookie.editThought();
        }
    }
    
}

