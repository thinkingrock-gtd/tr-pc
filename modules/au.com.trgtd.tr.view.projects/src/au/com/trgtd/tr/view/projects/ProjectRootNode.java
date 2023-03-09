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

import au.com.trgtd.tr.view.CollapseAction;
import au.com.trgtd.tr.view.CollapseCookie;
import au.com.trgtd.tr.view.ExpandAction;
import au.com.trgtd.tr.view.ToggleHideDoneAction;
import au.com.trgtd.tr.view.projects.goals.ToggleShowGoalsAction;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.openide.actions.PasteAction;
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
            Project p = dropNode.getLookup().lookup(Project.class);
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
