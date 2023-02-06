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
package au.com.trgtd.tr.sync.device.usb;

import au.com.trgtd.tr.sync.device.Constants;
import static au.com.trgtd.tr.sync.device.Constants.ENCODING;
import au.com.trgtd.tr.sync.device.SyncHandlerThread;
import au.com.trgtd.tr.sync.device.prefs.SyncPrefsADB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Android ADB sync TCP connector thread. Polls for connection and starts handler.
 *
 * @author Jeremy Moore
 */
final class TCPConnector extends Thread {

    private static TCPConnector sThread;
    
    private static boolean isThreadAlive() {
        return sThread != null && sThread.isAlive();
    }    
    
    /**
     * Start the TCP connector thread if it is not already running.
     */ 
    static void startThread() {
        if (!isThreadAlive()) {
            sThread = new TCPConnector();
            sThread.start();
        }
    }

    /**
     * Stop the TCP manager thread if it is running.
     */ 
    static void stopThread() {
        if (isThreadAlive()) {
            sThread.cancel();            
            sThread = null;
        }
    }    
    
    
    private static final Logger LOG = Logger.getLogger(TCPConnector.class.getName());
    private static final int READ_AHEAD_LIMIT = 1024;
    private static final int SLEEP_TIMEOUT_MS = 1000;
    
    
    private boolean mCancel;
    
    private TCPConnector() {
        super("TR - Android ADB TCP Connector");
    }

    private void cancel() {
        mCancel = true;
    }

    @Override
    public void run() {

        LOG.log(Level.INFO, "Android USB sync connector started.");                        
        
        Socket socket;
        
        mCancel = false;

        while (!mCancel) {
            
//            try {
//                socket = connect();
//                if (socket == null) {
//                    // no connection so try again soon.
//                    sleep();
//                    continue;
//                }
//            } catch (Exception se) {
//                return; // severe exception so give up.
//            }
            try {
                socket = connect();
                if (socket == null) {
                    // no connection so try again soon.
                    sleep();
                    continue;
                }
            } catch (Exception se) {
                return; // severe exception so give up.
            }
            
            // Connected to ADB server.
            
            // Android device may or may not be connected to ADB server so
            // try to read handshake message from device
            BufferedReader reader = getReader(socket);
            if (receiveHandshake(reader)) {
                startSyncThread(socket, reader);
                sleep();                
            } else {
                close(socket);                
                sleep();
            }            
        }
        
        LOG.log(Level.INFO, "Android USB sync connector stopped");                        
    }

    private void startSyncThread(Socket socket, BufferedReader reader) {
        SyncHandlerThread handler = new SyncHandlerThread(socket, reader);
        handler.start();
    }

    /*
     * Attempt to connect. 
     * @return the connected socket if successful or null if not successful. 
     * @throws an exception if a fatal exception occurs.
     */
    private Socket connect() throws Exception {
        
        SyncPrefsADB adbSyncPrefs = SyncPrefsADB.getCurrent();
        if (adbSyncPrefs.off()) {
            cancel();
            return null;
        }
        
        
//        InetSocketAddress socketAddress = new InetSocketAddress("localhost", adbSyncPrefs.getADBPort());
                
        Socket socket;
        try {
            socket = new Socket("localhost", adbSyncPrefs.getADBPort());
//            socket = new Socket();
//            socket.setReuseAddress(true);
//            socket.bind(socketAddress);
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Socket Exception", ex);
            throw ex; // fatal
        } catch (ConnectException ex) {
//          LOG.log(Level.WARNING, "ConnectException", ex);
            return null;
        } catch (IOException ex) {
//          LOG.log(Level.WARNING, "IOException", ex);
            return null;
//      } catch (Exception ex) {
//          LOG.log(Level.WARNING, "Exception", ex);
//          return null;
        }

//      if (socket == null) {
//          LOG.log(Level.INFO, "SOCKET IS NULL");
//          return null;
//      }
        if (socket.isClosed()) {
//          LOG.log(Level.INFO, "SOCKET IS CLOSED");
            return null;
        }
        if (!socket.isConnected()) {
//          LOG.log(Level.INFO, "SOCKET IS NOT CONNECTED");
            return null;
        }
        if (!socket.isBound()) {
//          LOG.log(Level.INFO, "SOCKET IS NOT BOUND");
            return null;
        }

//      LOG.log(Level.INFO, "SOCKET  LOCAL ADDR: {0}", socket.getLocalSocketAddress().toString());
//      LOG.log(Level.INFO, "SOCKET REMOTE ADDR: {0}", socket.getRemoteSocketAddress().toString());

        try {
            socket.setReuseAddress(true);
        } catch (SocketException ex) {
            LOG.log(Level.WARNING, null, ex);
        }

        return socket;
    }

    private BufferedReader getReader(Socket socket) { 
        if (socket == null) {
            return null;
        }
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));
        } catch (IOException ex) {
            LOG.log(Level.WARNING, null, ex);
            return null;
        }
    }

    private boolean receiveHandshake(BufferedReader reader) { 
        if (reader == null) {
            return false;
        }
        try {
            reader.mark(READ_AHEAD_LIMIT);
            String line = reader.readLine();
            if (line == null) {
                return false;
            } else {
                reader.reset();
                return line.startsWith(Constants.CODE_HANDSHAKE);                
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, null, ex);
            return false;
        }
    }

    private void close(Socket socket) {
        if (socket == null) {
            return;
        } 
        try {
            socket.close();
        } catch (IOException ex) {
        }
    }
   
    private void sleep() {
        try {
            Thread.sleep(SLEEP_TIMEOUT_MS);
        } catch (InterruptedException ex) {
        }
    }
}
