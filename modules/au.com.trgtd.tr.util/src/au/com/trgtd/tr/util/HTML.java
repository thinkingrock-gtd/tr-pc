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
package au.com.trgtd.tr.util;

import java.awt.Color;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Some static utility methods for HTML stuff.
 *
 * @author Jeremy Moore
 */
public final class HTML {
    
    private static final Logger LOG = Logger.getLogger("tr.util");
    
    /**
     * Utility method to format a color to HTML RGB color format (e.g. #FF0000 for Color.red).
     * @param color The color.
     * @return the HTML RGB color string.
     */
    public static final String format(Color c) {
        String r = (c.getRed() < 16) ? "0" + Integer.toHexString(c.getRed()) : Integer.toHexString(c.getRed());
        String g = (c.getGreen() < 16) ? "0" + Integer.toHexString(c.getGreen()) : Integer.toHexString(c.getGreen());
        String b = (c.getBlue() < 16) ? "0" + Integer.toHexString(c.getBlue()) : Integer.toHexString(c.getBlue());
        return "#" + r + g + b;
    }
    
    /**
     * Utility method to convert HTML to text.
     * @param html The string containing HTML.
     * @return a String containing the derived text .
     */
    public static final String html2text(String html) {
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            Reader reader = new StringReader(html);
            kit.read(reader, doc, 0);
            return doc.getText(0, doc.getLength());
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            return "";
        }
    }
    
}



