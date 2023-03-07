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
import au.com.trgtd.tr.view.filters.FilterComboAbstract;
import ca.odell.glazedlists.matchers.Matcher;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import org.openide.util.NbBundle;
import tr.model.information.Information;

/**
 * MatcherEditor the matches information references for a search string.
 *
 * @author Jeremy Moore
 */
public class MatcherEditorSearch extends MatcherEditorBase
        implements PropertyChangeListener {

    private final SearchComboBox searchCombo;

    public MatcherEditorSearch() {
        searchCombo = new SearchComboBox();
        searchCombo.addValueChangeListener(this);
    }

    @Override
    public Component getComponent() {
        return searchCombo;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent e) {
        final String string = (String) searchCombo.getSelectedItem();
        if (string == null || string.trim().isEmpty()) {
            fireMatchAll();
        } else {
            fireChanged(new SearchMatcher(string));
        }
    }

    @Override
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-search");
    }

    @Override
    public Serializable getSerializable() {
        return (Serializable) searchCombo.getSelectedItem();
    }

    @Override
    public void setSerializable(final Serializable serializable) {
        searchCombo.setSelectedItem(serializable);
    }

    private static class SearchMatcher implements Matcher<Information> {

        private final String search;

        public SearchMatcher() {
            this.search = null;
        }

        public SearchMatcher(final String search) {
            this.search = search.toLowerCase();
        }

        @Override
        public boolean matches(Information info) {
            if (info.getDescription().toLowerCase().contains(search)) {
                return true;
            }
            return info.getNotes().toLowerCase().contains(search);
        }
    }

    private class SearchComboBoxModel extends DefaultComboBoxModel<String> {

        public final List<String> searches = new ArrayList<>();

        public void addSearch(final String search) {
            if (search == null) {
                return;
            }
            final String trimmed = search.trim();
            if (!trimmed.isEmpty() && !searches.contains(trimmed)) {
                searches.add(trimmed);
                fireContentsChanged(trimmed, 0, searches.size());
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

    @Override
    public void reset() {
        searchCombo.setSelectedItem(null);
        searchCombo.fireValueChange();
    }

    private class SearchComboBox extends FilterComboAbstract<String> {

        public String string;

        public SearchComboBox() {
            super(new SearchComboBoxModel());
            setEditable(true);
            addActionListener(listener);
        }

        @Override
        public void stopChangeEvents() {
            removeActionListener(listener);
        }

        @Override
        public void startChangeEvents() {
            addActionListener(listener);
        }

        private final ActionListener listener = (ActionEvent e) -> {
            setString(null);
            if (getSelectedItem() instanceof String str) {
                final String trimmed = str.trim();
                if (!trimmed.isEmpty()) {
                    setString(trimmed);
                }
            }
        };

        private void setString(final String string) {
            if (string == null || string.trim().isEmpty()) {
                this.string = null;
            } else {
                this.string = string.trim().toLowerCase();
                final SearchComboBoxModel model = (SearchComboBoxModel) this.getModel();
                model.addSearch(string);
            }
            fireValueChange();
        }

        public Component getTableCellRendererComponent(
                final JTable table, final Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            final JComboBox dummy = new TRComboBox<>();
            dummy.setEnabled(true);
            dummy.setBackground(Color.white);
            return dummy;
        }
    }
}
