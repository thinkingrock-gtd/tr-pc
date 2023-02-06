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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SyncMsg102 {

    private static final Logger LOG = Logger.getLogger(SyncHandler.class.getName());
    public static final String VERSION = "V1.02";

    public static enum Type {
        Handshake, HangUp, Thought, ActionUpdate, NextAction, NextTopic, 
        NextContext, NextTime, NextEnergy, NextPriority, Unknown
    }
    public static final SyncMsg102 MSG_HANGUP = new SimpleMsg(Type.HangUp);
    public static final SyncMsg102 MSG_UNKNOWN = new SimpleMsg(Type.Unknown);
    public static final SyncMsg102 MSG_NEXT_TOPIC = new SimpleMsg(Type.NextTopic);
    public static final SyncMsg102 MSG_NEXT_CONTEXT = new SimpleMsg(Type.NextContext);
    public static final SyncMsg102 MSG_NEXT_ACTION = new SimpleMsg(Type.NextAction);
    public static final SyncMsg102 MSG_NEXT_TIME = new SimpleMsg(Type.NextTime);
    public static final SyncMsg102 MSG_NEXT_ENERGY = new SimpleMsg(Type.NextEnergy);
    public static final SyncMsg102 MSG_NEXT_PRIORITY = new SimpleMsg(Type.NextPriority);
    public final Type type;

    public SyncMsg102(Type type) {
        this.type = type;
    }

    public static MsgHandshake getHandshakeMsg(String line) {
        if (line.startsWith(VERSION + "|HELLO|")) {
            return new MsgHandshake(line);
        }
        return null;
    }

    public static SyncMsg102 getNextMsg(BufferedReader in) throws IOException {

        String str = readMsg(in);
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase("q")) {
            return MSG_HANGUP;
        }
        if (str.equals("NextContext")) {
            return MSG_NEXT_CONTEXT;
        }
        if (str.equals("NextTopic")) {
            return MSG_NEXT_TOPIC;
        }
        if (str.equals("NextAction")) {
            return MSG_NEXT_ACTION;
        }
        if (str.equals("NextTime")) {
            return MSG_NEXT_TIME;
        }
        if (str.equals("NextEnergy")) {
            return MSG_NEXT_ENERGY;
        }
        if (str.equals("NextPriority")) {
            return MSG_NEXT_PRIORITY;
        }
        if (str.startsWith(VERSION + "|HELLO|")) {
            return new MsgHandshake(str);
        }
        if (str.startsWith(VERSION + "|Thought|")) {
            return new MsgThought(str);
        }
        if (str.startsWith(VERSION + "|ActionUpdate|")) {
            return new MsgActionUpdate(str);
        }

        LOG.log(Level.WARNING, "UNKNOWN MESSAGE: {0}", str);
        return MSG_UNKNOWN;
    }

    public static class SimpleMsg extends SyncMsg102 {

        public SimpleMsg(Type type) {
            super(type);
        }

        @Override
        public String toString() {
            return type.name();
        }
    }

    public static class MsgUnknown extends SyncMsg102 {

        private final String msg;

        public MsgUnknown(String msg) {
            super(Type.Unknown);
            this.msg = msg;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    public static class MsgHandshake extends SyncMsg102 {

        public final String msg;
        public final int nbrThoughts;
        public final int nbrActions;

        public MsgHandshake(String msg) {
            super(Type.Handshake);
            this.msg = msg;
            int n;
            try {
                n = Integer.parseInt(SyncUtils.getFieldValue("Thoughts", msg));
            } catch (NumberFormatException x) {
                n = -1;
            }
            nbrThoughts = n;
            try {
                n = Integer.parseInt(SyncUtils.getFieldValue("ActionUpdates", msg));
            } catch (NumberFormatException x) {
                n = -1;
            }
            nbrActions = n;
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    public static class MsgThought extends SyncMsg102 {

        public final String msg;
        public final String title;
        public final String notes;
        public final String topic;

        public MsgThought(String msg) {
            super(Type.Thought);
            this.msg = msg;
            title = SyncUtils.getFieldValue("Title", msg);
            notes = SyncUtils.getFieldValue("Notes", msg);
            topic = SyncUtils.getFieldValue("Topic", msg);
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    public static class MsgActionUpdate extends SyncMsg102 {

        public final String msg;
        public final int id;
        public final boolean done;
//      public final String notes;

        public MsgActionUpdate(String msg) {
            super(Type.ActionUpdate);
            this.msg = msg;
            id = getInteger(SyncUtils.getFieldValue("ID", msg));
            done = Boolean.parseBoolean(SyncUtils.getFieldValue("Done", msg));
//          notes = SyncUtils.getFieldValue("Notes", msg);
        }

        @Override
        public String toString() {
            return msg;
        }
    }

    private static Integer getInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException x) {
            return null;
        }
    }


    private static final String ETX = "\u0003";
    private static String line;
    
    private static final String readMsg(BufferedReader in) throws IOException {
        while (!in.ready()) {
        }

        String msg = null;

        while ((line = in.readLine()) != null) {

            msg = (msg == null ? line : msg + "\n" + line);

            if (msg.endsWith(ETX)) {
                return msg.substring(0, msg.length() - 1);
            }

        }
        
        return null;
    }

}
