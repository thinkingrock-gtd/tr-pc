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

package au.com.trgtd.tr.view.contexts.dialog;

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
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.Data;
import tr.model.context.Context;

/**
 * Dialog for creating and modifying contexts.
 *
 * @author Jeremy Moore
 */
public class ContextDialog extends JDialog {
    
    private static enum Mode { CREATE, MODIFY };
    private Mode mode;
    private Data data;
    private ContextDialogPanel contextPanel;
    private Context context;
    private JButton okButton;
    private JButton cancelButton;
    
    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public ContextDialog(Data data) {
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
        JRootPane rootPane = getRootPane();
        rootPane.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        contextPanel = new ContextDialogPanel();
        contextPanel.setPreferredSize(new Dimension(700, 100));
        
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
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(okButton);
        
        add(contextPanel, BorderLayout.CENTER);
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
    
    private boolean isValid(Context context) {
        if (context.getName().trim().length() == 0) {
            return false;
        }
        return true;
    }
    
    /* Handle OK. */
    private void ok() {
        context.setName(contextPanel.getContextName());
        context.setDescription(contextPanel.getDescription());
        if (!isValid(context)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (mode == Mode.MODIFY) {
            setVisible(false);
            dispose();
        } else { // (mode == Mode.CREATE)
            data.getContextManager().add(context);
            context = new Context(data.getNextID());
            contextPanel.setContextName(context.getName());
            contextPanel.setDescription(context.getDescription());
            contextPanel.focus();
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
        context = new Context(data.getNextID());
        contextPanel.setContextName(context.getName());
        contextPanel.setDescription(context.getDescription());
        setMode(Mode.CREATE);
        showDialog();
    }
    
    /**
     * Shows the modify dialog for the given context.
     * @param context The context to modify.
     */
    public void showModifyDialog(Context context) {
        if (context == null) {
            return;
        }
        this.context = context;
        contextPanel.setContextName(context.getName());
        contextPanel.setDescription(context.getDescription());
        setMode(Mode.MODIFY);
        showDialog();
    }
    
}
