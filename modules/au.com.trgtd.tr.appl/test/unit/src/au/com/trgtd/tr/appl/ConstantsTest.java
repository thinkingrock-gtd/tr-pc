package au.com.trgtd.tr.appl;

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
import static org.junit.Assert.*;
import org.junit.Test;

public class ConstantsTest {

    @Test
    public void getPrefsWithNullPathThrows() {
        try {
            Constants.getPrefs(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertEquals("Bad preferences path: null", ex.getMessage());
        }
    }

    @Test
    public void getPrefsWithSlashStartingPathThrows() {
        try {
            Constants.getPrefs("/foo");
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertEquals("Bad preferences path: /foo", ex.getMessage());
        }
    }

    @Test
    public void getPrefsWithNonSlashStartingPath() {
        var prefs = Constants.getPrefs("foo");
        assertEquals("/ThinkingRock/3.5.1/foo", prefs.absolutePath());
    }
}
