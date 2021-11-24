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

/**
 * MatcherEditor the matches actions for a due to date.
 *
 * @author Jeremy Moore
 */
public class FilterDateDueTo extends FilterDate
        implements PropertyChangeListener {
    
    public static final byte INDEX = 17;
    
    private final static DateItem[] dateItems = new DateItem[] {
        DateItem.DATE_CHOOSER,
        DateItem.YESTERDAY,
        DateItem.TODAY,
        DateItem.TOMORROW,
        DateItem.END_LAST_WEEK,
        DateItem.END_THIS_WEEK,
        DateItem.END_NEXT_WEEK,
        DateItem.WEEKS_1,
        DateItem.WEEKS_2,
        DateItem.WEEKS_3,
        DateItem.WEEKS_4
    };
    
    /** Constructs a new instance. */
    public FilterDateDueTo() {
        initialise();
    }
    
    protected void initialise() {
        combo = new FilterComboDate(dateItems, false);
        combo.addValueChangeListener(this);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        DateItem item = (DateItem)combo.getSelectedItem();
        if (item == null) {
            fireMatchAll();
        } else {
            fireChanged(new ToDateMatcher(combo.getDate(item), excludeNulls));
        }
    }
    
    public String getLabel() {
        return NbBundle.getMessage(getClass(), "filter-due-to");
    }
    
    public byte getIndex() {
        return INDEX;
    }
    
    private static class ToDateMatcher implements Matcher<Action> {
        
        private final Date matchDate;
        private final boolean excludeNulls;
        
        public ToDateMatcher(Date date, boolean excludeNulls) {
            this.matchDate = DateUtils.getEnd(date);
            this.excludeNulls = excludeNulls;
        }
        
        public boolean matches(Action action) {
            if (matchDate == null) {
                return true;
            }
            Date date = action.getDueDate();
            if (date == null) {
                return ! excludeNulls;
            }
            return ! date.after(matchDate);
        }
    }
    
}

