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

import au.com.trgtd.tr.view.filters.FilterCombo;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import tr.model.action.Action;

/**
 * Abstract base class for matcher editors.
 *
 * @author Jeremy Moore
 */
public abstract class ActionsFilter extends AbstractMatcherEditor<Action> {
    
//  private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    protected boolean used;
    protected boolean shown;    
    protected boolean excludeNulls;
    
    /** Default constructor. */
    public ActionsFilter() {}
    
    /** Initialize for construction. */
    protected abstract void initialise();
    
    /** Gets the ID. */
    public abstract byte getIndex();
    
    /** Gets the label. */
    public abstract String getLabel();
    
    /** Gets the combo box. */
    public abstract FilterCombo getFilterCombo();
    
    
    /** Determines whether the filter has the ability to exclude null values. */
    protected abstract boolean canExcludeNulls();
    
    /** Determines whether null values are to be excluded. */
    public final boolean isExcludeNulls() {
        return excludeNulls;
    }
    
    /** Sets the exclude nulls value. */
    public final void setExcludeNulls(boolean exclude) {
        this.excludeNulls = exclude;
    }
    
    /** Gets the selected values as (serializable) strings. */
    public abstract String[] getSerialValues();
    
    /** Sets the selected values from (serializable) strings. */
    public abstract void setSerialValues(String[] values);
    
    public final boolean isShown() {
        return shown;
    }
    
    public final void setShown(boolean shown) {
        if (this.shown != shown) {
            this.shown = shown;
        }
    }
    
    public final boolean isUsed() {
        return used;
    }
    
    public void setUsed(boolean used) {
        if (this.used != used) {
            this.used = used;
            if (!used) {
                setShown(false);
                if (getFilterCombo() != null) {
                    getFilterCombo().setSelectedItem(null);
                }
            }
        }
    }
    
    /** Get field display name. */
    @Override
    public String toString() {
        return getLabel();
    };
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ActionsFilter)) {
            return false;
        }
        ActionsFilter other = (ActionsFilter)object;
        if (this.getIndex() != other.getIndex()) return false;
        if (this.shown != other.shown) return false;
        if (this.used != other.used) return false;
        return true;
    }
    
//    public void persist(PersistenceOutputStream out) throws Exception {
//        out.writeByte(VERSION);
//        out.writeBoolean(used);
//        out.writeBoolean(shown);
//    }
//
//    public void restore(PersistenceInputStream in) throws Exception {
//        byte version = in.readByte();
//        if (version != VERSION) {
//            throw new Exception("Unknown persistance version for " + getClass());
//        }
//        used = in.readBoolean();
//        shown = in.readBoolean();
//    }
    
}
