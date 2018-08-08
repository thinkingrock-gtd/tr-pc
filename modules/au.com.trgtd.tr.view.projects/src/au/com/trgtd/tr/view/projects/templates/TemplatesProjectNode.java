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
import org.openide.explorer.view.TreeView;
import org.openide.windows.TopComponent;
import tr.model.project.Project;
import au.com.trgtd.tr.view.projects.ProjectNode;

/**
 * Node for a templates project.
 *
 * @author Jeremy Moore
 */
public class TemplatesProjectNode extends ProjectNode {
    
    /**
     * Constructs a new instance for a given project.
     * @param project The project.
     * @param showDone Whether or not to show done items.
     */
    public TemplatesProjectNode(Project project, boolean showDone) {
        super(new TemplatesProjectChildren(project, showDone));
    }
    
    @Override
    public TreeView getTreeView() {
        return TemplatesTopComponent.findInstance().getTreeView();
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return TemplatesTopComponent.findInstance().getExplorerManager();
    }
    
    public TopComponent getTopComponent() {
        return TemplatesTopComponent.findInstance();
    }
    
}
