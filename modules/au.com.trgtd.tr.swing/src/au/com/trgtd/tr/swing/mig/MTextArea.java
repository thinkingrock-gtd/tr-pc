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
package au.com.trgtd.tr.swing.mig;

import java.awt.KeyboardFocusManager;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.text.Document;

/**
 * JTextArea extension for MIGLayout.
 *
 * @author Jeremy Moore
 */
public class MTextArea extends JTextArea {

    public MTextArea() {
        super();
        initComponent();
    }

    public MTextArea(Document document) {
        super(document);
        initComponent();
    }

    public MTextArea(Document document, String text, int rows, int cols) {
        super(document, text, rows, cols);
        initComponent();
    }

    public MTextArea(int rows, int cols) {
        super(rows, cols);
        initComponent();
    }

    public MTextArea(String text) {
        super(text);
        initComponent();
    }

    public MTextArea(String text, int rows, int cols) {
        super(text, rows, cols);
        initComponent();
    }

    public void initComponent() {
        setOpaque(true);
        setBorder(UIManager.getBorder("TextField.border"));
        setFont(UIManager.getFont("TextField.font"));
//      setLineWrap(true);
        setWrapStyleWord(true);
        // Change Tab and Ctrl-Tab to go to next and previous field.
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }
}
