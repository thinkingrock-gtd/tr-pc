package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import tr.model.action.Recurrence;

public class ModifyRecurrenceWizardPanelModAction1 implements WizardDescriptor.Panel {

    private final Recurrence recurrence;
    private ModifyRecurrenceVisualPanelModAction1 panel;

    public ModifyRecurrenceWizardPanelModAction1(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new ModifyRecurrenceVisualPanelModAction1(recurrence);
        }
        return panel;
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

    public void readSettings(Object settings) {
        if (panel == null) {
            return;
        }
        panel.initPanel();
    }

    public void storeSettings(Object settings) {
        WizardDescriptor wd = (WizardDescriptor) settings;
        wd.putProperty("mod.action.recurrence", recurrence);
    }
}
