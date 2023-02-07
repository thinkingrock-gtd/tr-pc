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
import tr.model.action.Action;
import tr.model.action.Recurrence;

public class ModifyRecurrenceWizardPanelModProject1 implements WizardDescriptor.Panel,
        PropertyChangeListener {

    private final Recurrence recurrence;
    private final Action action;
    private ModifyRecurrenceVisualPanelModProject1 panel;

    public ModifyRecurrenceWizardPanelModProject1(Recurrence recurrence, Action action) {
        this.recurrence = recurrence;
        this.action = action;
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new ModifyRecurrenceVisualPanelModProject1(recurrence, action);
            panel.addPropertyChangeListener(this);
        }
        return panel;
    }

    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    public boolean isValid() {
        return panel != null && panel.isFormValid();
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
    }

    public void storeSettings(Object settings) {
        if (panel == null) {
            return;
        }
        WizardDescriptor wd = (WizardDescriptor) settings;
        wd.putProperty("mod.project.newProject", panel.getNewProject());
        wd.putProperty("mod.project.isMoveAll", panel.isMoveAll());
        wd.putProperty("mod.project.moveDate", panel.getMoveDate());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        this.fireChangeEvent(); // validity may have changed.
    }
}
