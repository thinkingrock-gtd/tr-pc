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
