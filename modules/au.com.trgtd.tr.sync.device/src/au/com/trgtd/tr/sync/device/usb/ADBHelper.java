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

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.TimeoutException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ADB helper
 */
final class ADBHelper {

    private static final Logger LOGGER = Logger.getLogger(ADBHelper.class.getName());
    
    ADBHelper() {
    }
    
    void initiate() {
        try {
            AndroidDebugBridge.initIfNeeded(false /* clientSupport */);
            LOGGER.log(Level.INFO, "ADB initialized.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);            
        }
    }
    
    void terminate() {
        try {
            AndroidDebugBridge.terminate();
            LOGGER.log(Level.INFO, "ADB terminated.");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);            
        }
    }
    
    void addDeviceListener(IDeviceChangeListener listener) {
        AndroidDebugBridge.addDeviceChangeListener(listener);
    }

    void removeDeviceListener(IDeviceChangeListener listener) {
        AndroidDebugBridge.removeDeviceChangeListener(listener);
    }

    void addBridgeListener(IDebugBridgeChangeListener listener) {
        AndroidDebugBridge.addDebugBridgeChangeListener(listener);
    }

    void removeBridgeListener(IDebugBridgeChangeListener listener) {
        AndroidDebugBridge.removeDebugBridgeChangeListener(listener);
    }
    
    void createPortForward(int port) {
        AndroidDebugBridge bridge = AndroidDebugBridge.getBridge();
        if (bridge == null) {
            LOGGER.log(Level.WARNING, "ADB does not have a connected bridge.");                            
            return;
        }
        for (IDevice device : bridge.getDevices()) {
            createPortForward(device, port);
        }                
    }
    
    private void createPortForward(IDevice device, int port) {  
        if (device == null) {
            LOGGER.log(Level.WARNING, "ADB device is null.");                            
            return;
        }
        if (!device.isOnline()) {
            LOGGER.log(Level.WARNING, "ADB device {0} is not online.",  device);                            
            return;            
        }        

        try {
            Object[] params = new Object[] {port, device.getSerialNumber()};
            
            LOGGER.log(Level.INFO, "Attempting to create port forward {0} on device {1}.",  params);                            

            device.createForward(port, port);
            LOGGER.log(Level.INFO, "ADB created port forward {0} on device {1}", params);                            
        } catch (TimeoutException | AdbCommandRejectedException | IOException ex) {
            LOGGER.log(Level.SEVERE, "ADB could not create port forward.", ex);
        }
    }
    
    void removePortForward(int port) {  
        AndroidDebugBridge bridge = AndroidDebugBridge.getBridge();
        if (bridge == null) {
            LOGGER.log(Level.WARNING, "ADB does not have a connected bridge.");                            
            return;
        }
        for (IDevice device : bridge.getDevices()) {
            removePortForward(device, port);
        }        
    }
    
    private void removePortForward(IDevice device, int port) {  
        if (device == null) {
            LOGGER.log(Level.WARNING, "ADB device is null.");                            
            return;
        }
        if (!device.isOnline()) {
            LOGGER.log(Level.WARNING, "ADB device {0} is not online.",  device);                            
            return;            
        }        
        try {
            device.removeForward(port, port);
            // log success
            Object[] params = new Object[] {port, device.getSerialNumber()};
            LOGGER.log(Level.INFO, "ADB removed port forward {0} on device {1}", params);                            
        } catch (TimeoutException | AdbCommandRejectedException | IOException ex) {
            LOGGER.log(Level.SEVERE, "ADB could not remove port forward. {0}", ex.getMessage());
        }
    }    
    
    void createBridge(String adbPath) {
        AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(adbPath, true /* forceNewBridge */);
        if (bridge == null) {
            LOGGER.log(Level.SEVERE, "ADB could not create bridge using ADB path: {0}", adbPath);
        } else {
            LOGGER.log(Level.INFO, "ADB created bridge using ADB path: {0}", adbPath);
        }        
    }
    
    void removeBridge() {
        AndroidDebugBridge bridge = AndroidDebugBridge.getBridge();
        if (bridge == null) {
            LOGGER.log(Level.WARNING, "ADB does not have a bridge to disconnect.");
        } else {
            AndroidDebugBridge.disconnectBridge();
            LOGGER.log(Level.INFO, "ADB has disconnected the bridge.");            
        }
    }
    
    void logBridge() {
        logBridge(AndroidDebugBridge.getBridge());
    }

    void logBridge(AndroidDebugBridge bridge) {
        StringBuilder sb = new StringBuilder("BRIDGE");
        sb.append(" [");        
        if (bridge != null) {
            sb.append("connected: ").append(bridge.isConnected());
            sb.append(", hasInitialDeviceList: ").append(bridge.hasInitialDeviceList());
            sb.append(", connectionAttemptCount: ").append(bridge.getConnectionAttemptCount());
            sb.append(", restartAttemptCount: ").append(bridge.getRestartAttemptCount());
        }
        sb.append("]");
        
        LOGGER.log(Level.INFO, sb.toString());
    }
    
    void logDevices() {
        StringBuilder sb = new StringBuilder();
        AndroidDebugBridge adb = AndroidDebugBridge.getBridge();
        if (adb == null) {
            sb.append("DEVICES []");
        } else {
            IDevice[] devices = adb.getDevices();
            if (devices.length == 0) {
                sb.append("DEVICES []");
            } else {
                sb.append("DEVICES [").append("\n");
                for (IDevice device : devices) {
                    append(sb, device);
                    sb.append('\n');
                }
                sb.append("]");
            }
        }
        LOGGER.log(Level.INFO, sb.toString());
    }

    void logDevice(IDevice device) {
        StringBuilder sb = new StringBuilder();
        append(sb, device);
        LOGGER.log(Level.INFO, sb.toString());
    }
    
    private void append(StringBuilder sb, IDevice device) {
        sb.append("[DEVICE: ").append(device.getName())
          .append(", SERIAL: ").append(device.getSerialNumber())
          .append(", ONLINE: ").append(device.isOnline())
          .append("]");
    }
}
