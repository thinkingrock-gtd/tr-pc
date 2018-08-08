/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.data;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.resource.Resource;
import java.awt.EventQueue;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
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
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                dataChanged();
            }
        });
    }
    
    @Override
    protected String iconResource() {
        return Resource.DataSave;
    }

    private void dataChanged() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Data data = (Data)DataLookup.instance().lookup(Data.class);
                if (data == null) {
                    setEnabled(false);
                } else {
                    setEnabled(data.hasChanged());
                    data.addObserver(SaveAction.this);
                }
            }
        });
    }
    
    /** Save the current data source. */
    @Override
    public void performAction() {        
        DataStore ds = (DataStore)DataStoreLookup.instance().lookup(DataStore.class);
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
