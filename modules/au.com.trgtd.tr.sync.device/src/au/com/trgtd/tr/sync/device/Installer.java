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
