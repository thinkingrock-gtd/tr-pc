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
package au.com.trgtd.tr.sync.device.v100.data;

import au.com.trgtd.tr.datastore.DataStore;
import au.com.trgtd.tr.datastore.DataStoreLookup;
import au.com.trgtd.tr.services.Services;
import au.com.trgtd.tr.sync.device.exception.DataException;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgAction;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgProject;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgReference;
import au.com.trgtd.tr.sync.device.v100.message.receive.RecvMsgThought;
import au.com.trgtd.tr.sync.device.v100.message.send.*;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import tr.model.Data;
import tr.model.DataLookup;
import tr.model.Item.Item;
import tr.model.action.Action;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.context.Context;
import tr.model.criteria.Value;
import tr.model.information.Information;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * Manages data and messages. Provides send messages, accepts receive messages
 * and commits data changes.
 *
 * @author Jeremy Moore
 */
public final class DataMgr {

    private static final Logger LOG = Logger.getLogger(DataMgr.class.getName());
    private final Data data;

    private final Map<Integer, SendMsgAction> sndActionsMap;
    private List<SendMsgAction> sndActionsList;

    private Map<Integer, Project> projectsMap;
    private final Map<Integer, SendMsgProject> sndProjectsMap;
    private final List<SendMsgProject> sndProjectsList;

    private final Map<Integer, SendMsgReference> sndRefsMap;
    private List<SendMsgReference> sndRefsList;

    private final List<SendMsgTopic> sndTopicsList;
    private final List<SendMsgContext> sndContextsList;
    private final List<SendMsgTime> sndTimesList;
    private final List<SendMsgEnergy> sndEnergiesList;
    private final List<SendMsgPriority> sndPrioritiesList;

    private int sndActnIndex;
    private int sndTopcIndex;
    private int sndCntxIndex;
    private int sndTimeIndex;
    private int sndEngyIndex;
    private int sndPrtyIndex;
    private int sndRefsIndex;
    private int sndProjectsIndex;

    private final List<RecvMsgAction> rcvActnList;
    private final List<RecvMsgProject> rcvProjectList;
    private final List<RecvMsgThought> rcvThgtList;
    private final List<RecvMsgReference> rcvRefsList;

    private int nbrRcvActions;      // number of update actions to receive
    private int nbrRcvProjects;     // number of update projects to receive
    private int nbrRcvThoughts;     // number of new thoughts to receive
    private int nbrRcvRefs;         // number of changed references to receive

    private final String dataID;
    private final boolean isTimeUsed;
    private final boolean isEnergyUsed;
    private final boolean isPriorityUsed;
//  private final Date maxToDate;

    /**
     * Constructs a new instance, initializing data structures.
     *
     * @throws DataMgrException If the TR data instance is not found.
     */
    public DataMgr() throws DataException {
        this.data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            throw new DataException("ThinkingRock data not found.");
        }

//      this.maxToDate = getTodayPlus30Days();
        this.dataID = getDataFileName();

        this.isTimeUsed = data.getTimeCriterion().isUse();
        this.isEnergyUsed = data.getEnergyCriterion().isUse();
        this.isPriorityUsed = data.getPriorityCriterion().isUse();
        this.sndRefsMap = createSendRefsMap();
        this.sndProjectsList = createSendProjectsList();
        this.sndProjectsMap = createSendProjectsMap(sndProjectsList);
        this.sndActionsMap = createSendActionsMap();

        this.sndTopicsList = createSendTopicList();
        this.sndContextsList = createSendContextList();
        this.sndTimesList = createSendTimeList();
        this.sndEnergiesList = createSendEnergiesList();
        this.sndPrioritiesList = createSendPrioritiesList();

        this.rcvThgtList = new ArrayList<>();
        this.rcvActnList = new ArrayList<>();
        this.rcvRefsList = new ArrayList<>();
        this.rcvProjectList = new ArrayList<>();
    }

    private String getDataFileName() throws DataException {
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            throw new DataException("Datastore was not found.");
        }
        File datafile = new File(ds.getPath());
        if (!datafile.exists()) {
            throw new DataException("Data file was not found.");
        }
        return datafile.getName();
    }

    public String getDataID() {
        return dataID;
    }

    public int getNbrSendActions() {
        return sndActionsMap.size();
    }

    public int getNbrSendProjects() {
        return this.sndProjectsMap.size();
    }

    public int getNbrSendReferences() {
        return sndRefsMap.size();
    }

    public int getNbrSendTopics() {
        return sndTopicsList.size();
    }

    public int getNbrSendContexts() {
        return sndContextsList.size();
    }

    public int getNbrSendTimes() {
        return sndTimesList.size();
    }

    public int getNbrSendEnergies() {
        return sndEnergiesList.size();
    }

    public int getNbrSendPriorities() {
        return sndPrioritiesList.size();
    }

    public void setNbrUpdActns(int n) {
        nbrRcvActions = n > 0 ? n : 0;
    }

    public int getNbrRcvActions() {
        return nbrRcvActions;
    }

    public void setNbrRcvRefs(int n) {
        nbrRcvRefs = n > 0 ? n : 0;
    }

    public int getNbrRcvProjects() {
        return nbrRcvProjects;
    }

    public void setNbrUpdProjects(int n) {
        nbrRcvProjects = n > 0 ? n : 0;
    }

    public int getNbrRcvReferences() {
        return nbrRcvThoughts;
    }

    public void setNbrRcvThoughts(int n) {
        nbrRcvThoughts = n > 0 ? n : 0;
    }

    public int getNbrRcvThougths() {
        return nbrRcvThoughts;
    }

    public int getTotal() {
        return this.nbrRcvThoughts
                + this.nbrRcvActions
                + this.nbrRcvProjects
                + this.nbrRcvRefs
                + this.sndActionsMap.size()
                + this.sndProjectsMap.size()
                + this.sndRefsMap.size()
                + this.sndTopicsList.size()
                + this.sndContextsList.size()
                + this.sndTimesList.size()
                + this.sndEnergiesList.size()
                + this.sndPrioritiesList.size();
    }

    /**
     * Adds a received new thought message to the list.
     *
     * @param msg The new thought message.
     */
    public void addRcvThought(RecvMsgThought msg) {
        rcvThgtList.add(msg);
    }

    /**
     * Adds a received update action message to list and makes changes to the
     * corresponding send action message (if it exists).
     *
     * @param msg The update action message.
     */
    public void addRcvAction(RecvMsgAction msg) {
        rcvActnList.add(msg);

        SendMsgAction sendMesActn = sndActionsMap.get(msg.id);
        if (sendMesActn != null) {
            sendMesActn.setDate(msg.date);
            sendMesActn.setDoneDate(msg.doneDate);
            sendMesActn.setStartHr(msg.startHr);
            sendMesActn.setStartMn(msg.startMn);
            sendMesActn.setLengthHrs(msg.lengthHrs);
            sendMesActn.setLengthMns(msg.lengthMns);
            sendMesActn.setNotes(msg.notes);
        }
    }

    /**
     * Adds a received update project message to list and makes changes to the
     * corresponding send project message (if it exists).
     *
     * @param rcvMsg The update project message.
     */
    public void addRcvProject(RecvMsgProject rcvMsg) {
        rcvProjectList.add(rcvMsg);

        SendMsgProject sndMsg = sndProjectsMap.get(rcvMsg.id);
        if (sndMsg == null) {
            LOG.log(Level.WARNING, "Could not find project (ID: {0}) to update.", rcvMsg.id);
        } else {
            sndMsg.setChangedNotes(rcvMsg.notes);
            sndMsg.setChangedPurpose(rcvMsg.purpose);
            sndMsg.setChangedVision(rcvMsg.vision);
            sndMsg.setChangedBrainstorm(rcvMsg.brainstorm);
            sndMsg.setChangedOrganise(rcvMsg.organise);
            sndMsg.setChangedDueDate(rcvMsg.dueDate);
        }
    }

    /**
     * Adds a received reference message to list and makes changes to the send
     * references map.
     *
     * @param rcvMsgRef The received reference message.
     */
    public void addRcvReference(RecvMsgReference rcvMsgRef) {
        rcvRefsList.add(rcvMsgRef);

        // Make changes to send reference in map 
        if (rcvMsgRef.change.equals("D")) {
            // delete: remove send-reference message from map
            sndRefsMap.remove(rcvMsgRef.getId());
            return;
        }
        if (rcvMsgRef.change.equals("I")) {
            // insert: add new send-reference message to map
            int newID = data.getNextID();
            rcvMsgRef.setId(newID);
            SendMsgReference sndMsgRef = new SendMsgReference(null);
            sndMsgRef.setInsertID(newID);
            sndMsgRef.setChangeTitle(rcvMsgRef.title);
            sndMsgRef.setChangeNotes(rcvMsgRef.notes);
            sndMsgRef.setChangeTopicID(rcvMsgRef.topicID);
            sndRefsMap.put(newID, sndMsgRef);
            return;
        }
        if (rcvMsgRef.change.equals("U")) {
            // update: change send-reference message in map
            SendMsgReference sndMsgRef = sndRefsMap.get(rcvMsgRef.getId());
            if (sndMsgRef != null) {
                sndMsgRef.setChangeTitle(rcvMsgRef.title);
                sndMsgRef.setChangeNotes(rcvMsgRef.notes);
                sndMsgRef.setChangeTopicID(rcvMsgRef.topicID);
            }
        }
    }

    /**
     * Gets the next send reference message. It is assumed that this will be
     * called only after all receive references are added.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgReference getNxtSendMsgRef() {

        // if first time, create list from map (updated with any changes). 
        if (sndRefsList == null) {
            sndRefsList = new ArrayList<>(sndRefsMap.values());
        }

        if (sndRefsIndex < sndRefsList.size()) {
            return sndRefsList.get(sndRefsIndex++);
        }
        return null;
    }

    /**
     * Gets the next send project message. It is assumed that this will be
     * called only after all receive projects are added.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgProject getNxtSendMsgProject() {
        if (sndProjectsIndex < sndProjectsList.size()) {
            return sndProjectsList.get(sndProjectsIndex++);
        }
        return null;
    }

    /**
     * Gets the next send action message skipping any where the action has been
     * updated to done or scheduled date is removed or postponed after 30 days.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgAction getNxtSendMsgActn() {

        // if first time, create list from map (updated with any changes). 
        if (sndActionsList == null) {
            sndActionsList = new ArrayList<>(sndActionsMap.values());
        }

        while (sndActnIndex < sndActionsList.size()) {

            SendMsgAction sndMsgActn = sndActionsList.get(sndActnIndex++);

            if (sndMsgActn.isDone()) {
                LOG.log(Level.INFO, "Not sending action, updated to done.");
                continue;
            }

//            switch (sndMsgActn.getState()) {
//                case INACTIVE:
//                case SCHEDULED:
//                    Date date = sndMsgActn.getDate();
//                    if (date == null || !date.before(maxToDate)) {
//                        LOG.log(Level.INFO, "Not sending action, posponed date.");
//                        continue;
//                    }
//            }
            return sndMsgActn;
        }

        return null;
    }

    /**
     * Gets the next send topic message.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgTopic getNxtSendMsgTopc() {
        if (sndTopcIndex < sndTopicsList.size()) {
            return sndTopicsList.get(sndTopcIndex++);
        }
        return null;
    }

    /**
     * Gets the next send context message.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgContext getNxtSendMsgCntx() {
        if (sndCntxIndex < sndContextsList.size()) {
            return sndContextsList.get(sndCntxIndex++);
        }
        return null;
    }

    /**
     * Gets the next send time message.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgTime getNxtSendMsgTime() {
        if (sndTimeIndex < sndTimesList.size()) {
            return sndTimesList.get(sndTimeIndex++);
        }
        return null;
    }

    /**
     * Gets the next send energy message.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgEnergy getNxtSendMsgEnergy() {
        if (sndEngyIndex < sndEnergiesList.size()) {
            return sndEnergiesList.get(sndEngyIndex++);
        }
        return null;
    }

    /**
     * Gets the next send priority message.
     *
     * @return The next message or null if there are no more.
     */
    public SendMsgPriority getNxtSendMsgPriority() {
        if (sndPrtyIndex < sndPrioritiesList.size()) {
            return sndPrioritiesList.get(sndPrtyIndex++);
        }
        return null;
    }

    public Topic getTopic(int topicId) {
        for (Topic topic : data.getTopicManager().list()) {
            if (topic.getID() == topicId) {
                return topic;
            }
        }
        return Topic.getDefault();
    }

    /**
     * Commits data changes from received messages.
     */
    public void commitDataChanges() {

        LOG.info("Start storing device sync data ... ");

        // add new thoughts
        for (RecvMsgThought msgNewThgt : rcvThgtList) {
            Thought thought = new Thought(data.getNextID());
            thought.setDescription(msgNewThgt.title);
            thought.setNotes(msgNewThgt.notes);
            thought.setTopic(getTopic(msgNewThgt.topicID));
            data.getThoughtManager().add(thought);

            LOG.info(" ... added new thought");
        }

        // update actions
        for (RecvMsgAction msgUpdActn : rcvActnList) {
            Action action = Services.instance.getAction(msgUpdActn.id);
            if (action == null) {
                LOG.log(Level.WARNING, "Could not find action (ID: {0}) to update.", msgUpdActn.id);
            } else {
                action.setDoneDate(msgUpdActn.doneDate);
                action.setNotes(msgUpdActn.notes);
                switch (action.getState().getType()) {
                    case DOASAP:
                        action.setDueDate(msgUpdActn.date);
                        break;
                    case INACTIVE:
                        action.setStartDate(msgUpdActn.date);
                        break;
                    case SCHEDULED:
                        ActionStateScheduled state = (ActionStateScheduled) action.getState();
                        if (msgUpdActn.date == null) {
                            state.setDate(null);
                            state.setDurationHours(0);
                            state.setDurationMins(0);
                        } else {
                            state.setDate(msgUpdActn.date);
                            state.setSchdHour(msgUpdActn.startHr == null ? 0 : msgUpdActn.startHr);
                            state.setSchdMinute(msgUpdActn.startMn == null ? 0 : msgUpdActn.startMn);
                            state.setDurationHours(msgUpdActn.lengthHrs == null ? 0 : msgUpdActn.lengthHrs);
                            state.setDurationMins(msgUpdActn.lengthMns == null ? 0 : msgUpdActn.lengthMns);
                        }
                        break;
                    case DELEGATED:
                        ((ActionStateDelegated) action.getState()).setDate(msgUpdActn.date);
                        break;
                }
                LOG.info(" ... updated action");
            }
        }

        // update projects
        if (!rcvProjectList.isEmpty()) {
            projectsMap = new HashMap<>();
            for (Project project : Services.instance.getProjects()) {
                projectsMap.put(project.getID(), project);
            }
            for (RecvMsgProject updMsg : rcvProjectList) {
                Project project = projectsMap.get(updMsg.id);
                if (null == project) {
                    LOG.log(Level.WARNING, "Could not find project (ID: {0}) to update.", updMsg.id);
                } else {
                    project.setNotes(updMsg.notes);
                    project.setPurpose(updMsg.purpose);
                    project.setVision(updMsg.vision);
                    project.setBrainstorming(updMsg.brainstorm);
                    project.setOrganising(updMsg.organise);
                    project.setDueDate(updMsg.dueDate);
                    LOG.info(" ... updated project");
                }
            }
        }

        // update references
        for (RecvMsgReference msgCngRef : rcvRefsList) {
            if (msgCngRef.change.equals("I")) {
                Information info = new Information(msgCngRef.getId());
                info.setDescription(msgCngRef.title);
                info.setNotes(msgCngRef.notes);
                info.setTopic(Services.instance.getTopic(msgCngRef.topicID));
                if (data.getInformationManager().add(info)) {
                    LOG.info(" ... inserted reference");
                }
            } else {
                Information info = Services.instance.getReference(msgCngRef.getId());
                if (info == null) {
                    LOG.log(Level.WARNING, "Could not find reference (ID: {0}) to update.", msgCngRef.getId());
                } else if (msgCngRef.change.equals("U")) {
                    info.setDescription(msgCngRef.title);
                    info.setNotes(msgCngRef.notes);
                    info.setTopic(Services.instance.getTopic(msgCngRef.topicID));
                    LOG.info(" ... updated reference");
                } else if (msgCngRef.change.equals("D")) {
                    if (data.getInformationManager().remove(info)) {
                        LOG.info(" ... deleted reference");
                    }
                }
            }
        }

        // save data to file
        DataStore ds = DataStoreLookup.instance().lookup(DataStore.class);
        if (ds == null) {
            LOG.log(Level.SEVERE, "Data store could not be found.");
            return;
        }
        try {
            ds.store();
            LOG.info(" ... stored data");
            LOG.info("Finished storing device sync data.");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Data could not be stored.", ex);
        }
    }

//    private Date getTodayPlus30Days() {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(DateUtils.getEnd(new Date()));
//        cal.add(Calendar.DAY_OF_YEAR, 30);
//        return cal.getTime();
//    }
    // CHANGED to do the following:
    //  - create send actions map
    //  - populate send single actions (with ordinal)    
    //  - populate send project actions (with ordinal)
    //  - update all send projects with ordinal
    private Map<Integer, SendMsgAction> createSendActionsMap() {

        Map<Integer, SendMsgAction> map = new HashMap<>();

        // single actions
        int ordinal = 1;
        for (Action action : Services.instance.getSingleActions()) {
            if (!action.isDone()) {
                map.put(action.getID(), newSendMsgAction(action, ordinal++));
            }
        }

        // set ordinal on top level projects 
        ordinal = 1;
        for (Project project : data.getRootProjects().getChildren(Project.class)) {
            SendMsgProject smp = sndProjectsMap.get(project.getID());
            if (smp != null) {
                smp.setOrdinal(ordinal++);
            }
        }

        // populate send actions map with actions from each send project
        // also setting ordinal numbers for actions and sub-projects.
        for (SendMsgProject smp : sndProjectsMap.values()) {
            populateSendActionsMap(map, smp.getProject());
        }

        return Collections.unmodifiableMap(map);
    }

    private void populateSendActionsMap(Map<Integer, SendMsgAction> map, Project project) {
        int ordinal = 1;
        for (Item child : project.getChildren()) {
            if (child instanceof Action action) {
                if (!action.isDone()) {
                    map.put(action.getID(), newSendMsgAction(action, ordinal++));
                }
            } else if (child instanceof Project) {
                SendMsgProject smp = sndProjectsMap.get(child.getID());
                if (smp != null) {
                    smp.setOrdinal(ordinal++);
                }
            }
        }
    }

    private Map<Integer, SendMsgReference> createSendRefsMap() {
        Map<Integer, SendMsgReference> map = new HashMap<>();
        for (Information info : Services.instance.getReferences()) {
            map.put(info.getID(), new SendMsgReference(info));
        }
        return map;
    }

//    private SendMsgReference newSendMsgReference(Information info) {
//        return new SendMsgReference(info);
//    }
//    private List<SendMsgReference> getRefsList() {
//        return Collections.unmodifiableList(new ArrayList<>(sndRefsMap.values()));
//    }
//    private SendMsgAction newSendMsgActn(Action action) {
//        return new SendMsgAction(action, isTimeUsed, isEnergyUsed, isPriorityUsed);
//    }
    private SendMsgAction newSendMsgAction(Action action, int ordinal) {
        return new SendMsgAction(action, isTimeUsed, isEnergyUsed, isPriorityUsed, ordinal);
    }

//    private List<SendMsgAction> getActnList() {
//        return Collections.unmodifiableList(new ArrayList<>(sndActnMap.values()));
//    }
    private List<SendMsgTopic> createSendTopicList() {
        List<SendMsgTopic> list = new ArrayList<>();
        for (Topic item : data.getTopicManager().list()) {
            list.add(new SendMsgTopic(item));
        }
        return Collections.unmodifiableList(list);
    }

    private List<SendMsgContext> createSendContextList() {
        List<SendMsgContext> list = new ArrayList<>();
        for (Context item : data.getContextManager().list()) {
            list.add(new SendMsgContext(item));
        }
        return Collections.unmodifiableList(list);
    }

    private List<SendMsgTime> createSendTimeList() {
        List<SendMsgTime> list = new ArrayList<>();
        if (isTimeUsed) {
            for (Value item : data.getTimeCriterion().values.list()) {
                list.add(new SendMsgTime(item));
            }
        }
        return Collections.unmodifiableList(list);
    }

    private List<SendMsgEnergy> createSendEnergiesList() {
        List<SendMsgEnergy> list = new ArrayList<>();
        if (isEnergyUsed) {
            for (Value item : data.getEnergyCriterion().values.list()) {
                list.add(new SendMsgEnergy(item));
            }
        }
        return Collections.unmodifiableList(list);
    }

    private List<SendMsgPriority> createSendPrioritiesList() {
        List<SendMsgPriority> list = new ArrayList<>();
        if (isPriorityUsed) {
            for (Value item : data.getPriorityCriterion().values.list()) {
                list.add(new SendMsgPriority(item));
            }
        }
        return Collections.unmodifiableList(list);
    }

    private List<SendMsgProject> createSendProjectsList() {
        final List<SendMsgProject> list = new ArrayList<>();
        for (Project project : Services.instance.getProjectsInPathOrder()) {
            if (!project.isDone()) {
                list.add(new SendMsgProject(project));
            }
        }
        return Collections.unmodifiableList(list);
    }

    private Map<Integer, SendMsgProject> createSendProjectsMap(List<SendMsgProject> list) {
        final Map<Integer, SendMsgProject> map = new HashMap<>();
        for (SendMsgProject smp : list) {
            map.put(smp.getID(), smp);
        }
        return Collections.unmodifiableMap(map);
    }

    // Added for dropbx sync
    public List<SendMsgAction> getSendActions() {
        return new ArrayList<>(sndActionsMap.values());
    }

    public List<SendMsgProject> getSendProjects() {
        return sndProjectsList;
    }

    public List<SendMsgReference> getSendReferences() {
        return new ArrayList<>(sndRefsMap.values());
    }

    public List<SendMsgTopic> getSendTopics() {
        return sndTopicsList;
    }

    public List<SendMsgContext> getSendContexts() {
        return sndContextsList;
    }

    public List<SendMsgTime> getSendTimes() {
        return sndTimesList;
    }

    public List<SendMsgEnergy> getSendEnergies() {
        return sndEnergiesList;
    }

    public List<SendMsgPriority> getSendPriorities() {
        return sndPrioritiesList;
    }

}
