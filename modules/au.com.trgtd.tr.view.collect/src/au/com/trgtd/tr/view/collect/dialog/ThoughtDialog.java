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
        
        ActionListener cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        };
        
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
//        JRootPane rootPane = getRootPane();
        rootPane.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        thoughtPanel = new ThoughtDialogPanel();
        
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel")); // NOI18N
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok")); // NOI18N
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok();
            }
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
