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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Priority value add/edit dialog panel.
 */
public class PriorityDialogPanel extends JPanel {

    /** Constructs a new instance. */
    public PriorityDialogPanel(JButton okButton) {
        this.okButton = okButton;
        initView();
    }

    private void initView() {
        titleLabel = new JLabel(NbBundle.getMessage(CLASS, "title.label"));
        titleField = new JTextField();
        titleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    validation();
                });
            }
        });
        icalLabel = new JLabel(NbBundle.getMessage(CLASS, "ical.label"));
        icalField = getSpinner();

        setLayout(new MigLayout("", "4[]2[grow]4", "4[]4[]24"));
        add(titleLabel, "align left");
        add(titleField, "align left, growx, wrap");
        add(icalLabel, "align left");
        add(icalField, "align left, wrap");
    }

    public void setTitle(String title) {
        titleField.setText(title);
        validation();
    }

    public String getTitle() {
        return titleField.getText();
    }

    public void setICalValue(Integer value) {
        icalField.setValue(null == value ? new Integer(0) : value);
    }

    public Integer getICalValue() {
        Object object = icalField.getValue();
        if (object instanceof Integer integer) {
            return integer;
        } else {
            return null;
        }
    }
    
    public void focus() {
        requestFocusInWindow();
        titleField.requestFocusInWindow();
    }

    private void validation() {
        okButton.setEnabled(isValidInput());
    }

    public boolean isValidInput() {
        return titleField.getText().trim().length() > 0;
    }

    private JSpinner getSpinner() {
        int min = 0;
        int max = 99;
        int inc = 1;
        int val = 0;
        SpinnerModel model = new SpinnerNumberModel(val, min, max, inc);
        return new JSpinner(model);
    }

    private final static Class CLASS = PriorityDialogPanel.class;
    private final JButton okButton;
    private JTextField titleField;
    private JLabel titleLabel;
    private JLabel icalLabel;
    private JSpinner icalField;

}
