package au.com.trgtd.tr.swing;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.UIManager;

public class TRComboBox extends JComboBox {

    public TRComboBox() {
        this.setOpaque(false);        
//      this.putClientProperty("JComponent.sizeVariant", "small");
    }

    public TRComboBox(Object[] model) {
        super(model);
        this.setOpaque(false);        
    }

    public TRComboBox(ComboBoxModel model) {
        super(model);
        this.setOpaque(false);        
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
