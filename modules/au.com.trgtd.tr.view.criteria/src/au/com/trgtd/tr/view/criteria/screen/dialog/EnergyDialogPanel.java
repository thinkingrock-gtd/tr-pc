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
 * Energy value add/edit dialog panel.
 *
 * @author Jeremy Moore
 */
public class EnergyDialogPanel extends JPanel {

    /** Constructs a new instance. */
    public EnergyDialogPanel(JButton okButton) {
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
        setLayout(new MigLayout("", "4[]2[grow]4", "4[]24"));
        add(titleLabel, "align left");
        add(titleField, "align left, growx, wrap");
    }

    public void setTitle(String title) {
        titleField.setText(title);
        validation();
    }

    public String getTitle() {
        return titleField.getText();
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

    private final static Class CLASS = EnergyDialogPanel.class;
    private final JButton okButton;
    private JTextField titleField;
    private JLabel titleLabel;

}
