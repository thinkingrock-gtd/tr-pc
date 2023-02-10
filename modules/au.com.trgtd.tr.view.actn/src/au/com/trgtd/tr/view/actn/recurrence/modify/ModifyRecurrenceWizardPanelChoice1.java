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

import java.beans.PropertyChangeEvent;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import tr.model.action.Recurrence;

public class ModifyRecurrenceWizardPanelChoice1 implements WizardDescriptor.Panel,
        PropertyChangeListener {

    private final Recurrence recurrence;
    private ModifyRecurrenceVisualPanelChoice1 panel;

    public ModifyRecurrenceWizardPanelChoice1(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new ModifyRecurrenceVisualPanelChoice1(recurrence);
            panel.addPropertyChangeListener(this);
        }
        return panel;
    }

    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    public boolean isValid() {
        return panel != null && panel.isModificationChosen();
    }

    private final Set<ChangeListener> listeners = new HashSet<>(1);

    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    public void readSettings(Object settings) {
        if (panel == null) {
            return;
        }
        panel.initPanel();
    }

    public void storeSettings(Object settings) {
        if (panel == null) {
            return;
        }
        WizardDescriptor wd = (WizardDescriptor) settings;
        wd.putProperty(Modification.PROPERTY, panel.getClientProperty(Modification.PROPERTY));
        wd.putProperty("recurrence", null);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // validity may have changed
        this.fireChangeEvent();
    }
}
