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

import au.com.trgtd.tr.resource.Icons;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.project.Project;

/**
 * System action to add a TR action.
 *
 * @author Jeremy Moore
 */
public class AddActionAction extends CookieAction {
    
    private final Lookup.Result result;

    public AddActionAction() {
        super();
        setIcon(Icons.ActionAdd);
        result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        result.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                setEnabled(result.allInstances().size() > 0);
            }
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    /** Gets the initial action name. */
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_AddActionAction");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    protected Class[] cookieClasses() {
        return new Class[] { Project.class, Action.class, AddActionCookie.class };
    }

    @Override
    protected boolean enable(Node[] nodes) {
        if (nodes.length == 1) {
            if (nodes[0] instanceof ActionNode) {
                return ((ActionNode)nodes[0]).canAddAction();
            }
            if (nodes[0] instanceof ProjectNode) {
                return ((ProjectNode)nodes[0]).canAddAction();
            }
        }
        return false;
    }

    protected void performAction(Node[] nodes) {
        AddActionCookie cookie = (AddActionCookie)nodes[0].getCookie(AddActionCookie.class);
        if (cookie != null) {
            cookie.addAction();
        }
    }


//    @Override
//    protected int mode() {
//        return MODE_ONE;
//    }
//
//    @Override
//    protected Class<?>[] cookieClasses() {
//        return new Class[] {AddActionCookie.class};
//    }
//
//    @Override
//    protected void performAction(Node[] nodes) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public String getName() {
//        return "Some Action Name";
//    }
//
//    @Override
//    public HelpCtx getHelpCtx() {
//        return HelpCtx.DEFAULT_HELP;
//    }
//

}


