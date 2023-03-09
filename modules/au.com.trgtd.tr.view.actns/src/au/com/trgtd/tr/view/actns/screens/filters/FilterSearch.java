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
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.openide.util.NbBundle;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.project.Project;

/**
 * MatcherEditor the matches actions for a search string.
 *
 * @author Jeremy Moore
 */
public final class FilterSearch extends FilterChoice implements PropertyChangeListener {

    public static final byte INDEX = 13;

//    private static final byte VERSION = 1;
//    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    /**
     * Constructs a new instance.
     */
    public FilterSearch() {
        initialise();
    }

    protected void initialise() {
        combo = new SearchComboBox();
        combo.addValueChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent e) {
        String string = (String) combo.getSelectedItem();
        if (string == null || string.trim().length() == 0) {
            fireMatchAll();
        } else {
            fireChanged(new SearchMatcher(string));
        }
    }

    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-search");
    }

    public String[] getSerialValues() {


        SearchComboBox searchCombo = (SearchComboBox) combo;
//      if (searchCombo == null || searchCombo.string == null) {
        if (searchCombo == null || searchCombo.getSearchString() == null) {
            return null;
        }
//      return new String[]{searchCombo.string};
        return new String[]{searchCombo.getSearchString()};
    }

    public void setSerialValues(String[] values) {
        if (combo == null) {
            return;
        }
        combo.stopChangeEvents();
        if (values == null || values.length == 0) {
            combo.setSelectedItem(null);
        } else {
            combo.setSelectedItem(values[0]);
        }
        combo.startChangeEvents();
    }

    public Object getValueAt() {
        return "";
    }

    public byte getIndex() {
        return INDEX;
    }

    public boolean equals(Object object) {
        if (!(object instanceof FilterSearch)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        SearchComboBox thisSearchCombo = (SearchComboBox) combo;
        SearchComboBox thatSearchCombo = (SearchComboBox) ((FilterChoice) object).combo;
//        String thisString = (thisSearchCombo == null) ? null : thisSearchCombo.string;
//        String thatString = (thatSearchCombo == null) ? null : thatSearchCombo.string;
        String thisString = (thisSearchCombo == null) ? null : thisSearchCombo.getSearchString();
        String thatString = (thatSearchCombo == null) ? null : thatSearchCombo.getSearchString();
        return Utils.equal(thisString, thatString);
    }

    protected boolean canExcludeNulls() {
        return false;
    }

    private static class SearchMatcher implements Matcher<Action> {

        private final String search;

        public SearchMatcher() {
            this.search = null;
        }

        public SearchMatcher(String search) {
            this.search = search.toLowerCase();
        }

        public boolean matches(Action action) {
            if (action.getDescription().toLowerCase().contains(search)) {
                return true;
            }
            if (action.getNotes().toLowerCase().contains(search)) {
                return true;
            }
            if (action.isStateDelegated()) {
                ActionStateDelegated s = (ActionStateDelegated) action.getState();
                if (s != null && s.getTo() != null) {
                    if (s.getTo().toLowerCase().contains(search)) {
                        return true;
                    }
                }
            }

//            // search project description.  Mantis #0001376
            Project project = (Project) action.getParent();
//            if (project != null) {
//                if (project.getDescription().toLowerCase().contains(search)) {
//                    return true;
//                }
//            }
//            // end search project description

            // search project path (including description).
            if (project != null) {
                String path = getPath(project, "");
                if (path.toLowerCase().contains(search)) {
                    return true;
                }
            }
            // end search project path

            return false;
        }

        private String getPath(Project project, String path) {
            if (path.length() > 0) {
                path = project.getDescription() + "/" + path;
            } else {
                path = project.getDescription();
            }
            Project parent = (Project) project.getParent();
            if (!parent.isRoot()) {
                return getPath(parent, path);
            }
            return path;
        }
    }

    private class SearchComboBoxModel extends DefaultComboBoxModel<String> {

        public final Vector<String> searches = new Vector<>();

        public void addSearch(String search) {
            if (search == null) {
                return;
            }
            search = search.trim();
            if (!searches.contains(search)) {
                searches.add(search);
                fireContentsChanged(search, 0, searches.size());
            }
        }

        @Override
        public String getElementAt(int index) {
            return searches.get(index);
        }

        @Override
        public int getSize() {
            return searches.size();
        }
    }

    private final class SearchComboBox extends FilterComboAbstract<String> {

        public SearchComboBox() {
            super(new SearchComboBoxModel());
            setEditable(true);
            startChangeEvents();
        }

        public void startChangeEvents() {
            addActionListener(actListener);
            getDoc().addDocumentListener(docListener);
        }

        public void stopChangeEvents() {
            removeActionListener(actListener);
            getDoc().removeDocumentListener(docListener);
        }
        
        private ActionListener actListener = (ActionEvent e) -> {
            selectionChange();
        };
        
        private Document getDoc() {
            return ((JTextComponent)getEditor().getEditorComponent()).getDocument();            
        }

        private String getEditingText() {
            return ((JTextComponent)getEditor().getEditorComponent()).getText();            
        }
        
        private DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent de) {
                fireValueChange();
            }
            public void removeUpdate(DocumentEvent de) {
                fireValueChange();
            }
            public void changedUpdate(DocumentEvent de) {
                fireValueChange();
            }
        };
        
        private void selectionChange() {
            String s = getSearchString();
            if (s != null && s.length() > 0) {
                ((SearchComboBoxModel) getModel()).addSearch(s);
            }
            fireValueChange();
        }
        
        public String getSearchString() {
            Object object = getSelectedItem();
            if (object instanceof String string) {
                return string.trim().toLowerCase();
            }
            object = getEditingText();
            if (object instanceof String string) {
                return string.trim().toLowerCase();
            }
            return null;
        }
        
    }
}
