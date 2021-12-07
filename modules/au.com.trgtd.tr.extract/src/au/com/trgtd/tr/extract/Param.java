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
package au.com.trgtd.tr.extract;

import java.util.List;
import au.com.trgtd.tr.swing.date.combo.DateItem;

/**
 * Base class for user selection parameters.
 *
 * @author Jeremy Moore
 */
public class Param {
    
    /**
     * Parameter type enumeration.
     */
    public static enum Type { COMBOBOX, CHECKBOX, DATESCOMBO }
    
    /** The parameter type. */
    public final Type type;

    /** The parameter id. */
    public final String id;
    
    /** The parameter display label. */
    public final String display;
        
    /* The default option value or the actual value if there are no items. */
    protected String value;
    
    /** Item class. */
    public final static class Item {
        /** Display label. */
        public final String display;
        /** Value. */
        public final String value;
        /** Constructs a new instance. */
        public Item(String display, String value) {
            this.display = display;
            this.value = value;
        }
        public Item(String display, long value) {
            this.display = display;
            this.value = "" + value;
        }
        @Override
        public String toString() {
            return display;
        }
    }
    
    /* The items for the combo box. */
    protected List<Item> items;

    /* The date items for the date combo box. */
    protected List<DateItem> dateItems;

    /** 
     * Constructs a new instance with just an ID and value. 
     * @param id The parameter ID.
     * @param value The parameter value.
     */    
    public Param(String id, String value) {
        this.id = id;
        this.value = value;
        this.type = null;
        this.display = null;
    }
    
    /** 
     * Constructs a new instance. 
     * @param type The parameter type.
     * @param id The parameter ID.
     * @param display The parameter display label text.
     */
    public Param(Type type, String id, String display) {
        this.type = type;
        this.id = id;
        this.display = display;
    }
    
    /** Gets the current value. */
    public String getValue() {
        return value;
    }

    /** Sets the default value. */
    public void setValue(String value) {
        this.value = value;
    }
    
    /** Gets the selection items. */
    public List<Item> getItems() {
        return items;
    }

    /** Sets the selection items. */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /** Gets the selection date items. */
    public List<DateItem> getDateItems() {
        return dateItems;
    }

    /** Sets the selection date items. */
    public void setDateItems(List<DateItem> dateItems) {
        this.dateItems = dateItems;
    }
}
