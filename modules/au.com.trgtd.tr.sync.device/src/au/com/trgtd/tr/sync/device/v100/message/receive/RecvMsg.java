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
package au.com.trgtd.tr.sync.device.v100.message.receive;

/**
 * Received message class. Extended for messages containing data.
 *
 * @author Jeremy Moore
 */
public class RecvMsg {

    public final Type type;

    /**
     * Constructs a new instance.
     * @param code The message code.
     */
    public RecvMsg(Type code) {
        this.type = code;
    }


    /** Message type enumerator. */
    public static enum Type {
        SUMMARY,
        NEWTHOUGHT,
        UPDACTION,
        UPDPROJECT,
        REFERENCE,
        NEXTACTION,
        NEXTPROJECT,
        NEXTTOPIC,
        NEXTCONTEXT,
        NEXTTIME,
        NEXTENERGY,
        NEXTPRIORITY,
        NEXTREFERENCE,
        GOODBYE,
        CANCEL;

        /**
         * Gets the message type from a message code.
         * @param code message code string.
         * @return the message type.
         */
        public static Type fromCode(String code) throws Exception {
            return Type.valueOf(code);
        }

        /**
         * Returns the code string.
         * @return the code string.
         */
        public String toCode() {
            return name();
        }
    }
}
