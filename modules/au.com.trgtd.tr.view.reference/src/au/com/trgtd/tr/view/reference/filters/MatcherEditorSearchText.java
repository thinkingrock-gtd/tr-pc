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

import ca.odell.glazedlists.matchers.Matcher;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import javax.swing.JTextField;
import org.openide.util.NbBundle;
import tr.model.information.Information;

/**
 * MatcherEditor the matches information references for a search string.
 *
 * @author Jeremy Moore
 */
public class MatcherEditorSearchText extends MatcherEditorBase {

    private final JTextField searchText;

    public MatcherEditorSearchText() {
        searchText = new JTextField();
        searchText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                search();
            }
        });
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    search();
                }
            }
        });
    }

    @Override
    public Component getComponent() {
        return searchText;
    }

    public void search() {
        final String string = searchText.getText();
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
        return searchText.getText();
    }

    @Override
    public void setSerializable(final Serializable serializable) {
        searchText.setText((String) serializable);
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
        public boolean matches(final Information info) {
            return info.getDescription().toLowerCase().contains(search);
        }
    }

    @Override
    public void reset() {
        searchText.setText("");
        search();
    }
}
