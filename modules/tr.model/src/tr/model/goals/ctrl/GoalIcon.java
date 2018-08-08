package tr.model.goals.ctrl;

import au.com.trgtd.tr.util.Utils;
import javax.swing.ImageIcon;

public class GoalIcon {

    public final String path;
    public final ImageIcon icon;

    public GoalIcon(String path, ImageIcon icon) {
        this.path = path;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return path;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof GoalIcon) {
            return Utils.equal(((GoalIcon)object).path, path);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

}
