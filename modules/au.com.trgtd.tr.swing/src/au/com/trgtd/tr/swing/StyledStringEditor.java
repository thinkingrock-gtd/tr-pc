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
package au.com.trgtd.tr.swing;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * Table cell editor for StyledString cells.
 */
public class StyledStringEditor extends AbstractCellEditor implements TableCellEditor {

    /**
     * Construct a new instance.
     */
    public StyledStringEditor() {
        tf = new JTextField();
        tf.setBorder(BorderFactory.createEmptyBorder());
//      tf.setFont(tf.getFont().deriveFont(tf.getFont().getSize() - 1.0f));
    }

    /**
     * Gets the value contained in the editor.
     * @return The styled string.
     */
    public Object getCellEditorValue() {
        ss.setString(tf.getText());
        return ss;
    }

    /**
     * Gets the cell editor component.
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ss = (StyledString) value;
        tf.setText(ss.getString());
        tf.setForeground(ss.getForeground());
        if (isSelected) {
            tf.setBackground(table.getSelectionBackground());
        } else {
            tf.setBackground(table.getBackground());
        }
        return tf;
    }
    private final JTextField tf;
    private StyledString ss;
}
