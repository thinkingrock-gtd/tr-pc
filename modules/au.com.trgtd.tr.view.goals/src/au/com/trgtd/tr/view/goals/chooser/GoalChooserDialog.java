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

        ActionListener cancelListener = (ActionEvent e) -> {
            cancel();
        };

        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        cancelButton.addActionListener(cancelListener);

        ActionListener selectListener = (ActionEvent e) -> {
            select();
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
