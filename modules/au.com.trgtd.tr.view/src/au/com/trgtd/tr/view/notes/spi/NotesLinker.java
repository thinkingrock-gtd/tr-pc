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
package au.com.trgtd.tr.view.notes.spi;

import au.com.trgtd.tr.view.notes.NotesLink;
import javax.swing.ImageIcon;

/**
 * Service provider interface for creating and viewing links in notes fields.
 * 
 * @author Jeremy Moore
 */
public interface NotesLinker {

    public String getScheme();

    public ImageIcon getIcon();

    public int getShortcutMask();

    public int getShortcutKey();

    public String getToolTip();

    public NotesLink getUserLink();

    public void openUserLink(String url);

}
