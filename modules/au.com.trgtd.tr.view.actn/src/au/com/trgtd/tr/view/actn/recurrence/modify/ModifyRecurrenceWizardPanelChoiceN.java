package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class ModifyRecurrenceWizardPanelChoiceN implements WizardDescriptor.Panel {

    public ModifyRecurrenceWizardPanelChoiceN() {
    }
    private Component component;

    /** Gets the visual component for the panel. */
    public Component getComponent() {
        if (component == null) {
            component = new ModifyRecurrenceVisualPanelChoiceN();
        }
        return component;
    }

    /** Gets the help context for the panel. */
    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    /** Determines whether the panel values are valid. */
    public boolean isValid() {
        return true;
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }

    public void readSettings(Object settings) {
    }

    public void storeSettings(Object settings) {
    }
}

