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
package au.com.trgtd.tr.sync.device.v100.message.process;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.send.ISender;

/**
 * Message handler abstract base class.
 *
 * @author Jeremy Moore
 */
abstract class Handler <RecvMsg> {

//  protected final ISocketSender sender;
    protected final ISender sender;
    protected final DataMgr data;

    /**
     * Construct the handler.
     * @param sender The message sender.
     */
//  Handler(ISocketSender sender, DataMgr data) {
    Handler(ISender sender, DataMgr data) {
        this.sender = sender;
        this.data = data;
    }

    /**
     * Handle a received message.
     * @param recvMsg The received message.
     */
    abstract void handle(RecvMsg recvMsg) throws Exception;
    
}
