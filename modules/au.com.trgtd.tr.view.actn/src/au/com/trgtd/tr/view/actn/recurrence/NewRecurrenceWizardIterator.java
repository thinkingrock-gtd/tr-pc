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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import tr.model.action.Action;
import tr.model.action.Recurrence;

public final class NewRecurrenceWizardIterator implements WizardDescriptor.Iterator, PropertyChangeListener {

    private final Recurrence recurrence;
    private final Action action;
    private int index;
    private WizardDescriptor.Panel[] currentPanels;
    private WizardDescriptor.Panel[] sequentPanels;
    private WizardDescriptor.Panel[] regularPanels;
    
    public NewRecurrenceWizardIterator(Recurrence recurrence, Action action) {
        this.recurrence = recurrence;
        this.action = action;
        initPanels();
    }

    private void initPanels() {

        WizardDescriptor.Panel p1 = new NewRecurrenceWizardPanel1(recurrence);
        WizardDescriptor.Panel p2 = new NewRecurrenceWizardPanel2(recurrence);
        WizardDescriptor.Panel p3 = new NewRecurrenceWizardPanel3(recurrence);
        WizardDescriptor.Panel p4 = new NewRecurrenceWizardPanel4(recurrence, action);
        WizardDescriptor.Panel[] panels = new WizardDescriptor.Panel[]{p1, p2, p3, p4};

        int nSteps = 3;
        String[] steps = new String[nSteps];

        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (i < steps.length) {
                steps[i] = c.getName();
            }
            if (c instanceof JComponent jc) {
                //Property changes
                jc.addPropertyChangeListener(this);
//              jc.putClientProperty("WizardPanel_contentSelectedIndex", i);
                jc.putClientProperty("WizardPanel_contentSelectedIndex", Math.min(i, nSteps - 1));
                // Sets steps names for a panel
                jc.putClientProperty("WizardPanel_contentData", steps);
                // Turn on subtitle creation on each step
                jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                // Show steps on the left side with the image on the background
                jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                // Turn on numbering of all steps
                jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
            }
        }

        sequentPanels = new WizardDescriptor.Panel[]{p1, p2, p3};
        regularPanels = new WizardDescriptor.Panel[]{p1, p2, p4};
        currentPanels = regularPanels;
    }

    /**
     * Gets the panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        return currentPanels;
    }

    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". " + org.openide.util.NbBundle.getMessage(NewRecurrenceWizardIterator.class, "of") + " " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }
    private Set<ChangeListener> listeners = new HashSet<>(1);

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

    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("type")) {
            String type = (String) evt.getNewValue();
            if (type.equals("subsequent")) {
                currentPanels = sequentPanels;
                fireChangeEvent();
            } else if (type.equals("regular")) {
                currentPanels = regularPanels;
                fireChangeEvent();
            }
        }
    }
}
