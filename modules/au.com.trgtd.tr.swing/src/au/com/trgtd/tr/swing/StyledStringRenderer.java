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
