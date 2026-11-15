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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void neverGetsSmallerThanTheMinimum() {
        Dimension size = ChooserWindowSize.sizeFor(new Dimension(120, 80), null, SCREEN);

        assertEquals(new Dimension(400, 400), size);
    }

    @Test
    public void neverGrowsBeyondFourFifthsOfTheScreen() {
        Dimension size = ChooserWindowSize.sizeFor(new Dimension(4000, 3000), null, SCREEN);

        assertEquals(new Dimension(1536, 864), size);
    }

    @Test
    public void prefersTheSizeTheUserHasGivenTheWindow() {
        Dimension size = ChooserWindowSize.sizeFor(
                new Dimension(700, 500), new Dimension(900, 650), SCREEN);

        assertEquals(new Dimension(900, 650), size);
    }

    @Test
    public void letsTheUserGoBeyondTheScreenShareButNotBeyondTheScreen() {
        Dimension stretched = ChooserWindowSize.sizeFor(
                new Dimension(700, 500), new Dimension(1800, 1000), SCREEN);

        assertEquals("the user may claim more than the automatic share",
                new Dimension(1800, 1000), stretched);

        Dimension beyond = ChooserWindowSize.sizeFor(
                new Dimension(700, 500), new Dimension(4000, 3000), SCREEN);

        assertEquals("but not more than the screen", new Dimension(1920, 1080), beyond);
    }

    @Test
    public void aScreenSmallerThanTheMinimumWinsOverIt() {
        Dimension size = ChooserWindowSize.sizeFor(
                new Dimension(700, 500), null, new Rectangle(0, 0, 320, 240));

        assertEquals(new Dimension(256, 192), size);
    }

    @Test
    public void treatsASmallDifferenceAsTheWindowManagerAndNotAsTheUser() {
        Dimension applied = new Dimension(800, 524);

        assertFalse("the same size is nobody's doing",
                ChooserWindowSize.isUserSize(new Dimension(800, 524), applied));
        assertFalse("five pixels less is a window manager adjusting decorations",
                ChooserWindowSize.isUserSize(new Dimension(800, 519), applied));
        assertTrue("dragging the window is unmistakable",
                ChooserWindowSize.isUserSize(new Dimension(1200, 524), applied));
    }

    @Test
    public void withoutAnAppliedSizeNothingCountsAsTheUsersDoing() {
        assertFalse(ChooserWindowSize.isUserSize(new Dimension(800, 524), null));
    }

    @Test
    public void doesNotHandBackOneOfTheGivenDimensions() {
        Dimension content = new Dimension(700, 500);
        Dimension userSize = new Dimension(900, 650);

        assertNotSame(content, ChooserWindowSize.sizeFor(content, null, SCREEN));
        assertNotSame(userSize, ChooserWindowSize.sizeFor(content, userSize, SCREEN));
    }
}
