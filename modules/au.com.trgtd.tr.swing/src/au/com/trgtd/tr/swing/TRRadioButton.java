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

import java.awt.Color;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

public class TRRadioButton extends JRadioButton {

    private static final JPanel JPANEL = new JPanel(); 
    private static final Color BG = JPANEL.getBackground();
    private static final Color FG = JPANEL.getForeground();

    public TRRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        this.setColors();
    }

    public TRRadioButton(String text, Icon icon) {
        super(text, icon);
        this.setColors();
    }

    public TRRadioButton(String text, boolean selected) {
        super(text, selected);
        this.setColors();
    }

    public TRRadioButton(String text) {
        super(text);
        this.setColors();
    }

    public TRRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
        this.setColors();
    }

    public TRRadioButton(Action a) {
        super(a);
        this.setColors();
    }

    public TRRadioButton(Icon icon) {
        super(icon);
        this.setColors();
    }

    public TRRadioButton() {
        this.setColors();
    }

    private void setColors() {
        this.setBackground(BG);
        this.setForeground(FG);
    }

    @Override
    public int getHeight() {
        if ("Aqua".equals(UIManager.getLookAndFeel().getID())) {
            return super.getHeight();
        } else {
            return 23;
        }
    }
}
