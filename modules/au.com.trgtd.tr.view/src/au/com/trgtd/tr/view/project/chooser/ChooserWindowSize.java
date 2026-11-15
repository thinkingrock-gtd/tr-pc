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

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * Decides how large the project chooser window should be.
 */
class ChooserWindowSize {

    private static final int MINIMUM_WIDTH = 400;
    private static final int MINIMUM_HEIGHT = 400;

    /** How much of the screen the window may take up on its own account. */
    private static final double SCREEN_SHARE = 0.8;

    private ChooserWindowSize() {
    }

    /**
     * Gets the size below which the chooser window is of no use any more.
     */
    static Dimension minimum() {
        return new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT);
    }

    /**
     * Gets the size for the chooser window.
     * @param content The size the window needs for its content.
     * @param userSize The size the user has given the window, or null if the
     * user has not resized it.
     * @param available The usable area of the screen the window is on.
     */
    static Dimension sizeFor(Dimension content, Dimension userSize, Rectangle available) {
        Dimension wanted = userSize == null ? content : userSize;
        return new Dimension(
                clamp(wanted.width, MINIMUM_WIDTH, (int)(available.width * SCREEN_SHARE)),
                clamp(wanted.height, MINIMUM_HEIGHT, (int)(available.height * SCREEN_SHARE)));
    }

    /** Clamps the given value, letting the maximum win over the minimum. */
    private static int clamp(int value, int minimum, int maximum) {
        return Math.min(Math.max(value, minimum), maximum);
    }
}
