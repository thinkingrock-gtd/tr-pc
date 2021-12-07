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

import au.com.trgtd.tr.sync.device.exception.ParserException;
import au.com.trgtd.tr.sync.device.socket.receive.ISocketReceiver;
import au.com.trgtd.tr.sync.device.socket.receive.SocketReceiverException;

/**
 * Receiver for handshake message using a wrapped socket receiver.
 *
 * @author Jeremy Moore
 */
public class SyncHSReceiver
{    
    private final ISocketReceiver receiver;
    private final SyncHSParser parser;

    /**
     * Construct a new instance.
     * @param receiver The wrapped socket receiver.
     * @throws ReceiverException if there is an exception thrown by the socket
     * receiver.
     * @throws MsgException if there is an exception parsing the low level
     * message string.
     */
    public SyncHSReceiver(ISocketReceiver receiver) throws SocketReceiverException
    {
        if (receiver == null) {
            throw new SocketReceiverException("Socket receiver is null.");
        }
        this.receiver = receiver;
        this.parser = new SyncHSParser();
    }

    public SyncHSMsg receive() throws ParserException, SocketReceiverException
    {
        return parser.parse(receiver.recv());
    }

}
