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
package au.com.trgtd.tr.view.someday.screen;

import au.com.trgtd.tr.view.ViewUtils;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import au.com.trgtd.tr.view.someday.filters.MatcherEditorCreatedFrom;
import au.com.trgtd.tr.view.someday.filters.MatcherEditorCreatedTo;
import au.com.trgtd.tr.view.someday.filters.MatcherEditorSearch;
import au.com.trgtd.tr.view.someday.filters.MatcherEditorTopic;
import tr.model.future.Future;

/**
 * Filters for future items.
 *
 * @author Jeremy Moore
 */
public class SomedayFilters {
    
    /** Constructs a new instance. */
    public SomedayFilters() {
        matcherEditorCreatedFrom = new MatcherEditorCreatedFrom();
        matcherEditorCreatedTo = new MatcherEditorCreatedTo();
        matcherEditorTopics = new MatcherEditorTopic();
        matcherEditorSearch = new MatcherEditorSearch();
    }
    
    public MatcherEditor<Future> getMatcherEditor() {
        if (matcherEditor == null) {
            BasicEventList<MatcherEditor<Future>> list = new BasicEventList<>();
            list.add(matcherEditorCreatedFrom);
            list.add(matcherEditorCreatedTo);
            list.add(matcherEditorTopics);
            list.add(matcherEditorSearch);
            matcherEditor = new CompositeMatcherEditor<>(list);
        }
        return matcherEditor;
    }
    
    public Component getComponent() {
        if (component == null) {
            component = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 1));
            component.setBackground(ViewUtils.COLOR_PANEL_BG);
            component.add(new JLabel(matcherEditorCreatedFrom.getLabel()));
            component.add(matcherEditorCreatedFrom.getComponent());
            component.add(new JLabel(matcherEditorCreatedTo.getLabel()));
            component.add(matcherEditorCreatedTo.getComponent());
            component.add(new JLabel(matcherEditorTopics.getLabel()));
            component.add(matcherEditorTopics.getComponent());
            component.add(new JLabel(matcherEditorSearch.getLabel()));
            component.add(matcherEditorSearch.getComponent());
        }
        return component;
    }
    
    /** Gets the serializable value. */
    public Serializable getSerializable() {
        Vector<Serializable> serialized = new Vector<>(4);
        serialized.add(matcherEditorCreatedFrom.getSerializable());
        serialized.add(matcherEditorCreatedTo.getSerializable());
        serialized.add(matcherEditorTopics.getSerializable());
        serialized.add(matcherEditorSearch.getSerializable());
        return serialized;
    }
    
    public void setSerializable(Serializable serializable) {
        if (serializable instanceof Vector) {
            Vector<Serializable> v = (Vector<Serializable>)serializable;
            matcherEditorCreatedFrom.setSerializable(v.get(0));
            matcherEditorCreatedTo.setSerializable(v.get(1));
            matcherEditorTopics.setSerializable(v.get(2));
            matcherEditorSearch.setSerializable(v.get(3));
        }
    }

    public void reset() {
        matcherEditorCreatedFrom.reset();
        matcherEditorCreatedTo.reset();
        matcherEditorTopics.reset();
        matcherEditorSearch.reset();
    }

    private final MatcherEditorCreatedFrom matcherEditorCreatedFrom;
    private final MatcherEditorCreatedTo matcherEditorCreatedTo;
    private final MatcherEditorTopic matcherEditorTopics;
    private final MatcherEditorSearch matcherEditorSearch;
    private MatcherEditor<Future> matcherEditor;
    private JComponent component;
    
}
