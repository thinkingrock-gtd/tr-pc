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
