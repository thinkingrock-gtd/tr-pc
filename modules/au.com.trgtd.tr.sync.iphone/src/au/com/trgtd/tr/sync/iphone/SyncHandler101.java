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
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import tr.model.action.Action;
import tr.model.context.Context;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

public class SyncHandler101 extends SyncHandler {

    public static Timer tTotal = new Timer();
    public static Timer tProgress = new Timer();
    public static Timer tSending = new Timer();
    public static Timer tReading = new Timer();
    public static Timer tReadWait = new Timer();
    public static Timer tTemp = new Timer();

    public static class Timer {
        private double total;
        private double start;
        public void start() {
            start = getTime();
        }
        public void reset() {
            start = 0; total = 0;
        }
        public void stop() {
            total += (getTime() - start);
        }
        public double getTotal() {
            return total;
        }
        private long getTime() {
            return Calendar.getInstance().getTimeInMillis();
        }
    }

    public static final String VERSION = "V1.01";
    public static final int MAX_MSG_SIZE = 5000;
    public static final char ETX = '\u0003';
    
    public SyncHandler101(BufferedReader in, PrintWriter out, SyncProgress progress) {
        super(in, out, progress);
    }

    protected boolean sync(SyncMsg101.MsgHandshake msgHs) throws Exception {

        tTotal.start();

        boolean result = _sync(msgHs);

        tTotal.stop();

//        System.out.println("======>  TOTAL TIME (MILLISECS): " + tTotal.getTotal() + " (100%)");
//        System.out.println("======>  TIME UPDATING PROGRESS: " + tProgress.getTotal() + " (" + (int)((tProgress.getTotal() / tTotal.getTotal()) * 100) + "%)");
//        System.out.println("======>   TIME SENDING MESSAGES: " + tSending.getTotal() + " (" + (int)((tSending.getTotal() / tTotal.getTotal()) * 100) + "%)");
//        System.out.println("======>   TIME READING MESSAGES: " + tReading.getTotal() + " (" + (int)((tReading.getTotal() / tTotal.getTotal()) * 100) + "%)");
//        System.out.println("======>  TIME READING NOT READY: " + tReadWait.getTotal() + " (" + (int)((tReadWait.getTotal() / tTotal.getTotal()) * 100) + "%)");

        return result;
    }

    private boolean _sync(SyncMsg101.MsgHandshake msgHs) throws Exception {

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

        send(VERSION +"|HELLO|Contexts|" + nbrContextsToSend + "|Topics|" + nbrTopicsToSend + "|Actions|" + nbrActionsToSend + "|");

        while (true) {

            updateProgress();

            if (cancelSyncing) {
                send("Q");
                LOG.info("Server syncing was cancelled.");
                return false;
            }

            SyncMsg101 inMsg = SyncMsg101.getNextMsg(in);
            if (inMsg == null) {
                LOG.warning("RECIEVED: Unexpected end of input stream.");
                return false;
            }

            LOG.log(Level.INFO, "RECIEVED:{0}", inMsg.toString());

            switch (inMsg.type) {
                case Thought: {
                    SyncMsg101.MsgThought msgThought = (SyncMsg101.MsgThought) inMsg;
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
                    SyncMsg101.MsgActionUpdate msgActionUpdate = (SyncMsg101.MsgActionUpdate)inMsg;
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
        
        tProgress.start();

        int todo = nbrActionsToSend + nbrContextsToSend + nbrTopicsToSend + nbrThoughtsToGet + nbrActionsToGet;
        int done = nbrActionsSent + nbrContextsSent + nbrTopicsSent + nbrThoughtsGot + nbrActionsGot;
        updateProgress(done, todo);
        
        tProgress.stop();
    }

    private String getMsg(Context context) {
        // e.g "V1.01|Context|ID|0|Title|None|"
        return VERSION + "|Context|ID|" + context.getID() + "|Title|" + SyncUtils.escape(context.getName()) + "|";
    }

    private String getMsg(Topic topic) {
        // e.g "V1.01|Topic|ID|2|Title|Work|"
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
        // e.g "V1.01|Action|ID|208|Title|Buy Chocolate|Notes||Context|102|Topic|1|Date|20090701|"
        String notes = action.getNotes();
        if (reduceSize > 0) {
            int p = notes.length() - reduceSize;
            notes = (p < 1) ? "" : notes.substring(0, p);
        }
        
        Date date = action.getActionDate();

        return VERSION +
                "|Action|ID|" + action.getID() +
                "|Title|" + SyncUtils.escape(action.getDescription()) +
                "|Notes|" + SyncUtils.escape(notes) +
                "|Context|" + action.getContext().getID() +
                "|Topic|" + action.getTopic().getID() +
                "|Date|" + (date == null ? "" : DF.format(date)) +
                "|";
    }

    private void send(String msg) {
        tSending.start();

        log(msg);
        
        out.println(msg + ETX);

        tSending.stop();
    }

}
