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
package au.com.trgtd.tr.view.someday.filters;

import ca.odell.glazedlists.matchers.Matcher;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Date;
import org.openide.util.NbBundle;
import tr.model.future.Future;
import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.view.filters.FilterComboDate;

/**
 * MatcherEditor the matches created from date.
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
        return (Serializable) dateCombo.getSelectedItem();
    }
    
    public void setSerializable(Serializable serializable) {
        if (serializable instanceof DateItem dateItem) {
            dateCombo.setSelectedItem(dateItem);
        } else {
            dateCombo.setSelectedItem(null);
        }
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        DateItem item = (DateItem)dateCombo.getSelectedItem();
        if (item == null) {
            fireMatchAll();
        } else {
            fireChanged(new FromDateMatcher(FilterComboDate.getDate(item)));
        }
    }

    @Override
    public void reset() {
        dateCombo.setSelectedItem(null);
        dateCombo.fireValueChange();
    }
    
    private static class FromDateMatcher implements Matcher<Future> {
        
        private final Date date;
        
        public FromDateMatcher() {
            this.date = null;
        }
        
        public FromDateMatcher(Date date) {
            this.date = au.com.trgtd.tr.util.DateUtils.getStart(date);
        }
        
        public boolean matches(Future future) {
            return date == null || !future.getCreated().before(date);
        }
    }
    
}

