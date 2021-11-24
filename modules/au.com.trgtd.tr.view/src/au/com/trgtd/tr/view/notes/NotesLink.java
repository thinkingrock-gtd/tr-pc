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

public class NotesLink {
    
    public final String url;
    public final String text;

    /**
     * Constructs a new instance
     * @param url The link URL string which must not be null or an empty string.
     * @param text the human readable text for the link display, which can be
     * null to display the URL.
     */
    public NotesLink(String url, String text) {
        if (url == null || url.trim().length() == 0) {
            throw new IllegalArgumentException("Missing notes link URL.");
        }
        this.url = url.trim();
        this.text = (text != null && text.trim().length() > 0) ? text.trim() : null;
    }

    public String encode() {
        return text == null ? "[" + url + "]" : "[" + url + "|" + text + "]";
    }
    
}
