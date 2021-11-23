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
package au.com.trgtd.tr.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * Utilities for views.
 *
 * @author Jeremy Moore
 */
public class ViewUtils {

//  private static final Color COLOR_SEP_LINE = new Color(153, 153, 153);
    private static final Color COLOR_SEP_TEXT = Color.BLACK;
    public static final boolean AQUA = "Aqua".equals(UIManager.getLookAndFeel().getID());
    public static final Color COLOR_PANEL_BG = AQUA 
            ? UIManager.getColor("NbExplorerView.background")
            : UIManager.getColor("Panel.background");
//  public static final Color COLOR_BORDERS = darker(COLOR_PANEL_BG.darker());
    public static final Color COLOR_BORDERS = darker(COLOR_PANEL_BG, 10.0);
//  public static final Color COLOR_TRANSPARENT = new Color(0,0,0,0);
    
    private static final Font FONT_BOLD = (new JLabel()).getFont().deriveFont(Font.BOLD);
    public static final Border BORDER_TOOLBAR = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.darkGray);

    public static void addSeparator(JPanel panel) {
        JSeparator sep = new JSeparator();
//        if (AQUA) {
//            sep.setOpaque(true);
//            sep.setBackground(COLOR_SEP_LINE);
////          separator.setForeground(COLOR_PANEL_BG);
//            sep.setForeground(COLOR_TRANSPARENT);
//        }
        panel.add(sep, "gaptop 1, gapbottom 1, aligny center, span, growx, wrap");
    }

    public static void addSeparator(JPanel panel, String text) {
        addSeparator(panel, new JLabel(text));
    }

    public static void addSeparator(JPanel panel, JLabel label) {
        label.setForeground(COLOR_SEP_TEXT);
        label.setFont(FONT_BOLD);
        JSeparator separ = new JSeparator();
////        if (Utilities.isUnix()) {
////        } else {
//        if (AQUA) {
//            separ.setOpaque(true);
//            separ.setBackground(COLOR_SEP_LINE);
////          separ.setForeground(COLOR_PANEL_BG);
//            separ.setForeground(COLOR_TRANSPARENT);
//        }
        panel.add(label, "span, split 2, aligny center");
        panel.add(separ, "gapleft rel, growx, wrap");
    }

    public static boolean isAquaLaF() {
        return AQUA;
    }

    public static Color darker(Color c, double percent) {
        double factor = 1 - (percent / 100);
        return new Color(
                Math.max((int)(c.getRed() * factor), 0),
                Math.max((int)(c.getGreen() * factor), 0),
                Math.max((int)(c.getBlue() * factor), 0));
    }

    public static Color lighter(Color c, double percent) {
        double factor = 1 + (percent / 100);
        return new Color(
                Math.min((int)(c.getRed() * factor), 255),
                Math.min((int)(c.getGreen() * factor), 255),
                Math.min((int)(c.getBlue() * factor), 255));
    }

}
