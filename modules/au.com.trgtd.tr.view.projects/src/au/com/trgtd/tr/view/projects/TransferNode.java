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

package au.com.trgtd.tr.view.projects;

/**
 * Interface for holding the node transfer type.
 *
 * @author Jeremy Moore
 */
public interface TransferNode {
    
    /** 
     * Sets the transfer type.
     * @param type The transfer type which should be DndConstants.ACTION_COPY or 
     * DnDConstants.ACTION_MOVE.
     */
    public void setTransferType(int type);

    /** 
     * Determines whether a move transfer type has been set.
     * @return true iff the transfer type of the latest setTransferAction() was 
     * DnDConstants.ACTION_MOVE.
     */
    public boolean isMoveTransfer();

    /** 
     * Determines whether a copy transfer type has been set.
     * @return true iff the transfer type of the latest setTransferAction() was 
     * DnDConstants.ACTION_COPY.
     */
    public boolean isCopyTransfer();

}
