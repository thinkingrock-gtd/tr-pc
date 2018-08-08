package au.com.trgtd.tr.swing.date.chooser;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.UIManager;

public abstract class DayLabel extends JLabel {

    protected static final Dimension SIZE = new Dimension(36, 36);
    protected static final Color FG = UIManager.getDefaults().getColor("List.foreground");
    protected static final Color BG = UIManager.getDefaults().getColor("List.background");
    protected static final Color FG_SEL = UIManager.getDefaults().getColor("List.selectionForeground");
    protected static final Color BG_SEL = UIManager.getDefaults().getColor("List.selectionBackground");

    public void setNbr(int nbr) {
    }

    public int getNbr() {
        return 0;
    }

    public void select() {
    }

    public void deselect() {
    }

}
