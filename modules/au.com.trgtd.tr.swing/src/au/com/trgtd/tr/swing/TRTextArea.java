
package au.com.trgtd.tr.swing;

import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * JTextArea wrapper scrollpane.
 * 
 * @author jmoore
 */
public class TRTextArea extends JTextArea {

    private final static JTextField TEXTFIELD = new JTextField();
//  static { TEXTFIELD.setMargin(new Insets(0,0,0,0)); }

    public TRTextArea() {
        super();
        initComponent();
    }

    public TRTextArea(Document document) {
        super(document);
        initComponent();
    }

    public TRTextArea(Document document, String text, int rows, int cols) {
        super(document, text, rows, cols);
        initComponent();
    }
    
    public TRTextArea(int rows, int cols) {
        super(rows, cols);
        initComponent();
    }

    public TRTextArea(String text) {
        super(text);
        initComponent();
    }

    public TRTextArea(String text, int rows, int cols) {
        super(text, rows, cols);
        initComponent();
    }

    public void initComponent() {
        setOpaque(true);

        setBorder(TEXTFIELD.getBorder());
//      setMargin(TEXTFIELD.getMargin());
        setMargin(new Insets(0,0,0,0));
        setFont(TEXTFIELD.getFont());
        setLineWrap(true);
        setWrapStyleWord(true);
        // Change Tab and Ctrl-Tab to traverse fields.
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

}
