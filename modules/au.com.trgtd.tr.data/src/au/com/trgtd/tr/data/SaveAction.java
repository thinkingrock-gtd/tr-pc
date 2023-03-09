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
package au.com.trgtd.tr.data;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.resource.Resource;
import java.awt.EventQueue;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup; 
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

public final class SaveAction extends CallableSystemAction implements Observer {
    
    /** Constructs a new instance. */
    public SaveAction() {
        super();
        setEnabled(false);
        dataChanged();
        Lookup.Result r = DataLookup.instance().lookupResult(Data.class);
        r.addLookupListener((LookupEvent lookupEvent) -> {
            dataChanged();
        });
    }
    
    @Override
    protected String iconResource() {
        return Resource.DataSave;
    }

    private void dataChanged() {
        EventQueue.invokeLater(() -> {
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                setEnabled(false);
            } else {
                setEnabled(data.hasChanged());
                data.addObserver(SaveAction.this);
            }
        });
    }
    
    /** Save the current data source. */
    @Override
    public void performAction() {        
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) return;        
        
        try {
            ds.store();
        } catch (Exception ex) {
        }
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(SaveAction.class, "CTL_SaveAction");
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    @Override
    public void update(Observable observable, Object arguement) {
        dataChanged();
    }
    
}
