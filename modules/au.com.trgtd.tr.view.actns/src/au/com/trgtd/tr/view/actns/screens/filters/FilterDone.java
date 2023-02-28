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
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.NbBundle;
import tr.model.action.Action;

/**
 * MatcherEditor the matches information references for done selection.
 *
 * @author Jeremy Moore
 */
public class FilterDone extends FilterChoice implements PropertyChangeListener {
    
    public static final byte INDEX = 0;
    
//    private static final byte VERSION = 1;
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    /** Constructs a new instance. */
    public FilterDone() {
        initialise();
    }
    
    protected void initialise() {
        combo = new DoneChoiceComboBox(new DoneChoiceComboBoxModel());
        combo.addValueChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        Choice choice = (Choice)combo.getSelectedItem();
        if (choice instanceof Done) {
            fireChanged(new DoneMatcher(true));
        } else if (choice instanceof ToDo) {
            fireChanged(new DoneMatcher(false));
        } else {
            fireMatchAll();
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-done");
    }
    
    public String[] getSerialValues() {
        if (combo == null) return null;
        
        Choice choice = (Choice)combo.getSelectedItem();
        
        return (choice == null) ? null : new String[] { choice.getID() };
    }
    
    public void setSerialValues(String[] values) {
        if (combo == null) return;
        
        combo.stopChangeEvents();
        
        if (values == null || values.length == 0) {
            combo.setSelectedItem(null);
        } else {
            combo.setSelectedItem(getChoice(values[0]));
        }
        
        combo.startChangeEvents();
    }
    
    private Choice getChoice(String id) {
        if (id == null) return null;
        if (id.equals(All.ID)) return new All();
        if (id.equals(Done.ID)) return new Done();
        if (id.equals(ToDo.ID)) return new ToDo();
        return null;
    }
    
    
    public byte getIndex() {
        return INDEX;
    }
    
    public boolean isShowDone() {
        return ! (combo.getSelectedItem() instanceof ToDo);
    }
    
    public boolean equals(Object object) {
        if (!(object instanceof FilterDone)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        Choice thisChoice = (Choice)combo.getSelectedItem();
        Choice thatChoice = (Choice)((FilterChoice)object).combo.getSelectedItem();
        return Utils.equal(thisChoice, thatChoice);
    }
    
    protected boolean canExcludeNulls() {
        return false;
    }
    
    public final class All extends Choice {
        public static final String ID = "0";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-all");
        }
    }
    public final class Done extends Choice {
        public static final String ID = "1";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-done-done");
        }
    }
    public final class ToDo extends Choice {
        public static final String ID = "2";
        public String getID() {
            return ID;
        }
        public String getLabel() {
            return NbBundle.getMessage(getClass(), "filter-done-todo");
        }
    }
    
    private class DoneChoiceComboBoxModel extends DefaultComboBoxModel<Choice> {
        private final Choice[] items = new Choice[] {
            new All(),
            new Done(),
            new ToDo()
        };
        public Choice getElementAt(int index) {
            return items[index];
        }
        public int getSize() {
            return items.length;
        }
    }
    
    private class DoneChoiceComboBox extends FilterComboAbstract<Choice> {
        
        public DoneChoiceComboBox(DoneChoiceComboBoxModel model) {
            super(model);
            addActionListener(listener);
        }
        
        public void stopChangeEvents() {
            removeActionListener(listener);
        }
        
        public void startChangeEvents() {
            addActionListener(listener);
        }
        
        private ActionListener listener = (ActionEvent e) -> {
            fireValueChange();
        };
    }
    
    private static class DoneMatcher implements Matcher<Action> {
        
        private final boolean done;
        
        public DoneMatcher(boolean done) {
            this.done = done;
        }
        
        public boolean matches(Action action) {
            return action.isDone() == done;
        }
    }
    
//    public void persist(PersistenceOutputStream out) throws Exception {
//        super.persist(out);
//        out.writeByte(VERSION);
//        out.writeStrings(getSerialValues());
//    }
//
//    public void restore(PersistenceInputStream in) throws Exception {
//        super.restore(in);
//        byte version = in.readByte();
//        if (version != VERSION) {
//            throw new Exception("Unknown persistance version for " + getClass().getName());
//        }
//        setSerialValues(in.readStrings());
//    }
    
}

