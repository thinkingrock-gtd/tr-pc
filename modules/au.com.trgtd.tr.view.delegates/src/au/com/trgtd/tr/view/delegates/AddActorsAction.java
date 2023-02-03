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
package au.com.trgtd.tr.view.delegates;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.InitialAction;
import au.com.trgtd.tr.view.actors.ActorDialog;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add actors using the actors dialog.
 *
 * @author Jeremy Moore
 */
public class AddActorsAction extends CallableSystemAction implements InitialAction {
    
    private final Lookup.Result result;
    
    public AddActorsAction() {
        super();
        setIcon(Icons.Add);
        result = DataLookup.instance().lookupResult(Data.class);
        result.addLookupListener((LookupEvent lookupEvent) -> {
            setEnabled(!result.allInstances().isEmpty());
        });
    }
    
    /** 
     * Gets the action identifier.
     * @return The ID.
     */
    @Override
    public String getID() {
        return "AddActorsAction";
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /** 
     * Gets the initial action name.
     * @return The name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_AddActorsAction");
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public void performAction() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        new ActorDialog(data).showCreateDialog();
    }
    
}
