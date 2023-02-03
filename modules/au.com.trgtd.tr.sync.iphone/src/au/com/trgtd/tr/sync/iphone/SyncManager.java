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
package au.com.trgtd.tr.sync.iphone;

import au.com.trgtd.tr.sync.iphone.SyncState.State;
import au.com.trgtd.tr.util.Utils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncManager {

    // STATIC
    private static SyncManager instance;

    /**
     * Get the singleton instance.
     * @return the instance.
     */
    public static SyncManager getDefault() {
        if (instance == null) {
            instance = new SyncManager();
        }
        return instance;
    }

    private static final Logger LOG = Logger.getLogger(SyncManager.class.getName());
    private static final Object SYNCHRONIZE_OBJECT = new Object();
    private static final int CONNECT_TIMEOUT = 5000; // 5 seconds

    private static synchronized String log(String x) {
        LOG.log(Level.INFO, "Sent:{0}", x);
        return x;
    }

    // INSTANCE
    private SyncManager() {
        propertyChangeSupport = new PropertyChangeSupport(this);
        
        state = new SyncState();
        state.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            propertyChangeSupport.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        });
        progress = new SyncProgress();
        progress.addPropertyChangeListener((PropertyChangeEvent evt) -> {
            propertyChangeSupport.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        });
    }

    private final SyncState state;
    private final SyncProgress progress;
    private ServerSocket serverSocket;
    private Socket connection;
    private SyncThread syncThread;
    private boolean cancelConnect;
    private boolean cancelSync;

    private final PropertyChangeSupport propertyChangeSupport;
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
    }
    
    public synchronized boolean canStartSync() {
        if (syncThread == null || !syncThread.isAlive()) {
            return true;
        }
        return !Utils.equal(state, State.Connecting) && !Utils.equal(state, State.Syncing);
    }

    public synchronized void startSync(InetAddress inetAddress) {
        if (!canStartSync()) {
            LOG.severe("CAN NOT START SYNC");
            return;
        }
        state.setState(State.None);
        progress.setProgress(0);
        syncThread = new SyncThread(inetAddress);
        syncThread.start();
    }

    public void cancelConnect() {
        cancelConnect = true;
    }

    public void cancelSync() {
        cancelSync = true;
        if (syncThread != null) {
            syncThread.cancelSync();
        }
    }

    private synchronized void setState(SyncState.State syncState) {
        assert (state != null);

        state.setState(syncState);

        switch (syncState) {
            case None:
            case Connecting:
                cancelConnect = false;
                cancelSync = false;
                progress.setProgress(-1);
                break;
            case Syncing:
                progress.setProgress(0);
                break;
            case Finished:
                progress.setProgress(100);
                break;
            case Failed:
                break;
            case Cancelled:
                break;
            case Aborted:
                break;
        }
    }

    private class SyncThread extends Thread {

        private final InetAddress inetAddress;
        private SyncHandler syncHandler;

        public SyncThread(InetAddress inetAddress) {
            super("TR iPhone Sync");
            this.inetAddress = inetAddress;
        }

        @Override
        public void run() {
            synchronized (SYNCHRONIZE_OBJECT) {
                LOG.log(Level.INFO, "iPhone sync started at: {0}", new Date());
                try {
                    setState(SyncState.State.Connecting);
                    if (!connect()) {
                        setState(SyncState.State.Cancelled);
                        closeConnection();
                        return;
                    }
                } catch (Exception ex) {
                    closeConnection();
                    setState(SyncState.State.Failed);
                    LOG.log(Level.SEVERE, "iPhone connection failed. {0}", ex.getMessage());
                    ex.printStackTrace();
                    return;
                }
                try {
                    setState(SyncState.State.Syncing);
                    if (!sync()) {
                        closeConnection();
                        setState(SyncState.State.Cancelled);
                        return;
                    }
                    closeConnection();
                    setState(SyncState.State.Finished);
                } catch (FileIDException ex) {
                    closeConnection();
                    setState(SyncState.State.Aborted);
                    return;
                } catch (Exception ex) {
                    closeConnection();
                    setState(SyncState.State.Failed);
                    LOG.log(Level.SEVERE, "iPhone syncing failed. {0}", ex.getMessage());
                    ex.printStackTrace();
                    return;
                }
                LOG.log(Level.INFO, "iPhone sync finished at: {0}", new Date());
            }
        }

        public void cancelSync() {
            if (syncHandler != null) {
                syncHandler.cancel();
            }
        }

        // Make sure connection is closed.
        private void closeConnection() {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "Close socket failed. {0}", ex.getMessage());
                    ex.printStackTrace();
                }
            }
            connection = null;

            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    LOG.log(Level.WARNING, "Close server socket failed. {0}", ex.getMessage());
                    ex.printStackTrace();
                }
            }
            serverSocket = null;
        }

        private boolean connect() throws Exception {
            serverSocket = new ServerSocket(SyncPrefs.getPort(), 0, inetAddress);



            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(CONNECT_TIMEOUT);

//            serverSocket = new ServerSocket();
//            serverSocket.setReuseAddress(true);
//            serverSocket.setSoTimeout(CONNECT_TIMEOUT);
////            serverSocket.setReceiveBufferSize(32);
////            serverSocket.setPerformancePreferences(0, 2, 1);
//
//
//            serverSocket.bind(new InetSocketAddress(inetAddress, SyncPrefs.getPort()), 0);

            while (connection == null || !connection.isConnected()) {
                try {
                    connection = serverSocket.accept();
                    
                    // try thsi
//                    connection.setKeepAlive(true);
//                    connection.setTcpNoDelay(false);
//                    connection.setReceiveBufferSize(64);


                } catch (SocketTimeoutException ex) {
                    if (cancelConnect) {
                        LOG.info("Connect to iPhone cancelled.");
                        return false;
                    }
                }
            }

            return true;
        }

        private boolean sync() throws Exception {

//            System.out.println("Conn receive buffer size: " + connection.getReceiveBufferSize());
//            System.out.println("Conn keep alive: " + connection.getKeepAlive());
//            System.out.println("Conn TCP no delay: " + connection.getTcpNoDelay());
            
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"), true);

            boolean result = false;

            while (true) {
                if (cancelSync) {
                    out.println(log("Q"));
                    LOG.info("Server syncing was cancelled.");
                    break;
                }

                String line = in.readLine();

                LOG.log(Level.INFO, "RECIEVED LINE:{0}", line == null ? "null" : line);

                if (line == null) {
                    LOG.warning("RECIEVED: Unexpected end of input stream.");
                    break;
                }

                SyncMsg104.MsgHandshake msgHs104 = SyncMsg104.getHandshakeMsg(line);
                if (msgHs104 != null) {
                    syncHandler = new SyncHandler104(in, out, progress);
                    result = ((SyncHandler104)syncHandler).sync(msgHs104);
                    break;
                }
                SyncMsg103.MsgHandshake msgHs103 = SyncMsg103.getHandshakeMsg(line);
                if (msgHs103 != null) {
                    syncHandler = new SyncHandler103(in, out, progress);
                    result = ((SyncHandler103)syncHandler).sync(msgHs103);
                    break;
                }
                SyncMsg102.MsgHandshake msgHs102 = SyncMsg102.getHandshakeMsg(line);
                if (msgHs102 != null) {
                    syncHandler = new SyncHandler102(in, out, progress);
                    result = ((SyncHandler102)syncHandler).sync(msgHs102);
                    break;
                }
                SyncMsg101.MsgHandshake msgHs101 = SyncMsg101.getHandshakeMsg(line);
                if (msgHs101 != null) {
                    syncHandler = new SyncHandler101(in, out, progress);
                    result = ((SyncHandler101)syncHandler).sync(msgHs101);
                    break;
                }
                SyncMsg100.MsgHandshake msgHs100 = SyncMsg100.getHandshakeMsg(line);
                if (msgHs100 != null) {
                    syncHandler = new SyncHandler100(in, out, progress);
                    result = ((SyncHandler100)syncHandler).sync(msgHs100);
                    break;
                }
            }

            closeConnection();

            return result;
        }
    }
}
