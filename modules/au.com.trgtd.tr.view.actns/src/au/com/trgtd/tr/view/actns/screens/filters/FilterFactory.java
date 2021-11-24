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
package au.com.trgtd.tr.view.actns.screens.filters;

/**
 * Filter factory.
 * @author Jeremy Moore
 */
public class FilterFactory {
    
    public static final FilterFactory instance = new FilterFactory();
    
    private FilterFactory() {}
    
    public static final byte[] INDICES = new byte[] {
        FilterDone.INDEX,
        FilterStatus.INDEX,
        FilterContext.INDEX,
        FilterCriterionTime.INDEX,
        FilterCriterionEnergy.INDEX,
        FilterCriterionPriority.INDEX,
        FilterTopic.INDEX,
        FilterDateDoneFrom.INDEX,
        FilterDateDoneTo.INDEX,
        FilterDateCreatedFrom.INDEX,
        FilterDateCreatedTo.INDEX,
        FilterDateActionFrom.INDEX,
        FilterDateActionTo.INDEX,
        FilterSearch.INDEX,
        FilterDateStartFrom.INDEX,
        FilterDateStartTo.INDEX,
        FilterDateDueFrom.INDEX,
        FilterDateDueTo.INDEX,
        FilterDateFollowupFrom.INDEX,
        FilterDateFollowupTo.INDEX,
        FilterDateScheduleFrom.INDEX,
        FilterDateScheduleTo.INDEX,
    };
    
    public static String getID(byte index) {
        switch (index) {
            case(FilterDone.INDEX) : return "done";
            case(FilterStatus.INDEX) : return "status";
            case(FilterContext.INDEX) : return "context";
            case(FilterCriterionTime.INDEX) : return "time";
            case(FilterCriterionEnergy.INDEX) : return "energy";
            case(FilterCriterionPriority.INDEX) : return "priority";
            case(FilterTopic.INDEX) : return "topic";
            case(FilterDateDoneFrom.INDEX) : return "done-from";
            case(FilterDateDoneTo.INDEX) : return "done-to";
            case(FilterDateCreatedFrom.INDEX) : return "created-from";
            case(FilterDateCreatedTo.INDEX) : return "created-to";
            case(FilterDateActionFrom.INDEX) : return "action-from";
            case(FilterDateActionTo.INDEX) : return "action-to";
            case(FilterSearch.INDEX) : return "search";
            case(FilterDateStartFrom.INDEX) : return "start-from";
            case(FilterDateStartTo.INDEX) : return "start-to";
            case(FilterDateDueFrom.INDEX) : return "due-from";
            case(FilterDateDueTo.INDEX) : return "due-to";
            case(FilterDateFollowupFrom.INDEX) : return "followup-from";
            case(FilterDateFollowupTo.INDEX) : return "followup-to";
            case(FilterDateScheduleFrom.INDEX) : return "schedule-from";
            case(FilterDateScheduleTo.INDEX) : return "schedule-to";
            default : return "";
        }
    }
    
    public ActionsFilter createFilter(byte id) {
        switch (id) {
            case FilterSearch.INDEX: return new FilterSearch();
            case FilterDone.INDEX: return new FilterDone();
            case FilterStatus.INDEX: return new FilterStatus();
            case FilterContext.INDEX: return new FilterContext();
            case FilterCriterionTime.INDEX: return new FilterCriterionTime();
            case FilterCriterionEnergy.INDEX: return new FilterCriterionEnergy();
            case FilterCriterionPriority.INDEX: return new FilterCriterionPriority();
            case FilterTopic.INDEX: return new FilterTopic();
            case FilterDateDoneFrom.INDEX: return new FilterDateDoneFrom();
            case FilterDateDoneTo.INDEX: return new FilterDateDoneTo();
            case FilterDateCreatedFrom.INDEX: return new FilterDateCreatedFrom();
            case FilterDateCreatedTo.INDEX: return new FilterDateCreatedTo();
            case FilterDateActionFrom.INDEX: return new FilterDateActionFrom();
            case FilterDateActionTo.INDEX: return new FilterDateActionTo();
            case FilterDateStartFrom.INDEX: return new FilterDateStartFrom();
            case FilterDateStartTo.INDEX: return new FilterDateStartTo();
            case FilterDateDueFrom.INDEX: return new FilterDateDueFrom();
            case FilterDateDueTo.INDEX: return new FilterDateDueTo();
            case FilterDateFollowupFrom.INDEX: return new FilterDateFollowupFrom();
            case FilterDateFollowupTo.INDEX: return new FilterDateFollowupTo();
            case FilterDateScheduleFrom.INDEX: return new FilterDateScheduleFrom();
            case FilterDateScheduleTo.INDEX: return new FilterDateScheduleTo();
        }
        return null;
    }
    
//    public ActionsFilter restore(PersistenceInputStream in) throws Exception {
//        byte id = in.readByte();
//        ActionsFilter filter = createFilter(id);
//        filter.restore(in);
//        return filter;
//    }
    
}
