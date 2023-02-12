/*
 * ThinkingRock, a project management tool for Personal Computers. 
 * Copyright (C) 2006 Avente Pty Ltd
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.view.projects;

import au.com.trgtd.tr.view.Window;
import java.awt.event.ActionEvent;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
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
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            setEnabled(!result.allInstances().isEmpty());
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
                if (tc instanceof Window window) {
                    window.takeFocus();
                }
            }
        }
    }
    
}
