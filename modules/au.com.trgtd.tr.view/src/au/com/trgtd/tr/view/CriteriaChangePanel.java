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
package au.com.trgtd.tr.view;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.Criteria;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;

/**
 * Criteria change action panel.
 * 
 * @author Jeremy Moore
 */
public class CriteriaChangePanel extends JPanel {

    /** Creates new form TickleDatePanel */
    public CriteriaChangePanel() {
        initView();
    }

    private void initView() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        criteriaLabel = new JLabel(getMsg("CTL_Criteria"));
        criteriaCombo = new TRComboBox<>(Criteria.values());
        criteriaCombo.setSelectedItem(Criteria.Priority);
        criteriaCombo.addActionListener((ActionEvent evt) -> {
            Criteria c = (Criteria)criteriaCombo.getSelectedItem();
            if (c == Criteria.Priority) {
                valueCombo.setModel(priorities);
            } else if (c == Criteria.Time) {
                valueCombo.setModel(times);
            } else if (c == Criteria.Energy) {
                valueCombo.setModel(energies);
            }
        });

        valueLabel = new JLabel(getMsg("CTL_Value"));
        priorities = new DefaultComboBoxModel<>(data.getPriorityCriterion().values.list());
        times = new DefaultComboBoxModel<>(data.getTimeCriterion().values.list());
        energies = new DefaultComboBoxModel<>(data.getEnergyCriterion().values.list());
        valueCombo = new TRComboBox<>(priorities);
 
        setLayout(new MigLayout("insets 12px", "2[]2[]4[]2[200]2", "2[]2"));

        add(criteriaLabel);
        add(criteriaCombo);
        add(valueLabel);
        add(valueCombo, "wrap");
    }

    public Criteria getCriteria() {
        return (Criteria)criteriaCombo.getSelectedItem();
    }

    public Value getValue() {
        return (Value)valueCombo.getSelectedItem();
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private DefaultComboBoxModel<Value> priorities;
    private DefaultComboBoxModel<Value> times;
    private DefaultComboBoxModel<Value> energies;
    private JLabel criteriaLabel;
    private JLabel valueLabel;
    private JComboBox<Criteria> criteriaCombo;
    private JComboBox<Value> valueCombo;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
