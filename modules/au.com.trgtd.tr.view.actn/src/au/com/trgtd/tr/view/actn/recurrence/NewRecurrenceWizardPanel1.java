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

public class NewRecurrenceWizardPanel1 implements WizardDescriptor.Panel {
    
    public NewRecurrenceWizardPanel1(Recurrence recurrence) {
        this.recurrence = recurrence;        
    }

    private final Recurrence recurrence;
    private Component component;    

    public Component getComponent() {
        if (component == null) {
            component = new NewRecurrenceVisualPanel1(recurrence);
        }
        return component;
    }

    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    public boolean isValid() {
        return true;
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    public void readSettings(Object settings) {        
        ((NewRecurrenceVisualPanel1)component).initPanel();
    }

    public void storeSettings(Object settings) {                       
    }
    
}

