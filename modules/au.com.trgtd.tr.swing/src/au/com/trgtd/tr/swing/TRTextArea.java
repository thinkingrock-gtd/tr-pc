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
package au.com.trgtd.tr.swing;

import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * JTextArea wrapper scrollpane.
 * 
 * @author jmoore
 */
public class TRTextArea extends JTextArea {

    private final static JTextField TEXTFIELD = new JTextField();
//  static { TEXTFIELD.setMargin(new Insets(0,0,0,0)); }

    public TRTextArea() {
        super();
        initComponent();
    }

    public TRTextArea(Document document) {
        super(document);
        initComponent();
    }

    public TRTextArea(Document document, String text, int rows, int cols) {
        super(document, text, rows, cols);
        initComponent();
    }
    
    public TRTextArea(int rows, int cols) {
        super(rows, cols);
        initComponent();
    }

    public TRTextArea(String text) {
        super(text);
        initComponent();
    }

    public TRTextArea(String text, int rows, int cols) {
        super(text, rows, cols);
        initComponent();
    }

    public void initComponent() {
        setOpaque(true);

        setBorder(TEXTFIELD.getBorder());
//      setMargin(TEXTFIELD.getMargin());
        setMargin(new Insets(0,0,0,0));
        setFont(TEXTFIELD.getFont());
        setLineWrap(true);
        setWrapStyleWord(true);
        // Change Tab and Ctrl-Tab to traverse fields.
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

}
