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

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.criteria.screen.dialog.TimeDialog;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add time criteria values.
 *
 * @author Jeremy Moore
 */
public class TimeAddAction extends CallableSystemAction {

    private final Lookup.Result<Data> result;

    public TimeAddAction() {
        super();
        setIcon(Icons.Add);
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            setEnabled(!result.allInstances().isEmpty());
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_TimeAddAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void performAction() {
        new TimeDialog().showCreateDialog();
    }
    
}
