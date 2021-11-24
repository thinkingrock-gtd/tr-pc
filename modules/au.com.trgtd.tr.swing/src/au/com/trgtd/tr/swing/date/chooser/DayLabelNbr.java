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
package au.com.trgtd.tr.swing.date.chooser;

import java.awt.Font;
import javax.swing.JLabel;

public final class DayLabelNbr extends DayLabel {

    public int nbr;

    public DayLabelNbr(int nbr) {
        super.setOpaque(true);
        super.setHorizontalAlignment(JLabel.CENTER);
        super.setPreferredSize(SIZE);
        super.setMaximumSize(SIZE);
        super.setMinimumSize(SIZE);
        super.setFont(super.getFont().deriveFont(Font.BOLD));
        this.setNbr(nbr);
        this.deselect();
    }

    @Override
    public void setNbr(int nbr) {
        this.nbr = nbr;
        super.setText(nbr > 0 ? Integer.toString(nbr) : "");
    }

    @Override
    public int getNbr() {
        return nbr;
    }

    @Override
    public final void select() {
        super.setForeground(FG_SEL);
        super.setBackground(BG_SEL);
        super.invalidate();
        super.repaint();
    }

    @Override
    public final void deselect() {
        super.setForeground(FG);
        super.setBackground(BG);
        super.invalidate();
        super.repaint();
    }
    
}
