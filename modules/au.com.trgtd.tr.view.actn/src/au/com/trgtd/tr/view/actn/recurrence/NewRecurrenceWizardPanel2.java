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
package au.com.trgtd.tr.view.actn.recurrence;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import tr.model.action.Recurrence;

public class NewRecurrenceWizardPanel2 implements WizardDescriptor.Panel {

    public NewRecurrenceWizardPanel2(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    private final Recurrence recurrence;
    private Component component;

    /** Gets the visual component for the panel. */
    public Component getComponent() {
        if (component == null) {
            component = new NewRecurrenceVisualPanel2(recurrence);
        }
        return component;
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
        ((NewRecurrenceVisualPanel2)component).loadValues();
    }

    public void storeSettings(Object settings) {
    }
}

