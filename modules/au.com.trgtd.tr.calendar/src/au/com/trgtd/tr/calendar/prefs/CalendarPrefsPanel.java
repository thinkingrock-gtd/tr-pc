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
package au.com.trgtd.tr.calendar.prefs;
 
import au.com.trgtd.tr.calendar.spi.CalendarSynchronizer;
import au.com.trgtd.tr.calendar.spi.CalendarSynchronizerOptions;
import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

final class CalendarPrefsPanel extends javax.swing.JPanel {

    private final CalendarOptionsPanelController controller;
    private final SynchronizerItem noneItem = new SynchronizerItem("", null);
    private final Lookup.Result<CalendarSynchronizer> lookup = Lookup.getDefault().lookupResult(CalendarSynchronizer.class);

    CalendarPrefsPanel(CalendarOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(CalendarPrefsPanel.class, key);
    }

    private void initForm() {
        initComponents();
        initFields();
        add(getView(), BorderLayout.CENTER);
    }

    private void initFields() {
        doasapDueTypeButtonGroup = new ButtonGroup();
        delegatedTypeButtonGroup = new ButtonGroup();
        inactiveTypeButtonGroup = new ButtonGroup();

        emptyPanel = new JPanel();

        requiredCheck = new JCheckBox(getMsg("CTL_Required"));
        requiredCheck.addActionListener((ActionEvent evt) -> {
            RequiredCheckBoxActionPerformed();
        });
        
        inactiveCheck = new JCheckBox(getMsg("inactive.action.date.checkbox"));
        inactiveCheck.addActionListener((ActionEvent evt) -> {
            inactiveCheckBoxActionPerformed();
        });
        inactiveAsEventRadio = new JRadioButton(getMsg("as.events"));
        inactiveAsToDoRadio = new JRadioButton(getMsg("as.todos"));
        inactiveTypeButtonGroup.add(inactiveAsEventRadio);
        inactiveTypeButtonGroup.add(inactiveAsToDoRadio);

        doasapDueCheck = new JCheckBox(getMsg("doasap.due.date.checkbox"));
        doasapDueCheck.addActionListener((ActionEvent evt) -> {
            doasapDueCheckBoxActionPerformed();
        });
        doasapDueAsEventRadio = new JRadioButton(getMsg("as.events"));
        doasapDueAsToDoRadio = new JRadioButton(getMsg("as.todos"));
        doasapDueTypeButtonGroup.add(doasapDueAsEventRadio);
        doasapDueTypeButtonGroup.add(doasapDueAsToDoRadio);
        
        doasapNoDueCheck = new JCheckBox(getMsg("doasap.no.due.date.checkbox"));
        doasapNoDueCheck.addActionListener((ActionEvent evt) -> {
            doasapNoDueCheckBoxActionPerformed();
        });
        
        delegatedCheck = new JCheckBox(getMsg("delegated.checkbox"));
        delegatedCheck.addActionListener((ActionEvent evt) -> {
            delegatedCheckBoxActionPerformed();
        });
        delegatedAsEventRadio = new JRadioButton(getMsg("as.events"));
        delegatedAsToDoRadio = new JRadioButton(getMsg("as.todos"));
        delegatedTypeButtonGroup.add(delegatedAsEventRadio);
        delegatedTypeButtonGroup.add(delegatedAsToDoRadio);
        
        delegatedNoDateCheck = new JCheckBox(getMsg("delegated.no.date.checkbox"));
        delegatedNoDateCheck.addActionListener((ActionEvent evt) -> {
            delegatedNoDateCheckBoxActionPerformed();
        });
        
        scheduledCheck = new JCheckBox(getMsg("scheduled.checkbox"));
        scheduledCheck.addActionListener((ActionEvent evt) -> {
            scheduledCheckBoxActionPerformed();
        });

        syncProjectsCheck = new JCheckBox(getMsg("sync.projects.checkbox"));
        syncProjectsCheck.addActionListener((ActionEvent evt) -> {
            syncProjectsCheckBoxActionPerformed();
        });
        syncProjectsSepCheck = new JCheckBox(getMsg("sync.projects.sep.checkbox"));
        syncProjectsSepCheck.addActionListener((ActionEvent evt) -> {
            syncProjectsSepCheckBoxActionPerformed();
        });
        syncFutureProjectsCheck = new JCheckBox(getMsg("sync.future.projects.checkbox"));
        syncFutureProjectsCheck.addActionListener((ActionEvent evt) -> {
            syncFutureProjectsCheckBoxActionPerformed();
        });
        syncFutureProjectsSepCheck = new JCheckBox(getMsg("sync.future.projects.sep.checkbox"));
        syncFutureProjectsSepCheck.addActionListener((ActionEvent evt) -> {
            syncFutureProjectsSepCheckBoxActionPerformed();
        });

        synchronizerLabel = new JLabel(getMsg("CalendarPanel.importerLabel.text"));

        synchronizerCombo = new TRComboBox<>();
        synchronizerCombo.addActionListener((ActionEvent evt) -> {
            synchronizerComboBoxActionPerformed();
        });

        synchronizerScrollPane = new JScrollPane();

        synchronizerPanel = new JPanel();
        synchronizerPanel.setLayout(new BorderLayout());
        synchronizerPanel.add(synchronizerScrollPane, BorderLayout.CENTER);
    }

    private JComponent getView() {
        JPanel panel = new JPanel(new MigLayout("", "0[]2[]2[grow]0", "0[]2[]2[]2[]2[]2[]2[]2[]2[]4[]2[grow]0"));

        panel.add(requiredCheck, "align left, span, wrap");

        panel.add(inactiveCheck,        "align left");
        panel.add(inactiveAsEventRadio, "align left");
        panel.add(inactiveAsToDoRadio,  "align left, wrap");

        panel.add(doasapDueCheck,       "align left");
        panel.add(doasapDueAsEventRadio,"align left");
        panel.add(doasapDueAsToDoRadio, "align left, wrap");

        panel.add(doasapNoDueCheck,     "align left, spanx, wrap");

        panel.add(delegatedCheck,       "align left");
        panel.add(delegatedAsEventRadio,"align left");
        panel.add(delegatedAsToDoRadio, "align left, wrap");

        panel.add(delegatedNoDateCheck, "align left, spanx, wrap");

        panel.add(scheduledCheck,       "align left, spanx, wrap");

        panel.add(syncProjectsCheck,    "align left, spanx, split 2");
        panel.add(syncProjectsSepCheck, "align left, wrap");

        panel.add(syncFutureProjectsCheck, "align left, spanx, split 2");
        panel.add(syncFutureProjectsSepCheck, "align left, wrap");

        panel.add(synchronizerLabel, "align left, spanx, split 2");
        panel.add(synchronizerCombo, "align left, wrap");

        panel.add(synchronizerPanel, "align left, spanx 100, growx 100, growy 100");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    private void enableDisableFields() {
        boolean b = requiredCheck.isSelected();
        inactiveCheck.setEnabled(b);
        inactiveAsEventRadio.setEnabled(b && inactiveCheck.isSelected());
        inactiveAsToDoRadio.setEnabled(b && inactiveCheck.isSelected());
        doasapDueCheck.setEnabled(b);
        doasapDueAsEventRadio.setEnabled(b && doasapDueCheck.isSelected());
        doasapDueAsToDoRadio.setEnabled(b && doasapDueCheck.isSelected());
        doasapNoDueCheck.setEnabled(b);
        delegatedCheck.setEnabled(b);
        delegatedAsEventRadio.setEnabled(b && delegatedCheck.isSelected());
        delegatedAsToDoRadio.setEnabled(b && delegatedCheck.isSelected());
        delegatedNoDateCheck.setEnabled(b);
        scheduledCheck.setEnabled(b);
        syncProjectsCheck.setEnabled(b);
        syncProjectsSepCheck.setEnabled(b && syncProjectsCheck.isSelected());
        syncFutureProjectsCheck.setEnabled(b);
        syncFutureProjectsSepCheck.setEnabled(b && syncFutureProjectsCheck.isSelected());
        synchronizerLabel.setEnabled(b);
        synchronizerCombo.setEnabled(b);
        if (getSelectedSynchronizer() != null) {
            getSelectedSynchronizer().getOptions().enable(b);
        }
    }

    private CalendarSynchronizer getSelectedSynchronizer() {
        SynchronizerItem synchronizerItem = (SynchronizerItem) synchronizerCombo.getSelectedItem();
        if (synchronizerItem == null) {
            return null;
        }
        return synchronizerItem.synchronizer;
    }

    void load() {
        requiredCheck.setSelected(CalendarPrefs.isICalendarRequired());

        inactiveCheck.setSelected(CalendarPrefs.isSyncInactive());
        if (CalendarPrefs.isInactiveAsTodo()) {
            inactiveAsToDoRadio.setSelected(true);
        } else {
            inactiveAsEventRadio.setSelected(true);
        }

        doasapDueCheck.setSelected(CalendarPrefs.isSyncDoasapDueDate());
        if (CalendarPrefs.isDoasapDueAsTodo()) {
            doasapDueAsToDoRadio.setSelected(true);
        } else {
            doasapDueAsEventRadio.setSelected(true);
        }

        doasapNoDueCheck.setSelected(CalendarPrefs.isSyncDoasapNoDueDate());

        delegatedCheck.setSelected(CalendarPrefs.isSyncDelegated());
        if (CalendarPrefs.isDelegatedAsTodo()) {
            delegatedAsToDoRadio.setSelected(true);
        } else {
            delegatedAsEventRadio.setSelected(true);
        }
        delegatedNoDateCheck.setSelected(CalendarPrefs.isSyncDelegatedNoDate());

        scheduledCheck.setSelected(CalendarPrefs.isSyncScheduled());

        syncProjectsCheck.setSelected(CalendarPrefs.isSyncProjects());
        syncProjectsSepCheck.setSelected(CalendarPrefs.isSyncProjectsSeparate());
        syncFutureProjectsCheck.setSelected(CalendarPrefs.isSyncFutureProjects());
        syncFutureProjectsSepCheck.setSelected(CalendarPrefs.isSyncFutureProjectsSeparate());

        SynchronizerItem selected = null;
        List<SynchronizerItem> synchronizers = new Vector<>();
        synchronizers.add(noneItem);
        for (CalendarSynchronizer synchronizer : lookup.allInstances()) {
            SynchronizerItem item = new SynchronizerItem(synchronizer.getID(), synchronizer);
            synchronizers.add(item);
            if (synchronizer.getID().equals(CalendarPrefs.getSynchronizerID())) {
                selected = item;
            }
        }
        synchronizerCombo.setModel(new DefaultComboBoxModel<>(synchronizers.toArray(SynchronizerItem[]::new)));
        if (selected == null) {
            loadSynchronizerOptions(null);
        } else {
            synchronizerCombo.setSelectedItem(selected);
            loadSynchronizerOptions(selected.synchronizer);
        }

        enableDisableFields();
    }

    void loadSynchronizerOptions(CalendarSynchronizer synchronizer) {
        if (synchronizer == null) {
            synchronizerScrollPane.setViewportView(emptyPanel);
        } else {
            CalendarSynchronizerOptions syncOptions = synchronizer.getOptions();
            if (syncOptions == null) {
                synchronizerScrollPane.setViewportView(emptyPanel);
            } else {
                syncOptions.load();
                synchronizerScrollPane.setViewportView(syncOptions.getPanel());
            }
        }
        synchronizerPanel.revalidate();
        synchronizerPanel.repaint();
    }

    void store() {
        CalendarPrefs.setICalendarRequired(requiredCheck.isSelected());
        CalendarPrefs.setSyncInactive(inactiveCheck.isSelected());
        CalendarPrefs.setInactiveAsTodo(inactiveAsToDoRadio.isSelected());
        CalendarPrefs.setSyncDoASAPWithDueDate(doasapDueCheck.isSelected());
        CalendarPrefs.setDoasapDueAsTodo(doasapDueAsToDoRadio.isSelected());
        CalendarPrefs.setSyncDoASAPWithNoDueDate(doasapNoDueCheck.isSelected());
        CalendarPrefs.setSyncDelegated(delegatedCheck.isSelected());
        CalendarPrefs.setDelegatedAsTodo(delegatedAsToDoRadio.isSelected());
        CalendarPrefs.setSyncDelegatedNoDate(delegatedNoDateCheck.isSelected());
        CalendarPrefs.setSyncScheduled(scheduledCheck.isSelected());
        CalendarPrefs.setSyncProjects(syncProjectsCheck.isSelected());
        CalendarPrefs.setSyncProjectsSeparate(syncProjectsSepCheck.isSelected());
        CalendarPrefs.setSyncFutureProjects(syncFutureProjectsCheck.isSelected());
        CalendarPrefs.setSyncFutureProjectsSeparate(syncFutureProjectsSepCheck.isSelected());
        CalendarSynchronizer synchronizer = getSelectedSynchronizer();
        if (synchronizer == null) {
            CalendarPrefs.setSynchronizerID("");
        } else {
            CalendarPrefs.setSynchronizerID(synchronizer.getID());
            synchronizer.getOptions().store();
        }
    }

    boolean valid() {
        if (!requiredCheck.isSelected()) {
            return true;
        }
        CalendarSynchronizer synchronizer = getSelectedSynchronizer();
        if (synchronizer == null) {
            return false;
        }
        return synchronizer.getOptions().valid();
    }

    private final class SynchronizerItem {

        final String id;
        final CalendarSynchronizer synchronizer;

        SynchronizerItem(String id, CalendarSynchronizer synchronizer) {
            this.id = id;
            this.synchronizer = synchronizer;
        }

        @Override
        public String toString() {
            return synchronizer == null ? "None" : synchronizer.getName();
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof SynchronizerItem item) {
                return id.equals(item.id);
            } else {
                return false;
            }
        }
    }

    private void RequiredCheckBoxActionPerformed() {
        enableDisableFields();
        controller.changed();
    }

    private void delegatedCheckBoxActionPerformed() {
        controller.changed();
        delegatedAsEventRadio.setEnabled(delegatedCheck.isSelected());
        delegatedAsToDoRadio.setEnabled(delegatedCheck.isSelected());
    }

    private void doasapDueCheckBoxActionPerformed() {
        controller.changed();
        doasapDueAsEventRadio.setEnabled(doasapDueCheck.isSelected());
        doasapDueAsToDoRadio.setEnabled(doasapDueCheck.isSelected());
    }

    private void doasapNoDueCheckBoxActionPerformed() {
        controller.changed();
    }

    private void scheduledCheckBoxActionPerformed() {
        controller.changed();
    }

    private void synchronizerComboBoxActionPerformed() {
        SynchronizerItem synchronizerItem = (SynchronizerItem) synchronizerCombo.getSelectedItem();
        if (synchronizerItem == null || synchronizerItem.equals(noneItem)) {
            loadSynchronizerOptions(null);
        } else {
            loadSynchronizerOptions(synchronizerItem.synchronizer);
        }
        controller.changed();
    }

    private void delegatedNoDateCheckBoxActionPerformed() {
        controller.changed();
    }

    private void inactiveCheckBoxActionPerformed() {
        controller.changed();
        inactiveAsEventRadio.setEnabled(inactiveCheck.isSelected());
        inactiveAsToDoRadio.setEnabled(inactiveCheck.isSelected());
    }

    private void syncProjectsCheckBoxActionPerformed() {
        enableDisableFields();
        controller.changed();
    }
    private void syncProjectsSepCheckBoxActionPerformed() {
        controller.changed();
    }
    private void syncFutureProjectsCheckBoxActionPerformed() {
        enableDisableFields();
        controller.changed();
    }
    private void syncFutureProjectsSepCheckBoxActionPerformed() {
        controller.changed();
    }
    
    private JPanel emptyPanel;
    private JRadioButton delegatedAsEventRadio;
    private JRadioButton delegatedAsToDoRadio;
    private JCheckBox delegatedCheck;
    private JCheckBox delegatedNoDateCheck;
    private ButtonGroup delegatedTypeButtonGroup;
    private JRadioButton doasapDueAsEventRadio;
    private JRadioButton doasapDueAsToDoRadio;
    private JCheckBox doasapDueCheck;
    private ButtonGroup doasapDueTypeButtonGroup;
    private JCheckBox doasapNoDueCheck;
    private JRadioButton inactiveAsEventRadio;
    private JRadioButton inactiveAsToDoRadio;
    private JCheckBox inactiveCheck;
    private ButtonGroup inactiveTypeButtonGroup;
    private JCheckBox requiredCheck;
    private JCheckBox scheduledCheck;

    private JCheckBox syncProjectsCheck;
    private JCheckBox syncProjectsSepCheck;
    private JCheckBox syncFutureProjectsCheck;
    private JCheckBox syncFutureProjectsSepCheck;

    private JComboBox<SynchronizerItem> synchronizerCombo;
    private JLabel synchronizerLabel;
    private JPanel synchronizerPanel;
    private JScrollPane synchronizerScrollPane;


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
