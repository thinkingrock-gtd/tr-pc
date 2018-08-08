package au.com.trgtd.tr.swing.date.chooser;

import java.awt.Font;
import javax.swing.JLabel;

public final class DayLabelNbr extends DayLabel {

    public int nbr;

    public DayLabelNbr(int nbr) {
        super.setOpaque(true);
        super.setHorizontalAlignment(JLabel.CENTER);
        super.setPreferredSize(SIZE);
        super.setMaximumSize(SIZE);
        super.setMinimumSize(SIZE);
        super.setFont(super.getFont().deriveFont(Font.BOLD));
        this.setNbr(nbr);
        this.deselect();
    }

    @Override
    public void setNbr(int nbr) {
        this.nbr = nbr;
        super.setText(nbr > 0 ? Integer.toString(nbr) : "");
    }

    @Override
    public int getNbr() {
        return nbr;
    }

    @Override
    public final void select() {
        super.setForeground(FG_SEL);
        super.setBackground(BG_SEL);
        super.invalidate();
        super.repaint();
    }

    @Override
    public final void deselect() {
        super.setForeground(FG);
        super.setBackground(BG);
        super.invalidate();
        super.repaint();
    }
    
}
