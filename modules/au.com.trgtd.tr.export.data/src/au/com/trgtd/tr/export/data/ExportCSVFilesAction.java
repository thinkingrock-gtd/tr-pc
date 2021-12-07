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
package au.com.trgtd.tr.export.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "au.com.trgtd.tr.export.data.ExportCSVFiles"
)
@ActionRegistration(
        displayName = "#CTL_ExportCSVFiles"
)
@ActionReferences({
    @ActionReference(path = "Menu/File/Export", position = 900, separatorBefore = 890),
    @ActionReference(path = "Shortcuts", name = "M-X M-D")
})
@Messages("CTL_ExportCSVFiles=Export CSV Files")
public final class ExportCSVFilesAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {        
        new ExportCSVFiles().export();        
    }
    
}
