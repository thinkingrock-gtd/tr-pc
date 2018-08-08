package au.com.trgtd.tr.imports.thoughts.prefs;

import au.com.trgtd.tr.swing.TRComboBox;
import java.awt.BorderLayout;
import java.nio.charset.Charset;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

final class ImportThoughtsPrefsPanel extends JPanel {
    
    private final ImportThoughtsPrefsPanelController controller;
    
    ImportThoughtsPrefsPanel(ImportThoughtsPrefsPanelController controller) {
        this.controller = controller;
        initComponents();
        add(getView(), BorderLayout.CENTER);
    }

    private JComponent getView() {
        encodingLabel = new JLabel(getMsg("Encoding"));
        encodingCombo = new TRComboBox(getEncodingModel());

        JPanel panel = new JPanel(new MigLayout("", "0[]2[]0", "0[]0"));
        panel.add(encodingLabel, "align right");
        panel.add(encodingCombo, "align left, wrap");

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return scrollPane;
    }

    private String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private ComboBoxModel getEncodingModel() {
        Vector<String> encodings = new Vector<String>();
        encodings.add("");
        encodings.addAll(Charset.availableCharsets().keySet());
        return new DefaultComboBoxModel(encodings);
    }

    void load() {
        encodingCombo.setSelectedItem(ImportThoughtsPrefs.getEncoding());
    }

    void store() {
        if (isValidEncoding()) {
            ImportThoughtsPrefs.setEncoding(getEncoding());
        }
    }

    boolean valid() {
        return isValidEncoding();
    }

    private boolean isValidEncoding() {
        String encoding = getEncoding();
        return encoding.length() == 0 || Charset.isSupported(encoding);
    }

    private String getEncoding() {
        String encoding = (String)encodingCombo.getSelectedItem();
        return encoding == null ? "" : encoding;
    }

    private JComboBox encodingCombo;
    private JLabel encodingLabel;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
