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
