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
package tr.model.goals.ctrl.impl;

import au.com.trgtd.tr.util.Utils;
import tr.model.goals.ctrl.GoalIcon;
import tr.model.goals.ctrl.GoalIcons;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.dto.LevelDTO;

public class LevelCtrlImpl extends CtrlImpl implements LevelCtrl {

    private final LevelDTO levelDTO;

    private String description;
    private String goalsIconPath;
    private Boolean goalsHaveProjects;
    private Boolean goalsHaveStart;
    private Boolean goalsHaveEnd;
    private Boolean goalsHaveVision;
    private Boolean goalsHaveAccountability;
    private Boolean goalsHaveRewards;
    private Boolean goalsHaveObstacles;
    private Boolean goalsHaveSupport;
    private Boolean goalsHaveBrainstorming;
    
    private GoalIcon goalsIcon;

    public LevelCtrlImpl(LevelDTO levelDTO) {
        super(levelDTO.id);
        this.levelDTO = levelDTO;
    }

    public String getName() {
        return getDescr();
    }


    @Override
    public boolean equals(Object that) {
        if (that instanceof LevelCtrl levelCtrl) {
            return levelCtrl.getID() == this.getID();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getID();
    }

    @Override
    public String toString() {
        return getDescr();
    }

    public String getDescr() {
        return description != null ? description : levelDTO.descr;
    }

    public boolean isGoalsHaveProjects() {
        return goalsHaveProjects != null ? goalsHaveProjects : levelDTO.goalsHaveProjects;
    }

    public boolean isGoalsHaveStartDate() {
        return goalsHaveStart != null ? goalsHaveStart : levelDTO.goalsHaveStart;
    }

    public boolean isGoalsHaveEndDate() {
        return goalsHaveEnd != null ? goalsHaveEnd : levelDTO.goalsHaveEnd;
    }

    public boolean isGoalsHaveAccountability() {
        return goalsHaveAccountability != null ? goalsHaveAccountability : levelDTO.goalsHaveAccountability;
    }

    public boolean isGoalsHaveBrainstorming() {
        return goalsHaveBrainstorming != null ? goalsHaveBrainstorming : levelDTO.goalsHaveBrainstorming;
    }

    public boolean isGoalsHaveObstacles() {
        return goalsHaveObstacles != null ? goalsHaveObstacles : levelDTO.goalsHaveObstacles;
    }

    public boolean isGoalsHaveRewards() {
        return goalsHaveRewards != null ? goalsHaveRewards : levelDTO.goalsHaveRewards;
    }

    public boolean isGoalsHaveSupport() {
        return goalsHaveSupport != null ? goalsHaveSupport : levelDTO.goalsHaveSupport;
    }

    public boolean isGoalsHaveVision() {
        return goalsHaveVision != null ? goalsHaveVision : levelDTO.goalsHaveVision;
    }


    public void setDescr(String newValue) {
        if (newValue == null || newValue.trim().length() == 0) {
            throw new IllegalArgumentException("Level description can not be blank.");
        }
        String oldValue = getDescr();
        if (newValue.equals(oldValue)) {
            return;
        }
        this.description = newValue;
        firePropertyChange(PROP_DESCR, oldValue, newValue);
    }

    public void setGoalsHaveProjects(boolean newValue) {
        boolean oldValue = isGoalsHaveProjects();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveProjects = newValue;
        firePropertyChange(PROP_PROJECTS, oldValue, newValue);
    }

    public void setGoalsHaveStartDate(boolean newValue) {
        boolean oldValue = isGoalsHaveStartDate();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveStart = newValue;
        firePropertyChange(PROP_START_DATES, oldValue, newValue);
    }

    public void setGoalsHaveEndDate(boolean newValue) {
        boolean oldValue = isGoalsHaveEndDate();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveEnd = newValue;
        firePropertyChange(PROP_END_DATES, oldValue, newValue);
    }


    public void setGoalsHaveAccountability(boolean newValue) {
        boolean oldValue = isGoalsHaveAccountability();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveAccountability = newValue;
        firePropertyChange(PROP_ACCOUNTABILITY, oldValue, newValue);
    }

    public void setGoalsHaveBrainstorming(boolean newValue) {
        boolean oldValue = isGoalsHaveBrainstorming();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveBrainstorming = newValue;
        firePropertyChange(PROP_BRAINSTORMING, oldValue, newValue);
    }

    public void setGoalsHaveObstacles(boolean newValue) {
        boolean oldValue = isGoalsHaveObstacles();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveObstacles = newValue;
        firePropertyChange(PROP_OBSTACLES, oldValue, newValue);
    }

    public void setGoalsHaveRewards(boolean newValue) {
        boolean oldValue = isGoalsHaveRewards();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveRewards = newValue;
        firePropertyChange(PROP_REWARDS, oldValue, newValue);
    }

    public void setGoalsHaveSupport(boolean newValue) {
        boolean oldValue = isGoalsHaveSupport();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveSupport = newValue;
        firePropertyChange(PROP_SUPPORT, oldValue, newValue);
    }

    public void setGoalsHaveVision(boolean newValue) {
        boolean oldValue = isGoalsHaveVision();
        if (newValue == oldValue) {
            return;
        }
        this.goalsHaveVision = newValue;
        firePropertyChange(PROP_VISION, oldValue, newValue);
    }

    public GoalIcon getGoalsIcon() {
        if (goalsIcon == null) {
            goalsIcon = GoalIcons.getDefault().getGoalIcon(getGoalsIconPath());
        }
        return goalsIcon;
    }

    public String getGoalsIconPath() {
        return goalsIconPath != null ? goalsIconPath : levelDTO.goalsIconPath;
    }

    public void setGoalsIconPath(String newValue) {
        String oldValue = getGoalsIconPath();
        if (Utils.equal(newValue, oldValue)) {
            return;
        }
        this.goalsIconPath = newValue;
        this.goalsIcon = null;
        firePropertyChange(PROP_ICON_PATH, oldValue, newValue);
    }

}
