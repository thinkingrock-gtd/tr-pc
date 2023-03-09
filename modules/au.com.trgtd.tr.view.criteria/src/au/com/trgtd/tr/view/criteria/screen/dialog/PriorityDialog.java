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
package au.com.trgtd.tr.view.criteria.screen.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.criteria.Value;

/**
 * Dialog for creating and modifying priority values.
 *
 * @author Jeremy Moore
 */
public class PriorityDialog extends JDialog {

    public static enum Result {
        OK, CANCEL
    };

    private static enum Mode {
        CREATE, MODIFY
    };

    private Mode mode;
    private PriorityDialogPanel panel;
    private Value value;
    private JButton submitButton;
    private JButton cancelButton;
    private Result result;

    /** Constructs a new instance. */
    public PriorityDialog() {
        super(WindowManager.getDefault().getMainWindow(), true);
        construct();
    }

    private void construct() {
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        cancelButton.addActionListener(cancelListener);
        ActionListener submitListener = (ActionEvent e) -> {
            submit();
        };
        submitButton = new JButton(); 
        submitButton.addActionListener(submitListener);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(submitButton);
        panel = new PriorityDialogPanel(submitButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(400, 200));
        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create.priority")); 
            submitButton.setText(NbBundle.getMessage(getClass(), "add"));
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify.priority")); 
            submitButton.setText(NbBundle.getMessage(getClass(), "save"));
        }
    }

    /* Handle OK. */
    private void submit() {
        if (!panel.isValidInput()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }        
        
        if (mode == Mode.MODIFY) {
            value.setName(panel.getTitle());
            value.setMappedValue(panel.getICalValue());
            result = Result.OK;
            setVisible(false);
            dispose();
            return;
        }

        // mode == Mode.CREATE
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        
        Value newValue = new Value(panel.getTitle(), data);
        newValue.setMappedValue(panel.getICalValue());
        data.getPriorityCriterion().values.add(newValue);            

        panel.setTitle("");
        panel.setICalValue(null);
        panel.focus();
    }

    /* Handle Cancel. */
    private void cancel() {
        result = Result.CANCEL;
        setVisible(false);
        dispose();
    }

    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(submitButton);
        setVisible(true);
    }

    /** Shows the dialog in create mode. */
    public void showCreateDialog() {
        panel.setTitle("");
        panel.setICalValue(null);
        setMode(Mode.CREATE);
        showDialog();
    }

    public Result showModifyDialog(Value value) {
        if (value == null) {
            return Result.CANCEL;
        }
        this.value = value;
        panel.setTitle(value.getName());
        panel.setICalValue(value.getMappedValue());
        setMode(Mode.MODIFY);
        showDialog();
        return result;
    }

}
