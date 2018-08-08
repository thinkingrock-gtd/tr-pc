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

package au.com.trgtd.tr.view.contexts;

import au.com.trgtd.tr.appl.InitialAction;
import au.com.trgtd.tr.appl.InitialActionLookup;
import org.openide.modules.ModuleInstall;
import org.openide.util.actions.SystemAction;

/**
 * Contexts module installer.
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {
    
    /** Adds the contexts action to the lookup of initial actions. */
    public void restored() {
        InitialAction action = (InitialAction)SystemAction.get(ContextsAction.class);
        InitialActionLookup.instance().add(action);
    }
    
}
