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

/**
 * Goal level controller.
 * 
 * @author Jeremy Moore
 */
public interface LevelCtrl extends Ctrl {

    public int getID();
    public String getDescr();
    public GoalIcon getGoalsIcon();
    public String getGoalsIconPath();
    public boolean isGoalsHaveProjects();
    public boolean isGoalsHaveStartDate();
    public boolean isGoalsHaveEndDate();
    public boolean isGoalsHaveAccountability();
    public boolean isGoalsHaveBrainstorming();
    public boolean isGoalsHaveObstacles();
    public boolean isGoalsHaveRewards();
    public boolean isGoalsHaveSupport();
    public boolean isGoalsHaveVision();

    public void setDescr(String s);
    public void setGoalsIconPath(String s);
    public void setGoalsHaveProjects(boolean b);
    public void setGoalsHaveStartDate(boolean b);
    public void setGoalsHaveEndDate(boolean b);
    public void setGoalsHaveAccountability(boolean goalsHaveAccountability);
    public void setGoalsHaveBrainstorming(boolean goalsHaveBrainstorming);
    public void setGoalsHaveObstacles(boolean goalsHaveObstacles);
    public void setGoalsHaveRewards(boolean goalsHaveRewards);
    public void setGoalsHaveSupport(boolean goalsHaveSupport);
    public void setGoalsHaveVision(boolean goalsHaveVision);

    public final static String PROP_DESCR = "level.descr";
    public final static String PROP_ICON_PATH = "level.icon.path";
    public final static String PROP_PROJECTS = "level.projects";
    public final static String PROP_START_DATES = "level.start.dates";
    public final static String PROP_END_DATES = "level.end.dates";
    public final static String PROP_ACCOUNTABILITY = "level.accountability";
    public final static String PROP_BRAINSTORMING = "level.brainstorming";
    public final static String PROP_OBSTACLES = "level.obstacles";
    public final static String PROP_REWARDS = "level.rewards";
    public final static String PROP_SUPPORT = "level.support";
    public final static String PROP_VISION = "level.vision";

}
