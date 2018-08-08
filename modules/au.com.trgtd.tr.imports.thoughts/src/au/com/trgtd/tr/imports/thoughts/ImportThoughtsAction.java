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

package au.com.trgtd.tr.imports.thoughts;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.view.collect.CollectThoughtsAction;

/**
 * Import thoughts action.
 *
 * @author Jeremy Moore
 */
public final class ImportThoughtsAction extends CallableSystemAction {
    
    /** Constructs a new instance. */
    public ImportThoughtsAction() {
        super();
        
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
        r.allInstances();
        
        enableDisable();
    }
    
    private void enableDisable() {
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }
    
    /** Save the current datastore as another file. */
    public void performAction() {
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        try {
            if (ImportThoughts.doImport(data)) {
                activateCollectThoughts();
            }
        } catch (Exception ex) {
            // Already handled.
        }
    }
    
    private void activateCollectThoughts() {
        CollectThoughtsAction collect = (CollectThoughtsAction)SystemAction.get(CollectThoughtsAction.class);
        collect.performAction();
    }
    
    public String getName() {
        return NbBundle.getMessage(ImportThoughtsAction.class, "CTL_ImportThoughtsAction");
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    protected boolean asynchronous() {
        return false;
    }
    
    private void updateEnablement(boolean b) {
        setEnabled(b);
    }
    
}
