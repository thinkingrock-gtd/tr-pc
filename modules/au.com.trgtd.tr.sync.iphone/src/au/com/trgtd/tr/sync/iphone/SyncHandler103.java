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

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.util.Utils;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.thought.Thought;
import tr.model.topic.Topic;
import static au.com.trgtd.tr.sync.iphone.SyncUtils.escape;

public class SyncHandler103 extends SyncHandler {

    public static final String VERSION = "V1.03";
    public static final int MAX_MSG_SIZE = 5000;
    public static final char ETX = '\u0003';

    public SyncHandler103(BufferedReader in, PrintWriter out, SyncProgress progress) {
        super(in, out, progress);
    }

    protected boolean sync(SyncMsg103.MsgHandshake msgHs) throws Exception {
        return _sync(msgHs);
    }

    protected String getFileID() {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.severe("Datastore was not found.");
            return "";
        }
        File datafile = new File(ds.getPath());
        if (!datafile.exists()) {
            LOG.severe("Data file was not found.");
            return "";
        }
        return datafile.getName();
    }

    private boolean _sync(SyncMsg103.MsgHandshake msgHs) throws Exception {

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

        String fileID = getFileID();
        boolean wrongFileID = msgHs.fileID.length() > 0 && !Utils.equal(msgHs.fileID, fileID);
        if (wrongFileID) {
            final Component parent = null;
            final String title = NbBundle.getMessage(getClass(), "wrong_sync_file_id_title");
            final Object messg = NbBundle.getMessage(getClass(), "wrong_sync_file_id_messg", fileID, msgHs.fileID);
            final int dtype = JOptionPane.YES_NO_OPTION;
            final int mtype = JOptionPane.QUESTION_MESSAGE;
            final Icon icon = null;
            final Object[] options = null;
            final Object option = null;
            int rslt = JOptionPane.showOptionDialog(parent, messg, title, dtype, mtype, icon, options, option);
            if (rslt == JOptionPane.NO_OPTION) {
                send("Q");
                LOG.info("Wrong File ID and server syncing was cancelled by user.");
                throw new FileIDException();
            }
        }

        if (!wrongFileID && msgHs.nbrThoughts > 0) {
            nbrThoughtsToGet = msgHs.nbrThoughts;
        } else {
            nbrThoughtsToGet = 0;
        }
        if (!wrongFileID && msgHs.nbrActions > 0) {
            nbrActionsToGet = msgHs.nbrActions;
        } else {
            nbrActionsToGet = 0;
        }

        send(VERSION + "|HELLO" +
                "|Contexts|" + nbrContextsToSend +
                "|Topics|" + nbrTopicsToSend +
                "|Actions|" + nbrActionsToSend +
                "|Times|" + nbrTimesToSend +
                "|Energies|" + nbrEnergiesToSend +
                "|Priorities|" + nbrPrioritiesToSend +
                "|FileID|" + fileID +
                "|");

        while (true) {

            updateProgress();

            if (cancelSyncing) {
                send("Q");
                LOG.info("Server syncing was cancelled.");
                return false;
            }

            SyncMsg103 inMsg = SyncMsg103.getNextMsg(in);

            LOG.log(Level.INFO, "RECIEVED:{0}", inMsg == null ? "null" : inMsg.toString());

            if (inMsg == null) {
                LOG.warning("RECIEVED: Unexpected end of input stream.");
                return false;
            }



            switch (inMsg.type) {
                case Thought: {
                    if (wrongFileID) {
                        LOG.log(Level.INFO, "Ignoring iPhone thought.");
                        send("OK");
                        break;
                    }
                    SyncMsg103.MsgThought msgThought = (SyncMsg103.MsgThought) inMsg;
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
                    if (wrongFileID) {
                        LOG.log(Level.INFO, "Ignoring iPhone action update.");
                        send("OK");
                        break;
                    }
                    SyncMsg103.MsgActionUpdate msgActionUpdate = (SyncMsg103.MsgActionUpdate) inMsg;
                    Action action = Services.instance.getAction(msgActionUpdate.id);
                    if (action == null) {
                        LOG.log(Level.WARNING, "COULD NOT FIND ACTION WITH ID: {0}", msgActionUpdate.id);
                        break;
                    }

                    if (!Utils.equal(action.getActionDate(), msgActionUpdate.date)) {
                        postponeAction(action, msgActionUpdate.date, msgActionUpdate.hh, msgActionUpdate.mm);
                        LOG.log(Level.INFO, "TR updated action with id: {0} Postponed action date", msgActionUpdate.id);
                    }

                    if (msgActionUpdate.done) {
                        action.setDone(true);
                        updateActionsToSend();
                        LOG.log(Level.INFO, "TR updated action with id: {0} Done set to True", msgActionUpdate.id);
                    }

                    action.setNotes(msgActionUpdate.notes.trim());

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

    protected void postponeAction(Action action, Date date, Integer hh, Integer mm) {

        switch (action.getState().getType()) {
            case DOASAP: {
                action.setDueDate(date);
                return;
            }
            case INACTIVE: {
                action.setStartDate(date);
                return;
            }
            case SCHEDULED: {
                ActionStateScheduled state = (ActionStateScheduled)action.getState();
                if (date == null) {
                    state.setDate(null);
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    if (hh != null) {
                        c.set(Calendar.HOUR_OF_DAY, hh);
                    }
                    if (mm != null) {
                        c.set(Calendar.MINUTE, mm);
                    }
                    state.setDate(c.getTime());
                }
                return;
            }
            case DELEGATED: {
                ActionStateDelegated state = (ActionStateDelegated)action.getState();
                state.setDate(date);
                return;
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
                if (action.isStateDelegated()) {
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

    protected String getMsg(Context context) {
        return VERSION + "|Context|ID|" + context.getID() + "|Title|" + escape(context.getName()) + "|";
    }

    protected String getMsg(Topic topic) {
        return VERSION + "|Topic|ID|" + topic.getID() + "|Title|" + escape(topic.getName()) + "|";
    }

    protected String getMsg(Action action) {
        String msg = getMsg(action, 0);
        while (msg.length() > MAX_MSG_SIZE) {
            msg = getMsg(action, msg.length() - MAX_MSG_SIZE);
        }
        return msg;
    }

    protected String getMsg(Action action, int reduceSize) {
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

        Topic topic = action.getTopic();

        String path = Services.instance.getPath(action);

        return VERSION +
                "|Action|ID|" + action.getID() +
                "|Title|" + escape(action.getDescription()) +
                "|Notes|" + escape(notes) +
                "|Context|" + action.getContext().getID() +
                "|Topic|" + (null == topic ? "" : topic.getID()) +
                "|Date|" + (null == date ? "" : DF.format(date)) +
                "|Path|" + escape(path) +
                "|TimeID|" + (isTimeUsed() ? getTimeID(action) : "") +
                "|EnergyID|" + (isEnergyUsed() ? getEnergyID(action) : "") +
                "|PriorityID|" + (isPriorityUsed() ? getPriorityID(action) : "") +
                "|State|" + state +
                "|SchdTime|" + schdTime + 
                "|SchdDurHrs|" + schdDurHrs +
                "|SchdDurMins|" + schdDurMins +
                "|Delegate|" + escape(delegate) +
                "|";
    }

    protected String getTimeMsg(Value value) {
        return VERSION + "|Time|ID|" + value.getID() + "|Name|" + escape(value.getName()) + "|";
    }

    protected String getEnergyMsg(Value value) {
        return VERSION + "|Energy|ID|" + value.getID() + "|Name|" + escape(value.getName()) + "|";
    }

    protected String getPriorityMsg(Value value) {
        return VERSION + "|Priority|ID|" + value.getID() + "|Name|" + escape(value.getName()) + "|";
    }

    private void send(String msg) {
        log(msg);
        out.println(msg + ETX);
    }

}
