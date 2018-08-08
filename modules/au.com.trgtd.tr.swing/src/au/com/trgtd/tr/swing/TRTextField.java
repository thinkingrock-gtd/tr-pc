package au.com.trgtd.tr.swing;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.text.Document;

public class TRTextField extends JTextField {

    public TRTextField(Document arg0, String arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    public TRTextField(String arg0, int arg1) {
        super(arg0, arg1);
    }

    public TRTextField(int arg0) {
        super(arg0);
    }

    public TRTextField(String arg0) {
        super(arg0);
    }

    public TRTextField() {
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
