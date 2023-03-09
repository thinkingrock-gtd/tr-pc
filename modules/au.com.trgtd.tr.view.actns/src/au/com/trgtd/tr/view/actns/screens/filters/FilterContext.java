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
import au.com.trgtd.tr.view.filters.ContextAll;
import au.com.trgtd.tr.view.filters.ContextMultiple;
import au.com.trgtd.tr.view.filters.ContextMultipleEdit;
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import au.com.trgtd.tr.view.filters.MultiChoiceDialog;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.context.Context;
import tr.model.util.Manager;

/**
 * MatcherEditor the matches information references for a context selection.
 *
 * @author Jeremy Moore
 */
public class FilterContext extends FilterChoice implements PropertyChangeListener {
    
    public static final byte INDEX = 2;
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    /** Constructs a new instance. */
    public FilterContext() {
        super();
        initialise();
    }
    
    protected void initialise() {
        combo = new ContextsCombo();
        combo.addValueChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        Context context = (Context)combo.getSelectedItem();
        if (context == null) {
            fireMatchAll();
        } else if (context instanceof ContextAll) {
            fireMatchAll();
        } else if (context instanceof ContextMultiple m) {
            fireChanged(new ContextMatcher(m.getChosen()));
        } else {
            fireChanged(new ContextMatcher(context));
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-context");
    }
    
    public byte getIndex() {
        return INDEX;
    }
    
    private static class ContextMatcher implements Matcher<Action> {
        private final boolean all;
        private final List<Context> contexts;
        
        public ContextMatcher() {
            this.all = true;
            this.contexts = null;
        }
        
        public ContextMatcher(Context context) {
            this.all = false;
            this.contexts = new Vector<>();
            this.contexts.add(context);
        }
        
        public ContextMatcher(List<Context> contexts) {
            this.all = false;
            this.contexts = contexts;
        }
        
        public boolean matches(Action action) {
            if (all) {
                return true;
            }
            for (Context context : contexts) {
                if (action.getContext().equals(context)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private class ContextsComboBoxModel extends DefaultComboBoxModel<Context> implements Observer {
        private final ContextAll contextAll;
        private final ContextMultiple contextMultiple;
        private final ContextMultipleEdit contextMultipleEdit;
        private Manager<Context> contextManager;
        private List<Context> contexts;
        private Lookup.Result result;
        /**
         * Creates a new instance for the given data model.
         * @param data The data model.
         */
        public ContextsComboBoxModel() {
            super();
            contextAll = new ContextAll();
            contextMultiple = new ContextMultiple();
            contextMultipleEdit = new ContextMultipleEdit();
            initialise();
        }
        
        private void initialise() {
            if (contextManager != null) {
                contextManager.removeObserver(this);
            }
            Data data = DataLookup.instance().lookup(Data.class);
            if (data == null) {
                contextManager = null;
                contexts = new Vector<>();
            } else {
                contextManager = data.getContextManager();
                contextManager.addObserver(this);
                contexts = contextManager.list();
                Collections.sort(contexts);
            }
            contexts.add(0, contextAll);
            contexts.add(1, contextMultiple);
//          if (Utilities.isWindows()) {
            if (!Utilities.isMac()) {
                contexts.add(2, contextMultipleEdit);
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
        @Override
        public Context getElementAt(int index) {
            return contexts.get(index);
        }
        
        /** Implement ListModel.getSize(). */
        @Override
        public int getSize() {
            return contexts.size();
        }
        
        /** Implement Observer to fire contents changed. */
        public void update(Observable o, Object arg) {
            initialise();
            fireContentsChanged(this, 0, getSize());
        }
    }
    
    public class ContextsCombo extends FilterComboAbstract<Context> {
        
        private final ActionListener listener;
        
        public ContextsCombo() {
            super(new ContextsComboBoxModel());
//          if (Utilities.isWindows()) {
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
        
        @Override
        public JComboBox<Context> getJComboBox() {
            return this;
        }
        
        private final class RealActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof ContextMultiple m) {
                    Vector<Context> all;
                    Data data = DataLookup.instance().lookup(Data.class);
                    if (data == null) {
                        all = new Vector<>();
                    } else {
                        all = data.getContextManager().list();
                    }
                    MultiChoiceDialog<Context> d = new MultiChoiceDialog<>(ContextsCombo.this, all, m.getChosen(), true);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-context"));
                    d.setLocationRelativeTo(ContextsCombo.this);
                    d.setVisible(true);
                    if (d.isOkay()) {
                        m.setChosen(d.getChosen());
                    }
                }
                fireValueChange();
            }
        };
        
        private Object lastSelectedItem;
        
        private final class WindowsActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof ContextMultipleEdit) {
                    ContextsComboBoxModel model = (ContextsComboBoxModel)getModel();
                    ContextMultiple m = model.contextMultiple;
                    Vector<Context> all;
                    Data data = DataLookup.instance().lookup(Data.class);
                    if (data == null) {
                        all = new Vector<>();
                    } else {
                        all = data.getContextManager().list();
                    }
                    MultiChoiceDialog<Context> d = new MultiChoiceDialog<>(ContextsCombo.this, all, m.getChosen(), true);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-context"));
                    d.setLocationRelativeTo(ContextsCombo.this);
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
        };
    }
    
    public String[] getSerialValues() {
        if (combo == null) return null;
        
        Context context = (Context)combo.getSelectedItem();
        if (context == null) {
            return null;
        }
        if (context instanceof ContextAll) {
            return new String[] {ContextAll.ID};
        }
        if (context instanceof ContextMultiple contextMultiple) {
            Vector<Context> chosen = contextMultiple.getChosen();
            if (chosen == null || chosen.isEmpty()) {
                return null;
            }
            Vector<String> values = new Vector<>();
            for (Context c : chosen) {
                if (c != null) {
                    values.add(c.getName());
                }
            }
            return values.toArray(new String[0]);
        }
        return new String[] { context.getName() };
    }
    
    public void setSerialValues(String[] values) {
        if (combo == null) return;
        
        combo.stopChangeEvents();
        
        if (values == null || values.length == 0) {
            combo.setSelectedItem(null);
        } else if (values.length == 1) {
            if (values[0].equals(ContextAll.ID)) {
                combo.setSelectedIndex(0); // all
            } else {
                combo.setSelectedItem(getContext(values[0]));
            }
        } else if (values.length > 1) {
            Object o = combo.getItemAt(1);
            if (o instanceof ContextMultiple multiple) {
                Vector<Context> chosen = new Vector<>();
                for (String name : values) {
                    Context context = getContext(name);
                    if (context != null) {
                        chosen.add(context);
                    }
                }
                multiple.setChosen(chosen);
            }
            combo.setSelectedIndex(1); // multiple
        }
        
        combo.startChangeEvents();
    }
    
    private Context getContext(String name) {
        if (name == null) {
            return null;
        }
        Data data = DataLookup.instance().lookup(Data.class);
        Manager<Context> contextManager = data.getContextManager();
        for (Context context : contextManager.list()) {
            if (context.getName().equals(name)) {
                return context;
            }
        }
        return null;
    }
    
    protected boolean canExcludeNulls() {
        return false;
    }
    
}
