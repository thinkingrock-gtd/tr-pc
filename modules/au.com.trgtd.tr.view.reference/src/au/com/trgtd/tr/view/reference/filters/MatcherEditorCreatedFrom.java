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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Date;
import org.openide.util.NbBundle;
import tr.model.information.Information;
import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.view.filters.FilterComboDate;

/**
 * MatcherEditor the matches information references for a created from date.
 *
 * @author Jeremy Moore
 */
public class MatcherEditorCreatedFrom extends MatcherEditorBase
        implements PropertyChangeListener {
    
    private final static DateItem[] dateItems = new DateItem[] {
        DateItem.DATE_CHOOSER,
        DateItem.TODAY,
        DateItem.YESTERDAY,
        DateItem.WEEKS_AGO_1,
        DateItem.WEEKS_AGO_2,
        DateItem.WEEKS_AGO_3,
        DateItem.WEEKS_AGO_4
    };
    
    private final FilterComboDate dateCombo;
    
    /** Constructs a new instance. */
    public MatcherEditorCreatedFrom() {
        dateCombo = new FilterComboDate(dateItems, false);
        dateCombo.addValueChangeListener(this);
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-created-from");
    }
    
    public Component getComponent() {
        return dateCombo;
    }
    
    public Serializable getSerializable() {
        return (DateItem)dateCombo.getSelectedItem();
    }
    
    public void setSerializable(Serializable serializable) {
        if (serializable instanceof DateItem) {
            dateCombo.setSelectedItem((DateItem)serializable);
        } else {
            dateCombo.setSelectedItem(null);
        }
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        DateItem item = (DateItem)dateCombo.getSelectedItem();
        if (item == null) {
            fireMatchAll();
        } else {
            fireChanged(new FromDateMatcher(dateCombo.getDate(item)));
        }
    }
    
    @Override
    public void reset() {
        dateCombo.setSelectedItem(null);
        dateCombo.fireValueChange();
    }

    private static class FromDateMatcher implements Matcher<Information> {
        
        private final Date date;
        
        public FromDateMatcher() {
            this.date = null;
        }
        
        public FromDateMatcher(Date date) {
            this.date = au.com.trgtd.tr.util.DateUtils.getStart(date);
        }
        
        public boolean matches(Information info) {
            return date == null || !info.getCreated().before(date);
        }
    }
    
}

