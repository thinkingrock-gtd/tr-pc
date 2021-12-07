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

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.UIManager;

public abstract class DayLabel extends JLabel {

    protected static final Dimension SIZE = new Dimension(36, 36);
    protected static final Color FG = UIManager.getDefaults().getColor("List.foreground");
    protected static final Color BG = UIManager.getDefaults().getColor("List.background");
    protected static final Color FG_SEL = UIManager.getDefaults().getColor("List.selectionForeground");
    protected static final Color BG_SEL = UIManager.getDefaults().getColor("List.selectionBackground");

    public void setNbr(int nbr) {
    }

    public int getNbr() {
        return 0;
    }

    public void select() {
    }

    public void deselect() {
    }

}
