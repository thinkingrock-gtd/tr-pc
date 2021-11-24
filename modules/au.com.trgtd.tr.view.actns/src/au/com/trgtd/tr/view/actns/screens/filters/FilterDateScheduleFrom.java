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
import tr.model.action.ActionStateScheduled;

/**
 * MatcherEditor the matches actions for a scheduled from date.
 *
 * @author Jeremy Moore
 */
public class FilterDateScheduleFrom extends FilterDate
        implements PropertyChangeListener {
    
    public static final byte INDEX = 20;
    
    private final static DateItem[] dateItems = new DateItem[] {
        DateItem.DATE_CHOOSER,
        DateItem.TODAY,
        DateItem.YESTERDAY,
        DateItem.BEG_NEXT_WEEK,
        DateItem.BEG_THIS_WEEK,
        DateItem.BEG_LAST_WEEK,
        DateItem.WEEKS_AGO_1,
        DateItem.WEEKS_AGO_2,
        DateItem.WEEKS_AGO_3,
        DateItem.WEEKS_AGO_4
    };
    
    /** Constructs a new instance. */
    public FilterDateScheduleFrom() {
        initialise();
    }
    
    protected void initialise() {
        combo = new FilterComboDate(dateItems, false);
        combo.addValueChangeListener(this);
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-schedule-from");
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
            if (!action.isStateScheduled()) {
                return false;
            }
            ActionStateScheduled state = (ActionStateScheduled)action.getState();
            Date date = state.getDate();
            if (date == null) {
                return ! excludeNulls;
            }
            return ! date.before(matchDate);
        }
    }
    
}

