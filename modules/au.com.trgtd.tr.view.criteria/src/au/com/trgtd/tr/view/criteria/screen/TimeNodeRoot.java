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
package au.com.trgtd.tr.view.criteria.screen;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

public class TimeNodeRoot extends AbstractNode {

    public TimeNodeRoot(TimeChildren children) {
        super(children);
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == Index.class) {
            return type.cast(((TimeChildren) getChildren()).getIndex());
        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

//    @Override
//    public Action[] getActions(boolean popup) {
//        return new Action[]{
//                    SystemAction.get(PasteAction.class)
//                };
//    }

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

}
