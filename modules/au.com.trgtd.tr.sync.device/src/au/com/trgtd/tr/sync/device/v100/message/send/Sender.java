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
package au.com.trgtd.tr.sync.device.v100.message.send;

import au.com.trgtd.tr.sync.device.socket.send.ISocketSender;
import au.com.trgtd.tr.sync.device.socket.send.SocketSenderException;

/**
 * Sender of  high level messages using a wrapped socket sender.
 *
 * @author Jeremy Moore
 */
public class Sender implements ISender
{    
    private final ISocketSender socketSender;

    /**
     * Construct instance.
     * @param socketSender The socket sender.
     * @throws Exception
     */
    public Sender(ISocketSender socketSender) throws Exception
    {
        if (socketSender == null) {
            throw new SocketSenderException("Socket sender is null.");
        }
        this.socketSender = socketSender;
    }

    @Override
    public void send(SendMsg msg) throws Exception
    {
        socketSender.send(msg.toSendString());
    }

}
