package au.com.trgtd.tr.swing.date.chooser;

import java.awt.Font;
import javax.swing.JLabel;

public final class DayLabelHdr extends DayLabel {

    public DayLabelHdr(String txt) {
        super.setText(txt);
        super.setFont(super.getFont().deriveFont(Font.BOLD));
        super.setHorizontalAlignment(JLabel.CENTER);
        super.setOpaque(true);
        super.setForeground(FG);
        super.setPreferredSize(SIZE);
        super.setMaximumSize(SIZE);
        super.setMinimumSize(SIZE);
    }

}
