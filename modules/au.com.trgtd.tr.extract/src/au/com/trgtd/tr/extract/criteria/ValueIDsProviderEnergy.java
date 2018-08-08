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

package au.com.trgtd.tr.extract.criteria;

import java.util.List;
import java.util.Vector;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Provider of values ID list for energy criterion values.
 *
 * @author Jeremy Moore
 */
public class ValueIDsProviderEnergy implements ValueIDsProvider, Observer {
    
    /** Singleton instance */
    public static ValueIDsProvider instance = new ValueIDsProviderEnergy();
    
    private Lookup.Result lookupResult;
    private List<Integer> ids;
    
    private ValueIDsProviderEnergy() {
        initialise();
        lookupResult = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        lookupResult.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                initialise();
            }
        });
    }
    
    private void initialise() {
        ids = new Vector<Integer>();
        
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        for (Value v : data.getEnergyCriterion().values.list()) {
            ids.add(v.getID());
        }
        
        data.getEnergyCriterion().addObserver(this);
    }
    
    public List<Integer> getIDs() {
        return ids;
    }
    
    public void update(Observable obs, Object arg) {
        initialise();
    }
    
}
