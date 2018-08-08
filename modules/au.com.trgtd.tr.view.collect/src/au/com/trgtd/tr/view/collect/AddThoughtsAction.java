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
import au.com.trgtd.tr.appl.InitialAction;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.view.collect.dialog.*;

/**
 * Action to add thoughts using the thought dialog.
 */
public class AddThoughtsAction extends CallableSystemAction implements InitialAction {

    public AddThoughtsAction() {
        super();
        enableDisable();
        Lookup.Result result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        result.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
    }

    private void enableDisable() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }

    @Override
    protected String iconResource() {
        return Resource.ThoughtAdd;
    }

    /** Gets the action identifier. */
    public String getID() {
        return "AddThoughtsAction";
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    /** Gets the initial action name. */
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_AddThoughtsAction");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    public void performAction() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data != null) {
            new ThoughtDialog(data).showCreateDialog();
        }
    }
}
