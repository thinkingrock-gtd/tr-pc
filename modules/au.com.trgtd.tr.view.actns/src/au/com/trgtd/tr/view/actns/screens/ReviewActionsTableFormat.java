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

import au.com.trgtd.tr.appl.Constants;
import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.swing.StyledDate;
import au.com.trgtd.tr.swing.StyledString;
import au.com.trgtd.tr.util.DateNoTime;
import au.com.trgtd.tr.util.DateUtils;
import au.com.trgtd.tr.view.actns.screens.columns.ActionsColumn;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;
import java.awt.Color;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import tr.model.action.Action;
import tr.model.action.ActionState;
import tr.model.action.ActionStateDelegated;
import tr.model.action.ActionStateScheduled;
import tr.model.criteria.Value;
import tr.model.project.Project;
import tr.model.thought.Thought;
import tr.model.topic.Topic;

/**
 * Review Actions table format.
 *
 * @author Jeremy Moore
 */
public class ReviewActionsTableFormat implements AdvancedTableFormat<Action>, WritableTableFormat<Action> {
    
    private final ActionsScreen screen;
    
    public ReviewActionsTableFormat(ActionsScreen screen) {
        this.screen = screen;
    }
    
    @Override
    public int getColumnCount() {
        return screen.getColumns().size();
    }
    
    @Override
    public String getColumnName(int column) {
        try {
            return screen.getColumns().get(column).getColumnName();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return "";
        }
    }
    
    @Override
    public Object getColumnValue(Action action, int column) {
        if (action == null)
            return null;

        Project project = (Project)action.getParent();
        
        ActionsColumn field;
        try {
            field = screen.getColumns().get(column);
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException("Field missing.");
            return null;
        }
        
        switch (field.getColumnIndex()) {
            
            case ActionsColumn.INDEX_START_DATE:
                if (action.isStateScheduled()) {
                    return getStyledDate(null, false, action);
                } else {
                    return getStyledDate(action.getStartDate(), false, action);
                }
            case ActionsColumn.INDEX_DUE_DATE: {
                Date date = action.isStateScheduled() ? null : action.getDueDate();
                Color bg = action.getTopic().getBackground();
                Color fg = action.getTopic().getForeground();
                if (date != null) {
                    Date today = Calendar.getInstance().getTime();
                    if (DateUtils.isBeforeDay(date, today)) {
                        fg = Constants.COLOR_DATE_PAST;
                    } else if (DateUtils.isSameDay(date, today)) {
                        fg = Constants.COLOR_DATE_TODAY;
                    } else if (DateUtils.isWithinDaysFuture(date, 7)) {
                        fg = Constants.COLOR_DATE_WEEK;
                    }
                }
                return getStyledDate(date, false, action.isDone(), fg, bg);
            }
            case ActionsColumn.INDEX_ACTION_DATE: {
                Date date = action.getActionDate();
                boolean time = !(date instanceof DateNoTime);
                Color bg = action.getTopic().getBackground();
                Color fg = action.getTopic().getForeground();
                if (date != null) {
                    Date today = Calendar.getInstance().getTime();
                    if (DateUtils.isBeforeDay(date, today)) {
                        fg = Constants.COLOR_DATE_PAST;
                    } else if (DateUtils.isSameDay(date, today)) {
                        fg = Constants.COLOR_DATE_TODAY;
                    } else if (DateUtils.isWithinDaysFuture(date, 7)) {
                        fg = Constants.COLOR_DATE_WEEK;
                    }
                }
                return getStyledDate(date, time, action.isDone(), fg, bg);
            }
            case ActionsColumn.INDEX_CONTEXT:
                return getStyledString(action.getContext().toString(), action);
            case ActionsColumn.INDEX_CREATED_DATE:
                return getStyledDate(action.getCreated(), false, action);
            case ActionsColumn.INDEX_DELEGATED_TO:
                ActionState state = action.getState();
                String s = (action.isStateDelegated()) ? ((ActionStateDelegated)state).getTo() : "";
                return getStyledString(s, action);
            case ActionsColumn.INDEX_DESCR:
                return getStyledString(action.getDescription(), action);
            case ActionsColumn.INDEX_DONE:
                return action.isDone();
            case ActionsColumn.INDEX_DONE_DATE:
                return getStyledDate(action.getDoneDate(), false, action);
            case ActionsColumn.INDEX_TIME:
                return getStyledValue(action.getTime(), action);
            case ActionsColumn.INDEX_ENERGY:
                return getStyledValue(action.getEnergy(), action);
            case ActionsColumn.INDEX_PRIORITY:
                return getStyledValue(action.getPriority(), action);
            case ActionsColumn.INDEX_FROM_DESCR:
                if (action.isSingleAction()) {
                    Thought thought = action.getThought();
                    if (thought != null) {
                        return getStyledString(thought.getDescription(), action.isDone(), thought.getTopic());
                    }
                    return getStyledString("", action.isDone(), action.getTopic());
                } else {
                    if (project != null) {
                        return getStyledString(project.getDescription(), project.isDone(), project.getTopic());
                    }
                    return getStyledString("", project.isDone(), project.getTopic());
                }
            case ActionsColumn.INDEX_FROM_ICON:
                if (action.isSingleAction()) {
                    Thought thought = action.getThought();
                    return (thought == null) ? null : Icons.Thought;
                } else {
                    return (project == null) ? null : project.getIcon(false); // Resources.ICON_PROJECT;
                }
            case ActionsColumn.INDEX_FROM_TOPIC:
                if (action.isSingleAction()) {
                    Thought thought = action.getThought();
                    if (thought != null) {
                        return getStyledString(thought.getTopic().toString(), action.isDone(), thought.getTopic());
                    }
                    return getStyledString("", action.isDone(), action.getTopic());
                } else {
                    if (project != null) {
                        return getStyledString(project.getTopic().toString(), project.isDone(), project.getTopic());
                    }
                    return getStyledString("", project.isDone(), project.getTopic());
                }
            case ActionsColumn.INDEX_ICON:
                return action.getIcon(false);
            case ActionsColumn.INDEX_TOPIC:
                return getStyledString(action.getTopic().toString(), action);
            case ActionsColumn.INDEX_FOLLOWUP_DATE: {
                if (!action.isStateDelegated()) {
                    return getStyledDate(null, false, action);
                }
                Color bg = action.getTopic().getBackground();
                Color fg = action.getTopic().getForeground();
                Date date = ((ActionStateDelegated)action.getState()).getDate();
                if (date != null) {
                    Date today = Calendar.getInstance().getTime();
                    if (DateUtils.isBeforeDay(date, today)) {
                        fg = Constants.COLOR_DATE_PAST;
                    } else if (DateUtils.isSameDay(date, today)) {
                        fg = Constants.COLOR_DATE_TODAY;
                    } else if (DateUtils.isWithinDaysFuture(date, 7)) {
                        fg = Constants.COLOR_DATE_WEEK;
                    }
                }
                return getStyledDate(date, false, action.isDone(), fg, bg);
            }
            case ActionsColumn.INDEX_SCHEDULE_DATE: {
                if (!action.isStateScheduled()) {
                    return getStyledDate(null, false, action);
                }
                Color bg = action.getTopic().getBackground();
                Color fg = action.getTopic().getForeground();
                Date date = ((ActionStateScheduled)action.getState()).getDate();
                if (date != null) {
                    Date today = Calendar.getInstance().getTime();
                    if (DateUtils.isBeforeDay(date, today)) {
                        fg = Constants.COLOR_DATE_PAST;
                    } else if (DateUtils.isSameDay(date, today)) {
                        fg = Constants.COLOR_DATE_TODAY;
                    } else if (DateUtils.isWithinDaysFuture(date, 7)) {
                        fg = Constants.COLOR_DATE_WEEK;
                    }
                }
                return getStyledDate(date, true, action.isDone(), fg, bg);
            }
            case ActionsColumn.INDEX_SCHEDULE_DURATION: {
                if (!action.isStateScheduled()) {
                    return getStyledString("", false, action.getTopic());
                }
                Color bg = action.getTopic().getBackground();
                Color fg = action.getTopic().getForeground();
                ActionStateScheduled ass = (ActionStateScheduled)action.getState();
                int hrs = ass.getDurationHours();
                String hrsString = (hrs < 10) ? "0" + String.valueOf(hrs) : String.valueOf(hrs);
                int mns = ass.getDurationMinutes();
                String mnsString = (mns < 10) ? "0" + String.valueOf(mns) : String.valueOf(mns);                
                return getStyledString(hrsString + ":" + mnsString, action);
            }
            case ActionsColumn.INDEX_PROJECT_PATH: {
                if (project == null) {
                    return getStyledString("", false, Color.black, Color.white);
                }                
                return getStyledString(getPath(project, ""), false, Color.BLACK, Color.WHITE);
            }
            case ActionsColumn.INDEX_TOP_PROJECT: {
                if (action.isSingleAction()) {
                    Thought thought = action.getThought();
                    if (thought == null) {
                        return getStyledString("", false, Color.black, Color.white);
                    } 
                    return getStyledString(thought.getDescription(), false, thought.getTopic());                        
                }
                if (project == null) {
                    return getStyledString("", false, Color.black, Color.white);
                }                
                Project topProject = getTopProject(project); 
                Color bg = topProject.getTopic().getBackground();
                Color fg = topProject.getTopic().getForeground();                
                return getStyledString(topProject.getDescription(), topProject.isDone(), fg, bg);
            }
            default: return "";
        }
    }
    
    private Project getTopProject(Project project) {
        Project parent = (Project)project.getParent();
        if (parent.isRoot()) {
            return project;
        } else {
            return getTopProject(parent);            
        }
    }
    
    private String getPath(Project project, String path) {
        if (path.length() > 0) {
            path = project.getDescription() + "/" + path;
        } else {
            path = project.getDescription();
        }
        Project parent = (Project)project.getParent();
        if (!parent.isRoot()) {
            return getPath(parent, path);
        }
        return path;
    }
    
    @Override
    public Class getColumnClass(int column) {
        try {
            return screen.getColumns().get(column).getColumnClass();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return String.class;
        }
    }
    
    @Override
    public Comparator getColumnComparator(int column) {
        try {
            return screen.getColumns().get(column).getColumnComparator();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return null;
        }
    }
    
    /*
     * Get the preferred column widths. Uses 10 times more than desired to force
     * proportions and not absolute widths.
     */
    public int getWidth(int column) {
        try {
            return screen.getColumns().get(column).getWidth();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return 0;
        }
    }
    
    /*
     * Get the maximum width for the given column.
     */
    public int getMaxWidth(int column) {
        try {
            return screen.getColumns().get(column).getMaximumWidth();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return 0;
        }
    }
    
    /*
     * Gets the minimum width for the given column.
     */
    public int getMinWidth(int column) {
        try {
            return screen.getColumns().get(column).getMinimumWidth();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return 0;
        }
    }
    
    /*
     * Determines whether each column should be resizable.
     */
    public boolean isResizable(int column) {
        try {
            return screen.getColumns().get(column).isResizable();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return true;
        }
    }
    
    /*
     * Determines whether each column should be visible.
     */
    public boolean isVisible(int column) {
        try {
            return screen.getColumns().get(column).isVisible();
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return false;
        }
    }
    
    /*
     * Gets the ID of the given column.
     */
    public Byte getID(int column) {
        try {
            return new Byte(screen.getColumns().get(column).getColumnIndex());
        } catch (IndexOutOfBoundsException ex) {
//            throw new IllegalStateException();
            return 0;
        }
    }
    
    private StyledDate getStyledDate(Date date, boolean showTime, Action action) {
        StyledDate sd = new StyledDate(Constants.DATE_FORMAT_FIXED, Constants.DATE_TIME_FORMAT_FIXED);
        sd.setDate(date);
        sd.setShowTime(showTime);
        sd.setStrike(action.isDone());
        sd.setForeground(action.getTopic().getForeground());
        sd.setBackground(action.getTopic().getBackground());
        return sd;
    }
    
    private StyledDate getStyledDate(Date date, boolean t, boolean d, Color fg, Color bg) {
        StyledDate sd = new StyledDate(Constants.DATE_FORMAT_FIXED, Constants.DATE_TIME_FORMAT_FIXED);
        sd.setDate(date);
        sd.setShowTime(t);
        sd.setStrike(d);
        sd.setForeground(fg);
        sd.setBackground(bg);
        return sd;
    }
    
    private StyledString getStyledString(String s, Action action) {
        Topic t = action.getTopic();
        return getStyledString(s, action.isDone(), t.getForeground(), t.getBackground());
    }
    
    private StyledString getStyledString(String s, boolean d, Topic t) {
        return getStyledString(s, d, t.getForeground(), t.getBackground());
    }
    
    private StyledString getStyledString(String s, boolean d, Color fg, Color bg) {
        StyledString ss = new StyledString();
        ss.setString(s);
        ss.setStrike(d);
        ss.setForeground(fg);
        ss.setBackground(bg);
        return ss;
    }
    
    private StyledValue getStyledValue(Value value, Action action) {
        Topic topic = action.getTopic();
        StyledValue styledValue = new StyledValue(value);
        styledValue.setStrike(action.isDone());
        styledValue.setForeground(topic.getForeground());
        styledValue.setBackground(topic.getBackground());
        return styledValue;
    };

    @Override
    public boolean isEditable(Action baseObject, int column) {
//        return screen.getColumns().get(column).getColumnIndex() == ActionsColumn.INDEX_DONE;
        return false;
    }

    @Override
    public Action setColumnValue(Action baseObject, Object editedValue, int column) {
//        
//        assert(screen.getColumns().get(column).getColumnIndex() == ActionsColumn.INDEX_DONE);
//        assert(baseObject instanceof Action);
//        assert(editedValue instanceof Boolean);
//        
//        Action action = (Action)baseObject;
//        boolean value = (Boolean)editedValue;
//        
//        if (action.canSetDone(value)) {
//            action.setDone(value);
//            return action;
//        } else {
//            return null;
//        }
//                
        return null;
    }
}
