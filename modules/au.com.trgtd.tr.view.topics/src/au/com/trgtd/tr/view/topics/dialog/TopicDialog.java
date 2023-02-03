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
package au.com.trgtd.tr.view.topics.dialog;

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
import tr.model.topic.Topic;

/**
 * Dialog for creating and modifying topics.
 *
 * @author Jeremy Moore
 */
public class TopicDialog extends JDialog {

    private static enum Mode {
        CREATE, MODIFY
    }
    
    private Mode mode;
    private Data data;
    private TopicDialogPanel topicPanel;
    private Topic topic;
    private JButton okButton;
    private JButton cancelButton;

    /**
     * Constructs a new instance.
     * @param data The data model.
     */
    public TopicDialog(Data data) {
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
        JRootPane rp = getRootPane();
        rp.registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        topicPanel = new TopicDialogPanel();
        topicPanel.setPreferredSize(new Dimension(680, 140));

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

        add(topicPanel, BorderLayout.CENTER);
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

    private boolean isValid(Topic topic) {
        if (topic.getName().trim().length() == 0) {
            return false;
        }
        return true;
    }

    /* Handle OK. */
    private void ok() {
        topic.setName(topicPanel.getTopicName());
        topic.setDescription(topicPanel.getDescription());
        topic.setBackground(topicPanel.getBgColor());
        topic.setForeground(topicPanel.getFgColor());
        if (!isValid(topic)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (mode == Mode.MODIFY) {
            setVisible(false);
            dispose();
        } else { // (mode == Mode.CREATE)
            data.getTopicManager().add(topic);
            topic = new Topic(data.getNextID());
            topicPanel.setTopicName(topic.getName());
            topicPanel.setDescription(topic.getDescription());
            topicPanel.setBgColor(topic.getBackground());
            topicPanel.setFgColor(topic.getForeground());
            topicPanel.focus();
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
        topic = new Topic(data.getNextID());
        topicPanel.setTopicName(topic.getName());
        topicPanel.setDescription(topic.getDescription());
        topicPanel.setBgColor(topic.getBackground());
        topicPanel.setFgColor(topic.getForeground());
        setMode(Mode.CREATE);
        showDialog();
    }

    /**
     * Shows the modify dialog for the given topic.
     * @param topic The topic to modify.
     */
    public void showModifyDialog(Topic topic) {
        if (topic == null) {
            return;
        }
        this.topic = topic;
        topicPanel.setTopicName(topic.getName());
        topicPanel.setDescription(topic.getDescription());
        topicPanel.setBgColor(topic.getBackground());
        topicPanel.setFgColor(topic.getForeground());
        setMode(Mode.MODIFY);
        showDialog();
    }
}
