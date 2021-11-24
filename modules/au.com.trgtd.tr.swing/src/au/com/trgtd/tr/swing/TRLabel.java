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

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class TRLabel extends JLabel {

    public TRLabel() {
    }

    public TRLabel(Icon arg0) {
        super(arg0);
    }

    public TRLabel(Icon arg0, int arg1) {
        super(arg0, arg1);
    }

    public TRLabel(String arg0) {
        super(arg0);
    }

    public TRLabel(String arg0, int arg1) {
        super(arg0, arg1);
    }

    public TRLabel(String arg0, Icon arg1, int arg2) {
        super(arg0, arg1, arg2);
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
