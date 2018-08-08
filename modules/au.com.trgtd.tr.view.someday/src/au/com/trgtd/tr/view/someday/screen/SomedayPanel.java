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

package au.com.trgtd.tr.view.someday.screen;

import au.com.trgtd.tr.swing.TRLabel;
import au.com.trgtd.tr.swing.TRTextField;
import au.com.trgtd.tr.swing.date.field.DateField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import tr.model.future.Future;
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

/**
 * Someday/Maybe item panel.
 *
 * @author Jeremy Moore
 */
public class SomedayPanel extends JPanel {

    private static final Logger LOG = Logger.getLogger("tr.view.future");

    private Future someday;

    /** Constructs a new instance. */
    public SomedayPanel() {
        super();
        initComponents();
        initModel(null);
    }

    /**
     * Initialises the data model and view.
     * @param future The future item.
     */
    public void initModel(Future someday) {
        this.someday = someday;
        initView();
    }

    /* Initialises the view form field values for the data model. */
    private void initView() {
        if (someday == null) {
            descrField.setText("");
            topicCombo.setSelectedIndex(0);
            notesLabel.setNotable(null);
            notesField.setNotable(null);
            tickleField.setDate(null);
            setEnabled(false);
        } else {
            descrField.setText(someday.getDescription());
            topicCombo.setSelectedItem(someday.getTopic());
            notesLabel.setNotable(someday);
            notesField.setNotable(someday);
            tickleField.setDate(someday.getTickle());
            setEnabled(true);
        }

        // scroll notes to top
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                notesScroll.getViewport().setViewPosition(new Point(0,0));
            }
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
        tickleLabel.setEnabled(enabled);
        tickleField.setEnabled(enabled);
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

        topicActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                changedTopic();
            }
        };
        topicCombo.addActionListener(topicActionListener);

        ticklePropertyListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                changedTickle((Date)e.getNewValue());
            }
        };
        tickleField.addPropertyChangeListener("value", ticklePropertyListener);
    }

    private void removeListeners() {
        descrField.removeFocusListener(descrFocusListener);
        topicCombo.removeActionListener(topicActionListener);
        tickleField.removePropertyChangeListener(ticklePropertyListener);
        descrFocusListener = null;
        topicActionListener = null;
        ticklePropertyListener = null;
    }

    /** Start editing if possible. */
    public void edit() {
        if (someday == null) {
            return;
        }
        descrField.requestFocus();
    }

    private void changedDescription() {
        if (someday == null) {
            return;
        }
        someday.setDescription(descrField.getText());
    }

    private void changedTopic() {
        if (someday == null) {
            return;
        }
        someday.setTopic((Topic)topicCombo.getSelectedItem());
    }

    private void changedTickle(Date date) {
        if (someday == null) {
            return;
        }
        someday.setTickle(date);
    }

    private void initComponents() {
        descrLabel = new TRLabel(NbBundle.getMessage(getClass(), "description"));
        descrField = new TRTextField();
        notesLabel = new NotesViewLabel();
        notesField = new NotesViewField();
        notesScroll = new JScrollPane();
        notesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        notesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        notesScroll.setViewportView(notesField);
        topicLabel = new TRLabel(NbBundle.getMessage(getClass(), "topic"));
        topicsModel = new TopicsComboBoxModel(false);
        topicCombo = new TopicsComboBox(topicsModel);
        topicCombo.setMaximumSize(new Dimension(250, 23));
        topicCombo.setMinimumSize(new Dimension(250, 23));
        topicCombo.setPreferredSize(new Dimension(250, 23));        
        tickleLabel = new TRLabel(NbBundle.getMessage(getClass(), "tickle.on"));
        tickleField = new DateField();

        setBackground(ViewUtils.COLOR_PANEL_BG);

        setLayout(new MigLayout("", "4[]4[grow]0", "4[]4[]4[grow]4[]4"));
        add(descrLabel, "align right");
        add(descrField, "align left, gap right 12, growx, wrap");
        add(topicLabel, "align right");
        add(topicCombo, "align left, wrap");
        add(notesLabel, "align right top, gap top 4, growy 0");
        add(notesScroll, "align left, gap left 3, grow, wrap");
        add(tickleLabel, "align right");
        add(tickleField, "align left, wrap");
    }

    private TRLabel descrLabel;
    private TRTextField descrField;
    private NotesViewLabel notesLabel;
    private NotesViewField notesField;
    private JScrollPane notesScroll;
    private TRLabel tickleLabel;
    private DateField tickleField;
    private TRLabel topicLabel;
    private JComboBox topicCombo;
    private TopicsComboBoxModel topicsModel;
    private FocusListener descrFocusListener;
    private ActionListener topicActionListener;
    private PropertyChangeListener ticklePropertyListener;

}
