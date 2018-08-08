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
 * Portions Copyright 2006-2010 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.view.goals.chooser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.goals.ctrl.GoalCtrl;

/**
 * Dialog for choosing a goal.
 *
 * @author Jeremy Moore
 */
public class GoalChooserDialog extends JDialog {

    public static enum Result {
        SELECT, CANCEL
    }

    private Result result;
    private GoalChooserPanel panel;
    private JButton selectButton;
    private JButton cancelButton;
    private final boolean showAll;

    /**
     * Constructs a new instance.
     */
    public GoalChooserDialog(boolean showAll) {
        super(WindowManager.getDefault().getMainWindow(), true);
        this.showAll = showAll;
        construct();
    }

    private void construct() {
        setTitle(NbBundle.getMessage(getClass(), "title"));

        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        };

        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        cancelButton.addActionListener(cancelListener);

        ActionListener selectListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select();
            }
        };
        
        selectButton = new JButton(NbBundle.getMessage(getClass(), "select"));
        selectButton.addActionListener(selectListener);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(selectButton);

        panel = new GoalChooserPanel(selectButton, showAll);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(420, 600));
        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }

    /* Handle SELECT. */
    private void select() {
        result = Result.SELECT;
        setVisible(false);
        dispose();
    }

    /* Handle Cancel. */
    private void cancel() {
        result = Result.CANCEL;
        setVisible(false);
        dispose();
    }

    public GoalCtrl getSelected() {
        return panel.getSelected();
    }

    public Result showDialog() {
        getRootPane().setDefaultButton(selectButton);
        panel.reset();
        setVisible(true);
        return result;
    }

}
