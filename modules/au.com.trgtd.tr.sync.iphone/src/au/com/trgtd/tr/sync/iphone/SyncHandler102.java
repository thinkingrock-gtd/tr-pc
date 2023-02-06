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
package au.com.trgtd.tr.sync.iphone;

import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.util.DateUtils;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import tr.model.Data;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

public class SyncHandler102 extends SyncHandler {

    public static final String VERSION = "V1.02";
    public static final int MAX_MSG_SIZE = 5000;
    public static final char ETX = '\u0003';

    public SyncHandler102(BufferedReader in, PrintWriter out, SyncProgress progress) {
        super(in, out, progress);
    }

    protected boolean sync(SyncMsg102.MsgHandshake msgHs) throws Exception {
        return _sync(msgHs);
    }

    private boolean _sync(SyncMsg102.MsgHandshake msgHs) throws Exception {

        nbrThoughtsToGet = 0;
        nbrThoughtsGot = 0;
        nbrActionsToGet = 0;
        nbrActionsGot = 0;
        nbrContextsToSend = getContexts().size();
        nbrContextsSent = 0;
        nbrTopicsToSend = getTopics().size();
        nbrTopicsSent = 0;
        nbrActionsToSend = getActions().size();
        nbrActionsSent = 0;
        nbrTimesToSend = getTimeValues().size();
        nbrTimesSent = 0;
        nbrEnergiesToSend = getEnergyValues().size();
        nbrEnergiesSent = 0;
        nbrPrioritiesToSend = getPriorityValues().size();
        nbrPrioritiesSent = 0;

        if (msgHs == null) {
            LOG.severe("Handshake message not recognised.");
            return false;
        }
        if (msgHs.nbrThoughts > -1) {
            nbrThoughtsToGet = msgHs.nbrThoughts;
        } else {
            LOG.warning("Could not determine number of thoughts to receive.");
            nbrThoughtsToGet = 0;
        }
        if (msgHs.nbrActions > -1) {
            nbrActionsToGet = msgHs.nbrActions;
        } else {
            LOG.warning("Could not determine number of action updates to receive.");
            nbrActionsToGet = 0;
        }

        send(VERSION + "|HELLO" +
                "|Contexts|" + nbrContextsToSend +
                "|Topics|" + nbrTopicsToSend +
                "|Actions|" + nbrActionsToSend +
                "|Times|" + nbrTimesToSend +
                "|Energies|" + nbrEnergiesToSend +
                "|Priorities|" + nbrPrioritiesToSend +
                "|");

        while (true) {

            updateProgress();

            if (cancelSyncing) {
                send("Q");
                LOG.info("Server syncing was cancelled.");
                return false;
            }

            SyncMsg102 inMsg = SyncMsg102.getNextMsg(in);
            if (inMsg == null) {
                LOG.warning("RECIEVED: Unexpected end of input stream.");
                return false;
            }

            LOG.log(Level.INFO, "RECIEVED:{0}", inMsg.toString());

            switch (inMsg.type) {
                case Thought: {
                    SyncMsg102.MsgThought msgThought = (SyncMsg102.MsgThought) inMsg;
                    Thought thought = new Thought(getData().getNextID());
                    thought.setDescription(msgThought.title);
                    thought.setNotes(msgThought.notes);
                    thought.setTopic(getTopic(msgThought.topic));
                    getData().getThoughtManager().add(thought);
                    nbrThoughtsGot++;
                    LOG.log(Level.INFO, "TR added thought: {0}", thought.getDescription());
                    send("OK");
                    break;
                }
                case ActionUpdate: {
                    SyncMsg102.MsgActionUpdate msgActionUpdate = (SyncMsg102.MsgActionUpdate) inMsg;
                    Action action = Services.instance.getAction(msgActionUpdate.id);
                    if (action == null) {
                        LOG.log(Level.WARNING, "COULD NOT FIND ACTION WITH ID: {0}", msgActionUpdate.id);
                        break;
                    }
                    if (msgActionUpdate.done) {
                        LOG.log(Level.INFO, "TR updated action with id: {0} Done set to True", msgActionUpdate.id);
                        action.setDone(true);
                        updateActionsToSend();
                    }
                    nbrActionsGot++;
                    send("OK");
                    break;
                }
                case NextAction: {
                    Action action = getNextActionToSend();
                    if (action == null) {
                        send("END");
                    } else {
                        send(getMsg(action));
                    }
                    break;
                }
                case NextTopic: {
                    if (nbrTopicsSent < getTopics().size()) {
                        send(getMsg(getTopics().get(nbrTopicsSent)));
                        nbrTopicsSent++;
                    } else {
                        send("END");
                    }
                    break;
                }
                case NextContext: {
                    if (nbrContextsSent < getContexts().size()) {
                        send(getMsg(getContexts().get(nbrContextsSent)));
                        nbrContextsSent++;
                    } else {
                        send("END");
                    }
                    break;
                }

                case NextTime: {
                    if (nbrTimesSent < getTimeValues().size()) {
                        send(getTimeMsg(getTimeValues().get(nbrTimesSent++)));
                    } else {
                        send("END");
                    }
                    break;
                }
                case NextEnergy: {
                    if (nbrEnergiesSent < getEnergyValues().size()) {
                        send(getEnergyMsg(getEnergyValues().get(nbrEnergiesSent++)));
                    } else {
                        send("END");
                    }
                    break;
                }
                case NextPriority: {
                    if (nbrPrioritiesSent < getPriorityValues().size()) {
                        send(getPriorityMsg(getPriorityValues().get(nbrPrioritiesSent++)));
                    } else {
                        send("END");
                    }
                    break;
                }
                case HangUp: {
                    LOG.info("Client has hung up");
                    return true;
                }
                case Handshake: {
                    LOG.warning("Unexpected handshake message.");
                    break;
                }
                case Unknown:
                default: {
                    LOG.warning("Unknown message");
                }
            }
        }
    }

    private Date getTodayPlus30Days() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getEnd(new Date()));
        cal.add(Calendar.DAY_OF_YEAR, 30);
        return cal.getTime();
    }

    @Override
    protected List<Action> getActions() {
        if (actions == null) {
            Date todayPlus30Days = this.getTodayPlus30Days();
            actions = Services.instance.getAllActions();
            for (Iterator<Action> iterator = actions.iterator(); iterator.hasNext();) {
                Action action = iterator.next();
                if (action.isDone()) {
                    iterator.remove();
                    continue;
                }
                if (action.isStateASAP()) {
                    continue;
                }
                Date actionDate = action.getActionDate();
                if (actionDate == null || !actionDate.before(todayPlus30Days)) {
                    iterator.remove();
                }
            }
        }
        return actions;
    }

    protected List<Value> timeValues;
    protected List<Value> getTimeValues() {
        if (timeValues == null) {
            Data data = getData();
            if (data == null || !data.getTimeCriterion().isUse()) {
                timeValues = Collections.emptyList();
            } else {
                timeValues = data.getTimeCriterion().values.list();
            }
        }
        return timeValues;
    }

    protected List<Value> energyValues;
    protected List<Value> getEnergyValues() {
        if (energyValues == null) {
            Data data = getData();
            if (data == null || !data.getEnergyCriterion().isUse()) {
                energyValues = Collections.emptyList();
            } else {
                energyValues = data.getEnergyCriterion().values.list();
            }
        }
        return energyValues;
    }
    protected List<Value> priorityValues;

    protected List<Value> getPriorityValues() {
        if (priorityValues == null) {
            Data data = getData();
            if (data == null || !data.getPriorityCriterion().isUse()) {
                priorityValues = Collections.emptyList();
            } else {
                priorityValues = data.getPriorityCriterion().values.list();
            }
        }
        return priorityValues;
    }

    private Action getNextActionToSend() {
        while (nbrActionsSent < getActions().size()) {
            Action action = getActions().get(nbrActionsSent++);
            if (!action.isDone()) {
                return action;
            } else {
                LOG.log(Level.INFO, "TR not sending action with id: {0} Was updated to Done", action.getID());
            }
        }
        return null;
    }

    private synchronized void updateProgress() {
        int todo =
                nbrActionsToSend +
                nbrContextsToSend +
                nbrTopicsToSend +
                nbrThoughtsToGet +
                nbrActionsToGet +
                nbrTimesToSend +
                nbrEnergiesToSend +
                nbrPrioritiesToSend;

        int done =
                nbrActionsSent +
                nbrContextsSent +
                nbrTopicsSent +
                nbrThoughtsGot +
                nbrActionsGot +
                nbrTimesSent +
                nbrEnergiesSent +
                nbrPrioritiesSent;

        updateProgress(done, todo);
    }

    private String getMsg(Context context) {
        // e.g "V1.02|Context|ID|0|Title|None|"
        return VERSION + "|Context|ID|" + context.getID() + "|Title|" + SyncUtils.escape(context.getName()) + "|";
    }

    private String getMsg(Topic topic) {
        // e.g "V1.02|Topic|ID|2|Title|Work|"
        return VERSION + "|Topic|ID|" + topic.getID() + "|Title|" + SyncUtils.escape(topic.getName()) + "|";
    }

    private String getMsg(Action action) {
        String msg = getMsg(action, 0);
        while (msg.length() > MAX_MSG_SIZE) {
            msg = getMsg(action, msg.length() - MAX_MSG_SIZE);
        }
        return msg;
    }

    private String getMsg(Action action, int reduceSize) {
        // e.g "V1.02|Action|ID|208|Title|Buy Chocolate|Notes||Context|102|Topic|1|Date|20090701|"
        String notes = action.getNotes();
        if (reduceSize > 0) {
            int p = notes.length() - reduceSize;
            notes = (p < 1) ? "" : notes.substring(0, p);
        }

        Date date = action.getActionDate();

        String schdTime = "";
        String schdDurHrs = "";
        String schdDurMins = "";
        String delegate = "";

        String state = "";

        if (action.isStateScheduled()) {
            state = "S";
            ActionStateScheduled schdState = (ActionStateScheduled) action.getState();
            if (schdState != null) {
                Date schdDate = schdState.getDate();
                if (schdDate != null) {
                    schdTime = DF_HHmm.format(date);
                }
                schdDurHrs = "" + schdState.getDurationHours();
                schdDurMins = "" + schdState.getDurationMinutes();
            }
        } else if (action.isStateDelegated()) {
            state = "D";
            ActionStateDelegated delegatedState = (ActionStateDelegated) action.getState();
            if (delegatedState != null) {
                delegate = delegatedState.getTo();
                if (delegate.length() > 100) {
                    delegate = delegate.substring(0, 100);
                }
            }
        } else if (action.isStateASAP()) {
            state = "A";
        } else {
            state = "I";
        }

        return VERSION +
                "|Action|ID|" + action.getID() +
                "|Title|" + SyncUtils.escape(action.getDescription()) +
                "|Notes|" + SyncUtils.escape(notes) +
                "|Context|" + action.getContext().getID() +
                "|Topic|" + action.getTopic().getID() +
                "|Date|" + (date == null ? "" : DF.format(date)) +
                "|Path|" + Services.instance.getPath(action) +
                "|TimeID|" + (isTimeUsed() ? getTimeID(action) : "") +
                "|EnergyID|" + (isEnergyUsed() ? getEnergyID(action) : "") +
                "|PriorityID|" + (isPriorityUsed() ? getPriorityID(action) : "") +
                "|State|" + state +
                "|SchdTime|" + schdTime + 
                "|SchdDurHrs|" + schdDurHrs +
                "|SchdDurMins|" + schdDurMins +
                "|Delegate|" + delegate +
                "|";
    }

    private String getTimeMsg(Value value) {
        return VERSION + "|Time|ID|" + value.getID() + "|Name|" + value.getName() + "|";
    }

    private String getEnergyMsg(Value value) {
        return VERSION + "|Energy|ID|" + value.getID() + "|Name|" + value.getName() + "|";
    }

    private String getPriorityMsg(Value value) {
        return VERSION + "|Priority|ID|" + value.getID() + "|Name|" + value.getName() + "|";
    }

    private void send(String msg) {
        log(msg);
        out.println(msg + ETX);
    }

}
