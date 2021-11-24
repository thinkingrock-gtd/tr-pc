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
package au.com.trgtd.tr.sync.device.v100.message.send;

/**
 * Send message abstract base class.
 *
 * @author Jeremy Moore
 */
public abstract class SendMsg {

    /** Send message type enumerator. */
    public static enum Type {
        SUMMARY,
        ACTION,
        TOPIC,
        CONTEXT,
        TIME,
        ENERGY,
        PRIORITY,
        REFERENCE,
        PROJECT,
        END,
        OK,
        CANCEL,
        GOODBYE;
        public String getCode() {
            return name();
        }
    }

    public final Type type;

    /**
     * Constructs a new instance.
     * @param type The message type.
     */
    public SendMsg(Type type) {
        this.type = type;
    }

    public abstract String toSendString();

}
