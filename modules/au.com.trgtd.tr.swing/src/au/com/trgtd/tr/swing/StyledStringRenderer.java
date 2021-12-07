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
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * Table cell renderer for StyledString cell values.
 *
 * @author Jeremy Moore
 */
public class StyledStringRenderer implements TableCellRenderer {

    private StyledLabel sl;
    private StyledString ss;

    /**
     * Constructs a new instance.
     */
    public StyledStringRenderer() {
//	sl = new StyledLabel();
//	sl.setBorder(BorderFactory.createEmptyBorder());
//	sl.setFont(sl.getFont().deriveFont(sl.getFont().getSize() - 1.0f));
//	sl.setFont(sl.getFont().deriveFont(Font.PLAIN));
//	sl.setOpaque(true);
    }

    /**
     * Returns the table cell renderer component which is the StyledLabel.
     */
    public Component getTableCellRendererComponent(JTable table, Object object,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (!(object instanceof StyledString)) {
            return null;
        }

        ss = (StyledString) object;

        sl = new StyledLabel();
//////  sl.setForeground(ss.getForeground());
        if (isSelected) {
            sl.setForeground(table.getSelectionForeground());
            sl.setBackground(table.getSelectionBackground());
        } else {
            sl.setForeground(ss.getForeground());
            sl.setBackground(ss.getBackground());
        }
        sl.strike = ss.isStrike();
        sl.setText(ss.getString());

        return sl;
    }

    private static class StyledLabel extends JLabel {

//        private final static Font df = new JLabel().getFont();
        private final static Font df = new JTextField().getFont();
//        private final static Font font = df.deriveFont(df.getSize() - 1.0f).deriveFont(Font.PLAIN);
        public boolean strike;

        public StyledLabel() {
            super();
            setBorder(BorderFactory.createEmptyBorder());
//            setFont(font);
            setOpaque(true);
        }

        @Override
        public String getText() {
            String text = super.getText();
            if (text == null || text.trim().length() == 0) {
                return "";
            }
            if (strike) {
                return "<html><strike>" + text + "</strike></html>";
            }
            return text;
        }
    }
}
