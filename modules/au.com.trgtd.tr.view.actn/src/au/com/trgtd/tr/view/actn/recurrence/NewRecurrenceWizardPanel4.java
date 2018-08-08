package au.com.trgtd.tr.view.actn.recurrence;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
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

public class NewRecurrenceWizardPanel4 implements WizardDescriptor.Panel, 
        PropertyChangeListener {

    private final Recurrence recurrence;
    private final Date actionScheduledDate;
    private NewRecurrenceVisualPanel4 panel;
    
    public NewRecurrenceWizardPanel4(Recurrence recurrence, Action action) {
        this.recurrence = recurrence;
        this.actionScheduledDate = ((ActionStateScheduled) action.getState()).getDate();        
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new NewRecurrenceVisualPanel4(recurrence, actionScheduledDate);
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
        ((NewRecurrenceVisualPanel4) panel).initPanel();
    }

    public void storeSettings(Object settings) {
    }

    public void propertyChange(PropertyChangeEvent evt) {
        fireChangeEvent();
    }
}

