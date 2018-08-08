package au.com.trgtd.tr.view.delegates.screen;

import au.com.trgtd.tr.view.delegates.Resources;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import tr.model.actor.Actor;

/**
 * Actors table format.
 *
 * @author Jeremy Moore
 */
public class ActorsTableFormat implements AdvancedTableFormat<Actor> {
    
    private static final Class CLASS = ActorsTableFormat.class;
    private static final String NAME = NbBundle.getMessage(CLASS, "actor.name");
    private static final String EMAIL = NbBundle.getMessage(CLASS, "actor.email");
    private static final String INACTIVE = NbBundle.getMessage(CLASS, "actor.inactive");
    private static final String TEAM = NbBundle.getMessage(CLASS, "actor.team");
    private static final ImageIcon ICON = Resources.DELEGATE_ICON;
    
    @Override
    public int getColumnCount() {
        return 5;
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "";
            case 1: return NAME;
            case 2: return EMAIL;
            case 3: return TEAM;
            case 4: return INACTIVE;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public Object getColumnValue(Actor actor, int column) {
        switch (column) {
            case 0: return ICON;
            case 1: return actor.getName();
            case 2: return actor.getEmail();
            case 3: return actor.isTeam();
            case 4: return actor.isInactive();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0: return Icon.class;
            case 1: return String.class;
            case 2: return String.class;
            case 3: return Boolean.class;
            case 4: return Boolean.class;
        }
        throw new IllegalStateException();
    }
    
    @Override
    public Comparator getColumnComparator(int column) {
        switch (column) {
            case 0: return null;
            case 1: return new StringComparator();
            case 2: return new StringComparator();
            case 3: return null;
            case 4: return null;
        }
        throw new IllegalStateException();
    }
    
    /*
     * Get the preferred column widths. Uses 10 times more than desired to force
     * proportions and not absolute widths.
     */
    public int getColumnWidth(int column) {
        switch (column) {
            case 0: return 20;
            case 1: return 2000;
            case 2: return 4000;
            case 3: return 100;
            case 4: return 100;
        }
        throw new IllegalStateException();
    }
    
    /*
     * Determines whether each column should be resizable.
     */
    public boolean isResizable(int column) {
        switch (column) {
            case 0: return false;
            case 1: return true;
            case 2: return true;
            case 3: return true;
            case 4: return true;
        }
        throw new IllegalStateException();
    }
    
    public static class StringComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String s1 = (String)o1;
            String s2 = (String)o2;
            return s1.compareToIgnoreCase(s2);
        }
    }
    
}
