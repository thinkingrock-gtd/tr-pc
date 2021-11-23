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
import au.com.trgtd.tr.swing.date.combo.DateType;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.filters.FilterCombo;
import au.com.trgtd.tr.view.filters.FilterComboDate;
import java.util.logging.Logger;

/**
 * Abstract base class for date matcher editors.
 *
 * @author Jeremy Moore
 */
public abstract class FilterDate extends ActionsFilter {

    private static final Logger LOG = Logger.getLogger("tr.view.actions");
//    private static final byte VERSION = 1;
    protected FilterComboDate combo;

//    /** @since 2.0.1 */
//    protected boolean excludeNulls;
    /** Constructs a new instance. */
    public FilterDate() {
        super();
    }

//    public final boolean excludeNulls() {
//        return excludeNulls;
//    }
//    
    protected boolean canExcludeNulls() {
        return true;
    }

//    public final void setExcludeNulls(boolean b) {
//        this.excludeNulls = b;
//    }
    /** Gets the component. */
    public final FilterCombo getFilterCombo() {
        return combo;
    }

    public String[] getSerialValues() {
//        if (combo == null) return null;
//
//        String selected = "";
//
//        DateItem item = (DateItem)combo.getSelectedItem();
//        if (item != null && item.type != DateType.NONE) {
//            selected = String.valueOf(item.value);
//        }
//
//        return new String[] { selected, Boolean.toString(excludeNulls) }
        if (combo == null) {
            return null;
        }
        DateItem item = (DateItem)combo.getSelectedItem();
        if (item == null) {
            String dateTypeID = String.valueOf(DateType.NONE.id);
            String dateValue = "";
            return new String[]{dateValue, Boolean.toString(excludeNulls), dateTypeID};
        } else {
            String dateTypeID = String.valueOf(item.type.id);
            String dateValue = String.valueOf(item.value);
            return new String[]{dateValue, Boolean.toString(excludeNulls), dateTypeID};
        }
    }

    public void setSerialValues(String[] values) {
        if (combo == null) {
            return;
        }

        combo.stopChangeEvents();

        if (values == null || values.length < 1) {            
            combo.setSelectedItem(DateItem.NONE);
        } else if (values.length == 3) {
            // new format
            try {
                long dateValue = Long.parseLong(values[0]);
                excludeNulls = Boolean.parseBoolean(values[1]);
                int dateTypeID = Integer.parseInt(values[2]);
                DateType dateType = DateType.getDateType(dateTypeID);
                combo.setSelectedItem(combo.getDateItem(dateType, dateValue));
            } catch (Exception ex) {
                combo.setSelectedItem(DateItem.NONE);
            }
        } else {
            // old format
            try {
                long value = Long.parseLong(values[0]);
                if (value < 1000) { // assume days
                    combo.setSelectedItem(combo.getDateItem(DateType.DAYS, value));
                } else { // assume a date value in milliseconds since ...
                    combo.setSelectedItem(combo.getDateItem(DateType.MS, value));
                }
            } catch (Exception ex) {
                combo.setSelectedItem(DateItem.NONE);
            }
            if (values.length > 1) {
                try {
                    excludeNulls = Boolean.parseBoolean(values[1]);
                } catch (Exception ex) {
                }
            }
        }

        combo.startChangeEvents();
    }

    public boolean equals(Object object) {
        if (!(object instanceof FilterDate)) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        DateItem thisDateItem = (DateItem) combo.getSelectedItem();
        DateItem thatDateItem = (DateItem) ((FilterDate) object).combo.getSelectedItem();
        return Utils.equal(thisDateItem, thatDateItem);
    }
    
//    public void persist(PersistenceOutputStream out) throws Exception {
//        super.persist(out);
//        out.writeByte(VERSION);
//        out.writeDateItem(combo == null ? null : (DateItem)combo.getSelectedItem());
//        out.writeBoolean(excludeNulls);
//    }
//
//    public void restore(PersistenceInputStream in) throws Exception {
//        super.restore(in);
//        byte version = in.readByte();
//        if (version != VERSION) {
//            throw new Exception("Unknown persistance version for " + getClass().getName());
//        }
//        getFilterCombo().setSelectedItem((DateItem)in.readDateItem());
//        excludeNulls = in.readBoolean();
//    }
}
