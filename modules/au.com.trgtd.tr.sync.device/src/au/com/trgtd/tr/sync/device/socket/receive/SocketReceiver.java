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

import static au.com.trgtd.tr.sync.device.Constants.ENCODING;
import static au.com.trgtd.tr.sync.device.Constants.ETX;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Basic socket receiver.
 *
 * @author Jeremy Moore
 */
public class SocketReceiver implements ISocketReceiver {    
    
    private final BufferedReader mReader;

    /**
     * Construct basic reader.
     * @param socket The connection socket.
     * @throws ReceiverException if socket is null or not connected or there is
     * an exception creating the input reader.
     */
    public SocketReceiver(Socket socket, BufferedReader reader) throws SocketReceiverException {
        if (socket == null) {
            throw new SocketReceiverException("Can not construct receiver. Socket is null.");
        }
        if (!socket.isConnected()) {
            throw new SocketReceiverException("Can not construct receiver. Socket is not connected.");
        }
        if (reader != null) {
            mReader = reader;
            return;
        }
        try {
             mReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));
        } catch (UnsupportedEncodingException ex) {
            throw new SocketReceiverException("Can not create reader. Unsupported encoding: " + ENCODING);
        } catch (IOException ex) {
            throw new SocketReceiverException("Can not create reader.", ex);
        }
    }

    @Override
    public String recv() throws SocketReceiverException {
        
        String msg = readLine();
    	while (!msg.endsWith(ETX)) {
            msg += ("\n" + readLine());
    	}
        return msg;
    }

    private String readLine() throws SocketReceiverException {
    	String line = null;
        try {
            line = mReader.readLine();
        } catch (Exception ex) {
            throw new SocketReceiverException("Receive message failed.", ex);
        }
        if (line == null) {
////////////throw new SocketReceiverException("Unexpected end of communication.");
            line = "";
        }
        return line;
    }

}
