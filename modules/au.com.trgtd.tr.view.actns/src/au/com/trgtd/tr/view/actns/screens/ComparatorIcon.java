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
package au.com.trgtd.tr.view.actns.screens;

import java.util.Comparator;
import javax.swing.Icon;

/**
 * Comparator for due dates.
 *
 * @author Jeremy Moore
 */
public class ComparatorIcon implements Comparator<Icon> {
    
    public int compare(Icon icon1, Icon icon2) {
        if (icon1 == icon2) return 0;
        if (icon1 == null) return 1;
        if (icon2 == null) return -1;
        return icon1.toString().compareTo(icon2.toString());
    }
    
}

