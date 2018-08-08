package au.com.trgtd.tr.sync.dropbox;

import org.openide.modules.ModuleInstall;

/**
 * Module installer.
 */
public class Installer extends ModuleInstall {

    private static final String NAME = "DropBox sync manager";
    private DBoxSyncMgr syncMgrThread;

    @Override
    public void restored() {
        syncMgrThread = new DBoxSyncMgr();
        syncMgrThread.setDaemon(true);
        syncMgrThread.start();
    }

    @Override
    public boolean closing() {
        try {
            if (null != syncMgrThread) {
                syncMgrThread.cancel();
                syncMgrThread.interrupt();
                syncMgrThread = null;
            }
        } catch (Exception ex) {
        }
        return super.closing();
    }
}
