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
package tr.model.goals.dto;

public class LevelDTO {

    public final int id;
    public final String descr;
    public final String goalsIconPath;
    public final boolean goalsHaveProjects;
    public final boolean goalsHaveStart;
    public final boolean goalsHaveEnd;
    public final boolean goalsHaveVision;
    public final boolean goalsHaveAccountability;
    public final boolean goalsHaveRewards;
    public final boolean goalsHaveObstacles;
    public final boolean goalsHaveSupport;
    public final boolean goalsHaveBrainstorming;

    public LevelDTO(
            int id,
            String descr,
            boolean goalsHaveProjects,
            boolean goalsHaveStart,
            boolean goalsHaveEnd,
            boolean goalsHaveVision,
            boolean goalsHaveAccountability,
            boolean goalsHaveRewards,
            boolean goalsHaveObstacles,
            boolean goalsHaveSupport,
            boolean goalsHaveBrainstorming,
            String goalsIconPath)
    {
        this.id = id;
        this.descr = descr;
        this.goalsHaveProjects = goalsHaveProjects;
        this.goalsHaveStart = goalsHaveStart;
        this.goalsHaveEnd = goalsHaveEnd;
        this.goalsHaveVision = goalsHaveVision;
        this.goalsHaveAccountability = goalsHaveAccountability;
        this.goalsHaveRewards = goalsHaveRewards;
        this.goalsHaveObstacles = goalsHaveObstacles;
        this.goalsHaveSupport = goalsHaveSupport;
        this.goalsHaveBrainstorming = goalsHaveBrainstorming;
        this.goalsIconPath = goalsIconPath;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof LevelDTO levelDTO) {
            return levelDTO.id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
