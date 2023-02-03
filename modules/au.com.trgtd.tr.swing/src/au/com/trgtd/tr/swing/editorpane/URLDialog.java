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
        
        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = getRootPane();
        rootPane.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW );
        
        urlPanel = new URLDialogPanel();
        
        cancelButton = new JButton(org.openide.util.NbBundle.getMessage(URLDialog.class, "cancel"));
        cancelButton.addActionListener((ActionEvent e) -> {
            cancel();
        });
        okButton = new JButton(org.openide.util.NbBundle.getMessage(URLDialog.class, "ok"));
        okButton.addActionListener((ActionEvent e) -> {
            ok();
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
