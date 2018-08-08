package au.com.trgtd.tr.swing;

import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Scrollpane for TRTextArea.
 * 
 * @author jmoore
 */
public class TRTextAreaScrollPane extends JScrollPane {

    public final static Border BORDER = new EmptyBorder(2, 4, 2, 4);
    
    public final TRTextArea textarea;

    public TRTextAreaScrollPane() {
        this(new TRTextArea());
    }

    public TRTextAreaScrollPane(TRTextArea textarea) {
        super(textarea);
        super.setOpaque(false);
        super.setBorder(BORDER);
        this.textarea = textarea;
    }

}
