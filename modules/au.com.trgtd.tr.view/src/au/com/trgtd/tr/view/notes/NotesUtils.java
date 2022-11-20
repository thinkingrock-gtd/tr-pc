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
package au.com.trgtd.tr.view.notes;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Static utility methods.
 *
 * @author Jeremy Moore
 */
public class NotesUtils {
    
    private static final Logger LOG = Logger.getLogger("tr.view.notes.utils");
    
    private final static String[] SCHEMES = {
        "http://",
        "https://",
        "file:/",
        "ref:",
        "notes://",
        "outlook:"
    };
    
    /**
     * Converts a text string to HTML for rendering. Replaces recognized URL
     * strings within the string. URL strings in the following format are
     * recognized:
     *      (scheme)(word)
     *      [(scheme)(anything)]
     *      [(scheme)(anything)|(anything)]
     * where:
     *      (scheme) is any of "http://", "https://", "file:/", "www."
     *      (word) denotes any contiguous non-whitespace characters
     *      (anything) denotes any sequence or characters including whitespace
     *      [, ] and | are literal characters
     *
     * @param text the text to convert to HTML.
     * @return The HTML string.
     */
    public static String text2html(String text) {
        text = (text == null) ? "" : text;
        text = StringEscapeUtils.escapeXml10(text);
        text = convertURLs(text);
        text = preserveWhitespace(text);
        text = fixProblems(text);
        return "<html><body>" + text + "</body></html>";
    }
    
    /**
     * Replaces recognized URL strings in a string.
     * @param string The given string.
     * @return The converted string.
     */
    private static String convertURLs(String string) {
        
        StringBuilder sb = new StringBuilder();
        
        int lastEnd = 0;
        
        // match: [...] or contiguous non-whitespace
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]+\\]|[\\S]+");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            
            String skipped = string.substring(lastEnd, matcher.start());
            sb.append(skipped);
            
            String segment = string.substring(matcher.start(), matcher.end());
            
            if (segment.startsWith("[")) {
                sb.append(processBracketed(segment));
            } else {
                sb.append(processNotBracketed(segment));
            }
            
            lastEnd = matcher.end();
        }
        
        String skipped = string.substring(lastEnd, string.length());
        sb.append(skipped);
        
        return sb.toString();
    }
    
    /**
     * Processes a square bracketed term.
     * @param string In the form of: "[" + x + "]" or: "[" + x + "|" + y + "]".
     * @return If x is a recognized URL return the appropriate HTML A tag,
     * otherwise return the input string.
     */
    private static String processBracketed(String string) {
        
//        String url, txt, str = string.substring(1, string.length() - 1);        
//        for (int i = 0; i < SCHEMES.length; i++) {            
//            if (str.startsWith(SCHEMES[i])) {                
//                int p = str.indexOf("|");
//                if (p == -1) {
//                    url = str;
//                    txt = str;
//                } else {
//                    url = str.substring(0, p);
//                    txt = str.substring(p + 1);
//                }                 
//                return "<a href=\"" + url + "\">" + txt + "</a>";
//            }
//        }        
//        return string;

        
        // remove square brackets from beginning and end
        String str = string.substring(1, string.length() - 1);        
        
        // get URI part and text part
        String uri;
        String txt;
        int p = str.indexOf("|");
        if (p == -1) {
            uri = str;
            txt = str;
        } else {
            uri = str.substring(0, p);
            txt = str.substring(p + 1);
        }                 
        
        if (hasRecognizedScheme(uri) || isValidURI(uri)) {
            return "<a href=\"" + uri + "\">" + txt + "</a>";
        } else {
            return string;            
        }
    }
    
    /**
     * For a given string of contiguous non-whitespace characters, determines
     * whether it is a recognized URL string and if so returns a corresponding
     * HTML A tag.
     * @param string A string of contiguous non-whitespace characters.
     * @return If the string is a recognized, the corresponding HTML A tag
     * string, otherwise the original string is returned.
     */
    private static String processNotBracketed(String string) {
        
//        for (int i = 0; i < SCHEMES.length; i++) {
//            if (string.startsWith(SCHEMES[i])) {
//                String url = string;
//                return "<a href=\"" + url + "\">" + string + "</a>";
//            }
//        }
//        return string;        
        if (hasRecognizedScheme(string) || isValidURI(string)) {
            return "<a href=\"" + string + "\">" + string + "</a>";
        } else {
            return string;            
        }
    }
    
    private static boolean hasRecognizedScheme(String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        for (String scheme : SCHEMES) {
            if (string.startsWith(scheme)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isValidURI(String string) {
        
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        
        // A URI must have a scheme so must contain ":/" but not as the first
        // or last characters
        int index = string.indexOf(":/");
        if (index < 1 || index > string.length() -3) { 
            return false;
        }

        try {
            URI uri = new URI(string);
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }
    
    /**
     * Preserves whitespace in HTML for a given string.
     */
    private static String preserveWhitespace(String html) {
        
        // replace line terminators
        html = html.replace("\r\n", "<br>"); // carriage return + newline
        html = html.replace("\r", "<br>");   // carriage return
        html = html.replace("\n", "<br>");   // newline
        
        // replace whitespace
        html = html.replace("\u0020\u0020", "&nbsp;&nbsp;");  // pairs of spaces
        html = html.replace("\u000B", "&nbsp;");              // vertical tab
        html = html.replace("\t", "&nbsp;");                  // tab
        html = html.replace("\f", "&nbsp;");                  // form feed
        html = html.replace("\r", "&nbsp;");                  // carriage return
        
        return html;
    }
    
    /** Fix residual problems in HTML string. */
    private static String fixProblems(String html) {
        return html.replace("&apos;", "'"); // put back apostrophes
    }

    public static void openLinkWithDesktop(String urlStr) {
        
        LOG.log(Level.INFO, "Desktop browse URI string: {0}", urlStr);

        if (!Desktop.isDesktopSupported()) {
            LOG.log(Level.SEVERE, "Cannot open link. Desktop not supported.");
            return;
        }
        
        URI uri;
        try {
            uri = new URI(urlStr);
        } catch (URISyntaxException usex) {
            LOG.log(Level.INFO, usex.getMessage());
            return;
        }
        
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException ioex) {
            LOG.log(Level.INFO, ioex.getMessage());
        }
    }
    
}



