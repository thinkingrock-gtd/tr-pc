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
package au.com.trgtd.tr.view.actns.screens;

import java.util.logging.Logger;
import tr.model.util.Manager;
import au.com.trgtd.tr.swing.date.combo.DateItem;
import au.com.trgtd.tr.view.actns.screens.columns.ActionsColumn;
import au.com.trgtd.tr.view.actns.screens.filters.ActionsFilter;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDate;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateActionTo;
import au.com.trgtd.tr.view.actns.screens.filters.FilterContext;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateActionFrom;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDone;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateDoneFrom;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateDoneTo;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateDueFrom;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateDueTo;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateFollowupFrom;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateFollowupTo;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateScheduleFrom;
import au.com.trgtd.tr.view.actns.screens.filters.FilterDateScheduleTo;
import au.com.trgtd.tr.view.actns.screens.filters.FilterSearch;
import au.com.trgtd.tr.view.actns.screens.filters.FilterStatus;
import au.com.trgtd.tr.view.actns.screens.filters.FilterTopic;
import au.com.trgtd.tr.view.filters.ContextAll;
import au.com.trgtd.tr.view.filters.TopicAll;

/**
 * Actions screens.
 *
 * @author Jeremy Moore
 */
public final class ActionsScreens {
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
    private Manager<ActionsScreen> screens;
    
    /** Constructor. */
    public ActionsScreens() {
    }
    
    public Manager<ActionsScreen> getScreens() {
        if (screens == null) {
            screens = new Manager<>();
        }
        return screens;
    }
    
    public void setScreens(Manager<ActionsScreen> screens) {
        this.screens = screens;
    }
    
    public static final Manager<ActionsScreen> createDefaultScreens() {
        LOG.info("Creating default actions screens.");
        
        Manager<ActionsScreen> screens = new Manager<>();
        
        ActionsScreen screen;
        ActionsFilter filter;
        FilterDate dateFilter;
        
        // Today
        screen = ActionsScreen.create("screen-today");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_ACTION_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_SCHEDULE_DURATION).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DELEGATED_TO).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_PROJECT_PATH).setVisible(true);
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterDone.ToDo.ID});
        filter = screen.getFilters().get(FilterStatus.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterStatus.All.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { TopicAll.ID});
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateActionFrom.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(false);
        dateFilter.getFilterCombo().setSelectedItem(DateItem.TODAY);
        dateFilter.setExcludeNulls(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateActionTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(false);
        dateFilter.getFilterCombo().setSelectedItem(DateItem.TODAY);
        dateFilter.setExcludeNulls(true);
        
        screens.add(screen);

        // This Week
        screen = ActionsScreen.create("screen-week");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_ACTION_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_SCHEDULE_DURATION).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DELEGATED_TO).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_PROJECT_PATH).setVisible(true);
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterDone.ToDo.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { TopicAll.ID});
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateActionFrom.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(false);
        dateFilter.getFilterCombo().setSelectedItem(DateItem.TOMORROW);
        dateFilter.setExcludeNulls(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateActionTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(false);
        dateFilter.getFilterCombo().setSelectedItem(DateItem.WEEKS_1);
        dateFilter.setExcludeNulls(true);
        screens.add(screen);

        // Do ASAP
        screen = ActionsScreen.create("screen-do-asap");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CREATED_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DUE_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_START_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_TOPIC).setVisible(true);
        
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {FilterDone.ToDo.ID});
        filter = screen.getFilters().get(FilterStatus.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterStatus.ChoiceDoASAP.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { TopicAll.ID});
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateDueFrom.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.WEEKS_AGO_4);
        dateFilter.setExcludeNulls(false);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateDueTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.TOMORROW);
        dateFilter.setExcludeNulls(false);
        
        screens.add(screen);
        
        // Delegated
        screen = ActionsScreen.create("screen-delegated");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DUE_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_START_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FOLLOWUP_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DELEGATED_TO).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_TOPIC).setVisible(true);
        
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {FilterDone.ToDo.ID});
        filter = screen.getFilters().get(FilterStatus.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterStatus.ChoiceDelegated.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {TopicAll.ID});
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateFollowupFrom.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
  //    dateFilter.getFilterCombo().setSelectedItem(DateItem.WEEKS_AGO_4);
        dateFilter.setExcludeNulls(false);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateFollowupTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.TOMORROW);
        dateFilter.setExcludeNulls(false);
        
        screens.add(screen);
        
        // Scheduled screen
        screen = ActionsScreen.create("screen-scheduled");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_SCHEDULE_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_TOPIC).setVisible(true);
        
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {FilterDone.ToDo.ID});
        filter = screen.getFilters().get(FilterStatus.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterStatus.ChoiceScheduled.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {TopicAll.ID});
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateScheduleFrom.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.TODAY);
        dateFilter.setExcludeNulls(false);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateScheduleTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.TOMORROW);
        dateFilter.setExcludeNulls(false);       
        screens.add(screen);


        // All actions screen
        screen = ActionsScreen.create("screen-actions");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_ACTION_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DELEGATED_TO).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_TOPIC).setVisible(true);
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {FilterDone.ToDo.ID});
        filter = screen.getFilters().get(FilterStatus.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {FilterStatus.All.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { TopicAll.ID});
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateActionTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
  //    dateFilter.getFilterCombo().setSelectedItem(DateItem.TODAY);
        dateFilter.setExcludeNulls(false);
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        screens.add(screen);

        // Done screen
        screen = ActionsScreen.create("screen-done");
        screen.setShowFilters(true);
        screen.getColumns().get(ActionsColumn.INDEX_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CREATED_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DONE_DATE).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_DELEGATED_TO).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_CONTEXT).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_TOPIC).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_ICON).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_DESCR).setVisible(true);
        screen.getColumns().get(ActionsColumn.INDEX_FROM_TOPIC).setVisible(true);
        
        filter = screen.getFilters().get(FilterDone.INDEX);
        filter.setUsed(true);
        filter.setShown(false);
        filter.setSerialValues(new String[] {FilterDone.Done.ID});
        filter = screen.getFilters().get(FilterContext.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] { ContextAll.ID});
        filter = screen.getFilters().get(FilterTopic.INDEX);
        filter.setUsed(true);
        filter.setShown(true);
        filter.setSerialValues(new String[] {TopicAll.ID});
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateDoneFrom.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.WEEKS_AGO_4);
        dateFilter.setExcludeNulls(false);
        dateFilter = (FilterDate)screen.getFilters().get(FilterDateDoneTo.INDEX);
        dateFilter.setUsed(true);
        dateFilter.setShown(true);
//      dateFilter.getFilterCombo().setSelectedItem(DateItem.TODAY);
        dateFilter.setExcludeNulls(false);
        filter = screen.getFilters().get(FilterSearch.INDEX);
        filter.setUsed(true);
        filter.setShown(true);        
        screens.add(screen);
        
        return screens;
    }
    
}

