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
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
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
