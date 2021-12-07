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
package au.com.trgtd.tr.sync.device.v100.message.receive;

import au.com.trgtd.tr.sync.device.exception.ReceiveException;
import au.com.trgtd.tr.sync.device.socket.receive.ISocketReceiver;
import au.com.trgtd.tr.sync.device.socket.receive.SocketReceiverException;
import au.com.trgtd.tr.sync.device.v100.message.parse.Parser;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsg.Type;

/**
 * Receiver for obtaining high level messages using a wrapped socket receiver.
 *
 * @author Jeremy Moore
 */
public class Receiver implements IReceiver
{    
    private final ISocketReceiver receiver;
    private final Parser parser;

    /**
     * Construct high level message receiver.
     * @param receiver The wrapped basic receiver.
     * @throws ReceiverException if there is an exception thrown by the basic
     * receiver.
     * @throws MsgException if there is an exception parsing the low level
     * message string.
     */
    public Receiver(ISocketReceiver receiver) throws SocketReceiverException
    {
        if (receiver == null) {
            throw new SocketReceiverException("Receiver is null.");
        }
        this.receiver = receiver;
        this.parser = new Parser();
    }

    @Override
    public RecvMsg recv() throws Exception
    {
        return parser.parse(receiver.recv());
    }

    @Override
    public RecvMsg recv(Type code) throws Exception
    {
        RecvMsg msg = recv();
        if (code == msg.type) {
            return msg;
        }
        
        throw new ReceiveException("Unexpected message: " + msg.type + " Expected " + code);
    }


}
