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

package au.com.trgtd.tr.view;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * Action for printing views.
 *
 * @author Jeremy Moore
 */
public class PrintAction extends AbstractAction implements LookupListener, ContextAwareAction {
    
    private Lookup context;
    Lookup.Result lookupResult;
    
    public PrintAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    private PrintAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(PrintAction.class, "Print_Action")); //NOI18N
        this.context = context;
    }
    
    void init() {
        if (lookupResult != null) {
            return;
        }
        Lookup.Template lookupTemplate = new Lookup.Template(Print.class);
        lookupResult = context.lookup(lookupTemplate);
        lookupResult.addLookupListener(this);
        resultChanged(null);
    }
    
    @Override
    public boolean isEnabled() {
        init();
        return super.isEnabled();
    }
    
    public void actionPerformed(ActionEvent e) {
        Print printable = (Print)context.lookup(Print.class);
        if (printable != null) {
            printable.print();
        }
    }
    
    public void resultChanged(LookupEvent ev) {
        setEnabled(lookupResult.allItems().size() != 0);
    }
    
    public Action createContextAwareInstance(Lookup context) {
        return new PrintAction(context);
    }
    
}
