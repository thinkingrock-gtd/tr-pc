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
package au.com.trgtd.tr.sync.device.handshake;

import au.com.trgtd.tr.sync.device.socket.send.ISocketSender;
import au.com.trgtd.tr.sync.device.socket.send.SocketSenderException;

/**
 * Receiver for obtaining high level messages using a wrapped socket receiver.
 *
 * @author Jeremy Moore
 */
public class SyncHSSender {

    private final ISocketSender sender;

    /**
     * Construct a new instance.
     *
     * @param sender The wrapped socket sender.
     * @throws SocketSenderException if there is an exception thrown by the
     * socket sender.
     */
    public SyncHSSender(ISocketSender sender) throws SocketSenderException {
        if (sender == null) {
            throw new SocketSenderException("Socket sender is null.");
        }
        this.sender = sender;
    }

    public void send(ISyncMsg msg) throws SocketSenderException {
        sender.send(msg.toSendString());
    }

}
