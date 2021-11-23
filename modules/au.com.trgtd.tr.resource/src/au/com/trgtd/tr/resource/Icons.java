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

import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 * ThinkingRock graphic icons.

 * @author Jeremy Moore
 */
public class Icons {

    private final static boolean LOCALIZED = false;
    
    // Collect
    public final static ImageIcon Collect = ImageUtilities.loadImageIcon(Resource.Collect, LOCALIZED);
    public final static ImageIcon Thought = ImageUtilities.loadImageIcon(Resource.Thought, LOCALIZED);
    public final static ImageIcon ThoughtAdd = ImageUtilities.loadImageIcon(Resource.ThoughtAdd, LOCALIZED);
//  public final static ImageIcon ThoughtDelete = ImageUtilities.loadImageIcon(Resource.ThoughtDelete, LOCALIZED);
    public final static ImageIcon ThoughtEdit = ImageUtilities.loadImageIcon(Resource.ThoughtEdit, LOCALIZED);
    // Action
    public final static ImageIcon ActionDoASAP = ImageUtilities.loadImageIcon(Resource.ActionDoASAP, LOCALIZED);
    public final static ImageIcon ActionInactive = ImageUtilities.loadImageIcon(Resource.ActionInactive, LOCALIZED);
    public final static ImageIcon ActionDelegated = ImageUtilities.loadImageIcon(Resource.ActionDelegated, LOCALIZED);
    public final static ImageIcon ActionScheduled = ImageUtilities.loadImageIcon(Resource.ActionScheduled, LOCALIZED);
    public final static ImageIcon ActionScheduledReg = ImageUtilities.loadImageIcon(Resource.ActionScheduledReg, LOCALIZED);
    public final static ImageIcon ActionScheduledSub = ImageUtilities.loadImageIcon(Resource.ActionScheduledSub, LOCALIZED);
    public final static ImageIcon Actions = ImageUtilities.loadImageIcon(Resource.Actions, LOCALIZED);
    public final static ImageIcon ActionAdd = ImageUtilities.loadImageIcon(Resource.ActionAdd, LOCALIZED);
    public final static ImageIcon StatusChange = ImageUtilities.loadImageIcon(Resource.ChangeStatus, LOCALIZED);
    // Project
    public final static ImageIcon Project = ImageUtilities.loadImageIcon(Resource.Project, LOCALIZED);
    public final static ImageIcon ProjectWarn = ImageUtilities.loadImageIcon(Resource.ProjectWarn, LOCALIZED);
    public final static ImageIcon ProjectOpen = ImageUtilities.loadImageIcon(Resource.ProjectOpen, LOCALIZED);
    public final static ImageIcon ProjectOpenWarn = ImageUtilities.loadImageIcon(Resource.ProjectOpenWarn, LOCALIZED);
    public final static ImageIcon Projects = ImageUtilities.loadImageIcon(Resource.Projects, LOCALIZED);
    public final static ImageIcon ProjectAdd = ImageUtilities.loadImageIcon(Resource.ProjectAdd, LOCALIZED);
    public final static ImageIcon ProjectChange = ImageUtilities.loadImageIcon(Resource.ProjectChange, LOCALIZED);
    public final static ImageIcon Projectise = ImageUtilities.loadImageIcon(Resource.Projectise, LOCALIZED);
    public final static ImageIcon ProjectsTemplate = ImageUtilities.loadImageIcon(Resource.ProjectsTemplate, LOCALIZED);
    public final static ImageIcon ProjectTemplate = ImageUtilities.loadImageIcon(Resource.ProjectTemplate, LOCALIZED);
    public final static ImageIcon ProjectTemplateOpen = ImageUtilities.loadImageIcon(Resource.ProjectTemplateOpen, LOCALIZED);
    public final static ImageIcon ProjectsFuture = ImageUtilities.loadImageIcon(Resource.ProjectsFuture, LOCALIZED);
    public final static ImageIcon ProjectFuture = ImageUtilities.loadImageIcon(Resource.ProjectFuture, LOCALIZED);
    public final static ImageIcon ProjectFutureOpen = ImageUtilities.loadImageIcon(Resource.ProjectFutureOpen, LOCALIZED);
    public final static ImageIcon SingleActions = ImageUtilities.loadImageIcon(Resource.SingleActions, LOCALIZED);
    // Context
    public final static ImageIcon Context = ImageUtilities.loadImageIcon(Resource.Context, LOCALIZED);
    public final static ImageIcon ContextAdd = ImageUtilities.loadImageIcon(Resource.ContextAdd, LOCALIZED);
    public final static ImageIcon ContextDelete = ImageUtilities.loadImageIcon(Resource.ContextDelete, LOCALIZED);
    public final static ImageIcon ContextEdit = ImageUtilities.loadImageIcon(Resource.ContextEdit, LOCALIZED);
    // Topic
    public final static ImageIcon Topics = ImageUtilities.loadImageIcon(Resource.Topics, LOCALIZED);
    public final static ImageIcon Topic = ImageUtilities.loadImageIcon(Resource.Topic, LOCALIZED);
    public final static ImageIcon TopicAdd = ImageUtilities.loadImageIcon(Resource.TopicAdd, LOCALIZED);
    public final static ImageIcon TopicDelete = ImageUtilities.loadImageIcon(Resource.TopicDelete, LOCALIZED);
    public final static ImageIcon TopicEdit = ImageUtilities.loadImageIcon(Resource.TopicEdit, LOCALIZED);
    // Process
    public final static ImageIcon Process = ImageUtilities.loadImageIcon(Resource.Process, LOCALIZED);
    public final static ImageIcon ProcessThoughts = ImageUtilities.loadImageIcon(Resource.ProcessThoughts, LOCALIZED);
    public final static ImageIcon Reprocess = ImageUtilities.loadImageIcon(Resource.Reprocess, LOCALIZED);
    // Reference
    public final static ImageIcon References = ImageUtilities.loadImageIcon(Resource.References, LOCALIZED);
    public final static ImageIcon Reference = ImageUtilities.loadImageIcon(Resource.Reference, LOCALIZED);
    public final static ImageIcon ReferenceEdit = ImageUtilities.loadImageIcon(Resource.ReferenceEdit, LOCALIZED);
    public final static ImageIcon ReferenceDelete = ImageUtilities.loadImageIcon(Resource.ReferenceDelete, LOCALIZED);
    // Someday/Maybe
    public final static ImageIcon SomedayMaybes = ImageUtilities.loadImageIcon(Resource.SomedayMaybes, LOCALIZED);
    public final static ImageIcon SomedayMaybe = ImageUtilities.loadImageIcon(Resource.SomedayMaybe, LOCALIZED);
    public final static ImageIcon SomedayMaybeEdit = ImageUtilities.loadImageIcon(Resource.SomedayMaybeEdit, LOCALIZED);
    public final static ImageIcon SomedayMaybeDelete = ImageUtilities.loadImageIcon(Resource.SomedayMaybeDelete, LOCALIZED);
    // Overview
    public final static ImageIcon Overview = ImageUtilities.loadImageIcon(Resource.Overview, LOCALIZED);
    // Criteria
    public final static ImageIcon Criteria = ImageUtilities.loadImageIcon(Resource.Criteria, LOCALIZED);
    public final static ImageIcon CriteriaEdit = ImageUtilities.loadImageIcon(Resource.CriteriaEdit, LOCALIZED);
    // Misc
    public final static ImageIcon PDF = ImageUtilities.loadImageIcon(Resource.PDF, LOCALIZED);
    public final static ImageIcon Report = ImageUtilities.loadImageIcon(Resource.Report, LOCALIZED);
    public final static ImageIcon FiltersEdit = ImageUtilities.loadImageIcon(Resource.FiltersEdit, LOCALIZED);
    public final static ImageIcon FiltersView = ImageUtilities.loadImageIcon(Resource.FiltersView, LOCALIZED);
    public final static ImageIcon ColumnsEdit = ImageUtilities.loadImageIcon(Resource.ColumnsEdit, LOCALIZED);
    public final static ImageIcon ShowDone = ImageUtilities.loadImageIcon(Resource.ShowDone, LOCALIZED);
    public final static ImageIcon Up = ImageUtilities.loadImageIcon(Resource.Up, LOCALIZED);
    public final static ImageIcon Down = ImageUtilities.loadImageIcon(Resource.Down, LOCALIZED);
    public final static ImageIcon Add = ImageUtilities.loadImageIcon(Resource.Add, LOCALIZED);
    public final static ImageIcon Delete = ImageUtilities.loadImageIcon(Resource.Delete, LOCALIZED);
    public final static ImageIcon Edit = ImageUtilities.loadImageIcon(Resource.Edit, LOCALIZED);
    public final static ImageIcon Trash = ImageUtilities.loadImageIcon(Resource.Trash, LOCALIZED);
    public final static ImageIcon Archive = ImageUtilities.loadImageIcon(Resource.Archive, LOCALIZED);
    public final static ImageIcon Text = ImageUtilities.loadImageIcon(Resource.Text, LOCALIZED);
    public final static ImageIcon XML = ImageUtilities.loadImageIcon(Resource.XML, LOCALIZED);
    public final static ImageIcon Web = ImageUtilities.loadImageIcon(Resource.Web, LOCALIZED);
    public final static ImageIcon SortAZ = ImageUtilities.loadImageIcon(Resource.SortAZ, LOCALIZED);
    public final static ImageIcon SortZA = ImageUtilities.loadImageIcon(Resource.SortZA, LOCALIZED);
    public final static ImageIcon ZoomIn = ImageUtilities.loadImageIcon(Resource.ZoomIn, LOCALIZED);
    public final static ImageIcon ZoomOut = ImageUtilities.loadImageIcon(Resource.ZoomOut, LOCALIZED);
    public final static ImageIcon CheckBox = ImageUtilities.loadImageIcon(Resource.CheckBox, LOCALIZED);
    
    public final static ImageIcon CalendarEdit = ImageUtilities.loadImageIcon(Resource.CalendarEdit, LOCALIZED);
    public final static ImageIcon CalendarEdit24 = ImageUtilities.loadImageIcon(Resource.CalendarEdit24, LOCALIZED);
    
    public final static ImageIcon Delegate = ImageUtilities.loadImageIcon(Resource.Delegate, LOCALIZED);
    public final static ImageIcon Delegates = ImageUtilities.loadImageIcon(Resource.Delegates, LOCALIZED);
    
}
