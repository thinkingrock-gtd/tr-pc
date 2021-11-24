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
package au.com.trgtd.tr.view.cal.tree;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.view.cal.CalModelImp;
import au.com.trgtd.tr.view.cal.tree.ASAPNodeRoot.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * Children of the root node.
 */
public class RootChildren extends Children.Keys<Node> { 

    private final DateCtlr dateCtlr;
    private final CalModelImp calModel;
    
    public RootChildren(DateCtlr dateCtlr, CalModelImp calModel) {
        this.dateCtlr = dateCtlr;
        this.calModel = calModel;
    }

    private List<Node> getItems() {
        List<Node> items = new ArrayList<>();
        items.add(new ASAPNodeRoot(dateCtlr, calModel, Type.Starting));
        items.add(new ASAPNodeRoot(dateCtlr, calModel, Type.Due));
        items.add(new ASAPNodeRoot(dateCtlr, calModel, Type.Overdue));
        items.add(new DelegatedNodeRoot(dateCtlr, calModel));
        return items;
    }
    
    @Override
    protected Node[] createNodes(Node node) {
        return new Node[]{node};
    }

    @Override
    protected void addNotify() {
        setKeys(getItems());
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        Set<Node> empty = Collections.emptySet();        
        setKeys(empty);
        super.removeNotify();
    }

}
