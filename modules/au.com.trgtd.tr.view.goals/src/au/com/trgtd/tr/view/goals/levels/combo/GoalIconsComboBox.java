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
public class GoalIconsComboBox extends JComboBox<GoalIcon> {

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

    private static class GoalIconsComboBoxModel extends DefaultComboBoxModel<GoalIcon>{

        private final GoalIcon[] goalIcons;
        
        /**
         * Creates a new default instance.
         */
        public GoalIconsComboBoxModel() {
            super();
            this.goalIcons = GoalIcons.getDefault().getGoalIcons();
        }

        @Override
        public GoalIcon getElementAt(int index) {
            return goalIcons[index];
        }

        @Override
        public int getSize() {
            return goalIcons.length;
        }
    }

    public static class GoalIconsRenderer extends JLabel implements  ListCellRenderer<GoalIcon> {

        private final ListCellRenderer<? super GoalIcon> std;

        public GoalIconsRenderer(ListCellRenderer<? super GoalIcon> std) {
            if (std == null) {
                throw new NullPointerException("Standard renderer is null.");
            }
            this.std = std;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends GoalIcon> list, GoalIcon goalIcon,
                int index, boolean isSelected, boolean cellHasFocus) {

            JLabel lbl = (JLabel)std.getListCellRendererComponent(list, goalIcon, index, isSelected, cellHasFocus);

            if (goalIcon != null) {
                lbl.setIcon(goalIcon.icon);
                lbl.setText("");
            }
            return lbl;
        }
    }

}
