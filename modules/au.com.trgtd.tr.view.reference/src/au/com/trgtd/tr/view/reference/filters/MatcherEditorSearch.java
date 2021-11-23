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

package au.com.trgtd.tr.view.reference.filters;

import au.com.trgtd.tr.swing.TRComboBox;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import org.openide.util.NbBundle;
import tr.model.information.Information;
import au.com.trgtd.tr.view.filters.FilterComboAbstract;

/**
 * MatcherEditor the matches information references for a search string.
 *
 * @author Jeremy Moore
 */
public class MatcherEditorSearch extends MatcherEditorBase implements PropertyChangeListener {
    
    private final SearchComboBox searchCombo;
    
    /** Constructs a new instance. */
    public MatcherEditorSearch() {
        searchCombo = new SearchComboBox();
        searchCombo.addValueChangeListener(this);
    }
    
    public Component getComponent() {
        return searchCombo;
    }
    
    public void propertyChange(PropertyChangeEvent e) {
//        if (!e.getPropertyName().equals(FilterCombo.PROPERTY_VALUE)) {
//            return;
//        }
        String string = (String)searchCombo.getSelectedItem();
        if (string == null || string.trim().length() == 0) {
            fireMatchAll();
        } else {
            fireChanged(new SearchMatcher(string));
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-search");
    }
    
    public Serializable getSerializable() {
        return (Serializable)searchCombo.getSelectedItem();
    }
    
    public void setSerializable(Serializable serializable) {
        searchCombo.setSelectedItem(serializable);
    }
    
    private static class SearchMatcher implements Matcher<Information> {
        
        private final String search;
        
        public SearchMatcher() {
            this.search = null;
        }
        
        public SearchMatcher(String search) {
            this.search = search.toLowerCase();
        }
        
        public boolean matches(Information info) {
            if (info.getDescription().toLowerCase().contains(search)) {
                return true;
            }
            if (info.getNotes().toLowerCase().contains(search)) {
                return true;
            }
            return false;
        }
    }
    
    private class SearchComboBoxModel extends DefaultComboBoxModel {
        
        public final Vector<String> searches = new Vector<String>();
        
        public void addSearch(String search) {
            if (search == null) {
                return;
            }
            search = search.trim();
            if (search.length() > 0 && !searches.contains(search)) {
                searches.add(search);
                fireContentsChanged(search, 0, searches.size());
            }
        }
        
        public Object getElementAt(int index) {
            return searches.get(index);
        }
        
        public int getSize() {
            return searches.size();
        }
    }
    
    @Override
    public void reset() {
        searchCombo.setSelectedItem(null);
        searchCombo.fireValueChange();
    }

    private class SearchComboBox extends FilterComboAbstract {
        
        public SearchComboBox() {
            super(new SearchComboBoxModel());
            setEditable(true);
            addActionListener(listener);
        }
        
        public void stopChangeEvents() {
            removeActionListener(listener);
        }
        
        public void startChangeEvents() {
            addActionListener(listener);
        }
        
        private ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setString(null);
                Object object = getSelectedItem();
                if (object instanceof String) {
                    String string = ((String)object).trim();
                    if (string.length() > 0) {
                        setString(string);
                    }
                }
            }
        };
        
        public String string;
// remove these comments to change to OR instead of AND
//      public String[] strings;
        
        private void setString(String string) {
            if (string == null || string.trim().length() == 0) {
                this.string = null;
// remove these comments to change to OR instead of AND
//              this.strings = null;
            } else {
                this.string = string.trim().toLowerCase();
// remove these comments to change to OR instead of AND
//              this.strings = this.string.split("\\s+");
                SearchComboBoxModel model = (SearchComboBoxModel)this.getModel();
                model.addSearch(string);
            }
            fireValueChange();
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JComboBox dummy = new TRComboBox();
            dummy.setEnabled(true);
            dummy.setBackground(Color.white);
            return dummy;
        }
    }
    
}

