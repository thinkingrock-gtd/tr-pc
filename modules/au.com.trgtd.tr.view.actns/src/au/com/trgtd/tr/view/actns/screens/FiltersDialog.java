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
package au.com.trgtd.tr.view.actns.screens;

import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.actns.screens.filters.FilterCriterion;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDate;
import au.com.trgtd.tr.view.actns.screens.filters.FilterFactory;
import au.com.trgtd.tr.view.filters.FilterCombo;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;

/**
 * Review actions screen filters dialog.
 *
 * @author Jeremy Moore
 */
public class FiltersDialog extends JDialog {
    
    /** Constructs a new instance. */
    public FiltersDialog(ActionsScreen screen) {
        super(WindowManager.getDefault().getMainWindow(), true);
        this.screen = screen;
        initComponents();
        scrollPane.setViewportView(getPanel());
    }

    private String getText(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private JPanel getPanel() {

        filtersList = new Vector<>();

        filtersPanel = new JPanel(new MigLayout("", "4[]4[]4[]4[]4[]4", "0[]0"));
        filtersPanel.setBackground(Color.white);

        filtersPanel.add(new HeadLabel("FiltersDialog.column.name"), "align center");
        filtersPanel.add(new HeadLabel("FiltersDialog.column.used"), "align center");
        filtersPanel.add(new HeadLabel("FiltersDialog.column.visible"), "align center");
        filtersPanel.add(new HeadLabel("FiltersDialog.column.values"), "align center");
        filtersPanel.add(new HeadLabel("FiltersDialog.column.nulls"), "align left, wrap");

        Map<Byte, ActionsFilter> actionsFilterMap = new HashMap<>();
        for (ActionsFilter af : screen.getFilters()) {
            actionsFilterMap.put(af.getIndex(), af);
        }

        for (int i = 0; i < FilterFactory.INDICES.length; i++) {
            byte index = FilterFactory.INDICES[i];
            if (!actionsFilterMap.containsKey(index)) {
                ActionsFilter af = FilterFactory.instance.createFilter(index);
                actionsFilterMap.put(af.getIndex(), af);
                screen.getFilters().add(af);
            }


            FilterFields ff = new FilterFields(actionsFilterMap.get(index));
            filtersList.add(ff);

            filtersPanel.add(ff.label, "align left");
            filtersPanel.add(ff.useCheckBox, "align left");
            filtersPanel.add(ff.seeCheckBox, "align left");
            if (ff.nullsCheckBox == null) {
                filtersPanel.add(ff.valFilterCombo.getJComboBox(), "align left, sgx g1, wrap");
            } else {
                filtersPanel.add(ff.valFilterCombo.getJComboBox(), "align left, sgx g1");
                filtersPanel.add(ff.nullsCheckBox, "align left, wrap");
            }
        }

        return filtersPanel;
    }

    public int showDialog() {
        ok = false;
        setVisible(true);
        return ok ? JOptionPane.OK_OPTION : JOptionPane.CANCEL_OPTION;
    }

    private static class HeadLabel extends JLabel {
        public HeadLabel(String key) {
            super(NbBundle.getMessage(FiltersDialog.class, key));
            setFont(getFont().deriveFont(Font.BOLD));
        }
    }

    private static class FilterFields {
        public final ActionsFilter filter;
        public final JLabel label;
        public final JCheckBox useCheckBox;
        public final JCheckBox seeCheckBox;
        public final JCheckBox nullsCheckBox;
        public final FilterCombo valFilterCombo;
        private final ActionsFilter tempFilter;

        public FilterFields(ActionsFilter filter) {
            this.filter = filter;
            label = new JLabel(filter.getLabel());
            label.setBackground(Color.white);

            useCheckBox = new JCheckBox();
            useCheckBox.setBackground(Color.white);
            useCheckBox.setSelected(filter.isUsed());
            useCheckBox.addActionListener((ActionEvent actionEvent) -> {
                enableDisableComponents(useCheckBox.isSelected());
            });

            seeCheckBox = new JCheckBox();
            seeCheckBox.setBackground(Color.white);
            seeCheckBox.setSelected(filter.isShown());

            tempFilter = FilterFactory.instance.createFilter(filter.getIndex());
            tempFilter.setSerialValues(filter.getSerialValues());
            valFilterCombo = tempFilter.getFilterCombo();

            if (filter instanceof FilterDate dateFilter) {
                nullsCheckBox = new JCheckBox();
                nullsCheckBox.setBackground(Color.white);
                nullsCheckBox.setSelected(dateFilter.isExcludeNulls());
            } else if (filter instanceof FilterCriterion criterionFilter) {
                nullsCheckBox = new JCheckBox();
                nullsCheckBox.setBackground(Color.white);
                nullsCheckBox.setSelected(criterionFilter.isExcludeNulls());
            } else {
                nullsCheckBox = null;
            }
            enableDisableComponents(useCheckBox.isSelected());
        }

        private void enableDisableComponents(boolean used) {
            if (!used) {
                seeCheckBox.setSelected(false);
                valFilterCombo.getJComboBox().setSelectedItem(null);
                if (nullsCheckBox != null) {
                    nullsCheckBox.setSelected(false);
                }
            }
            seeCheckBox.setEnabled(used);
            valFilterCombo.getJComboBox().setEnabled(used);
            if (nullsCheckBox != null) {
                nullsCheckBox.setEnabled(used);
            }
        }

        public void commit() {
            filter.setUsed(useCheckBox.isSelected());
            filter.setShown(seeCheckBox.isSelected());
            filter.setSerialValues(tempFilter.getSerialValues());
            if (filter instanceof FilterDate filterDate) {
                filterDate.setExcludeNulls(nullsCheckBox.isSelected());
            } else if (filter instanceof FilterCriterion filterCriterion) {
                filterCriterion.setExcludeNulls(nullsCheckBox.isSelected());
            }
            filter.getFilterCombo().fireValueChange();
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bottomPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        panel = getPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(getText("FiltersDialog.title")); // NOI18N
        setModal(true);

        bottomPanel.setFocusable(false);
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        cancelButton.setText(NbBundle.getMessage(FiltersDialog.class, "FiltersDialog.cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelButton);

        okButton.setText(getText("FiltersDialog.ok")); // NOI18N
        okButton.setFocusCycleRoot(true);
        okButton.setSelected(true);
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });
        bottomPanel.add(okButton);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        scrollPane.setViewportView(panel);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelActionPerformed
  
    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        for (FilterFields ff : filtersList) {
            ff.commit();
        }
        ok = true;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_okActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
   
    private final ActionsScreen screen;
    private JPanel filtersPanel;
    private boolean ok;
    private List<FilterFields> filtersList;
}
