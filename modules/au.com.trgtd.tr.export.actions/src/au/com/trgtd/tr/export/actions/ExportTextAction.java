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

package au.com.trgtd.tr.export.actions;

import au.com.trgtd.tr.export.ExportAction;
import au.com.trgtd.tr.resource.Icons;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;
 
/**
 * Export to a text file action.
 *
 * @author Jeremy Moore
 */
public final class ExportTextAction extends ExportAction {
    
    /** Constructs a new instance. */
    public ExportTextAction() {
        super();
        setIcon(Icons.Text);
    }
    
    /** Save the current data store as another file. */
    @Override
    public void performAction() {
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;            
        }        
        try {
            new ExportText().process(data);
        } 
        catch (Exception ex) {
            ex.printStackTrace(System.err);            
        }
    } 
    
    /** Get the report action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(ExportTextAction.class, "CTL_ExportTextAction");
    }
    
//  /** Get the help context. */
//  public HelpCtx getHelpCtx() {
//      return HelpCtx.DEFAULT_HELP;
//  }
    
}
