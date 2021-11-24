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
package au.com.trgtd.tr.sync.device.dbx;

import au.com.trgtd.tr.sync.device.v100.data.DataMgr;
import au.com.trgtd.tr.sync.device.v100.message.fields.Fields;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgAction;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgProject;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgReference;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgThought;
import au.com.trgtd.tr.util.Utils;
import java.io.IOException;
import java.net.MalformedURLException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Date;

/**
 * Parser for sync up JSON file.
 */
public class DbxParser {

    private static final double VERSION = DbxSyncThread.DBX_SYNC_VERSION;

    private String dataID;
    private Double version;

    /** 
     * Creates a new instance.
     */
    public DbxParser() {
    }

    /**
     * Gets the parsed data ID.
     * @return The parsed data ID or null.
     */
    public String getDataID() {
        return dataID;
    }

    /**
     * Gets the parsed DropBox sync version.
     * @return The parsed version.
     */
    public Double getVersion() {
        return version;
    }
    
    public void parse(File syncupFile, DataMgr dataMgr) throws MalformedURLException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        // can either use mapper.readTree(source), or mapper.readValue(source, JsonNode.class);
//      JsonNode rootNode = mapper.readTree(syncupFile);
        JsonNode rootNode = null;

        // Try to read file for 30 seconds as it may not be fully written yet.
        int nTries = 0;
        while (rootNode == null) {
            try {
                rootNode = mapper.readTree(syncupFile);                
            } catch (IOException ex) {
                if (++nTries > 30) {
                    throw ex;
                }
                Utils.sleep(1000);
            }
        }        
        
        JsonNode versionNode = rootNode.path(Fields.Common.VERSION);
        version = getDouble(versionNode, 0.0);
        if (version != VERSION) {
            StringBuilder sb = new StringBuilder();
            sb.append("Incorrect sync version. ");
            sb.append("Expected ").append(VERSION).append(" Was ").append(version).append("\n");
            if (version < VERSION) {
                sb.append("You need to update your Android ThinkingRock app.");                
            } else {
                sb.append("You need to update this plugin module.");                
            }
            throw new IOException(sb.toString());
        }

        JsonNode dataIdNode = rootNode.path(Fields.Receive.Summary.DATA_ID);
        dataID = getText(dataIdNode, "");
        
        JsonNode actionsNode = rootNode.path(Fields.Receive.Summary.ACTIONS);
        JsonNode projectsNode = rootNode.path(Fields.Receive.Summary.PROJECTS);
        JsonNode referencesNode = rootNode.path(Fields.Receive.Summary.REFERENCES);
        JsonNode thoughtsNode = rootNode.path(Fields.Receive.Summary.THOUGHTS);

        for (int i = 0; i < referencesNode.size(); i++) {
            JsonNode node = referencesNode.get(i);
            int id = node.get(Fields.Receive.Reference.ID).asInt();
            String title = getText(node.get(Fields.Receive.Reference.TITLE), "");
            String notes = getText(node.get(Fields.Receive.Reference.NOTES), "");
            Integer topicId = getInt(node.get(Fields.Receive.Reference.TOPIC_ID), null);
            String change = getText(node.get(Fields.Receive.Reference.CHANGE), null);
            dataMgr.addRcvReference(new RecvMsgReference(id, title, notes, topicId, change));
        }

        for (int i = 0; i < thoughtsNode.size(); i++) {
            JsonNode node = thoughtsNode.get(i);
            String title = getText(node.get(Fields.Common.TITLE), "");
            String notes = getText(node.get(Fields.Common.NOTES), "");
            Integer topicId = getInt(node.get(Fields.Common.TOPIC_ID), null);
            dataMgr.addRcvThought(new RecvMsgThought(title, notes, topicId));
        }

        for (int i = 0; i < actionsNode.size(); i++) {
            JsonNode node = actionsNode.get(i);
            int id = node.get(Fields.Receive.UpdAction.ID).asInt();
            Date date = getDate(node.get(Fields.Receive.UpdAction.DATE), null);
            
            boolean done;
            Date doneDate;
            if (getVersion() < 2.1) {
                done = getBoolean(node.get(Fields.Receive.UpdAction.DONE), false);
                doneDate = done ? new Date() : null;
            } else {
                Long doneTime = getLong(node.get(Fields.Receive.UpdAction.DONE), 0L);
                if (doneTime == 0L) {
                    done = false;
                    doneDate = null;
                } else {
                    done = true;
                    doneDate = new Date(doneTime);
                }
            }
            Integer lenHrs = getInt(node.get(Fields.Receive.UpdAction.LENGTH_HRS), null);
            Integer lenMns = getInt(node.get(Fields.Receive.UpdAction.LENGTH_MNS), null);
            Integer startHr = getInt(node.get(Fields.Receive.UpdAction.START_HR), null);
            Integer startMn = getInt(node.get(Fields.Receive.UpdAction.START_MN), null);
            String notes = getText(node.get(Fields.Receive.UpdAction.NOTES), "");
            dataMgr.addRcvAction(new RecvMsgAction(id, done, doneDate, notes, date, startHr, startMn, lenHrs, lenMns));
        }

        for (int i = 0; i < projectsNode.size(); i++) {
            JsonNode node = projectsNode.get(i);
            int id = node.get(Fields.Receive.UpdProject.ID).asInt();
            String notes   = getText(node.get(Fields.Receive.UpdProject.NOTES), "");
            String purpose = getText(node.get(Fields.Receive.UpdProject.PURPOSE), "");
            String vision  = getText(node.get(Fields.Receive.UpdProject.VISION), "");
            String brains  = getText(node.get(Fields.Receive.UpdProject.BRAINSTORM), "");
            String organs  = getText(node.get(Fields.Receive.UpdProject.ORGANISE), "");
            Date due = getDate(node.get(Fields.Receive.UpdProject.DUE), null);
            dataMgr.addRcvProject(new RecvMsgProject(id, notes, purpose, vision, brains, organs, due));
        }
    }

    private Date getDate(JsonNode node, Date defaultValue) {
        if (node == null) {
            return defaultValue;
        }
        return node.isLong() ? new Date(node.asLong()) : defaultValue;
    }

    private Integer getInt(JsonNode node, Integer defaultValue) {
        if (node == null) {
            return defaultValue;
        }
        return node.isInt() ? node.asInt() : defaultValue;
    }

    private Long getLong(JsonNode node, Long defaultValue) {
        if (node == null) {
            return defaultValue;
        }
        return node.isLong() ? node.asLong() : defaultValue;
    }

    private Double getDouble(JsonNode node, Double defaultValue) {
        if (node == null || node.isNull()) {
            return defaultValue;            
        }
        if (node.isInt()) {
            return (double)node.asInt();
        }
        if (node.isLong()) {
            return (double)node.asLong();
        }
        if (node.isDouble() || node.isFloat()) {
            return node.asDouble();
        } 
        return defaultValue;            
    }

    private String getText(JsonNode node, String defaultValue) {
        if (node == null) {
            return defaultValue;
        }
        return node.isTextual() ? node.asText() : defaultValue;
    }

    private Boolean getBoolean(JsonNode node, Boolean defaultValue) {
        if (node == null) {
            return defaultValue;
        }
        return node.isBoolean() ? node.asBoolean() : defaultValue;
    }

}
