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
package au.com.trgtd.tr.view.reference.linker;

import au.com.trgtd.tr.view.notes.NotesViewField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import tr.model.information.Information;

/**
 * Reference viewer panel.
 *
 * @author Jeremy Moore
 */
class RefViewerPanel extends JPanel {

    private final static Class CLASS = RefViewerPanel.class;
    private JLabel descrLabel;
    private JLabel descrField;
    private JLabel topicLabel;
    private JLabel topicField;
    private JLabel notesLabel;
    private NotesViewField notesField;
    private JScrollPane notesPanel;
    private Information ref;

    /** Constructs a new instance. */
    public RefViewerPanel() {
        super();
        initFields();
        initPanel();
        reset(null);
    }

    private void initFields() {
        descrLabel = new Label(NbBundle.getMessage(CLASS, "ref.descr"));
        descrLabel.setIcon(getIcon());
        descrField = new Field();
        topicLabel = new Label(NbBundle.getMessage(CLASS, "ref.topic"));
        topicField = new Field();
        notesLabel = new Label(NbBundle.getMessage(CLASS, "ref.notes"));
        notesLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
        notesField = new NotesViewField(false);
        notesField.setFocusable(true); // needs to be focusable to copy text
        notesField.setEnabled(true);
        notesField.setKeymap(null); // disable keyboard - enter key for ok button
        notesField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    activateOkButton();
                }
            }
        });
        notesPanel = new JScrollPane(notesField);
        notesPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        notesPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        notesPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public ImageIcon getIcon() {
        return ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/reference/linker/ref24.png", false);
    }

    private void initPanel() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new MigLayout("", "10[]8[grow]20", "10[]8[]8[grow]0"));
        add(descrLabel, "align right");
        add(descrField, "growx, wrap");
        add(topicLabel, "align right");
        add(topicField, "align left, wrap");
        add(notesLabel, "align right top");
        add(notesPanel, "grow");
    }

    private void activateOkButton() {
        JRootPane rp = getRootPane();
        if (rp != null) {
            rp.getDefaultButton().doClick();
        }
    }

    public void reset(Information ref) {
        this.ref = ref;
        initView();
    }

    private void initView() {
        if (ref == null) {
            descrField.setText("");
            topicField.setText("");
            notesField.setNotable(null);
        } else {
            descrField.setText(ref.getDescription());
            topicField.setText(ref.getTopic().getName());
            topicField.setForeground(ref.getTopic().getForeground());
            topicField.setBackground(ref.getTopic().getBackground());
            topicField.setOpaque(!ref.getTopic().getBackground().equals(Color.WHITE));
            notesField.setNotable(ref);
            // scroll notes to top
            SwingUtilities.invokeLater(() -> {
                notesPanel.getViewport().setViewPosition(new Point(0, 0));
            });
        }
    }

    private class Label extends JLabel {

        public Label(String text) {
            super(text);
            setForeground(Color.GRAY.darker());
        }
    }

    private class Field extends JLabel {

        public Field() {
            super();
            setOpaque(false);
            setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(2, 4, 2, 4)));
        }
    }
}

