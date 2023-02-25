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
package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.util.Collections;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

public class FoundChildren extends Children.Keys implements Observer {

    private final FoundItems items;

    public FoundChildren(FoundItems controller) {
        this.items = controller;
    }

    @Override
    protected Node[] createNodes(Object key) {
        if (key instanceof FoundItem foundItem) {
            return new Node[]{new FoundNode(foundItem)};
        }
        return new Node[0];
    }

    @Override
    protected void addNotify() {
        synchronized (this) {
            if (items != null) {
                setKeys(items.getItems());
                items.addObserver(this);
            }
            super.addNotify();
        }
    }

    @Override
    protected void removeNotify() {
        synchronized (this) {
            setKeys(Collections.EMPTY_SET);
            if (items != null) {
                items.removeObserver(this);
            }
            super.removeNotify();
        }
    }

    @Override
    public void update(Observable obs, Object arg) {
        synchronized (this) {
            if (items != null) {
                setKeys(items.getItems());
            } else {
                setKeys(Collections.EMPTY_SET);
            }
        }
    }
}

