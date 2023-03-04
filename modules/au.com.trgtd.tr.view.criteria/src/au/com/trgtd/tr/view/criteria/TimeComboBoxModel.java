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
package au.com.trgtd.tr.view.criteria;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Criterion;
import tr.model.criteria.Value;

/**
 * Combo box model for time criteria.
 */
public class TimeComboBoxModel extends DefaultComboBoxModel<Value> implements Observer {

    private Criterion criterion;
    private Lookup.Result result;

    /**
     * Constructs a new instance.
     */
    public TimeComboBoxModel() {
        super();
        initialise();
    }

    private void initialise() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            criterion = null;
            return;
        }

        if (criterion != null) {
            criterion.removeObserver(this);
        }

        criterion = data.getTimeCriterion();

        criterion.addObserver(this);

        if (result == null) {
            result = DataLookup.instance().lookupResult(Data.class);
            result.addLookupListener((LookupEvent lookupEvent) -> {
                update(null, null);
            });
        }
    }

    @Override
    public Value getElementAt(int index) {
        return criterion == null || !criterion.isUse() ? null : criterion.values.get(index);
    }

    @Override
    public int getSize() {
        return criterion == null || !criterion.isUse() ? 0 : criterion.values.size();
    }

    // Implement Observer to fire contents changed.
    @Override
    public void update(Observable obs, Object arg) {
        initialise();
        fireContentsChanged(this, 0, getSize());
    }

}
