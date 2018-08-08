package au.com.trgtd.tr.view.actors;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import tr.model.actor.Actor;

/**
 * List cell renderer for actors combo box.
 *
 * @author Jeremy Moore
 */
public class ActorsListCellRenderer extends JLabel implements ListCellRenderer {

    private final ListCellRenderer std;

    public ActorsListCellRenderer(ListCellRenderer std) {
        if (std == null) {
            throw new NullPointerException("Standard renderer is null.");
        }
        this.std = std;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        JLabel lbl = (JLabel)std.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Actor) {
            Actor actor = (Actor) value;
            if (actor.isInactive()) {
                lbl.setText("<HTML><STRIKE>" + actor.getName() + "</STRIKE></HTML>");
            } else {
                lbl.setText(actor.getName());
            }
        }

        return lbl;
    }
}




