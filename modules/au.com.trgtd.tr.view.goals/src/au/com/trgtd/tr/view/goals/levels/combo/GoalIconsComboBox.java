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
package au.com.trgtd.tr.view.goals.levels.combo;

import au.com.trgtd.tr.appl.Constants;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tr.model.goals.ctrl.GoalIcon;
import tr.model.goals.ctrl.GoalIcons;

/**
 * Combo box for goal icons.
 *
 * @author Jeremy Moore
 */
public class GoalIconsComboBox extends JComboBox {

    /**
     * Constructs a new default instance.
     */
    public GoalIconsComboBox() {
        super(new GoalIconsComboBoxModel());
        setRenderer(new GoalIconsRenderer(this.getRenderer()));
        setFont(getFont().deriveFont(Font.PLAIN));
        setMaximumRowCount(Constants.COMBO_MAX_ROWS);
//      setToolTipText(NbBundle.getMessage(getClass(), "TTT_LevelsComboBox"));
    }

    private static class GoalIconsComboBoxModel extends DefaultComboBoxModel {

        private final GoalIcon[] goalIcons;
        
        /**
         * Creates a new default instance.
         */
        public GoalIconsComboBoxModel() {
            super();
            this.goalIcons = GoalIcons.getDefault().getGoalIcons();
        }

        @Override
        public Object getElementAt(int index) {
            return goalIcons[index];
        }

        @Override
        public int getSize() {
            return goalIcons.length;
        }
    }

    public static class GoalIconsRenderer extends JLabel implements ListCellRenderer {

        private final ListCellRenderer std;

        public GoalIconsRenderer(ListCellRenderer std) {
            if (std == null) {
                throw new NullPointerException("Standard renderer is null.");
            }
            this.std = std;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            JLabel lbl = (JLabel)std.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof GoalIcon) {
                lbl.setIcon(((GoalIcon)value).icon);
                lbl.setText("");
            }
            return lbl;
        }
    }

}
