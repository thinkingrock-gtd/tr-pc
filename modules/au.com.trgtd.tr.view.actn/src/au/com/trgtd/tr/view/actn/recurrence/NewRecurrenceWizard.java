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
        Data data = DataLookup.instance().lookup(Data.class);
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

        WizardDescriptor.Iterator<WizardDescriptor> iterator = new NewRecurrenceWizardIterator(recurrence, action);
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
