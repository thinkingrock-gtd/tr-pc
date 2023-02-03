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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent; 
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.actor.Actor;
 
/**
 * Dialog for creating and modifying actors.
 *
 * @author Jeremy Moore
 */
public class ActorDialog extends JDialog {
    
    public static enum Result { OK, CANCEL };
    private static enum Mode { CREATE, MODIFY };
    private final Data data;
    private Mode mode;
    private ActorDialogPanel actorPanel;
    private Actor actor;
    private JButton okButton;
    private JButton cancelButton;
    private Result result;
    
    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public ActorDialog(Data data) {
        super(WindowManager.getDefault().getMainWindow(), true);
        this.data = data;
        construct();
    }
    
    private void construct() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel")); // NOI18N
        cancelButton.addActionListener((ActionEvent e) -> {
            cancel();
        });
        
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok")); // NOI18N
        okButton.addActionListener((ActionEvent e) -> {
            ok();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(okButton);

        actorPanel = new ActorDialogPanel(okButton);
        
        add(new JScrollPane(actorPanel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);        

        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }
    
    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create")); // NOI18N
            okButton.setText(NbBundle.getMessage(getClass(), "add"));  // NOI18N
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify")); // NOI18N
            okButton.setText(NbBundle.getMessage(getClass(), "ok"));  // NOI18N
        }
    }
    
    private boolean isValid(Actor actor) {
        return StringUtils.isNotBlank(actor.getName());
    }
    
    /* Handle OK. */
    private void ok() {
        if (!actorPanel.isValidInput()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        actor.setName(actorPanel.getActorName());
        actor.setEmail(actorPanel.getEmail());
        actor.setInactive(actorPanel.getInactive());
        actor.setTeam(actorPanel.getTeam());

        if (mode == Mode.MODIFY) {
            result = Result.OK;
            setVisible(false);
            dispose();
        } else { // (mode == Mode.CREATE)
            data.getActorManager().add(actor);
            actor = new Actor(data.getNextID());
            actorPanel.focus();
            actorPanel.setActorName(actor.getName());
            actorPanel.setEmail(actor.getEmail());
            actorPanel.setInactive(actor.isInactive());
            actorPanel.setTeam(actor.isTeam());                 
        }
    }
    
    /* Handle Cancel. */
    private void cancel() {
        result = Result.CANCEL;
        setVisible(false);
        dispose();
    }
    
    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
    }
    
    /** Shows the dialog in create mode. */
    public void showCreateDialog() {
        actor = new Actor(data.getNextID());
        actorPanel.setActor(actor);
        actorPanel.setActorName(actor.getName());
        actorPanel.setEmail(actor.getEmail());
        actorPanel.setInactive(actor.isInactive());
        actorPanel.setTeam(actor.isTeam());          
        setMode(Mode.CREATE);
        showDialog();
    }
    
    public Result showModifyDialog(Actor actor) {
        if (actor == null) {
            return Result.CANCEL;
        }
        this.actor = actor;
        actorPanel.setActor(actor);
        actorPanel.setActorName(actor.getName());
        actorPanel.setEmail(actor.getEmail());
        actorPanel.setInactive(actor.isInactive());
        actorPanel.setTeam(actor.isTeam());                
        setMode(Mode.MODIFY);
        showDialog();        
        return result;
    }

}
