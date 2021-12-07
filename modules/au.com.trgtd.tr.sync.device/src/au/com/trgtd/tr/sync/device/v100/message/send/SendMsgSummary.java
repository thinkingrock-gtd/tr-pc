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

import static au.com.trgtd.tr.sync.device.Constants.DELIM;
import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import static au.com.trgtd.tr.sync.device.v100.message.fields.Fields.Send.Summary.*;
import java.text.MessageFormat;

/**
 * Summary send message.
 */
public class SendMsgSummary extends SendMsg {

    private final DataMgr dataMgr;

    public SendMsgSummary(DataMgr dataMgr) {
        super(SendMsg.Type.SUMMARY);
        this.dataMgr = dataMgr;
    }

    @Override
    public String toSendString() {
        return MessageFormat.format(PATTERN, 
                getDataID(),
                getTopics(),
                getContexts(),
                getTimes(),
                getEnergies(),
                getPriorities(),
                getActions(),
                getProjects(),
                getReferences());
    }

    private static final String PATTERN =
            SendMsg.Type.SUMMARY.getCode() + DELIM +
            DATA_ID       + DELIM + "{0}"  + DELIM +
            TOPICS        + DELIM + "{1}"  + DELIM +
            CONTEXTS      + DELIM + "{2}"  + DELIM +
            TIMES         + DELIM + "{3}"  + DELIM +
            ENERGIES      + DELIM + "{4}"  + DELIM +
            PRIORITIES    + DELIM + "{5}"  + DELIM +
            ACTIONS       + DELIM + "{6}"  + DELIM +
            PROJECTS      + DELIM + "{7}"  + DELIM +
            REFERENCES    + DELIM + "{8}"  + DELIM;

    private String getDataID() {
        return dataMgr.getDataID();
    }
    private String getTopics() {
        return String.valueOf(dataMgr.getNbrSendTopics());
    }
    private String getContexts() {
        return String.valueOf(dataMgr.getNbrSendContexts());
    }
    private String getTimes() {
        return String.valueOf(dataMgr.getNbrSendTimes());
    }
    private String getEnergies() {
        return String.valueOf(dataMgr.getNbrSendEnergies());
    }
    private String getPriorities() {
        return String.valueOf(dataMgr.getNbrSendPriorities());
    }
    private String getActions() {
        return String.valueOf(dataMgr.getNbrSendActions());
    }
    private String getProjects() {
        return String.valueOf(dataMgr.getNbrSendProjects());
    }
    private String getReferences() {
        return String.valueOf(dataMgr.getNbrSendReferences());
    }

}
