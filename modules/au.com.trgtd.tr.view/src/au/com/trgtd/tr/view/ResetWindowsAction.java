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
