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
package tr.model.goals.ctrl;

import org.openide.util.ImageUtilities;

/**
 * Available goal icons.
 *
 * @author Jeremy Moore
 */
public class GoalIcons {

    private static GoalIcons instance;

    public static GoalIcons getDefault() {
        if (instance == null) {
            instance = new GoalIcons();
        }
        return instance;
    }


    private static final String[] ICON_PATHS = {
        "au/com/trgtd/tr/view/goals/resource/Circle.png",
        "au/com/trgtd/tr/view/goals/resource/Ellipse.png",
        "au/com/trgtd/tr/view/goals/resource/Triangle.png",
        "au/com/trgtd/tr/view/goals/resource/Square.png",
        "au/com/trgtd/tr/view/goals/resource/Rectangle.png",
        "au/com/trgtd/tr/view/goals/resource/Rhomb.png",
        "au/com/trgtd/tr/view/goals/resource/Pentagon.png",
        "au/com/trgtd/tr/view/goals/resource/Hexagon.png",
        "au/com/trgtd/tr/view/goals/resource/Octagon.png",
        "au/com/trgtd/tr/view/goals/resource/Star.png",
        "au/com/trgtd/tr/view/goals/resource/NewCircle.png",
        "au/com/trgtd/tr/view/goals/resource/NewTriangle.png",
        "au/com/trgtd/tr/view/goals/resource/NewSquare.png",
        "au/com/trgtd/tr/view/goals/resource/NewDiamond.png",
        "au/com/trgtd/tr/view/goals/resource/NewPentagon.png",
        "au/com/trgtd/tr/view/goals/resource/NewHexagon.png",
        "au/com/trgtd/tr/view/goals/resource/NewOctagon.png",
    };

    private final GoalIcon[] goalIcons;

    private GoalIcons() {
        goalIcons = new GoalIcon[ICON_PATHS.length];
        for (int i = 0; i < ICON_PATHS.length; i++) {
            goalIcons[i] = new GoalIcon(ICON_PATHS[i],
                    ImageUtilities.loadImageIcon(ICON_PATHS[i], false));
        }
    }

    public GoalIcon[] getGoalIcons() {
        return goalIcons;
    }

    public GoalIcon getGoalIcon(String path) {
        if (path != null) {
            for (int i = 0; i < ICON_PATHS.length; i++) {
                if (path.hashCode() == goalIcons[i].path.hashCode()) {
                    return goalIcons[i];
                }
            }
        }
        return this.getDefaultGoalIcon();
    }

    public GoalIcon getDefaultGoalIcon() {
        return goalIcons[0];
    }

}
