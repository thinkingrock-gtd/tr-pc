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
package au.com.trgtd.tr.sync.device.socket.send;

import static au.com.trgtd.tr.sync.device.Constants.ENCODING;
import static au.com.trgtd.tr.sync.device.Constants.ETX;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Basic sender.
 *
 * @author Jeremy Moore
 */
public class SocketSender implements ISocketSender
{
    private static final boolean AUTOFLUSH = true;

    // Use PrintWriter for auto-flush on println and not on newlines in msg.
    private final PrintWriter writer;

    /**
     * Construct basic sender.
     * @param socket The connection socket.
     * @throws SenderException if socket is null or not connected or there is
     * an exception creating the output writer.
     */
    public SocketSender(Socket socket) throws SocketSenderException
    {
        if (socket == null) {
            throw new SocketSenderException("Can not construct sender. Socket is null.");
        }
        if (!socket.isConnected()) {
            throw new SocketSenderException("Can not construct sender. Socket is not connected.");
        }
        try {
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), ENCODING), AUTOFLUSH);
        }
        catch (UnsupportedEncodingException ex) {
            throw new SocketSenderException("Can not create writer. Unsupported encoding: " + ENCODING);
        }
        catch (IOException ex) {
            throw new SocketSenderException("Can not create writer.", ex);
        }
    }

    @Override
    public void send(String msg) throws SocketSenderException
    {
        writer.println(msg + ETX);

        if (writer.checkError()) {
            throw new SocketSenderException("Send message failed.");
        }
    }

}
