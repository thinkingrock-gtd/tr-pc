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
package au.com.trgtd.tr.export;

import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export action.
 *
 * @author Jeremy Moore
 */
public abstract class ExportAction extends CallableSystemAction {
    
    private final Lookup.Result dataResult;
    
    /** Constructs a new instance. */
    public ExportAction() {
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
    
    /** Run the export. */
    @Override
    public void performAction() {
    }
    
    /** Get the action name. */
    @Override
    public String getName() {
        return NbBundle.getMessage(ExportAction.class, "default-name-export");
    }
    
//    /** Get the action icon. */
//    protected abstract String iconResource();
//
//
//    /** Get the action icon for text exports. */
//    protected String iconResourceText() {
//        return Resources.PATH_TEXT_ICON;
//    }
//
//    /** Get the action icon. */
//    protected String iconResourceXML() {
//        return Resources.PATH_XML_ICON;
//    }
    
    /** Get the help context. */
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
}
