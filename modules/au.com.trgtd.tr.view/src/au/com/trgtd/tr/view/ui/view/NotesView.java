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
package au.com.trgtd.tr.view.ui.view;

import au.com.trgtd.tr.runtime.Open;
import au.com.trgtd.tr.view.ViewUtils;
import au.com.trgtd.tr.view.notes.NotesLinkerLookup;
import au.com.trgtd.tr.view.notes.NotesUtils;
import au.com.trgtd.tr.view.notes.spi.NotesLinker;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import tr.model.Item.Notable;

/**
 * JEditorPane extension for notes view only.
 *
 * @author Jeremy Moore
 */
public class NotesView extends JEditorPane implements PropertyChangeListener, HyperlinkListener {

    private final static Logger LOG = Logger.getLogger("tr.notes.view");
    
    private final static Color NOTES_COLOR = ViewUtils.lighter(new Color(255, 255, 204), 5);
    private final static String CONTENT_TYPE_HTML = "text/html";
    private final EditorKit editorKitHTML = new HTMLEditorKit();
    private Notable notable;

    /**
     * Constructs a new instance.
     */
    public NotesView() {
        super();
        construct();
    }

    private void construct() {
        super.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
        super.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        super.setFont(new JTextField().getFont());
        super.setEditorKitForContentType(CONTENT_TYPE_HTML, editorKitHTML);
        super.setContentType(CONTENT_TYPE_HTML);
        super.setEditable(false);
        super.setBackground(NOTES_COLOR);
        super.setPreferredSize(new Dimension(10, 34));
        super.addHyperlinkListener(this);
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
        }
            
        // If a valid URL, open using runtime
        if (url != null) {
            Open.open(url);
            return;
        }

        // Try Desktop browse (for lotus notes etc. links)
        NotesUtils.openLinkWithDesktop(urlStr);
    }

}
