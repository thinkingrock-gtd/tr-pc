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

import au.com.trgtd.tr.view.CollapseAction;
import au.com.trgtd.tr.view.CollapseCookie;
import au.com.trgtd.tr.view.ExpandAction;
import au.com.trgtd.tr.view.ToggleHideDoneAction;
import au.com.trgtd.tr.view.projects.goals.ToggleShowGoalsAction;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.openide.actions.PasteAction;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import tr.model.project.Project;
import tr.model.project.ProjectProjects;

/**
 * The root node for projects.
 *
 * @author Jeremy Moore
 */
public class ProjectRootNode extends ProjectNode {

    /** Constructs a new instance. */
    public ProjectRootNode(ProjectProjects root, boolean showDone) {
        super(root, showDone);
    }

    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[]{
                    SystemAction.get(ExpandAction.class),
                    SystemAction.get(CollapseAction.class),
                    null,
                    SystemAction.get(ToggleHideDoneAction.class),
                    SystemAction.get(ToggleShowGoalsAction.class),
                    null,
                    SystemAction.get(AddProjectAction.class),
                    null,
                    SystemAction.get(PasteAction.class),
                    null,
                    SystemAction.get(ImportProjectAction.class),};
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

    @Override
    public PasteType getDropType(Transferable t, final int type, int index) {
        final Node dropNode = NodeTransfer.node(t, MASK_ANY);
        if (dropNode != null && dropNode instanceof TransferNode) {
            final TransferNode transferNode = (TransferNode) dropNode;
            final boolean isMove = isMoveTransfer(type) || transferNode.isMoveTransfer();
            Project p = (Project) dropNode.getLookup().lookup(Project.class);
            if (p != null) {
                final Project transferProject = (isMove) ? p : (Project) p.copy(getData());
                if (transferProject != null && project.canAdd(transferProject)) {
                    return new PasteType() {

                        public Transferable paste() throws IOException {
                            if (isMove) {
                                move(transferProject, transferNode);
                            } else {
                                transferProject.setThought(null);
                                project.add(transferProject);
                            }
                            return null;
                        }
                    };
                }
            }
        }
        return null;
    }

    @Override
    public void collapse() {
        // collapse children but not this root node
        for (Node node : getChildren().getNodes()) {
            CollapseCookie c = node.getCookie(CollapseCookie.class);
            if (c != null) {
                c.collapse();
            }
        }
    }

}
