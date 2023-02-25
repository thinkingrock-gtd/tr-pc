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

import au.com.trgtd.tr.util.Utils;
import javax.swing.ImageIcon;

public class GoalIcon {

    public final String path;
    public final ImageIcon icon;

    public GoalIcon(String path, ImageIcon icon) {
        this.path = path;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof GoalIcon goalIcon) {
            return Utils.equal(goalIcon.path, path);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

}
