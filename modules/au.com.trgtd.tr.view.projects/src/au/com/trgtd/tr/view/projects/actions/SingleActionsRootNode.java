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

package au.com.trgtd.tr.view.projects.actions;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.io.IOException;
import org.openide.actions.PasteAction;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.TreeView;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import tr.model.action.Action;
import tr.model.project.Project;
import au.com.trgtd.tr.view.projects.AddActionAction;
import au.com.trgtd.tr.view.projects.TransferNode;
import au.com.trgtd.tr.view.projects.ProjectNode;
import au.com.trgtd.tr.view.ToggleHideDoneAction;

/**
 * Single actions root node.
 *
 * @author Jeremy Moore
 */
public class SingleActionsRootNode extends ProjectNode {
    
    private final Project root;
    
    /**
     * Constructs a new instance.
     * @param root The root.
     * @param showDone Whether or not to show done items.
     */
    public SingleActionsRootNode(Project root, boolean showDone) {
        super(new SingleActionsChildren(root, showDone));
        this.root = root;
    }
    
    @Override
    public javax.swing.Action[] getActions(boolean popup) {
        return new javax.swing.Action[] {
            SystemAction.get(ToggleHideDoneAction.class),
            null,
            SystemAction.get(AddActionAction.class),
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
    
    final static int MASK_ANY = DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT;
    
    @Override
    public PasteType getDropType(Transferable t, final int actn, int index) {
        final Node dropNode = NodeTransfer.node(t, MASK_ANY);
        if (dropNode != null && dropNode instanceof TransferNode) {
            final TransferNode transferNode = (TransferNode)dropNode;
            Action dropAction = (Action)dropNode.getLookup().lookup(Action.class);
            if (dropAction != null) {
                final Action transferAction = (transferNode.isMoveTransfer()) ? dropAction : (Action)dropAction.copy(getData());
                if (transferAction != null && root.canAdd(transferAction)) {
                    return new PasteType() {
                        public Transferable paste() throws IOException {
                            if (transferNode.isMoveTransfer()) {
                                move(transferAction, transferNode);
                            } else {
                                transferAction.setThought(null);
                                root.add(transferAction);
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
    public synchronized void setShowDone(boolean showDone) {
        if (this.showDone == showDone) {
            return;
        }        
        this.showDone = showDone;        
        setChildren(new SingleActionsChildren(root, showDone));        
        fireCookieChange();        
        TreeView treeView = getTreeView();
        if (!treeView.isExpanded(this)) {
            getTreeView().expandNode(this);
        }
    }
    
    
    @Override
    public TreeView getTreeView() {
        return SingleActionsLookup.lookup().getTreeView();
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return SingleActionsLookup.lookup().getExplorerManager();
    }
    
    @Override
    public boolean canAddAction() {
        return true;
    }
    
    @Override
    public boolean canAddProject() {
        return false;
    }
    
}
