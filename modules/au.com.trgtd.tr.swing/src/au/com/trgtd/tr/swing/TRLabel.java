package au.com.trgtd.tr.swing;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class TRLabel extends JLabel {

    public TRLabel() {
    }

    public TRLabel(Icon arg0) {
        super(arg0);
    }

    public TRLabel(Icon arg0, int arg1) {
        super(arg0, arg1);
    }

    public TRLabel(String arg0) {
        super(arg0);
    }

    public TRLabel(String arg0, int arg1) {
        super(arg0, arg1);
    }

    public TRLabel(String arg0, Icon arg1, int arg2) {
        super(arg0, arg1, arg2);
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
