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
package tr.model.goals.data;

/**
 * Records the fact that a project has a goal in the many to many relationship
 * between projects and goals.
 *
 * @author Jeremy Moore
 */
public class ProjectGoal {

    public final int goalID;
    public final int projectID;

    public ProjectGoal(int goalID, int projectID) {
        this.goalID = goalID;
        this.projectID = projectID;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ProjectGoal that) {
            return that.goalID == this.goalID && that.projectID == this.projectID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + this.goalID;
        hash = 73 * hash + this.projectID;
        return hash;
    }

}
