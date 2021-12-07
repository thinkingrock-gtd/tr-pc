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

import au.com.trgtd.tr.appl.Constants;
import java.util.Date;

public class GoalRoot extends Goal {

    public GoalRoot() {
        super(Constants.ID_ROOT_GOAL, null, null, "Goals", null, null, null, null, null, null, null, null, null, null, null);
    }

    @Override
    public String getAccountability() {
        return null;
    }

    @Override
    public Date getAchievedDate() {
        return null;
    }

    @Override
    public String getBrainstorming() {
        return null;
    }

    @Override
    public Date getCreatedDate() {
        return null;
    }

    @Override
    public Date getEndDate() {
        return null;
    }

    @Override
    public String getNotes() {
        return null;
    }

    @Override
    public String getObstacles() {
        return null;
    }

    @Override
    public String getRewards() {
        return null;
    }

    @Override
    public Date getStartDate() {
        return null;
    }

    @Override
    public String getSupport() {
        return null;
    }

    @Override
    public Integer getTopicID() {
        return null;
    }

    @Override
    public String getVision() {
        return null;
    }

    @Override
    public void setAccountability(String accountability) {
    }

    @Override
    public void setAchievedDate(Date achieved) {
    }

    @Override
    public void setBrainstorming(String brainstorming) {
    }

    @Override
    public void setDescr(String descr) {
    }

    @Override
    public void setEndDate(Date end) {
    }

    @Override
    public void setLevelID(Integer levelID) {
    }

    @Override
    public void setNotes(String notes) {
    }

    @Override
    public void setObstacles(String obstacles) {
    }

    @Override
    public void setRewards(String rewards) {
    }

    @Override
    public void setStartDate(Date start) {
    }

    @Override
    public void setSupport(String support) {
    }

    @Override
    public void setTopicID(Integer topicID) {
    }

    @Override
    public void setVision(String vision) {
    }

}
