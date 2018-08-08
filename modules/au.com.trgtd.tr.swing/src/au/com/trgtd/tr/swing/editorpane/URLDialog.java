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

package au.com.trgtd.tr.swing.editorpane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.openide.util.Utilities;
//import org.jdesktop.swingx.util.WindowUtils;

/**
 * URL dialog.
 *
 * @author Jeremy Moore
 */
final public class URLDialog extends JDialog {
    
    private int option;
    private JButton okButton;
    private JButton cancelButton;
    private URLDialogPanel urlPanel;
    private Component component;
    
    /**
     * Constructs a new instance.
     * @param frame The parent frame.
     * @param component The componenet to locate relative to.
     * @param title The dialog title.
     */
    public URLDialog(Frame frame, Component component, String title) {
        super(frame, title, true);
        this.component = component;
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
        rootPane.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        urlPanel = new URLDialogPanel();
        
        cancelButton = new JButton(org.openide.util.NbBundle.getMessage(URLDialog.class, "cancel"));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        okButton = new JButton(org.openide.util.NbBundle.getMessage(URLDialog.class, "ok"));
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
        
        add(urlPanel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setLocationRelativeTo(component);
        pack();
        
//      setLocation(WindowUtils.getPointForCentering(this));
        Rectangle rect = Utilities.findCenterBounds(getSize());        
        setLocation(rect.x, rect.y);
        
        urlPanel.initView();
    }
    
    /* Handle OK. */
    private void ok() {
        option = JOptionPane.OK_OPTION;
        setVisible(false);
        dispose();
    }
    
    /* Handle Cancel. */
    private void cancel() {
        option = JOptionPane.CANCEL_OPTION;
        setVisible(false);
        dispose();
    }
    
    public String getURL() {
        return (urlPanel == null) ? "" : urlPanel.getURL();
    }
    
    public String getLinkText() {
        return (urlPanel == null) ? "" : urlPanel.getLinkText();
    }
    
    /**
     * Shows the dialog and returns the option selected by the user.
     * @return JOptionPane.OK_OPTION or JOptionPane.CANCEL_OPTION
     */
    public int showDialog() {
        option = JOptionPane.CANCEL_OPTION;
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
        return option;
    }
}
