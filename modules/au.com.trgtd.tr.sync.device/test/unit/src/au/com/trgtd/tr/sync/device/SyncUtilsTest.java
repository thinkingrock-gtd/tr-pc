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
package au.com.trgtd.tr.sync.device;

import static org.junit.Assert.*;
import org.junit.Test;

public class SyncUtilsTest {

    @Test
    public void escapingNullReturnsBlank() {
        assertEquals("", SyncUtils.escape(null));
    }

    @Test
    public void escapingBlankReturnsBlank() {
        assertEquals("", SyncUtils.escape(""));
    }

    @Test
    public void escapingFooReturnsFoo() {
        assertEquals("foo", SyncUtils.escape("foo"));
    }

    @Test
    public void escapingSlashAndPipeReturns() {
        assertEquals("foo/2bar/1", SyncUtils.escape("foo|bar/"));
    }

    @Test
    public void unescapingSlashAndPipe() {
        assertEquals("foo|bar/", SyncUtils.unEscape("foo/2bar/1"));
    }
}
