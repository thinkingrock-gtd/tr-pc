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
package au.com.trgtd.tr.resource;

import java.net.URL;
import org.openide.filesystems.FileUtil;

/**
 * ThinkingRock resources.

 * @author Jeremy Moore
 */
public class Resource {

    public final static String PATH = "au/com/trgtd/tr/resource/icons/";

    // Collect
    public final static String Collect = PATH + "Collect.png";
    public final static String Thought = PATH + "Thought.png";
    public final static String ThoughtAdd = PATH + "ThoughtAdd.png";
    public final static String ThoughtDelete = PATH + "ThoughtDelete.png";
    public final static String ThoughtEdit = PATH + "ThoughtEdit.png";
    // Action
    public final static String ActionDoASAP = PATH + "ActionDoASAP.png";
    public final static String ActionInactive = PATH + "ActionInactive.png";
    public final static String ActionDelegated = PATH + "ActionDelegated.png";
    public final static String ActionScheduled = PATH + "ActionScheduled.png";
    public final static String ActionScheduledReg = PATH + "ActionScheduledReg.png";
    public final static String ActionScheduledSub = PATH + "ActionScheduledSub.png";
    public final static String Actions = PATH + "Actions.png";
    public final static String ActionAdd = PATH + "ActionAdd.png";
    public final static String ChangeStatus = PATH + "ChangeStatus.png";

    // Project
    public final static String Project = PATH + "Project.png";
    public final static String ProjectWarn = PATH + "ProjectWarn.png";
    public final static String ProjectOpen = PATH + "ProjectOpen.png";
    public final static String ProjectOpenWarn = PATH + "ProjectOpenWarn.png";
    public final static String ProjectAdd = PATH + "ProjectAdd.png";
    public final static String ProjectChange = PATH + "ProjectChange.png";    
    public final static String Projectise = PATH + "Projectise.png";
    public final static String Projects = PATH + "Projects.png";
    public final static String ProjectsTemplate = PATH + "ProjectsTemplate.png";
    public final static String ProjectTemplate = PATH + "ProjectTemplate.png";
    public final static String ProjectTemplateOpen = PATH + "ProjectTemplateOpen.png";
    public final static String ProjectsFuture = PATH + "ProjectsFuture.png";
    public final static String ProjectFuture = PATH + "ProjectFuture.png";
    public final static String ProjectFutureOpen = PATH + "ProjectFutureOpen.png";
    public final static String SingleActions = PATH + "SingleActions.png";
    // Reference
    public final static String References = PATH + "References.png";
    public final static String Reference = PATH + "Reference.png";
    public final static String ReferenceEdit = PATH + "ReferenceEdit.png";
    public final static String ReferenceDelete = PATH + "ReferenceDelete.png";
    // Someday/Maybe
    public final static String SomedayMaybes = PATH + "SomedayMaybes.png";
    public final static String SomedayMaybe = PATH + "SomedayMaybe.png";
    public final static String SomedayMaybeEdit = PATH + "SomedayMaybeEdit.png";
    public final static String SomedayMaybeDelete = PATH + "SomedayMaybeDelete.png";

    // Process
    public final static String Process = PATH + "Process.png";
    public final static String ProcessThoughts = PATH + "ProcessThoughts.png";
    public final static String Reprocess = PATH + "Reprocess.png";
    // Context
    public final static String Contexts = PATH + "Contexts.png";
    public final static String Context = PATH + "Context.png";
    public final static String ContextAdd = PATH + "ContextAdd.png";
    public final static String ContextDelete = PATH + "ContextDelete.png";
    public final static String ContextEdit = PATH + "ContextEdit.png";

    // Topic
    public final static String Topics = PATH + "Topics.png";
    public final static String Topic = PATH + "Topic.png";
    public final static String TopicAdd = PATH + "TopicAdd.png";
    public final static String TopicDelete = PATH + "TopicDelete.png";
    public final static String TopicEdit = PATH + "TopicEdit.png";
    // Criteria
    public final static String Criteria = PATH + "Criteria.png";
    public final static String CriteriaEdit = PATH + "CriteriaEdit.png";
    // Misc
    public final static String PDF = PATH + "PDF.png";
    public final static String Report = PATH + "Report.gif";
    public final static String FiltersEdit = PATH + "FiltersEdit.png";
    public final static String FiltersView = PATH + "FiltersView.png";
    public final static String ColumnsEdit = PATH + "ColumnsEdit.png";
    public final static String ShowDone = PATH + "ShowDone.png";
    public final static String Up = PATH + "Up.png";
    public final static String Down = PATH + "Down.png";
    public final static String Add = PATH + "Add.png";
    public final static String Edit = PATH + "Edit.png";
    public final static String Delete = PATH + "Delete.png";
    public final static String Trash = PATH + "Trash.gif";
    public final static String Archive = PATH + "Archive.gif";
    public final static String Text = PATH + "Text.gif";
    public final static String XML = PATH + "XML.gif";
    public final static String Web = PATH + "Web.png";
    public final static String SortAZ = PATH + "SortAZ.png";
    public final static String SortZA = PATH + "SortZA.png";
    public final static String ZoomIn = PATH + "ZoomIn.png";
    public final static String ZoomOut = PATH + "ZoomOut.png";
    public final static String CheckBox = PATH + "CheckBox.png";
    
    public final static String CalendarEdit = PATH + "CalendarEdit.png";
    public final static String CalendarEdit24 = PATH + "CalendarEdit24.png";
    
    public final static String Delegate = PATH + "Delegate.png";
    public final static String Delegates = PATH + "Delegates.png";

    // Help
    public final static String CSHelp = PATH + "CSHelp.png";
    // Data
    public final static String DataNew = PATH + "DataNew.png";
    public final static String DataSave = PATH + "DataSave.png";
    public final static String DataSaveAs = PATH + "DataSaveAs.png";
    public final static String DataSaveTo = PATH + "DataSaveTo.png";
    public final static String DataOpen = PATH + "DataOpen.png";
    // Overview
    public final static String Overview = PATH + "Overview.png";

    public final static URL getOverviewURL() {
        return FileUtil.getConfigFile("Overview/overview.svg").toURL();
    }

}
