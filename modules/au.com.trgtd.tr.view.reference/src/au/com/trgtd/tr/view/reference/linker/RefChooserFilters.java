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
package au.com.trgtd.tr.view.reference.linker;

import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.reference.filters.MatcherEditorSearchText;
import au.com.trgtd.tr.view.reference.filters.MatcherEditorTopic;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tr.model.information.Information;

/**
 * Filters for information references.
 *
 * @author Jeremy Moore
 */
class RefChooserFilters {
    
    /** Constructs a new instance. */
    public RefChooserFilters() {
        matcherEditorTopics = new MatcherEditorTopic();
        matcherEditorSearch = new MatcherEditorSearchText();
    }
    
    public MatcherEditor<Information> getMatcherEditor() {
        if (matcherEditor == null) {
            EventList<MatcherEditor<Information>> list = new BasicEventList<>();
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
        serialized.add(matcherEditorTopics.getSerializable());
        serialized.add(matcherEditorSearch.getSerializable());
        return serialized;
    }
    
    public void setSerializable(Serializable serializable) {
        if (serializable instanceof Vector) {
            Vector<Serializable> v = (Vector<Serializable>)serializable;
            matcherEditorTopics.setSerializable(v.get(2));
            matcherEditorSearch.setSerializable(v.get(3));
        }
    }

    public void reset() {
        matcherEditorTopics.reset();
        matcherEditorSearch.reset();
    }

    public final MatcherEditorTopic matcherEditorTopics;
    public final MatcherEditorSearchText matcherEditorSearch;
    private MatcherEditor<Information> matcherEditor;
    private JComponent component;
    
}
