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
package au.com.trgtd.tr.sync.device.socket.receive;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Message receiver that wraps another receiver and logs messages.
 *
 * @author Jeremy Moore
 */
public class SocketReceiverLogger implements ISocketReceiver
{
    private static final Logger LOG = Logger.getLogger("tr.sync.receiver");
    private final ISocketReceiver receiver;

    /**
     * Constructs a receiver that wraps another receiver and logs messages.
     * @param receiver The wrapped receiver.
     */
    public SocketReceiverLogger(ISocketReceiver receiver)
    {
        this.receiver = receiver;
    }

    @Override
    public String recv() throws SocketReceiverException
    {
        String msg = receiver.recv();

        LOG.log(Level.INFO, "Recv: {0}", msg);

        return msg;
    }

}
