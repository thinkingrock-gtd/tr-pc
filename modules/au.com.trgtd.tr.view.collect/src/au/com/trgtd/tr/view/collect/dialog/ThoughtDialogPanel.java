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

import au.com.trgtd.tr.swing.TRComboBox;
import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.TRTextField;
import au.com.trgtd.tr.view.notes.NotesEditorPanel;
import au.com.trgtd.tr.view.topics.TopicsComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.topic.Topic;

/**
 * Thought panel.
 *
 * @author Jeremy Moore
 */
public class ThoughtDialogPanel extends JPanel {

    /** Constructs a new default instance. */
    public ThoughtDialogPanel() {
        initComponents();
    }

    private void initComponents() {
        descrLabel = new TRLabel(NbBundle.getMessage(CLASS, "thought"));
        descrField = new TRTextField();
        descrField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                descrFocusGained(evt);
            }
        });
        topicLabel = new TRLabel(NbBundle.getMessage(CLASS, "topic"));
        topicCombo = new TopicsComboBox();
        topicCombo.addActionListener((ActionEvent evt) -> {
            topicActionPerformed();
        });
        notesLabel = new TRLabel(NbBundle.getMessage(CLASS, "notes"));
        notesField = new NotesEditorPanel();

//        setLayout(new MigLayout("", "4[]4[grow]0", "8[]4[]4[grow]0"));
//        add(descrLabel, "align right");
//        add(descrField, "align left, growx, gap right 12, wrap");
//        add(topicLabel, "align right");
//        add(topicCombo, "align left, wrap");
//        add(notesLabel, "align right top, gap top 38");
//        add(notesField, "align left, gapleft 3, grow, wrap");

        if (Utilities.isMac()) {
            setLayout(new MigLayout("", "4[]4[grow]0", "8[]4[]4[grow]4[]0"));
            add(descrLabel, "align right");
            add(descrField, "align left, growx, gap right 12, wrap");
            add(topicLabel, "align right");
            add(topicCombo, "align left, wrap");
            add(notesLabel, "align right top, gap top 38");
            add(notesField, "align left, gapleft 3, grow, wrap");
        } else {
            setLayout(new MigLayout("", "4[]4[grow]0", "8[fill]4[fill]4[grow]4[]2"));
            add(descrLabel, "align right");
            add(descrField, "align left, growx, gap right 12, wrap");
            add(topicLabel, "align right");
            add(topicCombo, "align left, wrap");
            add(notesLabel, "align right top, gap top 4");
            add(notesField, "align left, grow, wrap");
        }

    }

    private void descrFocusGained(FocusEvent evt) {
        notesField.setEditable(true);
    }

    private void topicActionPerformed() {
        Topic topic = (Topic)topicCombo.getSelectedItem();
        if (topic == null) {
            descrField.setBackground(TEXTFIELD.getBackground());
            descrField.setForeground(TEXTFIELD.getForeground());
        } else {
            descrField.setBackground(topic.getBackground());
            descrField.setForeground(topic.getForeground());
        }
    }

    public void setDescr(String desc) {
        descrField.setText(desc);
    }

    public String getDescr() {
        return descrField.getText();
    }

    public void setTopic(Topic topic) {
        topicCombo.setSelectedItem(topic);
    }

    public Topic getTopic() {
        return (Topic)topicCombo.getSelectedItem();
    }

    public void setNotes(String notes) {
        notesField.setText(notes);
    }

    public String getNotes() {
        return notesField.getText();
    }

    public void focus() {
        requestFocusInWindow();
        descrField.requestFocusInWindow();
    }

    private static final Class CLASS = ThoughtDialogPanel.class;
    private static final TRTextField TEXTFIELD = new TRTextField();
    private TRLabel descrLabel;
    private TRTextField descrField;
    private TRLabel topicLabel;
    private TRComboBox topicCombo;
    private TRLabel notesLabel;
    private NotesEditorPanel notesField;

}
