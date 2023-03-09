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
package au.com.trgtd.tr.imports.thoughts;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.SystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.view.collect.CollectThoughtsAction;

/**
 * Import thoughts action.
 *
 * @author Jeremy Moore
 */
public final class ImportThoughtsAction extends CallableSystemAction {
    
    /** Constructs a new instance. */
    public ImportThoughtsAction() {
        super();
        
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
        r.allInstances();
        
        enableDisable();
    }
    
    private void enableDisable() {
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }
    
    /** Save the current datastore as another file. */
    @Override
    public void performAction() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        try {
            if (ImportThoughts.doImport(data)) {
                activateCollectThoughts();
            }
        } catch (Exception ex) {
            // Already handled.
        }
    }
    
    private void activateCollectThoughts() {
        CollectThoughtsAction collect = SystemAction.get(CollectThoughtsAction.class);
        collect.performAction();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(ImportThoughtsAction.class, "CTL_ImportThoughtsAction");
    }
    
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    private void updateEnablement(boolean b) {
        setEnabled(b);
    }
    
}
