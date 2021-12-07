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
package au.com.trgtd.tr.view.actns;

import org.openide.util.NbBundle;

/**
 * Resources.
 *
 * @author Jeremy Moore
 */
public class Resources {
    /**
     * Gets the text for the given key from the resource Bundle.properties file.
     * @param key The key value.
     * @return the text value if the key exists, otherwise the key.
     */
    public static String getText(String key) {
        try {
            return NbBundle.getMessage(Resources.class, key);
        } catch (Exception ex) {
            return key;
        }
    }            
    
}
