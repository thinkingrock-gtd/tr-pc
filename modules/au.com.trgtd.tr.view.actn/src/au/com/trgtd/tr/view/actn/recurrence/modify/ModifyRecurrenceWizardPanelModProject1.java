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
