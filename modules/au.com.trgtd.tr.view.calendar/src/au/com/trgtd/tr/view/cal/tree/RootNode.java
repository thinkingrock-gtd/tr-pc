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
import org.openide.nodes.AbstractNode;

/**
 * The root node.
 * @author Jeremy Moore
 */
public class RootNode extends AbstractNode {

    /** Constructs a new instance. */
    public RootNode(DateCtlr dateCtlr, CalModelImp model) {
        super(new RootChildren(dateCtlr, model));
    }
    
}
