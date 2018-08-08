/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.view.reference.linker;

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
import au.com.trgtd.tr.view.reference.filters.MatcherEditorSearchText;
import au.com.trgtd.tr.view.reference.filters.MatcherEditorTopic;

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
    
    public MatcherEditor getMatcherEditor() {
        if (matcherEditor == null) {
            BasicEventList<MatcherEditor> list = new BasicEventList<MatcherEditor>();
            list.add(matcherEditorTopics);
            list.add(matcherEditorSearch);
            matcherEditor = new CompositeMatcherEditor(list);
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
        Vector<Serializable> serialized = new Vector<Serializable>(4);
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
    private MatcherEditor matcherEditor;
    private JComponent component;
    
}
