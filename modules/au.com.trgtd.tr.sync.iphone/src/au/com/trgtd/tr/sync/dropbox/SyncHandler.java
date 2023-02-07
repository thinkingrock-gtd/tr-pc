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
package au.com.trgtd.tr.sync.dropbox;

import au.com.trgtd.tr.sync.iphone.SyncHandler103;
import au.com.trgtd.tr.sync.iphone.SyncProgress;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tr.model.Data;
import tr.model.action.Action;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.topic.Topic;

/**
 *
 * @author Jeremy Moore
 */
public class SyncHandler extends SyncHandler103 {

    public SyncHandler(BufferedReader in, PrintWriter out, SyncProgress progress) {
        super(in, out, progress);
    }

    protected Map<Integer, Action> getActionMap() {
        Map<Integer, Action> actionMap = new HashMap<>();
        List<Action> actionList = super.getActions();
        for (Action action : actionList) {
            actionMap.put(action.getID(), action);
        }
        return actionMap;
    }

    @Override
    protected List<Value> getEnergyValues() {
        return super.getEnergyValues();
    }

    @Override
    protected List<Value> getPriorityValues() {
        return super.getPriorityValues();
    }

    @Override
    protected List<Value> getTimeValues() {
        return super.getTimeValues();
    }

    @Override
    protected List<Context> getContexts() {
        return super.getContexts();
    }

    @Override
    protected Data getData() {
        return super.getData();
    }

    @Override
    protected String getEnergyID(Action action) {
        return super.getEnergyID(action);
    }

    @Override
    protected String getPriorityID(Action action) {
        return super.getPriorityID(action);
    }

    @Override
    protected String getTimeID(Action action) {
        return super.getTimeID(action);
    }

    @Override
    protected Topic getTopic(String idString) {
        return super.getTopic(idString);
    }

    @Override
    protected List<Topic> getTopics() {
        return super.getTopics();
    }

    @Override
    protected boolean isEnergyUsed() {
        return super.isEnergyUsed();
    }

    @Override
    protected boolean isPriorityUsed() {
        return super.isPriorityUsed();
    }

    @Override
    protected boolean isTimeUsed() {
        return super.isTimeUsed();
    }

//    @Override
//    protected void updateActionsToSend() {
//        super.updateActionsToSend();
//    }

    @Override
    protected void updateProgress(int done, int todo) {
        super.updateProgress(done, todo);
    }

    @Override
    protected String getEnergyMsg(Value value) {
        return super.getEnergyMsg(value);
    }

    @Override
    protected String getFileID() {
        return super.getFileID();
    }

    @Override
    protected String getMsg(Context context) {
        return super.getMsg(context);
    }

    @Override
    protected String getMsg(Topic topic) {
        return super.getMsg(topic);
    }

    @Override
    protected String getMsg(Action action) {
        return super.getMsg(action);
    }

    @Override
    protected String getMsg(Action action, int reduceSize) {
        return super.getMsg(action, reduceSize);
    }

    @Override
    protected String getPriorityMsg(Value value) {
        return super.getPriorityMsg(value);
    }

    @Override
    protected String getTimeMsg(Value value) {
        return super.getTimeMsg(value);
    }

    @Override
    protected void postponeAction(Action action, Date date, Integer hh, Integer mm) {
        super.postponeAction(action, date, hh, mm);
    }
    
}
