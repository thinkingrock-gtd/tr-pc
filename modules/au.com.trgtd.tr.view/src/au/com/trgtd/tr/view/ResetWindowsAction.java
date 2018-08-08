/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.trgtd.tr.view;

import au.com.trgtd.tr.prefs.ui.utils.WindowUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "au.com.trgtd.tr.view.ResetWindowsAction"
)
@ActionRegistration(
        displayName = "#CTL_ResetWindowsAction"
)
@ActionReference(path = "Menu/Tools", position = 1600, separatorBefore = 1550, separatorAfter = 1650)
@Messages("CTL_ResetWindowsAction=Reset Windows")
public final class ResetWindowsAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        WindowUtils.closeWindows();
        FileUtil.getConfigObject("Actions/Window/org-netbeans-core-windows-actions-ResetWindowsAction.instance", Action.class).actionPerformed(null);         
        WindowUtils.openInitialWindow();
    }
}
