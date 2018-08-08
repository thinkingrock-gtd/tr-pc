package au.com.trgtd.tr.view.actn.recurrence;

import java.awt.Dialog;
import java.text.MessageFormat;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.action.Action;
import tr.model.action.ActionStateScheduled;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;

/**
 * Create recurrence wizard runner.
 * 
 * @author Jeremy Moore
 */
public class NewRecurrenceWizard {

    public NewRecurrenceWizard() {
    }

    public void start(Action action) {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        if (action == null) {
            return;
        }
        if (!action.isStateScheduled()) {
            return;
        }

        Recurrence recurrence = new Recurrence(data.getNextID(), action);

        WizardDescriptor.Iterator iterator = new NewRecurrenceWizardIterator(recurrence, action);
        WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
        // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
        // {1} will be replaced by WizardDescriptor.Iterator.name()
//      wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(org.openide.util.NbBundle.getMessage(NewRecurrenceWizard.class, "create.recurrence"));
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        wizardDescriptor.putProperty("dialog", dialog); // need owner for period dialogs                

        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (cancelled) {
            return;
        }

        // new recurrence wizard has finished successfully
        ActionStateScheduled state = (ActionStateScheduled) action.getState();
        state.setRecurrence(recurrence);
        if (recurrence.getBasis() == Basis.START_DATE) {
            recurrence.generateFromStartDate(action);
        }
        
//        dialog.setVisible(false);
    }
}
