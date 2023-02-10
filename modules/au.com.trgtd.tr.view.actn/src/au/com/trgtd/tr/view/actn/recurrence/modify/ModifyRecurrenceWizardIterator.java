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
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import tr.model.action.Action;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;

public final class ModifyRecurrenceWizardIterator implements WizardDescriptor.Iterator,
        PropertyChangeListener {

    private static final Logger LOG = Logger.getLogger("tr.view.actn");    
    private final Action action;
    private final Recurrence recurrenceOriginal;
    private final Recurrence recurrenceModSubsequent;
    private final Recurrence recurrenceModRegular;
    private final Recurrence recurrenceModAction;
    private final Recurrence recurrenceModProject;
    private int index;
    private WizardDescriptor.Panel[] currentPanels;
    private WizardDescriptor.Panel[] modifyChoicePanels;
    private WizardDescriptor.Panel[] modifyRecurPanels;
    private WizardDescriptor.Panel[] modifyActionPanels;
    private WizardDescriptor.Panel[] modifyProjectPanels;
    private WizardDescriptor.Panel[] removeRecurPanels;
    
    public ModifyRecurrenceWizardIterator(Action action, Recurrence recurrence) {
        this.action = action;
        recurrenceOriginal = copy(recurrence);
        recurrenceModSubsequent = copy(recurrence);
        recurrenceModSubsequent.setBasis(Basis.DONE_DATE);
        recurrenceModRegular = copy(recurrence);
        recurrenceModRegular.setBasis(Basis.START_DATE);
        recurrenceModAction = copy(recurrence);
        recurrenceModProject = copy(recurrence);
        initPanels();
    }

    private void putProperties(WizardDescriptor.Panel wdp, int index, String[] steps) {
        JComponent jc = (JComponent) wdp.getComponent();
        jc.addPropertyChangeListener(this);
        jc.putClientProperty("WizardPanel_contentSelectedIndex", index);
        jc.putClientProperty("WizardPanel_contentData", steps);
        jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
        jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
        jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
    }

    private void initPanels() {
        WizardDescriptor.Panel p1 = new ModifyRecurrenceWizardPanelChoice1(recurrenceOriginal);
        WizardDescriptor.Panel pN = new ModifyRecurrenceWizardPanelChoiceN();
        String[] stepsInitial = new String[]{
            p1.getComponent().getName(), // choose modification
            pN.getComponent().getName()  // ...
        };
        putProperties(p1, 0, stepsInitial);
        putProperties(pN, 1, stepsInitial);
        modifyChoicePanels = new WizardDescriptor.Panel[]{p1, pN};

        WizardDescriptor.Panel pR1 = new ModifyRecurrenceWizardPanelModRecur1(recurrenceOriginal.getBasis());
        WizardDescriptor.Panel pR2 = new ModifyRecurrenceWizardPanelModRecur2(recurrenceModSubsequent, recurrenceModRegular, action);
        String[] stepsModRec = new String[]{
            p1.getComponent().getName(),    // choose modification
            pR1.getComponent().getName(),   // choose recurrence type
            pR2.getComponent().getName()    // change recurrence
        };
        putProperties(pR1, 1, stepsModRec);
        putProperties(pR2, 2, stepsModRec);
        modifyRecurPanels = new WizardDescriptor.Panel[]{p1, pR1, pR2};

        WizardDescriptor.Panel pD1 = new ModifyRecurrenceWizardPanelRemRecur1();
        String[] stepsRemoval = new String[]{
            p1.getComponent().getName(),    // choose modification
            pD1.getComponent().getName()    // confirm removal
        };
        putProperties(pD1, 1, stepsRemoval);
        removeRecurPanels = new WizardDescriptor.Panel[]{p1, pD1};
        
        WizardDescriptor.Panel pA1 = new ModifyRecurrenceWizardPanelModAction1(recurrenceModAction);
        WizardDescriptor.Panel pA2 = new ModifyRecurrenceWizardPanelModAction2(recurrenceModAction, action);        
        String[] stepsModifyAction = new String[]{
            p1.getComponent().getName(),    // choose modification
            pA1.getComponent().getName(),   // change action details
            pA2.getComponent().getName()    // finalise change
        };
        putProperties(pA1, 1, stepsModifyAction);
        putProperties(pA2, 2, stepsModifyAction);
        modifyActionPanels = new WizardDescriptor.Panel[]{p1, pA1, pA2};
                
        WizardDescriptor.Panel pP1 = new ModifyRecurrenceWizardPanelModProject1(recurrenceModProject, action);
        String[] stepsModifyProject = new String[]{
            p1.getComponent().getName(),    // choose modification
            pP1.getComponent().getName(),   // change project details
        };
        putProperties(pP1, 1, stepsModifyProject);
        modifyProjectPanels = new WizardDescriptor.Panel[]{p1, pP1};

        currentPanels = modifyChoicePanels;
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
        return index + 1 + ". " + org.openide.util.NbBundle.getMessage(ModifyRecurrenceWizardIterator.class, "of") + " " + getPanels().length;
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
        if (prop.equals(Modification.PROPERTY)) {
            Modification modification = (Modification) evt.getNewValue();
            switch (modification) {
                case CHANGE_RECURRENCE: {
                    currentPanels = modifyRecurPanels;
                    break;
                }
                case REMOVE_RECURRENCE: {
                    currentPanels = removeRecurPanels;
                    break;
                }
                case CHANGE_ACTION: {
                    currentPanels = modifyActionPanels;
                    break;
                }
                case CHANGE_PROJECT: {
                    currentPanels = modifyProjectPanels;
                    break;
                }
                default: {
                    currentPanels = this.modifyChoicePanels;
                    break;
                }
            }
            fireChangeEvent();
        }
    }

    /* Makes a copy of a recurrence. */
    private Recurrence copy(Recurrence r) {
        Recurrence copy = new Recurrence(r.getID()); // same ID
        copy.setProject(r.getProject());
        copy.setDescription(r.getDescription());
        copy.setTopic(r.getTopic());
        copy.setContext(r.getContext());
        copy.setTime(r.getTime());
        copy.setEnergy(r.getEnergy());
        copy.setPriority(r.getPriority());
        copy.setScheduleHours(r.getScheduleHours());
        copy.setScheduleMins(r.getScheduleMins());
        copy.setDurationHours(r.getDurationHours());
        copy.setDurationMins(r.getDurationMins());
        copy.setBasis(r.getBasis());
        copy.setStartDate(r.getStartDate());
        copy.setPeriod(r.getPeriod().clone());
        copy.setFrequency(r.getFrequency());
        copy.setAdvanceNbr(r.getAdvanceNbr());
        copy.setEndNbr(r.getEndNbr());
        copy.setEndDate(r.getEndDate());
        copy.setGenToDate(r.getGenToDate());
        copy.setSuccess(r.getSuccess());
        copy.setNotes(r.getNotes());
        return copy;
    }
}
