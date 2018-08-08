package au.com.trgtd.tr.view.criteria.screen.dialog;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Time value add/edit dialog panel.
 *
 * @author Jeremy Moore
 */
public class TimeDialogPanel extends JPanel {

    /** Constructs a new instance. */
    public TimeDialogPanel(JButton okButton) {
        this.okButton = okButton;
        initView();
    }

    private void initView() {
        titleLabel = new JLabel(NbBundle.getMessage(CLASS, "title.label"));
        titleField = new JTextField();
        titleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        validation();
                    }
                });
            }
        });

        setLayout(new MigLayout("", "4[]2[grow]4", "4[]24"));
        add(titleLabel, "align left");
        add(titleField, "align left, growx, wrap");
    }

    public void setTitle(String title) {
        titleField.setText(title);
        validation();
    }

    public String getTitle() {
        return titleField.getText();
    }

    public void focus() {
        requestFocusInWindow();
        titleField.requestFocusInWindow();
    }

    private void validation() {
        okButton.setEnabled(isValidInput());
    }

    public boolean isValidInput() {
        return titleField.getText().trim().length() > 0;
    }

    private final static Class CLASS = TimeDialogPanel.class;
    private final JButton okButton;
    private JTextField titleField;
    private JLabel titleLabel;

}
