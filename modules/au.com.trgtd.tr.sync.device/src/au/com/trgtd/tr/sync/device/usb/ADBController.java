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

import au.com.trgtd.tr.sync.device.prefs.SyncPrefsADB;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDebugBridgeChangeListener;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ADBController implements IDeviceChangeListener, IDebugBridgeChangeListener {

    private static final Logger LOGGER = Logger.getLogger(ADBController.class.getName());
    private static ADBController instance;

    /**
     * Gets the singleton instance.
     * @return The instance.
     */
    public static ADBController instance() {
        if (instance == null) {
            instance = new ADBController();
        }
        return instance;
    }

    
    private final ADBHelper mAdbHelper;
    private SyncPrefsADB mAdbPrefs;

    private ADBController() {
        mAdbPrefs = new SyncPrefsADB(false/*active*/, null/*path*/, 0/*port*/);
        mAdbHelper = new ADBHelper();
    }

    public void onStartup() {
        mAdbPrefs = SyncPrefsADB.getCurrent();
        
        if (mAdbPrefs.on()) {
            startADB(mAdbPrefs);
        }

    }    
    private void startADB(SyncPrefsADB prefs) {
        mAdbHelper.initiate();
        mAdbHelper.addDeviceListener(this);
        mAdbHelper.createBridge(prefs.getADBPath());
        mAdbHelper.createPortForward(prefs.getADBPort());
        TCPConnector.startThread();
    }

    public void onShutdown() {
        if (mAdbPrefs.on()) {
            stopADB(mAdbPrefs);
        }
    }
    private void stopADB(SyncPrefsADB prefs) {
        TCPConnector.stopThread();
        mAdbHelper.removeDeviceListener(this);
        mAdbHelper.removePortForward(prefs.getADBPort());
        mAdbHelper.removeBridge();
        mAdbHelper.terminate();
    }

    public void onPrefsChange(SyncPrefsADB newPrefs) {
        if (newPrefs == null) {
            throw new IllegalArgumentException("ADB sync user prefs can not be null.");
        }

        final SyncPrefsADB oldPrefs = mAdbPrefs;
        mAdbPrefs = newPrefs;
        
        if (oldPrefs.off()) {
            if (newPrefs.on()) {
                startADB(newPrefs);                
            }
        } else /* oldPrefs.on() */ {
            if (newPrefs.off()) {
                stopADB(oldPrefs);                
            } else /* newPrefs.on() */ {
                if (!oldPrefs.getADBPath().equals(newPrefs.getADBPath())) {
                    // ADB program has changed
                    stopADB(oldPrefs);
                    stopADB(newPrefs);
                } else if (oldPrefs.getADBPort() != newPrefs.getADBPort()) {
                    // ADB port forward port has changed
                    mAdbHelper.removePortForward(oldPrefs.getADBPort());
                    mAdbHelper.createPortForward(newPrefs.getADBPort());                
                }                
            }
        }        
    }

    @Override
    public void deviceConnected(IDevice device) {
        LOGGER.log(Level.FINE, "AdbSyncCtlr.deviceConnected(...) device.isOnline()={0}", device.isOnline());
        
        if (mAdbPrefs.on() && device.isOnline()) {
            mAdbHelper.createPortForward(mAdbPrefs.getADBPort());                
        }
    }

    @Override
    public void deviceDisconnected(IDevice device) {
        LOGGER.log(Level.FINE, "AdbSyncCtlr.deviceDisconnected(...)");
    }

    @Override
    public void deviceChanged(IDevice device, int changeMask) {
        LOGGER.log(Level.FINE, "AdbSyncCtlr.deviceChanged(...) changeMask={0}", changeMask);
        
        if ((changeMask & IDevice.CHANGE_STATE) == IDevice.CHANGE_STATE) { 
            // changed state
            if (mAdbPrefs.on() && device.isOnline()) {
                mAdbHelper.createPortForward(mAdbPrefs.getADBPort());                
            }
        }
    }

    @Override
    public void bridgeChanged(AndroidDebugBridge bridge) {
        LOGGER.log(Level.FINE, "AdbSyncCtlr.bridgeChanged(...)");        
    }

}
