/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2021 Avente Pty Ltd
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
package au.com.trgtd.tr.extract;

import au.com.trgtd.tr.extract.Extract.FormatType;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormatTypeXmlEscapeTest {

    private final FormatType xml = FormatType.XML;

    @Test
    public void escapingNull_returnsBlank() {
        assertEquals("", xml.escape(null));
    }

    @Test
    public void escapingBlank_returnsBlank() {
        assertEquals("", xml.escape(""));
    }

    @Test
    public void escapingASCII_returnsSame() {
        String ascii = "ABCDEFghijkl012345()*+/=?$;:-_";
        assertEquals(ascii, xml.escape(ascii));
    }

    @Test
    public void escapingAmpersand_returnsEscapedUnicodeAmpersand() {
        String ascii = "a & b";
        assertEquals("a &amp;#38; b", xml.escape(ascii));
    }

    @Test
    public void escapingXmlChars_returnsThemEscaped() {
        String ascii = "<foo;bar>";
        assertEquals("&lt;foo;bar&gt;", xml.escape(ascii));
    }

    @Test
    public void escapingSmiley_returnsValidXml() {
        String smiley = "I’ll give you an update in few weeks 😉";
        assertEquals(smiley, xml.escape(smiley)
        );
    }
}
