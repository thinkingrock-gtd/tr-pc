package au.com.trgtd.tr.view.criteria.screen.dialog;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;

/**
 * Priority value add/edit dialog panel.
 */
public class PriorityDialogPanel extends JPanel {

    /** Constructs a new instance. */
    public PriorityDialogPanel(JButton okButton) {
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
        icalLabel = new JLabel(NbBundle.getMessage(CLASS, "ical.label"));
        icalField = getSpinner();

        setLayout(new MigLayout("", "4[]2[grow]4", "4[]4[]24"));
        add(titleLabel, "align left");
        add(titleField, "align left, growx, wrap");
        add(icalLabel, "align left");
        add(icalField, "align left, wrap");
    }

    public void setTitle(String title) {
        titleField.setText(title);
        validation();
    }

    public String getTitle() {
        return titleField.getText();
    }

    public void setICalValue(Integer value) {
        icalField.setValue(null == value ? new Integer(0) : value);
    }

    public Integer getICalValue() {
        Object object = icalField.getValue();
        if (object instanceof Integer) {
            return (Integer)object;
        } else {
            return null;
        }
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

    private JSpinner getSpinner() {
        int min = 0;
        int max = 99;
        int inc = 1;
        int val = 0;
        SpinnerModel model = new SpinnerNumberModel(val, min, max, inc);
        return new JSpinner(model);
    }

    private final static Class CLASS = PriorityDialogPanel.class;
    private final JButton okButton;
    private JTextField titleField;
    private JLabel titleLabel;
    private JLabel icalLabel;
    private JSpinner icalField;

}
