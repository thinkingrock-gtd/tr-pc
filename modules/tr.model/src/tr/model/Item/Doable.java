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

package tr.model.Item;

/**
 * Interface for classes of objects that can be done.
 *
 * @author Jeremy Moore
 */
public interface Doable {
    
    /**
     * Determine whether the item is done.
     * @return true iff the item is done.
     */
    public boolean isDone();

    /**
     * Determines whether setting the done state is permissable.
     * @param b The done state to consider.
     */
    public boolean canSetDone(boolean b);
    
    /**
     * Sets the done state.
     * @param b The new done state.
     */
    public void setDone(boolean b);
    
}
