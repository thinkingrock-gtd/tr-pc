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
        if (key instanceof Project) {
            return new Node[] { new TemplatesProjectNode((Project)key, showDone) };
        }
        if (key instanceof Action) {
            return new Node[] { new ActionNode((Action)key) };
        }
        return new Node[] {};
    }    

}
