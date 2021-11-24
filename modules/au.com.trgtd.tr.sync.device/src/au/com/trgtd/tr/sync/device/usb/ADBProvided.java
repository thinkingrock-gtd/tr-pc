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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Utilities;

/**
 * ADB (Android Debug Bridge) provided executable.
 */
public final class ADBProvided {

    private static final String MODULE = "au.com.trgtd.tr.sync.device";
    private static final Logger LOGGER = Logger.getLogger(ADBProvided.class.getName());
    
    /**
     * Gets the path of the provided ADB program. If the program is not 
     * executable, try to set it executable.
     * @return the absolute path of the provided ADB program or null. 
     */
    public static String getProvidedADBPath() {
        
        String relativePath;
        if (Utilities.isMac()) {
            relativePath = "adb/osx/adb";
        } else if (Utilities.isWindows()) {
            relativePath = "adb/windows/adb.exe";
        } else { // assume Linux
            relativePath = "adb/linux/adb";
        }        
        
        File file = InstalledFileLocator.getDefault().locate(relativePath, MODULE, false);        
        if (file == null)  {
            LOGGER.log(Level.INFO, "Can not find provided ADB program.");
            return null; 
        } 

        // if program is not executable, try and set it executable
        final boolean OWNER_ONLY = true;
        if (!file.canExecute()) {            
            try {
                if (file.setExecutable(!OWNER_ONLY)) {
                    LOGGER.log(Level.INFO, "Provided ADB program set executable for all.");                    
                } else {
                    LOGGER.log(Level.INFO, "Provided ADB program not set executable for all.");                    
                }
            } catch (Exception ex) {
                LOGGER.log(Level.INFO, "Provided ADB program cannot be set executable for all.", ex);                    
            }
        }        
        if (!file.canExecute()) {            
            try {
                if (file.setExecutable(OWNER_ONLY)) {
                    LOGGER.log(Level.INFO, "Provided ADB program set executable for owner only.");                    
                } else {
                    LOGGER.log(Level.INFO, "Provided ADB program not set executable for owner only.");                    
                }
            } catch (Exception ex) {                
                LOGGER.log(Level.INFO, "Provided ADB program can not be set executable for owner.", ex);
            }                
        }        

        
        if (file.canExecute()) {
            LOGGER.log(Level.INFO, "Provided ADB program is executable.");            
        } else {
            LOGGER.log(Level.INFO, "Provided ADB program is not executable.");            
        }
                
        try {
            return file.getCanonicalPath();
        } catch (IOException ex) {
            return file.getAbsolutePath();
        }
    }
        
}
