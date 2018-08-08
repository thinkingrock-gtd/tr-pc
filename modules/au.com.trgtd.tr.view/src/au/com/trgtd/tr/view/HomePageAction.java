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

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.runtime.Open;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * Action for opening the TR home page.
 *
 * @author Jeremy Moore
 */
public class HomePageAction extends AbstractAction {
    
    public HomePageAction() {
        this(Utilities.actionsGlobalContext());
    }
    
    private HomePageAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(getClass(), "HomePage_Action")); //NOI18N
        putValue(SMALL_ICON, Icons.Web);
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            Open.open(new URL("http://www.thinkingrock.com.au"));
        } catch (Exception ex) {
        }
    }
    
}
