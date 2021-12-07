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

import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.TreeView;
import org.openide.windows.TopComponent;
import tr.model.project.Project;
import au.com.trgtd.tr.view.projects.ProjectNode;

/**
 * Node for a future project.
 *
 * @author Jeremy Moore
 */
public class FutureProjectNode extends ProjectNode {
    
    /**
     * Constructs a new instance for a given data model project.
     * @param project The data model project.
     */
    public FutureProjectNode(Project project, boolean showDone) {
        super(new FutureProjectChildren(project, showDone));
    }
    
    @Override
    public TreeView getTreeView() {        
        return FutureTopComponent.findInstance().getTreeView();        
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return FutureTopComponent.findInstance().getExplorerManager();
    }
    
    public TopComponent getTopComponent() {
        return FutureTopComponent.findInstance();
    }
    
}
