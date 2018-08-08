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

package au.com.trgtd.tr.view.actns.screens.filters;

import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.view.filters.FilterComboDate;
import ca.odell.glazedlists.matchers.Matcher;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import org.openide.util.NbBundle;
import tr.model.action.Action;

/**
 * MatcherEditor the matches actions for a created from date.
 *
 * @author Jeremy Moore
 */
public class FilterDateCreatedFrom extends FilterDate
        implements PropertyChangeListener {
    
    public static final byte INDEX = 9;
    
    private final static DateItem[] dateItems = new DateItem[] {
        DateItem.DATE_CHOOSER,
        DateItem.TODAY,
        DateItem.YESTERDAY,
        DateItem.BEG_THIS_WEEK,
        DateItem.BEG_LAST_WEEK,
        DateItem.WEEKS_AGO_1,
        DateItem.WEEKS_AGO_2,
        DateItem.WEEKS_AGO_3,
        DateItem.WEEKS_AGO_4
    };
    
    /** Constructs a new instance. */
    public FilterDateCreatedFrom() {
        initialise();
    }
    
    protected void initialise() {
        combo = new FilterComboDate(dateItems, false);
        combo.addValueChangeListener(this);
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-created-from");
    }
    
    public byte getIndex() {
        return INDEX;
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        DateItem item = (DateItem)combo.getSelectedItem();
        if (item == null) {
            fireMatchAll();
        } else {
            fireChanged(new FromDateMatcher(combo.getDate(item), excludeNulls));
        }
    }
    
    private static class FromDateMatcher implements Matcher<Action> {
        
        private final Date matchDate;
        private final boolean excludeNulls;
        
        public FromDateMatcher(Date date, boolean excludeNulls) {
            this.matchDate = DateUtils.getStart(date);
            this.excludeNulls = excludeNulls;
        }
        
        public boolean matches(Action action) {
            if (matchDate == null) {
                return true;
            }
            Date date = action.getCreated();
            if (date == null) {
                return ! excludeNulls;
            }
            return ! date.before(matchDate);
        }
    }
    
}

