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
package au.com.trgtd.tr.view.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyEditorSupport;
import javax.swing.ImageIcon;

public class IconPropertyEditor extends PropertyEditorSupport {

    /** Creates a new instance. */
    public IconPropertyEditor() {
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics g, Rectangle r) {    
        ImageIcon icon = (ImageIcon)getValue();
        if (icon != null) {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            int yOffset = r.height > h ? (r.height - h) / 2 : 0;
            Graphics g2 = g.create(r.x, r.y, r.width, r.height);
            g2.drawImage(icon.getImage(), r.x, r.y + yOffset, w, h, null);
        }
    }
}
