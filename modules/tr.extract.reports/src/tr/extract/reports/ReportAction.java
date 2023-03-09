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
package tr.extract.reports;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Report action.
 *
 * @author Jeremy Moore
 */
public abstract class ReportAction extends CallableSystemAction {
    
    private final Lookup.Result dataResult;
    
    /** Constructs a new instance. */
    public ReportAction() {
        super();
        enableDisable();
        dataResult = DataLookup.instance().lookupResult(Data.class);
        dataResult.addLookupListener((LookupEvent lookupEvent) -> {
            enableDisable();
        });
        dataResult.allInstances();
        
    }
    
    private void enableDisable() {
        Data data = DataLookup.instance().lookup(Data.class);
        setEnabled(data != null);
    }
    
    /* Run the report. */
    @Override
    public void performAction() {
    }
    
    /* Get the action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(ReportAction.class, "default-name-report");
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /* Get the help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
}
