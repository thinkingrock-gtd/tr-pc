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
package au.com.trgtd.tr.view.notes;

import au.com.trgtd.tr.swing.TRLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.openide.util.NbBundle;
import tr.model.Item.Notable;

/**
 * Notes label with hyperlink for activating the notes dialog.
 *
 * @author Jeremy Moore
 */
public class NotesViewLabel extends TRLabel {

    private final static JLabel LABEL = new JLabel();
    private final static String TITLE = NbBundle.getMessage(NotesViewLabel.class, "notes.label");
    private Notable notable;
    private MouseListener mouseListener;

    /**
     * Constructs a new instance.
     */
    public NotesViewLabel() {
        super();
        this.mouseListener = new LabelMouseListener();
        this.setHorizontalTextPosition(SwingConstants.LEFT);
        this.setEnabled(true);
    }

    /**
     * Sets the object that has notes to edit.
     * @param notable The notable object.
     */
    public void setNotable(Notable notable) {
        this.notable = notable;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        removeMouseListener(mouseListener);
        if (enabled) {
            super.setText("<HTML><U>" + TITLE + "</U></HTML>");
            setForeground(getEnabledColor());
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(mouseListener);
        } else {
            super.setText(TITLE);
            setForeground(getDisabledColor());
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public Color getEnabledColor() {
        return Color.BLUE;
    }

    public Color getDisabledColor() {
        return LABEL.getForeground();
    }

    private class LabelMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            String notes = NotesEditor.getDefault().edit(notable.getNotes());
            if (notes != null) {
                notable.setNotes(notes);
            }
        }
    }
    
}
