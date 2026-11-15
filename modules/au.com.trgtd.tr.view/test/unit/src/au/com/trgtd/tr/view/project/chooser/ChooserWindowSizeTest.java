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
package au.com.trgtd.tr.view.project.chooser;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.Rectangle;
import org.junit.Test;

public class ChooserWindowSizeTest {

    /** A full HD screen, of which 80 percent is 1536 by 864. */
    private static final Rectangle SCREEN = new Rectangle(0, 0, 1920, 1080);

    @Test
    public void usesTheContentSizeWhenTheUserHasNotResized() {
        Dimension size = ChooserWindowSize.sizeFor(new Dimension(700, 500), null, SCREEN);

        assertEquals(new Dimension(700, 500), size);
    }
}
