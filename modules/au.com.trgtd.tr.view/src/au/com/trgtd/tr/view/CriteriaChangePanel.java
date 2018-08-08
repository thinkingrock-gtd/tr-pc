/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }

        criteriaLabel = new JLabel(getMsg("CTL_Criteria"));
        criteriaCombo = new TRComboBox(Criteria.values());
        criteriaCombo.setSelectedItem(Criteria.Priority);
        criteriaCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Criteria c = (Criteria)criteriaCombo.getSelectedItem();
                if (c == Criteria.Priority) {
                    valueCombo.setModel(priorities);
                } else if (c == Criteria.Time) {
                    valueCombo.setModel(times);
                } else if (c == Criteria.Energy) {
                    valueCombo.setModel(energies);
                }
            }
        });

        valueLabel = new JLabel(getMsg("CTL_Value"));
        priorities = new DefaultComboBoxModel(data.getPriorityCriterion().values.list());
        times = new DefaultComboBoxModel(data.getTimeCriterion().values.list());
        energies = new DefaultComboBoxModel(data.getEnergyCriterion().values.list());
        valueCombo = new TRComboBox(priorities);
 
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

    private DefaultComboBoxModel priorities;
    private DefaultComboBoxModel times;
    private DefaultComboBoxModel energies;
    private JLabel criteriaLabel;
    private JLabel valueLabel;
    private JComboBox criteriaCombo;
    private JComboBox valueCombo;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
