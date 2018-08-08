package au.com.trgtd.tr.task.messages;

import org.openide.modules.ModuleInstall;

/**
 * Module installer runs the messages check in a new thread.
 *
 * @author Jeremy Moore
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        (new UserMessagesThread()).start();
    }
    
}
