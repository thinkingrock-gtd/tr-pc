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

import tr.model.action.ActionState.Type;

/**
 * Inactive action state.
 *
 * @author Jeremy Moore
 */
public class ActionStateInactive extends ActionState {
    
    /** Constructs a new instance. */
    public ActionStateInactive() {
        super();
    }
    
    /**
     * Makes an exact copy of itself.
     * @return The copy.
     */
    @Override
    public ActionState copy() {
        ActionStateInactive copy = new ActionStateInactive();
        copy.created = this.created;
        return copy;
    }
    
    @Override
    public final ActionState.Type getType() {
        return Type.INACTIVE;
    }
    
}
