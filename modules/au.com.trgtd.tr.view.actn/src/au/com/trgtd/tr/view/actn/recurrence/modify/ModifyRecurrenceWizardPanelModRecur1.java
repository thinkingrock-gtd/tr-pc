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
package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import tr.model.action.Recurrence;

public class ModifyRecurrenceWizardPanelModRecur1 implements WizardDescriptor.Panel {

    private final Recurrence.Basis oldBasis;
    private ModifyRecurrenceVisualPanelModRecur1 panel;

    public ModifyRecurrenceWizardPanelModRecur1(Recurrence.Basis oldBasis) {
        this.oldBasis = oldBasis;
    }

    /** Gets the visual panel for the panel. */
    public Component getComponent() {
        if (panel == null) {
            panel = new ModifyRecurrenceVisualPanelModRecur1();
            panel.setModel(oldBasis);
        }
        return panel;
    }

    /** Gets the help context for the panel. */
    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    /** Determines whether the panel values are valid. */
    public boolean isValid() {
        return true;
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }

    public void readSettings(Object settings) {
        if (panel == null) {
            return;
        }
        panel.initPanel();
    }

    public void storeSettings(Object settings) {
        WizardDescriptor wd = (WizardDescriptor) settings;
        if (panel == null) {
            wd.putProperty("mod.old.basis", null);
            wd.putProperty("mod.new.basis", null);
        } else {
            wd.putProperty("mod.old.basis", oldBasis);
            wd.putProperty("mod.new.basis", panel.getBasis());
        }
    }
}
