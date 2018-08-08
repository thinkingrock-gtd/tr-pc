package au.com.trgtd.tr.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;

public class TRButton extends JButton {

    public TRButton(String arg0, Icon arg1) {
        super(arg0, arg1);
    }

    public TRButton(Action arg0) {
        super(arg0);
    }

    public TRButton(String arg0) {
        super(arg0);
    }

    public TRButton(Icon arg0) {
        super(arg0);
    }

    public TRButton() {
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
