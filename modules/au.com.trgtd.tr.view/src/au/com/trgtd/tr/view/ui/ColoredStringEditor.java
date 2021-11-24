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

import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyEditorSupport;


public class ColoredStringEditor extends PropertyEditorSupport {

//  private static final Composite COMPOSITE_TRANSLUCENT = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
    private static final RenderingHints RENDER_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    static {
        RENDER_HINTS.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    private static final int ARC_WIDTH = 12;
    private static final int ARC_HEIGHT = 12;
//  private static final Color COLOR_BG_DEFAULT = UIManager.getDefaults().getColor("NbExplorerView.background");
    private static final Color COLOR_BG_DEFAULT = Color.GRAY;
    private static final Color COLOR_FG_DEFAULT = Color.BLACK;

    /**
     * Creates a new instance
     */
    public ColoredStringEditor() {
    }

    private ColoredString getColorString() {
        try {
            return (ColoredString)getValue();
        } catch (Exception ex) {
            return new ColoredStringImpl("", COLOR_BG_DEFAULT, COLOR_FG_DEFAULT);
        }
    }

    @Override
    public String getAsText() {
        return getColorString().getString();
    }

    @Override
    public void setAsText(String s) {
        getColorString().setString(s);
    }

    @Override
    public boolean isPaintable() {
        return true;
    }

    @Override
    public void paintValue(Graphics g, Rectangle rectangle) {
        ColoredString colorString = getColorString();

        Graphics2D g2d = (Graphics2D)g;
        FontMetrics fms = g2d.getFontMetrics();

        Color oldColor = g2d.getColor();
        Composite oldComposite = g2d.getComposite();
        RenderingHints oldRenderHints = g2d.getRenderingHints();

        g2d.setRenderingHints(RENDER_HINTS);

//      g2d.setComposite(COMPOSITE_TRANSLUCENT);
 
        g2d.setColor(colorString.getBGColor());
        g2d.fillRoundRect(rectangle.x-1, rectangle.y+2, rectangle.width-1, rectangle.height-4, ARC_WIDTH, ARC_HEIGHT);

        g2d.setComposite(oldComposite);
        g2d.setColor(colorString.getFGColor());
        g2d.drawString(colorString.getString(), rectangle.x, rectangle.y+(rectangle.height-fms.getHeight())/2+fms.getAscent());

        g2d.setRenderingHints(oldRenderHints);
        g2d.setColor(oldColor);
    }

}
