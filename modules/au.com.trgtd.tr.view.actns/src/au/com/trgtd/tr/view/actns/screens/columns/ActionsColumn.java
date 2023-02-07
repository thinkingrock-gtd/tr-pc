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
package au.com.trgtd.tr.view.actns.screens.columns;

import au.com.trgtd.tr.swing.StyledDate;
import au.com.trgtd.tr.swing.StyledString;
import au.com.trgtd.tr.swing.StyledStringRenderer;
import au.com.trgtd.tr.util.ObservableImpl;
import au.com.trgtd.tr.view.actns.screens.*;
import java.util.Comparator;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.table.TableCellRenderer;
import org.openide.util.NbBundle;

/**
 * Actions screens column definition.
 *
 * @author Jeremy Moore
 */
public final class ActionsColumn extends ObservableImpl {
    
    private static final Logger LOG = Logger.getLogger("tr.view.actions");
    
//    private static final byte VERSION = 1;
    
    public static final byte INDEX_ICON = 0;
    public static final byte INDEX_DONE = 1;
    public static final byte INDEX_DESCR = 2;
    public static final byte INDEX_DELEGATED_TO = 3;
    public static final byte INDEX_CREATED_DATE = 4;
    public static final byte INDEX_DONE_DATE = 5;
    public static final byte INDEX_ACTION_DATE = 6;
    public static final byte INDEX_CONTEXT = 7;
    public static final byte INDEX_TIME = 8;
    public static final byte INDEX_ENERGY = 9;
    public static final byte INDEX_PRIORITY = 10;
    public static final byte INDEX_TOPIC = 11;
    public static final byte INDEX_FROM_ICON = 12;
    public static final byte INDEX_FROM_DESCR = 13;
    public static final byte INDEX_FROM_TOPIC = 14;
    public static final byte INDEX_START_DATE = 15;
    public static final byte INDEX_DUE_DATE = 16;
    public static final byte INDEX_FOLLOWUP_DATE = 17;
    public static final byte INDEX_SCHEDULE_DATE = 18;
    public static final byte INDEX_SCHEDULE_DURATION = 19;
    public static final byte INDEX_PROJECT_PATH = 20;
    public static final byte INDEX_TOP_PROJECT = 21;
    
    public static final byte[] COLUMN_INDICES = {
        INDEX_ICON,
        INDEX_DONE,
        INDEX_DESCR,
        INDEX_DELEGATED_TO,
        INDEX_CREATED_DATE,
        INDEX_DONE_DATE,
        INDEX_ACTION_DATE,
        INDEX_CONTEXT,
        INDEX_TIME,
        INDEX_ENERGY,
        INDEX_PRIORITY,
        INDEX_TOPIC,
        INDEX_FROM_ICON,
        INDEX_FROM_DESCR,
        INDEX_FROM_TOPIC,
        INDEX_START_DATE,
        INDEX_DUE_DATE,
        INDEX_FOLLOWUP_DATE,
        INDEX_SCHEDULE_DATE,
        INDEX_SCHEDULE_DURATION,
        INDEX_PROJECT_PATH,
        INDEX_TOP_PROJECT
    };
    
    private static final String[] COLUMN_IDS = {
        "icon",
        "done",
        "descr",
        "delegated-to",
        "created-date",
        "done-date",
        "action-date",
        "context",
        "time",
        "energy",
        "priority",
        "topic",
        "from-icon",
        "from-descr",
        "from-topic",
        "start-date",
        "due-date",
        "followup-date",
        "schedule-date",
        "schedule-duration",
        "project-path",
        "top-project"
    };
    
    private static final Class[] CLASSES = {
        Icon.class,
        Boolean.class,
        StyledString.class,
        StyledString.class,
        StyledDate.class,
        StyledDate.class,
        StyledDate.class,
        StyledString.class,
        StyledString.class,
        StyledString.class,
        StyledString.class,
        StyledString.class,
        Icon.class,
        StyledString.class,
        StyledString.class,
        StyledDate.class,
        StyledDate.class,
        StyledDate.class,
        StyledDate.class,
        StyledString.class,
        StyledString.class,
        StyledString.class,
    };
    
    private static final Comparator[] COMPARATORS = {
        new ComparatorIcon(),
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
        new ComparatorActionDate(),
        new ComparatorDefault<>(),
        new ComparatorValues(ValueIDsProviderTime.instance),
        new ComparatorValues(ValueIDsProviderEnergy.instance),
        new ComparatorValues(ValueIDsProviderPriority.instance),
        new ComparatorDefault<>(),
        new ComparatorIcon(),
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
        new ComparatorDueDate(),
        new ComparatorFollowupDate(),
        new ComparatorScheduleDate(),
        null,
        new ComparatorDefault<>(),
        new ComparatorDefault<>(),
    };
    
    private static final boolean[] RESIZABLE = {
        false,
        false,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        false,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        true,
    };
    
    private static final boolean[] DEFAULT_VISIBLE = {
        false,
        false,
        true,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
    };
    
    private static final int MAX_WIDTH = Integer.MAX_VALUE;
    private static final int MIN_WIDTH = 1;
    
//  private static final int[] PRE_WIDTHS = { 17, 17, -1, -1,  -1,  -1,  -1, -1, -1, -1, -1, -1, 17, -1, -1,  -1,  -1,  -1,  -1};
    private static final int[] PRE_WIDTHS = { 17, 17, 150, 150,  150,  150,  150, 150, 150, 150, 150, 150, 17, 150, 150,  150, 150, 150, 150, 150, 150, 150};
    private static final int[] MIN_WIDTHS = { 17, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    private static final int[] MAX_WIDTHS = { 17, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    
    public static ActionsColumn createColumn(byte index) {
        return new ActionsColumn(index);
    }
    
    private byte index;
    private boolean visible;
    private int width;
    
    /**
     * Constructs a new instance for the given values.
     */
    private ActionsColumn(byte index) {
        this.index = index;
        this.visible = DEFAULT_VISIBLE[index];
        this.width = getPreferredWidth();
    }
    
    /** Gets the column index. */
    public final byte getColumnIndex() {
        return index;
    }
    
    /** Gets the column identifier. */
    public final String getColumnID() {
        return COLUMN_IDS[index];
    }
    
    /** Gets the column identifier. */
    public static String getColumnID(byte index) {
        return COLUMN_IDS[index];
    }
    
    /** Gets the column display name from properties file using the key. */
    @Override
    public String toString() {
        return getName();
    }
    
    /** Gets the table column name. */
    public String getColumnName() {
        if (CLASSES[index] == Icon.class || CLASSES[index] == Boolean.class) {
            return "";
        }
        return getName();
    }

    /** Gets the column name. */
    public String getName() {
        try {
            return NbBundle.getMessage(getClass(), COLUMN_IDS[index]);
        } catch (Exception ex) {
            return COLUMN_IDS[index];
        }
    }
    
    /** Gets the visible state. */
    public boolean isVisible() {
        return visible;
    }
    
    /** Sets the visible state. */
    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;
            notifyObservers(this);
        }
    }
    
    /** Gets the table column class. */
    public Class getColumnClass() {
        return CLASSES[index];
    }
    
    /** Gets the table column comparator. */
    public Comparator getColumnComparator() {
        return COMPARATORS[index];
    }
    
    /** Determines whether the column width is re-sizable. */
    public boolean isResizable() {
        return RESIZABLE[index];
    }
    
    /** Gets the table column max width. */
    public int getMaximumWidth() {
        return (MAX_WIDTHS[index] > 0) ? MAX_WIDTHS[index] : MAX_WIDTH;
    }
    
    /** Gets the table column min width. */
    public int getMinimumWidth() {
        return (MIN_WIDTHS[index] > 0) ? MIN_WIDTHS[index] : MIN_WIDTH;
    }
    
    /** Gets the table column preferred width. */
    public int getPreferredWidth() {
        return (PRE_WIDTHS[index] > 0) ? PRE_WIDTHS[index] : getMinimumWidth();
    }
    
    /** Gets the table column width. */
    public int getWidth() {
        if (isResizable()) {
            return width;
        } else {
            return getPreferredWidth();
        }
    }
    
    /** Sets the table column width. */
    public void setWidth(int width) {
        if (isResizable()) {
            if (this.width != width) {
                this.width = width;
                notifyObservers(this);
            }
        }
    }
    
    /** Gets the table cell renderer. */
    public TableCellRenderer getRenderer() {
        if (CLASSES[index]==StyledString.class || CLASSES[index]==StyledDate.class) {
            return new StyledStringRenderer();
        }
        return null;
    };
    
//    public void persist(PersistenceOutputStream out) throws Exception {
//        out.writeByte(VERSION);
//        out.writeByte(index);
//        out.writeBoolean(visible);
//        out.writeInt(width);
//    }
//
//    public static ActionsColumn restore(PersistenceInputStream in) throws Exception {
//        byte version = in.readByte();
//        if (version != VERSION) {
//            throw new Exception("Unknown persistance version for ActionsField.class");
//        }
//        byte index = in.readByte();
//        boolean visible = in.readBoolean();
//        int width = in.readInt();
//
//        ActionsColumn column = ActionsColumn.createColumn(index);
//        column.setVisible(visible);
//        column.setWidth(width);
//        return column;
//    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ActionsColumn) ) {
            return false;
        }
        ActionsColumn column = (ActionsColumn)object;
        if (this.index != column.index) {
            return false;
        }
        if (this.visible != column.visible) {
            return false;
        }
        if (this.width != column.width) {
            return false;
        }
        return true;
    }
    
}
