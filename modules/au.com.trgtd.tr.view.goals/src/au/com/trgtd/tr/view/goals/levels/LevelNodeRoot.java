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
package au.com.trgtd.tr.view.goals.levels;

import javax.swing.Action;
import java.util.List;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.io.IOException;
import org.openide.actions.PasteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

public class LevelNodeRoot extends AbstractNode {

    private final LevelsCtrl levelsCtrl;

    public LevelNodeRoot(LevelChildren children) {
        super(children);
        levelsCtrl = LevelsCtrlLookup.getLevelsCtrl();
        setIconBaseWithExtension("au/com/trgtd/tr/view/goals/resource/Levels.png");
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == Index.class) {
            return type.cast(((LevelChildren) getChildren()).getIndex());
        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{
                    SystemAction.get(PasteAction.class)
                };
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
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {
        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        if (dropNode instanceof LevelNode dropLevelNode) {
            if (!this.equals(dropNode.getParentNode()) || !isMove(action)) {
                return new PasteType() {
                    public Transferable paste() throws IOException {
                        if (isMove(action)) {
                            // remove drop level control from levels control
                            levelsCtrl.deleteLevel(dropLevelNode.levelCtrl);
                            // insert drop level control in levels control
                            levelsCtrl.insert(dropLevelNode.levelCtrl);
                        } else { // copy
                            // insert copy of drop goal control in this goal control
                            levelsCtrl.insertLevel(
                                    dropLevelNode.levelCtrl.getDescr(),
                                    dropLevelNode.levelCtrl.isGoalsHaveProjects(),
                                    dropLevelNode.levelCtrl.isGoalsHaveStartDate(),
                                    dropLevelNode.levelCtrl.isGoalsHaveEndDate(),
                                    dropLevelNode.levelCtrl.isGoalsHaveVision(),
                                    dropLevelNode.levelCtrl.isGoalsHaveAccountability(),
                                    dropLevelNode.levelCtrl.isGoalsHaveRewards(),
                                    dropLevelNode.levelCtrl.isGoalsHaveObstacles(),
                                    dropLevelNode.levelCtrl.isGoalsHaveSupport(),
                                    dropLevelNode.levelCtrl.isGoalsHaveBrainstorming(),
                                    dropLevelNode.levelCtrl.getGoalsIconPath());
                        }
                        return null;
                    }
                };
            }
        }
        return null;
    }

    private boolean isMove(int action) {
        return (action & (NodeTransfer.CLIPBOARD_CUT | DnDConstants.ACTION_MOVE)) != 0;
    }

    @Override
    protected void createPasteTypes(Transferable t, List<PasteType> s) {
        super.createPasteTypes(t, s);

        PasteType pasteType = null;
        final Node cutNode = NodeTransfer.node(t, NodeTransfer.CLIPBOARD_CUT);
        if (null != cutNode) {
            pasteType = getDropType(t, NodeTransfer.CLIPBOARD_CUT, -1);
        } else {
            pasteType = getDropType(t, DnDConstants.ACTION_COPY, -1);
        }
        if (null != pasteType) {
            s.add(pasteType);
        }
    }
}
