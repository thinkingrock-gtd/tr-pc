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
package au.com.trgtd.tr.view.actors;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.util.UtilsEmail;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.actor.Actor;

/**
 * Actor panel.
 *
 * @author Jeremy Moore
 */
public class ActorDialogPanel extends JPanel {

    private final JButton okButton;
    private Actor actor;

    /** 
     * Constructs a new instance.
     * @param okButton 
     */
    public ActorDialogPanel(JButton okButton) {
        this.okButton = okButton;
        initView();
    }

    private void initView() {
        nameLabel = new JLabel(NbBundle.getMessage(ActorDialogPanel.class, "name"));
        nameLabel.setBackground(UIManager.getDefaults().getColor("Nb.ScrollPane.Border.color"));
        nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        nameField = new JTextField();
        nameVerifier = new NameVerifier();
        nameField.setInputVerifier(nameVerifier);
        emailLabel = new JLabel(NbBundle.getMessage(ActorDialogPanel.class, "email"));
        emailLabel.setBackground(UIManager.getDefaults().getColor("Nb.ScrollPane.Border.color"));
        emailLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        emailField = new JTextField();
        emailVerifier = new EmailVerifier();
        emailField.setInputVerifier(emailVerifier);
        teamLabel = new JLabel(NbBundle.getMessage(ActorDialogPanel.class, "team"));
        teamCheck = new JCheckBox();
        teamCheck.setHorizontalTextPosition(SwingConstants.LEADING);        
        inactiveLabel = new JLabel(NbBundle.getMessage(ActorDialogPanel.class, "inactive"));
        inactiveCheck = new JCheckBox();
        inactiveCheck.setHorizontalTextPosition(SwingConstants.LEADING);
        inactiveCheck.addActionListener((ActionEvent evt) -> {
            changedInactive();
        });
        msgLabel = new JLabel();
        msgLabel.setForeground(UIManager.getDefaults().getColor("nb.errorForeground"));
        msgLabel.setMinimumSize(new Dimension(100, 16));
        msgLabel.setPreferredSize(new Dimension(0, 16));
        
        setLayout(new MigLayout("", "4[]2[grow]4", "4[]2[]2[]2[]6[]4"));        
        setPreferredSize(new Dimension(400,140));
        add(nameLabel, "align left");
        add(nameField, "align left, growx, wrap");
        add(emailLabel, "align left");
        add(emailField, "align left, growx, wrap");
        add(teamLabel, "align left");
        add(teamCheck, "align left, growx, wrap");                
        add(inactiveLabel, "align left");
        add(inactiveCheck, "align left, growx, wrap");
        add(new JLabel(), "align left");
        add(msgLabel, "align left, growx, wrap");
    }

    private void changedInactive() {
        if (inactiveCheck.isSelected() && Services.instance.isUsed(actor, false)) {
            String m = NbBundle.getMessage(getClass(), "deactivate.actor.in.use.message", actor.getName());
            String t = NbBundle.getMessage(getClass(), "deactivate.actor.in.use.title");
            JOptionPane.showMessageDialog(null, m, t, JOptionPane.INFORMATION_MESSAGE);
            inactiveCheck.setSelected(false);
        }
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public void setActorName(String name) {
        nameField.setText(name);
    }

    public String getActorName() {
        return nameField.getText();
    }

    public void setEmail(String desc) {
        emailField.setText(desc);
    }

    public String getEmail() {
        return emailField.getText();
    }

    public boolean getTeam() {
        return teamCheck.isSelected();
    }

    public void setTeam(boolean b) {
        teamCheck.setSelected(b);
    }

    public boolean getInactive() {
        return inactiveCheck.isSelected();
    }

    public void setInactive(boolean b) {
        inactiveCheck.setSelected(b);
    }

    public void focus() {
        requestFocusInWindow();
        nameField.requestFocusInWindow();
    }

    private class EmailVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextComponent) input).getText().trim();
            boolean valid = text.length() == 0 || UtilsEmail.isEmailAddress(text);
            okButton.setEnabled(valid);
            msgLabel.setText(valid ? "" : "Email address does not have a valid format.");
            return valid;
        }
    }

    private class NameVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextComponent) input).getText().trim();
            boolean valid = text.trim().length() > 0;
            okButton.setEnabled(valid);
            msgLabel.setText(valid ? "" : "Delegate name must be entered.");
            return valid;
        }
    }

    public boolean isValidInput() {
        return nameVerifier.verify(nameField) && emailVerifier.verify(emailField);
    }

    private JTextField nameField;
    private JLabel nameLabel;
    private JTextField emailField;
    private JLabel emailLabel;    
    private JLabel teamLabel;
    private JCheckBox teamCheck;    
    private JLabel inactiveLabel;
    private JCheckBox inactiveCheck;
    private JLabel msgLabel;
    private NameVerifier nameVerifier;
    private EmailVerifier emailVerifier;    
}
