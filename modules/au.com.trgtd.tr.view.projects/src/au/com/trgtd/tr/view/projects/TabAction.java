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

import au.com.trgtd.tr.view.Window;
import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.SystemAction;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * System action to tab between tree and editor windows.
 *
 * @author Jeremy Moore
 */
public class TabAction extends SystemAction {
    
    private final Lookup.Result result;
    
    public TabAction() {
        super();
//      setIcon(Resources.ICON_G_ADD_ACTION);
        result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        result.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                setEnabled(result.allInstances().size() > 0);
            }
        });
    }
    
    @Override
    public String getName() {
        return "";
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        
        // get the currently active window
        TopComponent tc = WindowManager.getDefault().getRegistry().getActivated();
        if (tc == null) return;
        
        // get mode of current window
        Mode mode = WindowManager.getDefault().findMode(tc);
        if (mode == null) return;
        
        // get the project tree mode
        Mode treeMode = WindowManager.getDefault().findMode("projects-tree");
        if (treeMode == null || treeMode.getSelectedTopComponent() == null) {
            treeMode = WindowManager.getDefault().findMode("ra-projects");
            if (treeMode == null || treeMode.getSelectedTopComponent() == null) {
                return;
            }
        }
        
        // if tree mode change to editor mode
        if (mode.equals(treeMode)) {
            EditorTopComponent etc = EditorTopComponent.findInstance();
            etc.requestActive();
            etc.takeFocus();
        } else {
            // change to tree mode
            tc = treeMode.getSelectedTopComponent();
            if (tc != null) {
                tc.requestActive();
                if (tc instanceof Window) {
                    ((Window)tc).takeFocus();                    
                }
            }
        }
    }
    
}
