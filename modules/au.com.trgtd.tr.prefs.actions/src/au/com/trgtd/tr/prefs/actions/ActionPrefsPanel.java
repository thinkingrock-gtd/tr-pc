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
 * Portions Copyright 2006-2009 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.prefs.actions;

import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.time.HourSpinner;
import au.com.trgtd.tr.swing.time.MinuteSpinner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class ActionPrefsPanel extends JPanel {
    
    ActionPrefsPanel(ActionOptionsPanelController controller) {
        this.controller = controller;
        initForm();
    }

    void load() {
        noteEmailCheckBox.setSelected(ActionPrefs.isNoteDelegateEmail());
        successCheckBox.setSelected(ActionPrefs.isShowSuccess());
        startDateCheckBox.setSelected(ActionPrefs.isShowStartDate());
        dueDateCheckBox.setSelected(ActionPrefs.isShowDueDate());
        editCreateDateCheckBox.setSelected(ActionPrefs.isEditCreateDate());
        actionStatesCombo.setSelectedItem(ActionPrefs.getNewActionState());
        encodingCombo.setSelectedItem(ActionPrefs.getEmailEncoding());
        delegateModeComboBox.setSelected(ActionPrefs.isDelegateModeCombo());
        delegateModeFreeText.setSelected(!ActionPrefs.isDelegateModeCombo());
        schdTimeHrSpinner.setVal(ActionPrefs.getSchdTimeHr());
        schdTimeMnSpinner.setVal(ActionPrefs.getSchdTimeMn());
        schdDurHrsSpinner.setVal(ActionPrefs.getSchdDurHrs());
        schdDurMnsSpinner.setVal(ActionPrefs.getSchdDurMns());
    }

    void store() {
        ActionPrefs.setNoteDelegateEmail(noteEmailCheckBox.isSelected());
        ActionPrefs.setShowSuccess(successCheckBox.isSelected());
        ActionPrefs.setShowStartDate(startDateCheckBox.isSelected());
        ActionPrefs.setShowDueDate(dueDateCheckBox.isSelected());
        ActionPrefs.setEditCreateDate(editCreateDateCheckBox.isSelected());
        ActionPrefs.setNewActionState((ActionPrefs.ActionState)actionStatesCombo.getSelectedItem());
        ActionPrefs.setDelegateModeCombo(delegateModeComboBox.isSelected());
        if (isValidEncoding()) {
            ActionPrefs.setEmailEncoding(getEncoding());
        }
        ActionPrefs.setSchdTimeHr(schdTimeHrSpinner.getVal());
        ActionPrefs.setSchdTimeMn(schdTimeMnSpinner.getVal());
        ActionPrefs.setSchdDurHrs(schdDurHrsSpinner.getVal());
        ActionPrefs.setSchdDurMns(schdDurMnsSpinner.getVal());
    }

    boolean valid() {
        return isValidEncoding();
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(ActionPrefsPanel.class, key);
    }

    private void initForm() {
        noteEmailCheckBox = new JCheckBox(getMsg("CTL_NoteEmail"));
        noteEmailCheckBox.addActionListener(actionListener);
        delegateModeLabel = new JLabel(getMsg("delegate.mode"));
        delegateModeFreeText = new JRadioButton(getMsg("delegate.mode.text"));
        delegateModeFreeText.addActionListener(actionListener);
        delegateModeComboBox = new JRadioButton(getMsg("delegate.mode.list"));
        delegateModeComboBox.addActionListener(actionListener);
        delegateModeGroup = new ButtonGroup();
        delegateModeGroup.add(delegateModeFreeText);
        delegateModeGroup.add(delegateModeComboBox);
        successCheckBox = new JCheckBox(getMsg("CTL_IncludeSuccess"));
        successCheckBox.addActionListener(actionListener);
        startDateCheckBox = new JCheckBox(getMsg("CTL_IncludeStartDate"));
        startDateCheckBox.addActionListener(actionListener);
        dueDateCheckBox = new JCheckBox(getMsg("CTL_IncludeDueDate"));
        dueDateCheckBox.addActionListener(actionListener);
        editCreateDateCheckBox = new JCheckBox(getMsg("CTL_EditCreateDate"));
        editCreateDateCheckBox.addActionListener(actionListener);
        encodingLabel = new TRLabel(getMsg("CTL_EmailEncoding"));
        encodingCombo = new TRComboBox(getEncodingModel());
        actionStatesLabel = new TRLabel(getMsg("DefaultActionState"));
        states = new Vector<ActionPrefs.ActionState>();
        states.add(ActionPrefs.ActionState.INACTIVE);
        states.add(ActionPrefs.ActionState.DOASAP);
        states.add(ActionPrefs.ActionState.SCHEDULED);
        states.add(ActionPrefs.ActionState.DELEGATED);
        actionStatesCombo = new TRComboBox(new DefaultComboBoxModel(states));
        actionStatesCombo.setMaximumRowCount(states.size());
        schdTimeLabel = new TRLabel(getMsg("CTL_DefSchdTime"));
        schdTimeHrSpinner = new HourSpinner();
        schdTimeHrSpinner.addChangeListener(changeListener);
        schdTimeMnSpinner = new MinuteSpinner();        
        schdTimeMnSpinner.addChangeListener(changeListener);
        schdDurLabel = new TRLabel(getMsg("CTL_DefSchdDur"));
        schdDurHrsSpinner = new HourSpinner();
        schdDurHrsSpinner.addChangeListener(changeListener);
        schdDurMnsSpinner = new MinuteSpinner();
        schdDurMnsSpinner.addChangeListener(changeListener);
        
//      setLayout(new MigLayout("", "0[]0", "0[]2[]2[]2[]2[]2[]2[]2[]0"));
        setLayout(new MigLayout("", "0[]2[]2[]0", "0[]2[]2[]2[]2[]2[]2[]2[]0"));
        
        add(successCheckBox,        "align left, wrap");
        
        add(dueDateCheckBox,        "align left, wrap");
        
        add(startDateCheckBox,      "align left, wrap");
        
        add(editCreateDateCheckBox, "align left, wrap");
        
        add(noteEmailCheckBox,      "align left, wrap");

        add(delegateModeLabel,      "span, split 3, gapleft 6, align left");
        add(delegateModeFreeText,   "align left");
        add(delegateModeComboBox,   "align left, wrap");

        add(actionStatesLabel,      "span, split 2, gapleft 6, align left");
        add(actionStatesCombo,      "align left, wrap");
        
        add(encodingLabel,          "span, split 2, gapleft 6, align left");
        add(encodingCombo,          "align left, wrap");
        
        add(schdTimeLabel,          "gapleft 6, align left");
        add(schdTimeHrSpinner,      "align left");
        add(schdTimeMnSpinner,      "align left, wrap");
        
        add(schdDurLabel,           "gapleft 6, align left");
        add(schdDurHrsSpinner,      "align left");
        add(schdDurMnsSpinner,      "align left, wrap");
    }

    private ComboBoxModel getEncodingModel() {
        Vector<String> encodings = new Vector<String>();
        encodings.add("");
        encodings.addAll(Charset.availableCharsets().keySet());
        return new DefaultComboBoxModel(encodings);
    }

    private boolean isValidEncoding() {
        String encoding = getEncoding();
        return encoding.length() == 0 || Charset.isSupported(encoding);
    }

    private String getEncoding() {
        String encoding = (String)encodingCombo.getSelectedItem();
        return encoding == null ? "" : encoding;
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            controller.changed();
        }
    };
    
    private final ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent ce) {
            controller.changed();
        }            
    };
    
    private final ActionOptionsPanelController controller;
    private JCheckBox successCheckBox;
    private JCheckBox dueDateCheckBox;
    private JCheckBox startDateCheckBox;
    private JCheckBox editCreateDateCheckBox;
    private JCheckBox noteEmailCheckBox;
    private JComboBox encodingCombo;
    private JLabel encodingLabel;
    private JLabel actionStatesLabel;
    private JComboBox actionStatesCombo;
    private Vector<ActionPrefs.ActionState> states;

    private JLabel delegateModeLabel;
    private ButtonGroup delegateModeGroup;
    private JRadioButton delegateModeFreeText;
    private JRadioButton delegateModeComboBox;

    private JLabel schdTimeLabel;    
    private HourSpinner schdTimeHrSpinner;
    private MinuteSpinner schdTimeMnSpinner;    
    private JLabel schdDurLabel;    
    private HourSpinner schdDurHrsSpinner;
    private MinuteSpinner schdDurMnsSpinner;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
