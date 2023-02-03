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

import au.com.trgtd.tr.runtime.Open;
import au.com.trgtd.tr.view.notes.spi.NotesLinker;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.*;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

final class NotesEditorField extends JScrollPane {

    private final static Logger LOG = Logger.getLogger("tr.notes.editor");
    
    public NotesEditorField() {
        super();
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        textField = new NotesTextField();
        htmlField = new NotesHTMLField();
        htmlField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    e.consume();
                    setTextMode();
                }
            }
        });
        setTextMode();
    }

    @Override
    public synchronized void addFocusListener(FocusListener l) {
        textField.addFocusListener(l);
    }

    @Override
    public synchronized void removeFocusListener(FocusListener l) {
        textField.removeFocusListener(l);
    }

    @Override
    public void setEnabled(boolean enabled) {
        textField.setEnabled(enabled);
        htmlField.setEnabled(enabled);
    }

    public void setEditable(boolean editable) {
        textField.setEditable(editable);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        textField.addKeyListener(l);
        htmlField.addKeyListener(l);
    }

    @Override
    public synchronized void removeKeyListener(KeyListener l) {
        textField.removeKeyListener(l);
        htmlField.removeKeyListener(l);
    }

    @Override
    public boolean requestFocusInWindow() {
        if (editing) {
            return textField.requestFocusInWindow();
        } else {
            return htmlField.requestFocusInWindow();
        }
    }

    public void setTextMode() {
        final Point p = getViewport().getViewPosition();

        setViewportView(textField);
        editing = true;

        SwingUtilities.invokeLater(() -> {
            getViewport().setViewPosition(p);
        });
    }

    public void setHTMLMode() {
        final Point p = getViewport().getViewPosition();

        htmlField.setText(textField.getText());
        setViewportView(htmlField);
        editing = false;
        
        SwingUtilities.invokeLater(() -> {
            getViewport().setViewPosition(p);
        });
    }

    public boolean isTextMode() {
        return editing;
    }

    public boolean isHTMLMode() {
        return !editing;
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
        if (isHTMLMode()) {
            htmlField.setText(text);
        }
    }

    public void insert(String text) {
        if (isTextMode()) {
            textField.insert(text);
        } else {
            textField.append(text);
            htmlField.setText(textField.getText());
        }
    }

    public void scrollToTop() {
        SwingUtilities.invokeLater(() -> {
            getViewport().setViewPosition(new Point(0,0));
        });
    }

    public class NotesTextField extends JEditorPane {

        private final static String CONTENT_TYPE_TEXT = "text/plain";
        private final EditorKit editorKitTEXT = createEditorKitForContentType(CONTENT_TYPE_TEXT);

        /** Constructs a new instance. */
        public NotesTextField() {
            super();
            super.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            super.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            super.setFont(new JTextField().getFont());
            super.setEditorKitForContentType(CONTENT_TYPE_TEXT, editorKitTEXT);
            super.setEditable(true);
            super.setBackground(TEXT_BG);
            super.setForeground(TEXT_FG);
            super.setFocusable(true);
            super.setPreferredSize(new Dimension(10, 34));
            super.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_TAB) {
                        e.consume();
                        if (e.isShiftDown()) {
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                        } else {
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                        }
                    }
                }
            });
        }

        public void insert(String string) {
            insert(string, getCaretPosition());
        }

        public void append(String string) {
            insert(string, getText().length());
        }
        
        private void insert(String string, int caretPos) {
            if (string == null || string.length() == 0) {
                return;
            }
            String text = getText();
            // prepend space to string if non-whitespace before caret position.
            try {
                String charBeforePos = text.substring(caretPos - 1, caretPos);
                if (charBeforePos.matches("\\S")) { // non-whitespace
                    string = " " + string;
                }
            } catch (Exception e) {
            }
            // append space to string if non-whitespace at caret position.
            try {
                String charAtPos = text.substring(caretPos, caretPos + 1);
                if (charAtPos.matches("\\S")) { // non-whitespace
                    string = string + " ";
                }
            } catch (Exception e) {
            }
            // insert the string at the caret position
            try {
                this.getDocument().insertString(caretPos, string, null);

            } catch (BadLocationException ble) {
            }
        }
    }

    public final class NotesHTMLField extends JEditorPane implements HyperlinkListener {

        /** Constructs a new instance. */
        public NotesHTMLField() {
            super();
            super.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            super.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            super.setFont(new JTextField().getFont());
            super.setEditorKitForContentType("text/html", new HTMLEditorKit());
            super.setContentType("text/html");
            super.setEditable(false);
            super.setEnabled(true);
            super.setFocusable(true);
            super.setBackground(HTML_BG);
            super.setForeground(HTML_FG);
            super.setPreferredSize(new Dimension(10, 34));
            super.addHyperlinkListener(this);
        }

        @Override
        public void setText(String text) {
            super.setText(NotesUtils.text2html(text == null ? "" : text));
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

        @Override
        public void setEditable(boolean editable) {
            super.setEditable(false);
        }

        @Override
        public String getText() {
            throw new UnsupportedOperationException("Not supported.");
        }
        
    }

    private final static Color HTML_BG = new Color(255, 255, 204);
    private final static Color HTML_FG = Color.BLACK;
    private final static Color TEXT_BG = Color.WHITE;
    private final static Color TEXT_FG = Color.BLACK;
    private final NotesTextField textField;
    private final NotesHTMLField htmlField;
    private boolean editing;

}
