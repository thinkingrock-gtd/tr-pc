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

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;

public class TRComboBox<E> extends JComboBox<E> {

    public TRComboBox() {
        this.setOpaque(false);        
//      this.putClientProperty("JComponent.sizeVariant", "small");
    }

    public TRComboBox(E[] model) {
        super(model);
        this.setOpaque(false);        
    }

    public TRComboBox(ComboBoxModel<E> model) {
        super(model);
        this.setOpaque(false);        
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
