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
package au.com.trgtd.tr.sync.device;

import au.com.trgtd.tr.sync.device.dbx.DbxController;
import au.com.trgtd.tr.sync.device.usb.ADBController;
import au.com.trgtd.tr.sync.device.wifi.WiFiServiceManager;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        DbxController.instance().onStartup();
        ADBController.instance().onStartup();
        WiFiServiceManager.instance.initiate();                    
    }

    @Override
    public boolean closing() {
        DbxController.instance().onShutdown();
        ADBController.instance().onShutdown();
        WiFiServiceManager.instance.terminate();
        return true;
    }
    
}
