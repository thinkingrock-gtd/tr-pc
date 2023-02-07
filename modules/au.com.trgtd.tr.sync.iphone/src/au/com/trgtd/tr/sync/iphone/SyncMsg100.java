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

public abstract class SyncMsg100 {

    public static enum Type {
        Handshake, HangUp, Thought, NextAction, NextTopic, NextContext, Unknown
    }
    public static final String VERSION = "V1.00";
    public static final SyncMsg100 MSG_HANGUP = new SimpleMsg(Type.HangUp);
    public static final SyncMsg100 MSG_UNKNOWN = new SimpleMsg(Type.Unknown);
    public static final SyncMsg100 MSG_NEXT_TOPIC = new SimpleMsg(Type.NextTopic);
    public static final SyncMsg100 MSG_NEXT_CONTEXT = new SimpleMsg(Type.NextContext);
    public static final SyncMsg100 MSG_NEXT_ACTION = new SimpleMsg(Type.NextAction);

    public final Type type;

    public SyncMsg100(Type type) {
        this.type = type;
    }

    public static MsgHandshake getHandshakeMsg(String line) {
        if (line.startsWith(VERSION + "|HELLO|")) {
            return new MsgHandshake(line);
        }
        return null;
    }


    public static SyncMsg100 getNextMsg(BufferedReader in) throws IOException {
        String line = in.readLine();
        if (line == null) {
            return null;
        }
        if (line.equalsIgnoreCase("q")) {
            return MSG_HANGUP;
        }
        if (line.equals("NextContext")) {
            return MSG_NEXT_CONTEXT;
        }
        if (line.equals("NextTopic")) {
            return MSG_NEXT_TOPIC;
        }
        if (line.equals("NextAction")) {
            return MSG_NEXT_ACTION;
        }
        if (line.startsWith(VERSION + "|HELLO|")) {
            return new MsgHandshake(line);
        }
        if (line.startsWith(VERSION + "|Thought|")) {
            return new MsgThought(line, in);
        }
        return MSG_UNKNOWN;
    }

    public static class SimpleMsg extends SyncMsg100 {
        public SimpleMsg(Type type) {
            super(type);
        }
        @Override
        public String toString() {
            return type.name();
        }
    }

    public static class MsgUnknown extends SyncMsg100 {
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

    public static class MsgHandshake extends SyncMsg100 {
        public final String msg;
        public final int nbrThoughts;
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
        }
        @Override
        public String toString() {
            return msg;
        }
    }

    public static class MsgThought extends SyncMsg100 {
        public final static String RE = VERSION + "\\|Thought\\|ID\\|.*\\|Title\\|.*\\|Notes\\|(.|\\s)*\\|Topic\\|.*\\|";
        public final String msg;
        public final String title;
        public final String notes;
        public final String topic;
        public MsgThought(String firstLine, BufferedReader in) {
            super(Type.Thought);
            StringBuilder sb = new StringBuilder(SyncUtils.unEscape(firstLine.trim()));
            while (!sb.toString().matches(RE)) {
                String nextLine;
                try {
                    nextLine = in.readLine();
                } catch (IOException x) {
                    break;
                }
                if (nextLine == null) {
                    break;
                }
                sb.append("\n").append(SyncUtils.unEscape(nextLine.trim()));
            }
            msg = sb.toString();
            title = SyncUtils.getFieldValue("Title", msg);
            notes = SyncUtils.getFieldValue("Notes", msg);
            topic = SyncUtils.getFieldValue("Topic", msg);
        }
        @Override
        public String toString() {
            return msg;
        }
    }
}
