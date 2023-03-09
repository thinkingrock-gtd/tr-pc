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

import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.filters.Choice;
import au.com.trgtd.tr.view.filters.ChoiceAll;
import au.com.trgtd.tr.view.filters.ChoiceMultiple;
import au.com.trgtd.tr.view.filters.ChoiceMultipleEdit;
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import au.com.trgtd.tr.view.filters.MultiChoiceDialog;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.action.Action;

/**
 * MatcherEditor the matches information references for a topic selection.
 *
 * @author Jeremy Moore
 */
public class FilterStatus extends FilterChoice implements PropertyChangeListener {
    
    public static final byte INDEX = 1;
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    /** Constructs a new instance. */
    public FilterStatus() {
        initialise();
    }
    
    protected void initialise() {
        combo = new StatusComboBox();
        combo.addValueChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        Choice choice = (Choice)combo.getSelectedItem();
        if (choice == null) {
            fireMatchAll();
        } else if (choice instanceof All) {
            fireMatchAll();
        } else if (choice instanceof Multiple m) {
            fireChanged(new StatusMatcher(m.getChosen()));
        } else {
            fireChanged(new StatusMatcher(choice));
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-status");
    }
    
    public String[] getSerialValues() {
        if (combo == null) {
            return null;
        }
        Choice item = (Choice)combo.getSelectedItem();
        if (item == null) {
            return null;
        }
        if (item instanceof Multiple m) {
            if (m == null || m.getChosen() == null) {
                return null;
            }
            String[] values = new String[m.getChosen().size()];
            for (int i = 0; i < values.length; i++) {
                values[i] = (m.getChosen().get(i)).getID();
            }
            return values;
        }
        return new String[] { item.getID() };
    }
    
    public void setSerialValues(String[] values) {
        if (combo == null) return;
        
        combo.stopChangeEvents();
        
        if (values == null || values.length == 0) {
            combo.setSelectedItem(null);
        } else if (values.length == 1) {
            combo.setSelectedItem(getChoice(values[0]));
        } else if (values.length > 1) {
            combo.setSelectedIndex(1); // multiple
            Multiple multiple = (Multiple)combo.getItemAt(1);
            multiple.setChosen(new Vector<>());
            for (String id : values) {
                multiple.getChosen().add(getChoice(id));
            }
            combo.setSelectedItem(multiple);
        }
        
        combo.startChangeEvents();
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof FilterStatus)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Choice thisChoice = (Choice)combo.getSelectedItem();
        Choice thatChoice = (Choice)((FilterChoice)object).combo.getSelectedItem();
        return Utils.equal(thisChoice, thatChoice);
    }
    
    private Choice getChoice(String id) {
        if (id == null) return null;
        if (id.equals(All.ID)) return new All();
        if (id.equals(ChoiceDoASAP.ID)) return new ChoiceDoASAP();
        if (id.equals(ChoiceInactive.ID)) return new ChoiceInactive();
        if (id.equals(ChoiceDelegated.ID)) return new ChoiceDelegated();
        if (id.equals(ChoiceScheduled.ID)) return new ChoiceScheduled();
        return null;
    }
    
    public byte getIndex() {
        return INDEX;
    }
    
    protected boolean canExcludeNulls() {
        return false;
    }
    
    public class All extends ChoiceAll {
        public static final String ID = "0";
        public String getID() {
            return ID;
        }
    }
    private class Multiple extends ChoiceMultiple {
        public static final String ID = "1";
        public String getID() {
            return ID;
        }
    }
    public class ChoiceDelegated extends Choice {
        public static final String ID = "2";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-status-delegated");
        }
    }
    public class ChoiceDoASAP extends Choice {
        public static final String ID = "3";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-status-doasap");
        }
    }
    public class ChoiceInactive extends Choice {
        public static final String ID = "4";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-status-inactive");
        }
    }
    public class ChoiceScheduled extends Choice {
        public static final String ID = "5";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-status-scheduled");
        }
    }
    
    private static class StatusMatcher implements Matcher<Action> {
        
        private final List<Choice> choices;
        
        public StatusMatcher(Choice choice) {
            this.choices = new Vector<>();
            this.choices.add(choice);
        }
        
        public StatusMatcher(List<Choice> choices) {
            this.choices = choices;
        }
        
        public boolean matches(Action action) {
            for (Choice choice : choices) {
                if (choice instanceof All) {
                    return true;
                }
                if (action.isStateASAP() && choice instanceof ChoiceDoASAP) {
                    return true;
                }
                if (action.isStateInactive() && choice instanceof ChoiceInactive) {
                    return true;
                }
                if (action.isStateScheduled() && choice instanceof ChoiceScheduled) {
                    return true;
                }
                if (action.isStateDelegated() && choice instanceof ChoiceDelegated) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private class StatusComboBoxModel extends DefaultComboBoxModel<Choice> {
        
        private final Choice all = new All();
        private final Multiple multiple = new Multiple();
        private final ChoiceMultipleEdit multipleEdit = new ChoiceMultipleEdit();
        private final Choice doasap = new ChoiceDoASAP();
        private final Choice inactive = new ChoiceInactive();
        private final Choice delegated = new ChoiceDelegated();
        private final Choice scheduled = new ChoiceScheduled();
        private final Choice[] items;
        
        public StatusComboBoxModel() {
            super();
//            if (Utilities.isWindows()) {
            if (!Utilities.isMac()) {
                items = new Choice[] {
                    all,
                    multiple,
                    multipleEdit,
                    doasap,
                    inactive,
                    delegated,
                    scheduled,
                };
            } else {
                items = new Choice[] {
                    all,
                    multiple,
                    doasap,
                    inactive,
                    delegated,
                    scheduled,
                };
            }
        }
        
        public Choice getElementAt(int index) {
            return items[index];
        }
        
        public int getSize() {
            return items.length;
        }
    }
    
    public class StatusComboBox extends FilterComboAbstract<Choice> {
        
        private final Vector<Choice> options = new Vector<>();
        private final ActionListener listener;
        private Object lastSelectedItem;
        
        public StatusComboBox() {
            super(new StatusComboBoxModel());
            options.add(new ChoiceDoASAP());
            options.add(new ChoiceInactive());
            options.add(new ChoiceDelegated());
            options.add(new ChoiceScheduled());
//            if (Utilities.isWindows()) {
            if (!Utilities.isMac()) {
                listener = new WindowsActionListener();
            } else {
                listener = new RealActionListener();
            }
        }
        
        public void startChangeEvents() {
            addActionListener(listener);
            // for MS-Windows:
            lastSelectedItem = getSelectedItem();
        }
        
        public void stopChangeEvents() {
            removeActionListener(listener);
        }
        
        private final class RealActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof Multiple m) {
                    MultiChoiceDialog<Choice> d = new MultiChoiceDialog<>(StatusComboBox.this, options, m.getChosen(), true);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-status"));
                    d.setLocationRelativeTo(StatusComboBox.this);
                    d.setVisible(true);
                    if (d.isOkay()) {
                        m.setChosen(d.getChosen());
                    }
                }
                fireValueChange();
            }
        };
        
        private final class WindowsActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object object = getSelectedItem();
                if (object instanceof ChoiceMultipleEdit) {
                    StatusComboBoxModel model = (StatusComboBoxModel)getModel();
                    Multiple m = model.multiple;
                    MultiChoiceDialog<Choice> d = new MultiChoiceDialog<>(StatusComboBox.this, options, m.getChosen(), true);
                    d.setTitle(NbBundle.getMessage(getClass(), "filter-topic"));
                    d.setLocationRelativeTo(StatusComboBox.this);
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

