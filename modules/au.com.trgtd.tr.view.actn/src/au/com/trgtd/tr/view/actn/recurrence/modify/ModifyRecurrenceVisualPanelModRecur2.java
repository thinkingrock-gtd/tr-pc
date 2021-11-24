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

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.JPanel;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;

public final class ModifyRecurrenceVisualPanelModRecur2 extends JPanel
        implements PropertyChangeListener {

    private final Recurrence recurrenceSubsequent;  // for subsequent changes
    private final Recurrence recurrenceRegular;     // for regualr changes
    private final Date actionScheduledDate;
    private final Date recurrenceStartDate;
    private final RecurrenceRegularPanelModify regularPanelBasisNotChanged;
    private final RecurrenceRegularPanelModify regularPanelBasisChanged;
    private final RecurrenceSubsequentPanelModify subsequentPanelBasisNotChanged;
    private final RecurrenceSubsequentPanelModify subsequentPanelBasisChanged;
    private Basis oldBasis;
    private Basis newBasis;
    private Dialog dialog;
    private long counter;

    public ModifyRecurrenceVisualPanelModRecur2(
            Recurrence recurrenceSubsequent,
            Recurrence recurrenceRegular,
            Date actionScheduledDate,
            Date recurrenceStartDate) {
        this.recurrenceSubsequent = recurrenceSubsequent;
        this.recurrenceRegular = recurrenceRegular;
        this.actionScheduledDate = actionScheduledDate;
        this.recurrenceStartDate = recurrenceStartDate;
        this.subsequentPanelBasisNotChanged = new RecurrenceSubsequentPanelModify();
        this.subsequentPanelBasisNotChanged.setModel(recurrenceSubsequent, actionScheduledDate, false);
        this.subsequentPanelBasisNotChanged.addPropertyChangeListener(this);
        this.subsequentPanelBasisChanged = new RecurrenceSubsequentPanelModify();
        this.subsequentPanelBasisChanged.setModel(recurrenceSubsequent, actionScheduledDate, true);
        this.subsequentPanelBasisChanged.addPropertyChangeListener(this);
        this.regularPanelBasisNotChanged = new RecurrenceRegularPanelModify();
        this.regularPanelBasisNotChanged.setModel(recurrenceRegular, actionScheduledDate, recurrenceStartDate, false);
        this.regularPanelBasisNotChanged.addPropertyChangeListener(this);
        this.regularPanelBasisChanged = new RecurrenceRegularPanelModify();
        this.regularPanelBasisChanged.setModel(recurrenceRegular, actionScheduledDate, recurrenceStartDate, true);
        this.regularPanelBasisChanged.addPropertyChangeListener(this);
        initComponents();
    }

    @Override
    public String getName() {
        if (newBasis == Basis.DONE_DATE) {
            return org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModRecur2.class, "change.subsequent.recurrence");
        }
        if (newBasis == Basis.START_DATE) {
            return org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModRecur2.class, "change.regular.recurrence");
        }
        return org.openide.util.NbBundle.getMessage(ModifyRecurrenceVisualPanelModRecur2.class, "change.recurrence");
    }

    public boolean isValidForm() {
        if (newBasis == Basis.DONE_DATE) {
            if (newBasis == oldBasis) {
                return subsequentPanelBasisNotChanged.isValidForm();
            } else {
                return subsequentPanelBasisChanged.isValidForm();                
            }
        }
        if (newBasis == Basis.START_DATE) {
            if (newBasis == oldBasis) {
                return regularPanelBasisNotChanged.isValidForm();
            } else {
               return regularPanelBasisChanged.isValidForm();
            }
        }
        return false;
    }

    public void initPanel(Basis oldBasis, Basis newBasis, Dialog dialog) {
        this.removeAll();
        this.oldBasis = oldBasis;
        this.newBasis = newBasis;
        this.dialog = dialog;
        if (newBasis == Basis.DONE_DATE) {
            if (newBasis == oldBasis) {
                add(subsequentPanelBasisNotChanged, BorderLayout.CENTER);
            } else {
                add(subsequentPanelBasisChanged, BorderLayout.CENTER);                
            }
        } else if (newBasis == Basis.START_DATE) {
            if (newBasis == oldBasis) {
                regularPanelBasisNotChanged.setDialog(dialog);
                add(regularPanelBasisNotChanged, BorderLayout.CENTER);
            } else {
                regularPanelBasisChanged.setDialog(dialog);
                add(regularPanelBasisChanged, BorderLayout.CENTER);
            }
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        putClientProperty("state.change", Long.valueOf(++counter));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
