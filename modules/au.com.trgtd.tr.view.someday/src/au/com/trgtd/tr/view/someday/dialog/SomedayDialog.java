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

package au.com.trgtd.tr.view.someday.dialog;

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
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.future.Future;

/**
 * Dialog for creating and modifying someday/maybe items.
 *
 * @author Jeremy Moore
 */
public class SomedayDialog extends JDialog {
    
    private static enum Mode { CREATE, MODIFY };
    private Mode mode;
    private Data data;
    private SomedayDialogPanel somedayPanel;
    private Future future;
    private JButton okButton;
    private JButton cancelButton;
    
    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public SomedayDialog(Data data) {
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
        JRootPane rp = getRootPane();
        rp.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        somedayPanel = new SomedayDialogPanel();
        
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
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
        
        add(somedayPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800, 600));
        
        pack();
        
        setLocation(WindowUtils.getPointForCentering(this));
    }
    
    private JPanel createButtonsPanel() {
	JPanel p = new JPanel(new MigLayout("nogrid, fillx, aligny 100%, gapy unrel"));
	p.add(okButton, "sgx 0, tag ok");
	p.add(cancelButton, "sgx 1, tag cancel, wrap");
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
    
    private boolean isValid(Future future) {
        if (future.getDescription().trim().length() == 0) {
            return false;
        }
        return true;
    }
    
    /* Handle OK. */
    private void ok() {
        future.setDescription(somedayPanel.getDescr());
        future.setTopic(somedayPanel.getTopic());
        future.setNotes(somedayPanel.getNotes());
        future.setTickle(somedayPanel.getTickleDate());
        if (!isValid(future)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (mode == Mode.MODIFY) {
            setVisible(false);
            dispose();
        } else { // (mode == Mode.CREATE)
            data.getFutureManager().add(future);
            future = new Future(data.getNextID());
            somedayPanel.setDescr(future.getDescription());
            // following commenting to default to last topic used
//          panel.setTopic(future.getTopic());
            somedayPanel.setNotes(future.getNotes());
            somedayPanel.setTickle(future.getTickle());
            somedayPanel.focus();
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
    
    /** Shows the dialog in create mode. */
    public void showCreateDialog() {
        future = new Future(data.getNextID());
        somedayPanel.setDescr(future.getDescription());
        somedayPanel.setTopic(future.getTopic());
        somedayPanel.setNotes(future.getNotes());
        somedayPanel.setTickle(future.getTickle());
        setMode(Mode.CREATE);
        showDialog();
    }
    
    /**
     * Shows the modify dialog for the given context.
     * @param context The context to modify.
     */
    public void showModifyDialog(Future future) {
        if (future == null) {
            return;
        }
        this.future = future;        
        somedayPanel.setDescr(future.getDescription());
        somedayPanel.setTopic(future.getTopic());
        somedayPanel.setNotes(future.getNotes());
        somedayPanel.setTickle(future.getTickle());
        setMode(Mode.MODIFY);
        showDialog();
    }
    
}
