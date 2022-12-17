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

public class FormatTypeCsvEscapeTest {

    private static final String Q = "&quot;";

    private final FormatType csv = FormatType.CSV;

    @Test
    public void escapingNull_returnsBlank() {
        assertEquals("", csv.escape(null));
    }

    @Test
    public void escapingBlank_returnsQuotedBlank() {
        assertEquals("&quot;&quot;", csv.escape(""));
    }

    @Test
    public void escapingASCII_returnsSameQuoted() {
        String ascii = "ABCDEFghijkl012345()*+/=?$;:-_";
        assertEquals(Q + ascii + Q, csv.escape(ascii));
    }

    @Test
    public void escapingQuoted_escapesQuotesEncoded() {
        String ascii = "foo \"bar\" baz";
        assertEquals(
                Q + "foo " + Q + Q + "bar" + Q + Q + " baz" + Q,
                csv.escape(ascii)
        );
    }

    @Test
    public void escapingAmpersand_returnsEscapedUnicodeAmpersand() {
        String ascii = "a & b";
        assertEquals(Q + "a &amp; b" + Q, csv.escape(ascii));
    }

    @Test
    public void escapingXmlChars_returnsQuoted() {
        String ascii = "<foo;bar>";
        assertEquals(Q + "&lt;foo;bar&gt;" + Q, csv.escape(ascii));
    }
}
