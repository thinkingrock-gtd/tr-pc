package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class ModifyRecurrenceWizardPanelRemRecur1 implements WizardDescriptor.Panel { 
    
    public ModifyRecurrenceWizardPanelRemRecur1() {
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new ModifyRecurrenceVisualPanelRemRecur1();
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
        panel.initPanel();
    }

    public void storeSettings(Object settings) {
    }

    private ModifyRecurrenceVisualPanelRemRecur1 panel;
}

