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
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Root node for delegated actions.
 * @author Jeremy Moore
 */
public class DelegatedNodeRoot extends AbstractNode {

    private final InstanceContent content;

    /**
     * Constructs a new instance.
     * @param dateCtlr The date controller.
     * @param calModel The calendar model.
     */
    public DelegatedNodeRoot(DateCtlr dateCtlr, CalModelImp calModel) {
        this(new DelegatedChildren(dateCtlr, calModel), new InstanceContent());
    }

    private DelegatedNodeRoot(DelegatedChildren children, InstanceContent content) {
        super(children, new AbstractLookup(content));
        this.content = content;
        this.content.add(this);
        setIconBaseWithExtension("au/com/trgtd/tr/view/calendar/resource/folder.png");
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(CalModelImp.class, "delegated.followup");
    }
    
}
