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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import tr.model.action.Action;
import tr.model.context.Context;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

public class SyncHandler100 extends SyncHandler {

    public static final String VERSION = "V1.00";
    public static final int MAX_MSG_SIZE = 5000;
    
    public SyncHandler100(BufferedReader in, PrintWriter out, SyncProgress progress) {
        super(in, out, progress);
    }

    protected boolean sync(SyncMsg100.MsgHandshake msgHs) throws Exception {

        nbrThoughtsToGet = 0;
        nbrThoughtsGot = 0;
        nbrContextsToSend = getContexts().size();
        nbrContextsSent = 0;
        nbrTopicsToSend = getTopics().size();
        nbrTopicsSent = 0;
        nbrActionsToSend = getActions().size();
        nbrActionsSent = 0;

        if (msgHs == null) {
            LOG.severe("Handshake message is null.");
            return false;
        }
        if (msgHs.nbrThoughts > -1) {
            nbrThoughtsToGet = msgHs.nbrThoughts;
        } else {
            LOG.warning("Could not determine number of thoughts to receive.");
            nbrThoughtsToGet = 0;
        }

        updateProgress();

        out.println(log(VERSION + "|HELLO|Contexts|" + nbrContextsToSend + "|Topics|" + nbrTopicsToSend + "|Actions|" + nbrActionsToSend + "|"));

        while (true) {

            if (cancelSyncing) {
                out.println(log("Q"));
                LOG.info("Server syncing was cancelled.");
                return false;
            }

            SyncMsg100 inMsg = SyncMsg100.getNextMsg(in);
            if (inMsg == null) {
                LOG.warning("RECIEVED: Unexpected end of input stream.");
                return false;
            }
            LOG.log(Level.INFO, "RECIEVED:{0}", inMsg.toString());

            switch (inMsg.type) {
                case Thought: {
                    SyncMsg100.MsgThought msgThought = (SyncMsg100.MsgThought) inMsg;
                    Thought thought = new Thought(getData().getNextID());
                    thought.setDescription(msgThought.title);
                    thought.setNotes(msgThought.notes);
                    thought.setTopic(getTopic(msgThought.topic));
                    getData().getThoughtManager().add(thought);
                    nbrThoughtsGot++;
                    LOG.log(Level.INFO, "TR added thought: {0}", thought.getDescription());
                    out.println(log("OK"));
                    updateProgress();
                    break;
                }
                case NextAction: {
                    if (nbrActionsSent < getActions().size()) {
                        out.println(log(getMsg(getActions().get(nbrActionsSent))));
                        nbrActionsSent++;
                        updateProgress();
                    } else {
                        out.println(log("END"));
                    }
                    break;
                }
                case NextTopic: {
                    if (nbrTopicsSent < getTopics().size()) {
                        out.println(log(getMsg(getTopics().get(nbrTopicsSent))));
                        nbrTopicsSent++;
                        updateProgress();
                    } else {
                        out.println(log("END"));
                    }
                    break;
                }
                case NextContext: {
                    if (nbrContextsSent < getContexts().size()) {
                        out.println(log(getMsg(getContexts().get(nbrContextsSent))));
                        nbrContextsSent++;
                        updateProgress();
                    } else {
                        out.println(log("END"));
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
                    out.println(log("Unknown message"));
                }
            }
        }
    }

    private synchronized void updateProgress() {
        int todo = nbrActionsToSend + nbrContextsToSend + nbrTopicsToSend + nbrThoughtsToGet;
        int done = nbrActionsSent + nbrContextsSent + nbrTopicsSent + nbrThoughtsGot;
        updateProgress(done, todo);
    }

    private String getMsg(Context context) {
        // e.g "V1.00|Context|ID|0|Title|None|"
        return VERSION + "|Context|ID|" + context.getID() + "|Title|" + SyncUtils.escape(context.getName()) + "|";
    }

    private String getMsg(Topic topic) {
        // e.g "V1.00|Topic|ID|2|Title|Work|"
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
        // e.g "V1.00|Action|ID|208|Title|Buy Chocolate|Notes||Context|102|Topic|1|Date|20090701|"
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

}
