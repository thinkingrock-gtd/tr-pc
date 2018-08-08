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

package au.com.trgtd.tr.export;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export action.
 *
 * @author Jeremy Moore
 */
public abstract class ExportAction extends CallableSystemAction {
    
    private final Lookup.Result dataResult;
    
    /** Constructs a new instance. */
    public ExportAction() {
        super();
        enableDisable();
        dataResult = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        dataResult.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                enableDisable();
            }
        });
        dataResult.allInstances();
    }
    
    private void enableDisable() {
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }
    
    /** Run the export. */
    @Override
    public void performAction() {
    }
    
    /** Get the action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(ExportAction.class, "default-name-export");
    }
    
//    /** Get the action icon. */
//    protected abstract String iconResource();
//
//
//    /** Get the action icon for text exports. */
//    protected String iconResourceText() {
//        return Resources.PATH_TEXT_ICON;
//    }
//
//    /** Get the action icon. */
//    protected String iconResourceXML() {
//        return Resources.PATH_XML_ICON;
//    }
    
    /** Get the help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}
