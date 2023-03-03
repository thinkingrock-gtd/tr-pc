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
package au.com.trgtd.tr.view.actns.screens;

import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.filters.FilterCombo;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.CompositeMatcherEditor;
import ca.odell.glazedlists.matchers.MatcherEditor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.Utilities;
import tr.model.action.Action;

/**
 * Filters for actions.
 *
 * @author Jeremy Moore
 */
public class ReviewActionsFilters {
    
    private static final int LABEL_HEIGHT = 12;
    
    private final ActionsScreen screen;
    private MatcherEditor<Action> matcherEditor;
    private JPanel component;
    
    /** Constructs a new instance. */
    public ReviewActionsFilters(ActionsScreen screen) {
        this.screen = screen;
    }
    
    public MatcherEditor<Action> getMatcherEditor() {
        if (matcherEditor == null) {
            EventList<MatcherEditor<Action>> list = new BasicEventList<>();
            for (MatcherEditor<Action> m : screen.getFilters()) {
                list.add(m);
            }
//            for (ActionsFilter filter : screen.getFilters()) {
//                if (filter.isUsed()) {
//                    list.add(filter);
//                }
//            }
            matcherEditor = new CompositeMatcherEditor<>(list);
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
