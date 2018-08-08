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

package au.com.trgtd.tr.view.reference.filters;

import ca.odell.glazedlists.matchers.Matcher;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import org.openide.util.NbBundle;
import tr.model.information.Information;
import java.awt.event.FocusAdapter;
import java.awt.event.KeyAdapter;
import javax.swing.JTextField;

/**
 * MatcherEditor the matches information references for a search string.
 *
 * @author Jeremy Moore
 */
public class MatcherEditorSearchText extends MatcherEditorBase {
    
    private final JTextField searchText;
    
    /** Constructs a new instance. */
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
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    search();
                }
            }
        });
    }
    
    public Component getComponent() {
        return searchText;
    }
    
    public void search() {
        String string = searchText.getText();
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
        return (Serializable)searchText.getText();
    }
    
    public void setSerializable(Serializable serializable) {
        searchText.setText((String)serializable);
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
            return false;
        }
    }
    
    @Override
    public void reset() {
        searchText.setText("");
        search();
    }
   
}

