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

package tr.model.action;

import java.util.Calendar;
import java.util.Date;

import au.com.trgtd.tr.util.ObservableImpl;

/**
 * Action state abstract base class.
 *
 * @author Jeremy Moore
 */
public abstract class ActionState extends ObservableImpl {
    
    protected Date created;
    
    public enum Type { DOASAP, INACTIVE, DELEGATED, SCHEDULED };
    
    /**
     * Constructs a new instance.
     */
    public ActionState() {
        created = new Date();
    }
    
    /**
     * Makes an exact copy of itself.
     * @return The copy.
     */
    public abstract ActionState copy();
    
    /**
     * Gets the creation date.
     * @return The creation date.
     */
    public Date getCreated() {
        return created;
    }
    
    public abstract Type getType();
    
    /**
     * Overrides equals to compare with another object for equality.
     * @param object The object to compare with.
     * @return true if the object is an ActionState instance and its creation
     * date equals this state's creation date.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        return created.equals(((ActionState)object).created);
    }

    @Override
    public int hashCode() {
        return created == null ? 0 : created.hashCode();
    }
    
}
