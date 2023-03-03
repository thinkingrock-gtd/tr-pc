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
package au.com.trgtd.tr.view.reference.screen;

import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.TRTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.topic.Topic;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.notes.NotesViewField;
import au.com.trgtd.tr.view.notes.NotesViewLabel;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import au.com.trgtd.tr.view.topics.TopicsComboBoxModel;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import tr.model.information.Information;

/**
 * Reference item panel.
 *
 * @author Jeremy Moore
 */
public class ReferencePanel extends JPanel {

    /** Constructs a new instance. */
    public ReferencePanel() {
        super();
        initComponents();
        initModel(null);
    }

    /**
     * Initialises the data model and view.
     * @param ref The reference item.
     */
    public void initModel(Information ref) {
        this.ref = ref;
        initView();
    }

    /* Initialises the view form field values for the data model. */
    private void initView() {
        if (ref == null) {
            descrField.setText("");
            topicCombo.setSelectedIndex(0);
            notesLabel.setNotable(null);
            notesField.setNotable(null);
            setEnabled(false);
        } else {
            descrField.setText(ref.getDescription());
            topicCombo.setSelectedItem(ref.getTopic());
            notesLabel.setNotable(ref);
            notesField.setNotable(ref);
            setEnabled(true);
        }
        
        // scroll notes to top
        SwingUtilities.invokeLater(() -> {
            notesScroll.getViewport().setViewPosition(new Point(0,0));
        });

    }

    /** 
     * Enable or disable fields.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        descrLabel.setEnabled(enabled);
        descrField.setEnabled(enabled);
        topicLabel.setEnabled(enabled);
        topicCombo.setEnabled(enabled);
        notesLabel.setEnabled(enabled);
        notesField.setEnabled(enabled);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        addListeners();
    }

    @Override
    public void removeNotify() {
        removeListeners();
        super.removeNotify();
    }

    public void deactivate() {
        // focus retention problem on Mac
        if (Utilities.getOperatingSystem() == Utilities.OS_MAC) {
            if (descrField.hasFocus()) {
                changedDescription();
            }
        }
    }

    private void addListeners() {
        descrFocusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                changedDescription();
            }
        };
        descrField.addFocusListener(descrFocusListener);

        topicActionListener = (ActionEvent evt) -> {
            changedTopic();
        };
        topicCombo.addActionListener(topicActionListener);
    }

    private void removeListeners() {
        descrField.removeFocusListener(descrFocusListener);
        topicCombo.removeActionListener(topicActionListener);
        descrFocusListener = null;
        topicActionListener = null;
    }

    /** Start editing if possible. */
    public void edit() {
        if (ref == null) {
            return;
        }
        descrField.requestFocus();
    }

    private void changedDescription() {
        if (ref == null) {
            return;
        }
        ref.setDescription(descrField.getText());
    }

    private void changedTopic() {
        if (ref == null) {
            return;
        }
        ref.setTopic((Topic)topicCombo.getSelectedItem());
    }

    private void initComponents() {
        descrLabel = new TRLabel(NbBundle.getMessage(getClass(), "Reference.description"));
        descrField = new TRTextField();
        notesLabel = new NotesViewLabel();
        notesField = new NotesViewField();
        notesScroll = new JScrollPane();
        notesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        notesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        notesScroll.setViewportView(notesField);
        topicLabel = new TRLabel(NbBundle.getMessage(getClass(), "Reference.topic"));
        topicsModel = new TopicsComboBoxModel(false);
        topicCombo = new TopicsComboBox(topicsModel);
        topicCombo.setMaximumSize(new Dimension(250, 23));
        topicCombo.setMinimumSize(new Dimension(250, 23));
        topicCombo.setPreferredSize(new Dimension(250, 23));        

        setBackground(ViewUtils.COLOR_PANEL_BG);

        setLayout(new MigLayout("", "4[]4[grow]0", "4[]4[]4[grow]0"));
        add(descrLabel, "align right");
        add(descrField, "align left, gap right 12, growx, wrap");
        add(topicLabel, "align right");
        add(topicCombo, "align left, wrap");
        add(notesLabel, "align right top, gap top 4, growy 0");
        add(notesScroll, "align left, gap left 3, grow");
    }

    private Information ref;
    private TRLabel descrLabel;
    private TRTextField descrField;
    private NotesViewLabel notesLabel;
    private NotesViewField notesField;
    private JScrollPane notesScroll;
    private TRLabel topicLabel;
    private JComboBox<Topic> topicCombo;
    private TopicsComboBoxModel topicsModel;
    private FocusListener descrFocusListener;
    private ActionListener topicActionListener;

}
