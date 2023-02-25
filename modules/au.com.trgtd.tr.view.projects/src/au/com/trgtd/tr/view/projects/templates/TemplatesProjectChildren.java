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
package au.com.trgtd.tr.view.projects.templates;

import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import tr.model.action.Action;
import tr.model.project.Project;
import au.com.trgtd.tr.view.projects.ActionNode;
import au.com.trgtd.tr.view.projects.ProjectChildren;

/**
 * Children of a templates project node.
 */
public class TemplatesProjectChildren extends ProjectChildren {
    
    public TemplatesProjectChildren(Project project, boolean showDone) {
        super(project, showDone);
    }
    
    @Override
    protected ExplorerManager getExplorerManager() {
        return TemplatesTopComponent.findInstance().getExplorerManager();
    }
    
    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof Project project1) {
            return new Node[] { new TemplatesProjectNode(project1, showDone) };
        }
        if (key instanceof Action action) {
            return new Node[] { new ActionNode(action) };
        }
        return new Node[] {};
    }    

}
