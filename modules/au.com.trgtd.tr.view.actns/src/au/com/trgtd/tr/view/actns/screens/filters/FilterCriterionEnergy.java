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
package au.com.trgtd.tr.view.actns.screens.filters;

import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import au.com.trgtd.tr.view.filters.MultiChoiceDialog;
import au.com.trgtd.tr.view.filters.ValueAll;
import au.com.trgtd.tr.view.filters.ValueMultiple;
import au.com.trgtd.tr.view.filters.ValueMultipleEdit;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.criteria.Value;
import tr.model.util.Manager;

/**
 * MatcherEditor the matches the energy criterion.
 *
 * @author Jeremy Moore
 */
//public class FilterCriterionEnergy extends FilterChoice
//        implements PropertyChangeListener {
public class FilterCriterionEnergy extends FilterCriterion implements PropertyChangeListener {
    
    public static final byte INDEX = 4;
    public static final String ID = "energy";
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    /** Constructs a new instance. */
    public FilterCriterionEnergy() {
        initialise();
    }
    
    protected void initialise() {
        combo = new EnergyCombo();
        combo.addValueChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        Value value = (Value)combo.getSelectedItem();
        if (value == null) {
            fireMatchAll();
        } else if (value instanceof ValueAll) {
            fireMatchAll();
        } else if (value instanceof ValueMultiple multiple) {
            fireChanged(new ValueMatcher(multiple.getChosen(), excludeNulls));
        } else {
            fireChanged(new ValueMatcher(value, excludeNulls));
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-energy");
    }
    
    protected Value getValue(String id) {
        Data data = DataLookup.instance().lookup(Data.class);
        Manager<Value> manager = data.getEnergyCriterion().values;
        for (Value value : manager.list()) {
            try {
                if (value.getID() == Integer.parseInt(id)) {
                    return value;
                }
            } catch (Exception ex) {
            }
        }
        return null;
    }
    
    public byte getIndex() {
        return INDEX;
    }
    
    private static class ValueMatcher implements Matcher<Action> {
        
        private final List<Value> values;
        private final boolean excludeNulls;
        
        public ValueMatcher(Value value, boolean excludeNulls) {
            this.values = new Vector<>();
            this.values.add(value);
            this.excludeNulls = excludeNulls;
        }
        
        public ValueMatcher(List<Value> values, boolean excludeNulls) {
            this.values = values;
            this.excludeNulls = excludeNulls;
        }
        
        public boolean matches(Action action) {
            if (action.getEnergy() == null) {
                return ! excludeNulls;
            }
            for (Value value : values) {
                if (value.equals(action.getEnergy())) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private class EnergyComboBoxModel extends DefaultComboBoxModel<Value> implements Observer {
        private final ValueAll all;
        private final ValueMultiple multiple;
        private final ValueMultipleEdit multipleEdit;
        private Manager<Value> valueManager;
        private List<Value> values;
        private Lookup.Result result;
        /**
         * Creates a new instance for the given data model.
         * @param data The data model.
         */
        public EnergyComboBoxModel() {
            super();
            all = new ValueAll();
            multiple = new ValueMultiple();
            multipleEdit = new ValueMultipleEdit();
            initialise();
        }
        
        private void initialise() {
            if (valueManager != null) {
                valueManager.removeObserver(this);
            }
            
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                valueManager = null;
                values = new Vector<>();
            } else {
                valueManager = data.getEnergyCriterion().values;
                valueManager.addObserver(this);
                values = valueManager.list();
            }
            values.add(0, all);
            values.add(1, multiple);
//            if (Utilities.isWindows()) {
            if (!Utilities.isMac()) {
                values.add(2, multipleEdit);
            }
            // data lookup listener to force panel initialisation if data changes
            if (result == null) {
                result = DataLookup.instance().lookupResult(Data.class);
                result.addLookupListener((LookupEvent lookupEvent) -> {
                    update(null, null);
                });
            }
        }
        
        /** Implement ListModel.getElementAt(int index). */
        public Value getElementAt(int index) {
            return values.get(index);
        }
        
        /** Implement ListModel.getSize(). */
        public int getSize() {
            return values.size();
        }
        
        /** Implement Observer to fire contents changed. */
        public void update(Observable o, Object arg) {
            initialise();
            fireContentsChanged(this, 0, getSize());
        }
    }
    
    public class EnergyCombo extends FilterComboAbstract<Value> {
        
        private final ActionListener listener;
        
        public EnergyCombo() {
            super(new EnergyComboBoxModel());
//            if (Utilities.isWindows()) {
            if (!Utilities.isMac()) {
                listener = new WindowsActionListener();
            } else {
                listener = new RealActionListener();
            }
            addActionListener(listener);
        }
        
        public void stopChangeEvents() {
            removeActionListener(listener);
        }
        
        public void startChangeEvents() {
            addActionListener(listener);
            // for MS-Windows:
            lastSelectedItem = getSelectedItem();
        }
        
        private final class RealActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof ValueMultiple m) {
                    Vector<Value> all;
                    Data data = DataLookup.instance().lookup(Data.class);
                    if (data == null) {
                        all = new Vector<>();
                    } else {
                        all = data.getEnergyCriterion().values.list();
                    }
                    MultiChoiceDialog<Value> d = new MultiChoiceDialog<>(EnergyCombo.this, all, m.getChosen(), false);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-energy"));
                    d.setLocationRelativeTo(EnergyCombo.this);
                    d.setVisible(true);
                    if (d.isOkay()) {
                        m.setChosen(d.getChosen());
                    }
                }
                fireValueChange();
            }
        }
        
        private Object lastSelectedItem;
        
        private final class WindowsActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof ValueMultipleEdit) {
                    EnergyComboBoxModel model = (EnergyComboBoxModel)getModel();
                    ValueMultiple m = model.multiple;
                    Vector<Value> all;
                    Data data = DataLookup.instance().lookup(Data.class);
                    if (data == null) {
                        all = new Vector<>();
                    } else {
                        all = data.getEnergyCriterion().values.list();
                    }
                    MultiChoiceDialog<Value> d = new MultiChoiceDialog<>(EnergyCombo.this, all, m.getChosen(), false);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-energy"));
                    d.setLocationRelativeTo(EnergyCombo.this);
                    d.setVisible(true);
                    if (d.isOkay()) {
                        m.setChosen(d.getChosen());
                        setSelectedItem(m);
                        lastSelectedItem = m;                                                
                    } else {
                        setSelectedItem(lastSelectedItem);
                    }
                } else {
                    lastSelectedItem = object;
                }
                fireValueChange();
            }
        }
    }
    
}

