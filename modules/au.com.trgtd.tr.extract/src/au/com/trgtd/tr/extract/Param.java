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
