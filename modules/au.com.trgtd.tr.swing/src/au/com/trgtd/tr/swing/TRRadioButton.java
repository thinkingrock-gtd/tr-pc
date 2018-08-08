package au.com.trgtd.tr.swing;

import java.awt.Color;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

public class TRRadioButton extends JRadioButton {

    private static final JPanel JPANEL = new JPanel(); 
    private static final Color BG = JPANEL.getBackground();
    private static final Color FG = JPANEL.getForeground();

    public TRRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        this.setColors();
    }

    public TRRadioButton(String text, Icon icon) {
        super(text, icon);
        this.setColors();
    }

    public TRRadioButton(String text, boolean selected) {
        super(text, selected);
        this.setColors();
    }

    public TRRadioButton(String text) {
        super(text);
        this.setColors();
    }

    public TRRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
        this.setColors();
    }

    public TRRadioButton(Action a) {
        super(a);
        this.setColors();
    }

    public TRRadioButton(Icon icon) {
        super(icon);
        this.setColors();
    }

    public TRRadioButton() {
        this.setColors();
    }

    private void setColors() {
        this.setBackground(BG);
        this.setForeground(FG);
    }

    @Override
    public int getHeight() {
        if ("Aqua".equals(UIManager.getLookAndFeel().getID())) {
            return super.getHeight();
        } else {
            return 23;
        }
    }
}
