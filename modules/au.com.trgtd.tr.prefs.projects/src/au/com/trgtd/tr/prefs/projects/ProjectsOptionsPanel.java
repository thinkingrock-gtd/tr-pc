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
package au.com.trgtd.tr.prefs.projects;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class ProjectsOptionsPanel extends JPanel {

    private final ProjectsOptionsPanelController controller;

    ProjectsOptionsPanel(ProjectsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        autoSeqCheckBox = new JCheckBox(getMsg("Pref_AutoSequence"));
        autoSeqCheckBox.addActionListener((ActionEvent evt) -> {
            boolean b = autoSeqCheckBox.isSelected();
            autoSeqIncludeLabel.setEnabled(b);
            autoSeqSubprojectsCheckBox.setEnabled(b);
            autoSeqScheduledCheckBox.setEnabled(b);
            autoSeqDelegatedCheckBox.setEnabled(b);
            controller.changed();
        });

        autoSeqIncludeLabel = new JLabel(getMsg("Pref_AutoSeqInclude"));
        autoSeqSubprojectsCheckBox = new JCheckBox(getMsg("Pref_AutoSeqSubprojects"));
        autoSeqSubprojectsCheckBox.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });
        autoSeqScheduledCheckBox = new JCheckBox(getMsg("Pref_AutoSeqScheduled"));
        autoSeqScheduledCheckBox.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });
        autoSeqDelegatedCheckBox = new JCheckBox(getMsg("Pref_AutoSeqDelegated"));
        autoSeqDelegatedCheckBox.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });

        startDateCheckBox = new JCheckBox(getMsg("Pref_StartDate"));
        startDateCheckBox.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });
        dueDateCheckBox = new JCheckBox(getMsg("Pref_DueDate"));
        dueDateCheckBox.addActionListener((ActionEvent evt) -> {
            defaultActionDueDateCheckBox.setEnabled(dueDateCheckBox.isSelected());
            controller.changed();
        });
        defaultActionDueDateCheckBox = new JCheckBox(getMsg("Pref_DefaultActionDueDate"));
        defaultActionDueDateCheckBox.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });
        createDateCheckBox = new JCheckBox(getMsg("CTL_editCreateDate"));
        createDateCheckBox.addActionListener((ActionEvent evt) -> {
            controller.changed();
        });

        actionStatesLabel = new JLabel(getMsg("DefaultActionState"));
        states = new ArrayList<>();
        states.add(ProjectsPrefs.ActionState.INACTIVE);
        states.add(ProjectsPrefs.ActionState.DOASAP);
        states.add(ProjectsPrefs.ActionState.SCHEDULED);
        states.add(ProjectsPrefs.ActionState.DELEGATED);
        actionStatesCombo = new TRComboBox<>(new DefaultComboBoxModel<>(states.toArray(ProjectsPrefs.ActionState[]::new)));
        actionStatesCombo.setMaximumRowCount(states.size());
        printFormatLabel = new JLabel(getMsg("print.format.Label"));
        printFormatCombo = new JComboBox<>(new PageSizeChoice[] { PageSizeChoice.Prompt, PageSizeChoice.A4, PageSizeChoice.Letter });

        JPanel panel = new JPanel(new MigLayout("", "0[]2[grow]0", "0[]2[]2[]2[]2[]2[]0"));

        panel.add(autoSeqCheckBox, "align left, wrap");
        panel.add(autoSeqIncludeLabel, "align left, gapleft 28, span, split 4");
        panel.add(autoSeqSubprojectsCheckBox, "align left");
        panel.add(autoSeqScheduledCheckBox, "align left");
        panel.add(autoSeqDelegatedCheckBox, "align left, wrap");
        panel.add(startDateCheckBox, "align left, wrap");
//      panel.add(dueDateCheckBox, "align left, wrap");
        panel.add(dueDateCheckBox, "align left");
        panel.add(defaultActionDueDateCheckBox, "align left, wrap");
        panel.add(createDateCheckBox, "align left, wrap");
        panel.add(actionStatesLabel, "align left, gapleft 8, span, split 2");
        panel.add(actionStatesCombo, "align left, wrap");
        panel.add(printFormatLabel, "align left, gapleft 8, span, split 2");
        panel.add(printFormatCombo, "align left, wrap");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        return scrollPane;
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    void load() {
        autoSeqCheckBox.setSelected(ProjectsPrefs.isSequencing());
        autoSeqSubprojectsCheckBox.setSelected(ProjectsPrefs.getAutoSeqIncludeSubprojects());
        autoSeqScheduledCheckBox.setSelected(ProjectsPrefs.getAutoSeqIncludeScheduled());
        autoSeqDelegatedCheckBox.setSelected(ProjectsPrefs.getAutoSeqIncludeDelegated());
        startDateCheckBox.setSelected(ProjectsPrefs.isIncludeStartDate());
        dueDateCheckBox.setSelected(ProjectsPrefs.isIncludeDueDate());
        defaultActionDueDateCheckBox.setSelected(ProjectsPrefs.isDefaultActionDueDate());
        actionStatesCombo.setSelectedItem(ProjectsPrefs.getNewActionState());
        createDateCheckBox.setSelected(ProjectsPrefs.isEditCreateDate());
        printFormatCombo.setSelectedItem(PageSizeChoice.valueOf(ProjectsPrefs.getPageSizeChoiceName()));
    }

    void store() {
        ProjectsPrefs.setSequencing(autoSeqCheckBox.isSelected());
        ProjectsPrefs.setAutoSeqIncludeSubprojects(autoSeqSubprojectsCheckBox.isSelected());
        ProjectsPrefs.setAutoSeqIncludeScheduled(autoSeqScheduledCheckBox.isSelected());
        ProjectsPrefs.setAutoSeqIncludeDelegated(autoSeqDelegatedCheckBox.isSelected());
        ProjectsPrefs.setIncludeStartDate(startDateCheckBox.isSelected());
        ProjectsPrefs.setIncludeDueDate(dueDateCheckBox.isSelected());
        ProjectsPrefs.setDefaultActionDueDate(defaultActionDueDateCheckBox.isSelected());
        ProjectsPrefs.setNewActionState((ProjectsPrefs.ActionState) actionStatesCombo.getSelectedItem());
        ProjectsPrefs.setEditCreateDate(createDateCheckBox.isSelected());
        ProjectsPrefs.setPrintFormat(((Enum<PageSizeChoice>)printFormatCombo.getSelectedItem()).name());
    }

    boolean valid() {
        return true;
    }
    
    private List<ProjectsPrefs.ActionState> states;
    private JCheckBox autoSeqCheckBox;
    private JLabel autoSeqIncludeLabel;
    private JCheckBox autoSeqSubprojectsCheckBox;
    private JCheckBox autoSeqScheduledCheckBox;
    private JCheckBox autoSeqDelegatedCheckBox;
    private JCheckBox dueDateCheckBox;
    private JCheckBox defaultActionDueDateCheckBox;
    private JCheckBox createDateCheckBox;
    private JCheckBox startDateCheckBox;
    private JLabel actionStatesLabel;
    private JComboBox<ProjectsPrefs.ActionState> actionStatesCombo;
    private JLabel printFormatLabel;
    private JComboBox<PageSizeChoice> printFormatCombo;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
