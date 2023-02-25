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
package au.com.trgtd.tr.view.project.chooser;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;

/**
 * Project chooser.
 *
 * @author Jeremy Moore
 */
public final class ProjectChooser {
    
    private final Window owner;
    private final String title;
    private ProjectChooserDialog dialog;
    
    public ProjectChooser(Frame owner) {
        this(owner, null);
    }
    
    public ProjectChooser(Frame owner, String title) {
        this.owner = owner;
        this.title = title;
    }
    
    public ProjectChooser(Dialog owner) {
        this(owner, null);
    }
    
    public ProjectChooser(Dialog owner, String title) {
        this.owner = owner;
        this.title = title;
    }
    
    public ProjectChooserDialog getDialog() {
        synchronized(this) {
            if (dialog == null) {
                if (owner instanceof Frame frame) {
                    dialog = (title == null) ? new ProjectChooserDialog(frame) : new ProjectChooserDialog(frame, title);
                } else if (owner instanceof Dialog dlg) {
                    dialog = (title == null) ? new ProjectChooserDialog(dlg) : new ProjectChooserDialog(dlg, title);
                } else {
                    dialog = (title == null) ? new ProjectChooserDialog(new JFrame()) : new ProjectChooserDialog(new JFrame(), title);
                }
            }
            return dialog;
        }
    }
    
}
