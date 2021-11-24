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
package au.com.trgtd.tr.view.reference.linker;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import tr.model.information.Information;

class RefViewer {

    private static RefViewer instance;

    public final static RefViewer getDefault() {
        if (instance == null) {
            instance = new RefViewer();
        }
        return instance;
    }

    private RefViewer() {
        panel = new RefViewerPanel();
        title = NbBundle.getMessage(CLASS, "ref.title");
        Object ok = DialogDescriptor.OK_OPTION;
        Object[] opts = new Object[] {ok};
        int align = DialogDescriptor.DEFAULT_ALIGN;
        
//      this.descriptor = new DialogDescriptor(panel, title);
        this.descriptor = new DialogDescriptor(panel, title, true, opts, ok, align, null, null);
    }

    public void view(Information ref) {
        panel.reset(ref);
        DialogDisplayer.getDefault().notify(descriptor);
    }

    private final static Class CLASS = RefViewer.class;
    private final DialogDescriptor descriptor;
    private final RefViewerPanel panel;
    private final String title;

}
