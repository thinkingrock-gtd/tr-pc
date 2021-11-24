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

import au.com.trgtd.tr.swing.mig.MigUtils;
import java.awt.event.ActionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import tr.model.information.Information;

class RefChooser {

    private final static Object OK = DialogDescriptor.OK_OPTION;
    private final static Object CANCEL = DialogDescriptor.CANCEL_OPTION;

    private static RefChooser instance;

    public final static RefChooser getDefault() {
        if (instance == null) {
            instance = new RefChooser();
        }
        return instance;
    }

    private RefChooser() {
        title = NbBundle.getMessage(CLASS, "ref.link.dialog");
        panel = new RefChooserPanel();
//      descriptor = new DialogDescriptor(panel, title);
//      panel.setDescriptor(descriptor);
        boolean modal = true;
        Object[] opts = MigUtils.isOkCancelOrder() ? new Object[]{OK, CANCEL} : new Object[]{CANCEL, OK};
        Object defOpt = OK;
        int align = DialogDescriptor.DEFAULT_ALIGN;
        HelpCtx help = null;
        ActionListener al = null;
        boolean leaf = false;
        descriptor = new DialogDescriptor(panel, title, modal, opts, defOpt, align, help, al, leaf);
        panel.setDescriptor(descriptor);
    }

    public Information getUserChoice() {
        panel.reset();
        Object option = DialogDisplayer.getDefault().notify(descriptor);
        if (option == DialogDescriptor.OK_OPTION) {
            return panel.getSelected();
        } else {
            return null;
        }
    }

    private final static Class CLASS = RefChooser.class;
    private final DialogDescriptor descriptor;
    private final RefChooserPanel panel;
    private final String title;

}
