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
package au.com.trgtd.tr.view.collect.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.thought.Thought;

/**
 * Dialog for creating and modifying thoughts.
 *
 * @author Jeremy Moore
 */
public class ThoughtDialog extends JDialog {
    
    private static enum Mode { CREATE, MODIFY };
    private Mode mode;
    private Data data;
    private ThoughtDialogPanel thoughtPanel;
    private Thought thought;
    private JButton okButton;
    private JButton cancelButton;
    
    /**
     * Constructs a new instance.
     * @param frame The parent frame.
     * @param component The componenet to locate relative to.
     * @param title The dialog title.
     */
    public ThoughtDialog(Data data) {
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
//        JRootPane rootPane = getRootPane();
        rootPane.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        thoughtPanel = new ThoughtDialogPanel();
        
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel")); // NOI18N
        cancelButton.addActionListener((ActionEvent e) -> {
            cancel();
        });
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok")); // NOI18N
        okButton.addActionListener((ActionEvent e) -> {
            ok();
        });

        JPanel buttonsPanel = createButtonsPanel();
        
        add(thoughtPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800, 600));

        pack();
        
        setLocation(WindowUtils.getPointForCentering(this));
    }

    private JPanel createButtonsPanel() {
	JPanel p = new JPanel(new MigLayout("nogrid, fillx, aligny 100%, gapy unrel"));
//	p.add(okButton, "sgx 0, tag ok");
//	p.add(cancelButton, "sgx 1, tag cancel, wrap");
	p.add(okButton, "tag ok");
	p.add(cancelButton, "tag cancel, wrap");
	return p;
    }

    private void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.CREATE) {
            setTitle(NbBundle.getMessage(getClass(), "create"));
            okButton.setText(NbBundle.getMessage(getClass(), "add"));
        } else {
            setTitle(NbBundle.getMessage(getClass(), "modify"));
            okButton.setText(NbBundle.getMessage(getClass(), "ok"));
        }
    }
    
    private boolean isValid(Thought thought) {
        if (thought.getDescription().trim().length() == 0) {
            return false;
        }
        return true;
    }
    
    /* Handle OK. */
    private void ok() {
        requestFocusInWindow();
        
        thought.setDescription(thoughtPanel.getDescr());
        thought.setTopic(thoughtPanel.getTopic());
        thought.setNotes(thoughtPanel.getNotes());
        if (!isValid(thought)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (mode == Mode.MODIFY) {
            setVisible(false);
            dispose();
        } else { // (mode == Mode.CREATE)
            data.getThoughtManager().add(thought);
            
            thought = new Thought(data.getNextID());
            thoughtPanel.setDescr(thought.getDescription());
            //Next line is commented so that topic defaults to last one used.
            //thoughtPanel.setTopic(thought.getTopic());
            thoughtPanel.setNotes(thought.getNotes());
            thoughtPanel.focus();
        }
    }
    
    /* Handle Cancel. */
    private void cancel() {
        setVisible(false);
        dispose();
    }
    
    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
    }
    
    /**
     * Shows the dialog in create mode.
     */
    public void showCreateDialog() {
        thought = new Thought(data.getNextID());
        thoughtPanel.setDescr(thought.getDescription());
        thoughtPanel.setTopic(thought.getTopic());
        thoughtPanel.setNotes(thought.getNotes());
        setMode(Mode.CREATE);
        showDialog();
    }
    
    /**
     * Shows the modify dialog for the given thought.
     * @param thought The thought to modify.
     */
    public void showModifyDialog(Thought thought) {
        if (thought == null) {
            return;
        }
        this.thought = thought;
        thoughtPanel.setDescr(thought.getDescription());
        thoughtPanel.setTopic(thought.getTopic());
        thoughtPanel.setNotes(thought.getNotes());
        setMode(Mode.MODIFY);
        showDialog();
    }
    
}
