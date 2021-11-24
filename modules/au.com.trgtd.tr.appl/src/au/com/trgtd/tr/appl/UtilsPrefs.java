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
package au.com.trgtd.tr.appl;

import java.util.prefs.Preferences;

public class UtilsPrefs {

    /**
     * Copy old preferences value to new preferences value if it is set and not
     * equal to the default value.
     * @param oldprefs
     * @param newprefs
     * @param key
     * @param def
     */
    public static synchronized void copy(Preferences oldprefs, Preferences newprefs, String key, String def) {
        assert(oldprefs != null);
        assert(newprefs != null);
        assert(key != null);

        String val = oldprefs.get(key, null);
        if (val == null || val.equals(def)) {
            return;
        }

        newprefs.put(key, val);
    }

    /**
     * Copy old preferences value to new preferences value if it is set and not
     * equal to the default value.
     * @param oldprefs
     * @param newprefs
     * @param key
     * @param def
     */
    public static synchronized void copy(Preferences oldprefs, Preferences newprefs, String key, boolean def) {
        assert(oldprefs != null);
        assert(newprefs != null);
        assert(key != null);

        boolean value = oldprefs.getBoolean(key, def);
        if (value == def) {
            return;
        }
        newprefs.putBoolean(key, value);
    }

    /**
     * Copy old preferences value to new preferences value if it is set and not
     * equal to the default value.
     * @param oldprefs
     * @param newprefs
     * @param key
     * @param def
     */
    public static synchronized void copy(Preferences oldprefs, Preferences newprefs, String key, int def) {
        assert(oldprefs != null);
        assert(newprefs != null);
        assert(key != null);

        int value = oldprefs.getInt(key, def);
        if (value == def) {
            return;
        }
        newprefs.putInt(key, value);
    }

    /**
     * Copy old preferences value to new preferences value if it is set and not
     * equal to the default value.
     * @param oldprefs
     * @param newprefs
     * @param key
     * @param def
     */
    public static synchronized void copy(Preferences oldprefs, Preferences newprefs, String key, long def) {
        assert(oldprefs != null);
        assert(newprefs != null);
        assert(key != null);

        long value = oldprefs.getLong(key, def);
        if (value == def) {
            return;
        }
        newprefs.putLong(key, value);
    }
    
}
