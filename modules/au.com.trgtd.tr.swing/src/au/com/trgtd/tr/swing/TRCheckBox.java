package au.com.trgtd.tr.swing;

import java.awt.Color;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class TRCheckBox extends JCheckBox {

    private static final JPanel JPANEL = new JPanel();
    private static final Color BG = JPANEL.getBackground();
    private static final Color FG = JPANEL.getForeground();

    public TRCheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        this.setColors();
    }

    public TRCheckBox(String text, Icon icon) {
        super(text, icon);
        this.setColors();
    }

    public TRCheckBox(String text, boolean selected) {
        super(text, selected);
        this.setColors();
    }

    public TRCheckBox(Action a) {
        super(a);
        this.setColors();
    }

    public TRCheckBox(String text) {
        super(text);
        this.setColors();
    }

    public TRCheckBox(Icon icon, boolean selected) {
        super(icon, selected);
        this.setColors();
    }

    public TRCheckBox(Icon icon) {
        super(icon);
        this.setColors();
    }

    public TRCheckBox() {
        this.setColors();
    }


    private void setColors() {
        this.setBackground(BG);
        this.setForeground(FG);
    }

}
