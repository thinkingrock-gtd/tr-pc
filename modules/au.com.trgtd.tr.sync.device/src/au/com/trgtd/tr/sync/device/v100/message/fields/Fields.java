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
package au.com.trgtd.tr.sync.device.v100.message.fields;

/**
 * Message fields.
 *
 * @author Jeremy Moore
 */
public interface Fields {

    /** Common fields. */
    public interface Common {
	static final String VERSION = "Version";
        static final String DATA_ID = "DataID";
        static final String ID = "ID";
        static final String TITLE = "Title";
        static final String NOTES = "Notes";
        static final String TOPIC_ID = "TopicID";
        static final String DATE = "Date";
        static final String START_HR = "StartHr";
        static final String START_MN = "StartMn";
        static final String LENGTH_HRS = "LengthHrs";
        static final String LENGTH_MNS = "LengthMns";
        static final String CHANGE = "Change";
                
        static final String PURPOSE = "Purpose";
        static final String VISION = "Vision";
        static final String BRAINSTORM = "Brainstorm";
        static final String ORGANISE = "Organise";
        static final String DUE = "Due";
        
        static final String PARENT_ID = "ParentID";
        static final String ORDINAL = "Ordinal";
    }
    
    public interface Send {
        /** Summary message fields. */
        public interface Summary {
            static final String DATA_ID = Common.DATA_ID;
            static final String TOPICS = "Topics";
            static final String CONTEXTS = "Contexts";
            static final String TIMES = "Times";
            static final String ENERGIES = "Energies";
            static final String PRIORITIES = "Priorities";
            static final String ACTIONS = "Actions";
            static final String PROJECTS = "Projects";
            static final String REFERENCES = "References";
        }
        
        /** Project message fields. */
        public interface Project {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
            static final String NOTES = Common.NOTES;
            static final String PURPOSE = Common.PURPOSE;
            static final String VISION = Common.VISION;
            static final String BRAINSTORM = Common.BRAINSTORM;
            static final String ORGANISE = Common.ORGANISE;
            static final String PATH = "Path";
            static final String DEPTH = "Depth";
            static final String DUE = Common.DUE;
            static final String PARENT_ID = Common.PARENT_ID;
            static final String TOPIC_ID = Common.TOPIC_ID;
            static final String PRIORITY_ID = "PriorityID";
            static final String ORDINAL = "Ordinal";
        }
        
        /** Action message fields. */
        public interface Action {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
            static final String NOTES = Common.NOTES;
            static final String DATE = Common.DATE;
            static final String PATH = "Path";
            static final String TOPIC_ID = Common.TOPIC_ID;
            static final String CONTEXT_ID = "ContextID";
            static final String TIME_ID = "TimeID";
            static final String ENERGY_ID = "EnergyID";
            static final String PRIORITY_ID = "PriorityID";
            static final String STATE = "State";
            static final String START_HR = Common.START_HR;
            static final String START_MN = Common.START_MN;
            static final String LENGTH_HRS = Common.LENGTH_HRS;
            static final String LENGTH_MNS = Common.LENGTH_MNS;
            static final String DELEGATE = "Delegate";

            static final String PARENT_ID = Common.PARENT_ID;
            static final String ORDINAL = Common.ORDINAL;
        }        
        /** Reference message fields. */
        public interface Reference {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
            static final String NOTES = Common.NOTES;
            static final String TOPIC_ID = Common.TOPIC_ID;
        }        
        /** Topic message fields. */
        public interface Topic {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
        }
        /** Context message fields. */
        public interface Context {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
        }
        /** Time message fields. */
        public interface Time {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
        }
        /** Energy message fields. */
        public interface Energy {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
        }
        /** Priority message fields. */
        public interface Priority {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
        }
    }

    public interface Receive {
        /** Summary message fields. */
        public interface Summary {
            static final String DATA_ID = Common.DATA_ID;
            static final String THOUGHTS = "Thoughts";
            static final String ACTIONS = "Actions";
            static final String PROJECTS = "Projects";
            static final String REFERENCES = "References";
        }
        /** Update action fields. */
        public interface UpdAction {
            static final String ID = Common.ID;
            static final String DONE = "Done";
            static final String NOTES = Common.NOTES;
            static final String DATE = Common.DATE;
            static final String START_HR = Common.START_HR;
            static final String START_MN = Common.START_MN;
            static final String LENGTH_HRS = Common.LENGTH_HRS;
            static final String LENGTH_MNS = Common.LENGTH_MNS;
        }
        /** Update project fields. */
        public interface UpdProject {
            static final String ID = Common.ID;
            static final String NOTES = Common.NOTES;
            static final String PURPOSE = Common.PURPOSE;
            static final String VISION = Common.VISION;
            static final String BRAINSTORM = Common.BRAINSTORM;
            static final String ORGANISE = Common.ORGANISE;
            static final String DUE = Common.DUE;
        }
        /** New thought fields. */
        public interface NewThought {
            static final String TITLE = Common.TITLE;
            static final String NOTES = Common.NOTES;
            static final String TOPIC_ID = Common.TOPIC_ID;
        }
        /** Reference fields. */
        public interface Reference {
            static final String ID = Common.ID;
            static final String TITLE = Common.TITLE;
            static final String NOTES = Common.NOTES;
            static final String TOPIC_ID = Common.TOPIC_ID;
            static final String CHANGE = Common.CHANGE;
        }
    }
}
