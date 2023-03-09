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
package au.com.trgtd.tr.extract.criteria;

import java.util.List;
import java.util.Vector;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

/**
 * Provider of values ID list for time criterion values.
 *
 * @author Jeremy Moore
 */
public class ValueIDsProviderTime implements ValueIDsProvider, Observer {
    
    /** Singleton instance */
    public static ValueIDsProvider instance = new ValueIDsProviderTime();
    
    private Lookup.Result lookupResult;
    private List<Integer> ids;
    
    private ValueIDsProviderTime() {
        initialise();
        lookupResult = DataLookup.instance().lookupResult(Data.class);
        lookupResult.addLookupListener((LookupEvent lookupEvent) -> {
            initialise();
        });
    }
    
    private void initialise() {
        ids = new Vector<>();
        
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        for (Value v : data.getTimeCriterion().values.list()) {
            ids.add(v.getID());
        }
        
        data.getTimeCriterion().addObserver(this);
    }
    
    public List<Integer> getIDs() {
        return ids;
    }
    
    public void update(Observable obs, Object arg) {
        initialise();
    }
    
}
