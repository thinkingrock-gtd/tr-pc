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
package au.com.trgtd.tr.view.projects.future;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.CollapseAction;
import au.com.trgtd.tr.view.ExpandAction;
import java.awt.Image;
import org.openide.actions.PasteAction;
import org.openide.util.actions.SystemAction;
import tr.model.project.Project;
import au.com.trgtd.tr.view.projects.AddProjectAction;
import au.com.trgtd.tr.view.ToggleHideDoneAction;

/**
 * The root node for future projects.
 *
 * @author Jeremy Moore
 */
public class FutureRootNode extends FutureProjectNode {
    
    /** Constructs a new instance. */
    public FutureRootNode(Project root, boolean showDone) {
        super(root, showDone);
    }
    
    @Override
    public Image getIcon(int type) {
        return Icons.ProjectsFuture.getImage();
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return Icons.ProjectsFuture.getImage();
    }
    
    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[] {
            SystemAction.get(ExpandAction.class),
            SystemAction.get(CollapseAction.class),
            null,
            SystemAction.get(ToggleHideDoneAction.class),
            null,
            SystemAction.get(AddProjectAction.class),
            null,
            SystemAction.get(PasteAction.class),
        };
    }
    
    @Override
    public boolean canCopy() {
        return false;
    }
    
    @Override
    public boolean canCut() {
        return false;
    }
    
    @Override
    public boolean canDestroy() {
        return false;
    }
    
    @Override
    public boolean canRename() {
        return false;
    }
    
    @Override
    public boolean canAddAction() {
        return false;
    }
    
}
