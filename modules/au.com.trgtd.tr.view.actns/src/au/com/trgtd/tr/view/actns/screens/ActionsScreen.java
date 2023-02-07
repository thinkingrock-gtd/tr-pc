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

import java.util.Vector;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import tr.model.util.Manager;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.util.Observer;
import au.com.trgtd.tr.util.Utils;
import au.com.trgtd.tr.view.actns.screens.columns.ActionsColumn;
import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.actns.screens.filters.FilterFactory;
 
/**
 * Actions screen definition.
 *
 * @author Jeremy Moore
 */
public class ActionsScreen extends ObservableImpl implements Observable, Observer {
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
//    private static final byte VERSION = 1;
//
    private String key;             // bundle key or display name
    private byte[] columnOrder;
    private byte[] sortColumns;
    private byte[] sortStatus;
    private boolean showFilters;
    private Manager<ActionsColumn> columns;
    private Vector<ActionsFilter> filters;
    
    /**
     * Constructs a new instance.
     * @param key The property file key or display name.
     */
    private ActionsScreen(String key) {
        this.key = key;
    }
    
    /**
     * Creates and returns an instance.
     * @param key The name or properties key.
     */
    public static ActionsScreen create(String key) {
        ActionsScreen screen = new ActionsScreen(key);
        for (int i = 0; i < ActionsColumn.COLUMN_INDICES.length; i++) {
            screen.getColumns().add(ActionsColumn.createColumn(ActionsColumn.COLUMN_INDICES[i]));
        }
        for (int j = 0; j < FilterFactory.INDICES.length; j++) {
            byte index = FilterFactory.INDICES[j];
            screen.getFilters().add(FilterFactory.instance.createFilter(index));
        }
        return screen;
    }
    
    /**
     * Gets the key.
     * @return the key.
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Sets the property file key or display name.
     * @param key The new key.
     */
    public void setKey(String key) {
        key = (key == null) ? "" : key;
        if (!Utils.equal(this.key, key)) {
            this.key = key;
            notifyObservers(this);
        }
    }
    
    /**
     * Gets the display name.
     * @return The value for the key in the properties file if one exists,
     * otherwise the key value.
     */
    @Override
    public String toString() {
        try {
            return NbBundle.getMessage(getClass(), key);
        } catch (Exception ex) {
            return key;
        }
    }
    
    /**
     * Gets the columns.
     * @return the columns.
     */
    public Manager<ActionsColumn> getColumns() {
        if (columns == null) {
            columns = new Manager<>();
            columns.addObserver(this);
        }
        return columns;
    }
    
    /**
     * Sets the columns.
     * @param columns
     */
    public void setColumns(Manager<ActionsColumn> columns) {
        if (this.columns != null) {
            this.columns.removeObserver(this);
        }
        
        if (columns == null) {
            this.columns = new Manager<>();
        } else {
            this.columns = columns;
        }
        
        this.columns.addObserver(this);
    }
    
    /**
     * Gets the column order.
     * @return the column order.
     */
    public byte[] getColumnOrder() {
        if (columnOrder == null) {
            columnOrder = new byte[] {};
        }
        return columnOrder;
    }
    
    /**
     * Sets the column order.
     * @param order The column order.
     */
    public void setColumnOrder(byte[] order) {
        this.columnOrder = order;
        notifyObservers(this);
    }
    
    /**
     * Gets the sort columns.
     * @return the sort columns.
     */
    public byte[] getSortColumns() {
        if (sortColumns == null) {
            sortColumns = new byte[] {};
        }
        return sortColumns;
    }
    
    /**
     * Sets the sort columns.
     * @param columns The sort columns.
     */
    public void setSortColumns(byte[] columns) {
        this.sortColumns = columns;
        notifyObservers(this);
    }
    
    /**
     * Gets the sort status.
     * @return the sort status.
     */
    public byte[] getSortStates() {
        if (sortStatus == null) {
            sortStatus = new byte[] {};
        }
        return sortStatus;
    }
    
    /**
     * Sets the sort status.
     * @param status The sort status.
     */
    public void setSortStatus(byte[] status) {
        this.sortStatus = status;
        notifyObservers(this);
    }
    
    /**
     * Gets the filters.
     * @return the filters.
     */
    public Vector<ActionsFilter> getFilters() {
        if (filters == null) {
            filters = new Vector<>();
        }
        return filters;
    }
    
    /**
     * Sets the filters.
     * @param filters the filters.
     */
    public void setFilters(Vector<ActionsFilter> filters) {
        this.filters = filters;
    }
    
    /**
     * Resets observation of values.
     */
    @Override
    public void resetObservers() {
        if (columns != null) {
            columns.resetObservers();
            columns.addObserver(this);
        }
    }
    
    /**
     * Passes on notification of changes in contained values.
     * @param object
     */
    public void update(Observable observable, Object object) {
        notifyObservers(observable, object);
    }
    
    public boolean isShowFilters() {
        return showFilters;
    }
    
    public void setShowFilters(boolean showFilters) {
        this.showFilters = showFilters;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ActionsScreen)) {
            return false;
        }        
        ActionsScreen other = (ActionsScreen)object;
        if (!key.equals(other.key)) {
            return false;
        }
        if (!Utils.equal(columnOrder, other.columnOrder)) {
            return false;
        }
        if (!Utils.equal(sortColumns, other.sortColumns)) {
            return false;
        }
        if (!Utils.equal(sortStatus, other.sortStatus)) {
            return false;
        }
        if (showFilters != other.showFilters) {
            return false;
        }
        Vector<ActionsColumn> thisColumns = columns.list();
        Vector<ActionsColumn> thatColumns = other.columns.list();
        if (thisColumns.size() != thatColumns.size()) {
            return false;
        }
        for (int i = 0; i < thisColumns.size(); i++) {
            if (!thisColumns.get(i).equals(thatColumns.get(i))) {
                return false;
            }
        }
        if (filters.size() != other.filters.size()) {
            return false;
        }
        for (int i = 0; i < filters.size(); i++) {
            if (!filters.get(i).equals(other.filters.get(i))) {
                return false;
            }
        }
        return true;
    }
    
}
