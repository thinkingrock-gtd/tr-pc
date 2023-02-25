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

import java.io.Serializable;

public class Level implements Serializable {

    private final Integer id;
    private String descr;
    private boolean goalsHaveProjects;
    private boolean goalsHaveStart;
    private boolean goalsHaveEnd;
    private boolean goalsHaveVision;
    private boolean goalsHaveAccountability;
    private boolean goalsHaveRewards;
    private boolean goalsHaveObstacles;
    private boolean goalsHaveSupport;
    private boolean goalsHaveBrainstorming;
    private String goalsIconPath;

    public Level(
            Integer id,
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
        if (id == null) {
            throw new IllegalArgumentException("ID can not be null.");
        }
        if (descr == null) {
            throw new IllegalArgumentException("Description can not be null.");
        }
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
        if (that instanceof Level level) {
            return level.getID() == this.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getID() {
        return id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        if (descr == null) {
            throw new IllegalArgumentException("Description can not be null.");
        }
        this.descr = descr;
    }

    public boolean isGoalsHaveProjects() {
        return goalsHaveProjects;
    }

    public void setGoalsHaveProjects(boolean b) {
        this.goalsHaveProjects = b;
    }

    public boolean isGoalsHaveEnd() {
        return goalsHaveEnd;
    }

    public void setGoalsHaveEnd(boolean b) {
        this.goalsHaveEnd = b;
    }

    public boolean isGoalsHaveStart() {
        return goalsHaveStart;
    }

    public void setGoalsHaveStart(boolean goalsHaveStartDate) {
        this.goalsHaveStart = goalsHaveStartDate;
    }

    public boolean isGoalsHaveAccountability() {
        return goalsHaveAccountability;
    }

    public void setGoalsHaveAccountability(boolean goalsHaveAccountability) {
        this.goalsHaveAccountability = goalsHaveAccountability;
    }

    public boolean isGoalsHaveBrainstorming() {
        return goalsHaveBrainstorming;
    }

    public void setGoalsHaveBrainstorming(boolean goalsHaveBrainstorming) {
        this.goalsHaveBrainstorming = goalsHaveBrainstorming;
    }

    public boolean isGoalsHaveObstacles() {
        return goalsHaveObstacles;
    }

    public void setGoalsHaveObstacles(boolean goalsHaveObstacles) {
        this.goalsHaveObstacles = goalsHaveObstacles;
    }

    public boolean isGoalsHaveRewards() {
        return goalsHaveRewards;
    }

    public void setGoalsHaveRewards(boolean goalsHaveRewards) {
        this.goalsHaveRewards = goalsHaveRewards;
    }

    public boolean isGoalsHaveSupport() {
        return goalsHaveSupport;
    }

    public void setGoalsHaveSupport(boolean goalsHaveSupport) {
        this.goalsHaveSupport = goalsHaveSupport;
    }

    public boolean isGoalsHaveVision() {
        return goalsHaveVision;
    }

    public void setGoalsHaveVision(boolean goalsHaveVision) {
        this.goalsHaveVision = goalsHaveVision;
    }

    public String getGoalsIconPath() {
        return goalsIconPath;
    }

    public void setGoalsIconPath(String goalsIconPath) {
        this.goalsIconPath = goalsIconPath;
    }

}
