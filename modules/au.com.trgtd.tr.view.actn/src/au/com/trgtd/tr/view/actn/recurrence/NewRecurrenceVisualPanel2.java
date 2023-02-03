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
package au.com.trgtd.tr.view.actn.recurrence;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.action.Recurrence;
import tr.model.action.Recurrence.Basis;

public final class NewRecurrenceVisualPanel2 extends JPanel {

    public NewRecurrenceVisualPanel2(Recurrence recurrence) {
        this.recurrence = recurrence;
        initView();
        loadValues();
    }

    @Override
    public String getName() {
        return getMsg("choose.recurrence.type");
    }

    private void saveValues() {
        if (subRadio.isSelected()) {
            recurrence.setBasis(Basis.DONE_DATE);
            putClientProperty("type", "subsequent");
        } else {
            recurrence.setBasis(Basis.START_DATE);
            putClientProperty("type", "regular");
        }
    }

    public void loadValues() {
        regRadio.setSelected(recurrence.getBasis() == Basis.START_DATE);
        subRadio.setSelected(recurrence.getBasis() == Basis.DONE_DATE);
        saveValues();
    }

    private void initView() {
        regRadio = new JRadioButton(getMsg("regular"));
        regRadio.setFont(regRadio.getFont().deriveFont(Font.BOLD));
        regRadio.addActionListener((ActionEvent evt) -> {
            saveValues();
        });
        regHelp = new JTextArea(getMsg("regular.hint"));
        regHelp.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        regHelp.setEditable(false);
        regHelp.setFont(regHelp.getFont().deriveFont(Font.ITALIC));
        regHelp.setWrapStyleWord(true);
        regHelp.setLineWrap(true);

        subRadio = new JRadioButton(getMsg("subsequent"));
        subRadio.setFont(subRadio.getFont().deriveFont(Font.BOLD));
        subRadio.addActionListener((ActionEvent evt) -> {
            saveValues();
        });
        subHelp = new JTextArea(getMsg("subsequent.hint"));
        subHelp.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        subHelp.setEditable(false);
        subHelp.setFont(subHelp.getFont().deriveFont(Font.ITALIC));
        subHelp.setWrapStyleWord(true);
        subHelp.setLineWrap(true);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(regRadio);
        buttonGroup.add(subRadio);

        JPanel panel = new JPanel(new MigLayout("", "0[grow]0", "2[]4[]4[]4[]2"));
        panel.add(regRadio, "growx, wrap");
        panel.add(regHelp, "gapleft 25, growx, shrinkx, wrap");
        panel.add(subRadio, "growx, wrap");
        panel.add(subHelp, "gapleft 25, growx, shrinkx, wrap");

//        JScrollPane scrollPane = new JScrollPane(panel);
//        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        setLayout(new BorderLayout());
//        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private final Recurrence recurrence;
    private ButtonGroup buttonGroup;
    private JTextArea regHelp;
    private JRadioButton regRadio;
    private JTextArea subHelp;
    private JRadioButton subRadio;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

