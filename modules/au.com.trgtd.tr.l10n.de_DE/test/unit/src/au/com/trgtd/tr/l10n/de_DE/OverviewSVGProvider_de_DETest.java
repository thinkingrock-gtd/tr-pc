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
package au.com.trgtd.tr.l10n.de_DE;

import static org.junit.Assert.*;
import org.junit.Test;

public class OverviewSVGProvider_de_DETest {

    private final OverviewSVGProvider_de_DE instance = new OverviewSVGProvider_de_DE();

    @Test
    public void testGetLanguage() {
        assertEquals("de", instance.getLanguage());
    }

    @Test
    public void testGetCountry() {
        assertEquals("DE", instance.getCountry());
    }

//    @Test // TODO fixme
    public void testGetURL() {
        assertEquals(
                "nbfs://nbhost/SystemFileSystem/Overview/overview_de_DE.svg",
                instance.getURL().toString()
        );
    }
}
