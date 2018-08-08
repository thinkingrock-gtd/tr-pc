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

import au.com.trgtd.tr.runtime.Open;
import au.com.trgtd.tr.view.notes.spi.NotesLinker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import tr.model.Item.Notable;

/**
 * JEditorPane extension for notes display only.
 *
 * @author Jeremy Moore
 */
public class NotesViewField extends JEditorPane implements PropertyChangeListener, HyperlinkListener {

    private final static Color NOTES_COLOR = new Color(255, 255, 204);
    private final static String CONTENT_TYPE_HTML = "text/html";
    private final EditorKit editorKitHTML = new HTMLEditorKit();
    private final static String CONTENT_TYPE_TEXT = "text/plain";
    private final EditorKit editorKitTEXT = createEditorKitForContentType(CONTENT_TYPE_TEXT);
    private Notable notable;
    private MouseListener mouseListener;
    private KeyListener keyListener;

    /** Constructs a new instance. */
    public NotesViewField() {
        super();
        construct(true);
    }

    /**
     * Constructs a new instance.
     * @param enableEdit Whether or not user can invoke edit mode.
     */
    public NotesViewField(boolean enableEdit) {
        super();
        construct(enableEdit);
    }

    private void construct(boolean enableEdit) {
        super.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        super.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        super.setFont(new JTextField().getFont());
        super.setEditorKitForContentType(CONTENT_TYPE_TEXT, editorKitTEXT);
        super.setEditorKitForContentType(CONTENT_TYPE_HTML, editorKitHTML);
        super.setContentType(CONTENT_TYPE_HTML);
        super.setEditable(false);
        super.setBackground(NOTES_COLOR);
        super.setPreferredSize(new Dimension(10, 34));
        super.addHyperlinkListener(this);
        this.mouseListener = new LabelMouseListener();
        this.keyListener = new NotesKeyListener();
        if (enableEdit) {
            super.addMouseListener(mouseListener);
            super.addKeyListener(keyListener);
        }
    }

    /**
     * Overridden to disable edit mode.
     * @param editable Is ignored.
     */
    @Override
    public void setEditable(boolean editable) {
        super.setEditable(false);
    }

    /** 
     * Overridden to disable direct setting the text.
     * @param text Is ignored.
     */
    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * Sets the object that has notes to edit.
     * @param notable The notable object.
     */
    public void setNotable(Notable notable) {
        if (this.notable != null) {
            this.notable.removePropertyChangeListener(Notable.PROP_NOTES, this);
        }
        this.notable = notable;
        if (notable == null) {
            super.setText(NotesUtils.text2html(""));
        } else {
            super.setText(NotesUtils.text2html(notable.getNotes()));
            notable.addPropertyChangeListener(Notable.PROP_NOTES, this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.setText(NotesUtils.text2html(notable.getNotes()));
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
            return;
        }

        String urlStr;

        URL url = e.getURL();
        if (url != null) {
            urlStr = url.toExternalForm();
        } else {
            // lotus notes etc.
            urlStr = e.getDescription();
        }

        // Try to find and use a linker
        NotesLinker linker = NotesLinkerLookup.instance().getHandler(urlStr);
        if (linker != null) {
            linker.openUserLink(urlStr);
            return;
        }

        // If a valid URL open using Runtime
        if (url != null) {
            Open.open(url);
            return;
        }

        // Try Desktop browse (for lotus notes etc. links)
        NotesUtils.openLinkWithDesktop(urlStr);
    }

    private class LabelMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (isEnabled() && e.getClickCount() == 2) {
                e.consume();
                String notes = NotesEditor.getDefault().edit(notable.getNotes());
                if (notes != null) {
                    notable.setNotes(notes);
                }
            }
        }
    }

    private class NotesKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isControlDown() && e.isAltDown()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_E: {
                        e.consume();
                        String notes = NotesEditor.getDefault().edit(notable.getNotes());
                        if (notes != null) {
                            notable.setNotes(notes);
                        }
                    }
                }
            }
        }
    }
}
