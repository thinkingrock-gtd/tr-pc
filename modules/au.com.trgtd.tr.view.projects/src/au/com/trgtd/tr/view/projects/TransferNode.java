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
