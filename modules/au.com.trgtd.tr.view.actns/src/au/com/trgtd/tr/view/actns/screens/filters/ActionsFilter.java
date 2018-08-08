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
