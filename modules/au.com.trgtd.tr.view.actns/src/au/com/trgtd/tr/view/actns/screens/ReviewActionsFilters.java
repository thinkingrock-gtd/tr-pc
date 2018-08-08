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
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */

package au.com.trgtd.tr.view.actns.screens;

import au.com.trgtd.tr.view.ViewUtils;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.Utilities;
import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.filters.FilterCombo;

/**
 * Filters for actions.
 *
 * @author Jeremy Moore
 */
public class ReviewActionsFilters {
    
    private static final int LABEL_HEIGHT = 12;
    
    private final ActionsScreen screen;
    private MatcherEditor matcherEditor;
    private JPanel component;
    
    /** Constructs a new instance. */
    public ReviewActionsFilters(ActionsScreen screen) {
        this.screen = screen;
    }
    
    public MatcherEditor getMatcherEditor() {
        if (matcherEditor == null) {
            BasicEventList<MatcherEditor> list = new BasicEventList<MatcherEditor>();
            for (MatcherEditor m : screen.getFilters()) {
                list.add(m);
            }
//            for (ActionsFilter filter : screen.getFilters()) {
//                if (filter.isUsed()) {
//                    list.add(filter);
//                }
//            }
            matcherEditor = new CompositeMatcherEditor(list);
        }
        return matcherEditor;
    }
    
    
    public int getComponentWidth() {
        return FilterCombo.WIDTH;
    }
    
    public int getComponentHeight() {        
        int margin = Utilities.isMac() ? 2 : 6;                
        return FilterCombo.HEIGHT + LABEL_HEIGHT + margin;
    }
    
    public JPanel getPanel() {
        if (component == null) {
            component = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
            component.setBackground(ViewUtils.COLOR_PANEL_BG);
            for (ActionsFilter m : screen.getFilters()) {
                if (m.isUsed() && m.isShown()) {
                    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
                    panel.setBackground(ViewUtils.COLOR_PANEL_BG);
                    Dimension d = new Dimension(getComponentWidth(), getComponentHeight());
                    panel.setPreferredSize(d);
                    panel.setMinimumSize(d);
                    panel.setMaximumSize(d);
                    panel.add(new JLabel("   " + m.getLabel()));
                    panel.add(m.getFilterCombo().getJComboBox());
                    component.add(panel);
                }
            }
        }
        return component;
    }
    
    public JPanel getPanel(boolean rebuild) {
        if (rebuild) {
            component = null;
        }
        return getPanel();
    }
    
}
