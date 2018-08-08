package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.beans.PropertyChangeEvent;
import java.awt.Component;
import java.awt.Dialog;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import tr.model.action.Action;
import tr.model.action.ActionStateScheduled;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;

public class ModifyRecurrenceWizardPanelModRecur2 implements WizardDescriptor.Panel,
        PropertyChangeListener {

    private final Recurrence modRecurrenceSubsequent;
    private final Recurrence modRecurrenceRegular;
    private final Date recurrenceStartDate;
    private final Date actionScheduledDate;
    private ModifyRecurrenceVisualPanelModRecur2 panel;

    public ModifyRecurrenceWizardPanelModRecur2(
            Recurrence modRecurrenceSubsequent,
            Recurrence modRecurrenceRegular,
            Action action) {
        this.modRecurrenceSubsequent = modRecurrenceSubsequent;
        this.modRecurrenceRegular = modRecurrenceRegular;
        this.actionScheduledDate = ((ActionStateScheduled) action.getState()).getDate();
        this.recurrenceStartDate = modRecurrenceRegular.getStartDate();
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new ModifyRecurrenceVisualPanelModRecur2(
                    modRecurrenceSubsequent,
                    modRecurrenceRegular,
                    actionScheduledDate,
                    recurrenceStartDate);
            panel.addPropertyChangeListener(this);
        }
        return panel;
    }

    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    public boolean isValid() {
        return panel != null && panel.isValidForm();
    }
    
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);

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
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    public void readSettings(Object settings) {
        WizardDescriptor wd = (WizardDescriptor) settings;
        Basis oldBasis = (Basis) wd.getProperty("mod.old.basis");
        Basis newBasis = (Basis) wd.getProperty("mod.new.basis");
        Dialog dialog = (Dialog) wd.getProperty("dialog");
        assert(dialog != null);        
        panel.initPanel(oldBasis, newBasis, dialog);
    }

    public void storeSettings(Object settings) {
        WizardDescriptor wd = (WizardDescriptor) settings;
        if (panel == null) {
            wd.putProperty("mod.recurrence.subsequent", null);
            wd.putProperty("mod.recurrence.regular", null);
        } else {
            wd.putProperty("mod.recurrence.subsequent", modRecurrenceSubsequent);
            wd.putProperty("mod.recurrence.regular", modRecurrenceRegular);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        fireChangeEvent();
    }
}

