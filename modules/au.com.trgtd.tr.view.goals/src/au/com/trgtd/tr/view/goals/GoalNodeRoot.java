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
package au.com.trgtd.tr.view.goals;

import au.com.trgtd.tr.view.*;
import java.awt.Image;
import javax.swing.Action;
import org.openide.actions.PasteAction;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.actions.SystemAction;
import tr.model.goals.ctrl.GoalCtrl;

public class GoalNodeRoot extends GoalNode {

    private final Image icon;

    public GoalNodeRoot(OutlineView view, GoalCtrl goalCtrl) {
        super(view, null, goalCtrl);
        icon = ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/goals/resource/Circle.png", false).getImage();
    }

    @Override
    public Image getIcon(int type) {
        return icon;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return icon;
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    public void delete() {
    }

    @Override
    public boolean canEdit() {
        return false;
    }

    @Override
    public void edit() {
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        sheet.put(Sheet.createPropertiesSet());
        return sheet;
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[] {
            SystemAction.get(ExpandAction.class),
            SystemAction.get(CollapseAction.class),
            null,
            SystemAction.get(ToggleHideDoneAction.class),
            null,
            SystemAction.get(AddAction.class),
            null,
            SystemAction.get(PasteAction.class)
        };
    }

    @Override
    public final <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == Index.class) {
            return type.cast(((GoalChildren)getChildren()).getIndex());
        }
        if (type == ToggleHideDoneCookie.class) {
            return type.cast(this);
        }
        if (type == AddCookie.class) {
            return type.cast(this);
        }
        if (type == ExpandCookie.class) {
            return type.cast(this);
        }
        if (type == CollapseCookie.class) {
            return type.cast(this);
        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

    @Override
    public Action getPreferredAction() {
        return SystemAction.get(AddAction.class);
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

    /**
     * Refresh tree to show any changes like projects deleted or projects that
     * have become active or inactive, etc.
     */
    @Override
    public void bump() {
        Children children = getChildren();
        if (children instanceof GoalChildren goalChildren) {
            goalChildren.bump();
        }
    }

}
