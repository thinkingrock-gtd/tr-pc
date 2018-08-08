package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.action.Recurrence;
import au.com.trgtd.tr.swing.date.field.DateField;
import au.com.trgtd.tr.view.actn.recurrence.PeriodTypeComboBox;

public final class RecurrenceSubsequentPanel extends JPanel {

    public RecurrenceSubsequentPanel() {
        initView();
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
        initPanel();
    }

    public void initPanel() {
        freqSpinner.setValue(recurrence.getFrequency());
        periodCombo.setSelectedItem(recurrence.getPeriod().getType());
        endNbrRadio.setSelected(recurrence.getEndNbr() != null);
        endNbrSpinner.setValue(recurrence.getEndNbr() == null ? 2 : recurrence.getEndNbr());
        endDateRadio.setSelected(recurrence.getEndDate() != null);
        endDateField.setDate(recurrence.getEndDate());
        endNeverRadio.setSelected(!endNbrRadio.isSelected() && !endDateRadio.isSelected());
    }

    public boolean isValidForm() {
        return true;
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(RecurrenceSubsequentPanel.class, "change.subsequent.recurrence");
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        freqSpinner.setEnabled(enabled);
        periodCombo.setEnabled(enabled);
        endNeverRadio.setEnabled(enabled);
        endNbrRadio.setEnabled(enabled);
        endNbrSpinner.setEnabled(enabled && endNbrRadio.isSelected());
        endDateRadio.setEnabled(enabled);
        endDateField.setEnabled(enabled && endDateRadio.isSelected());
    }

     private void initView() {
        createLabel = new JLabel(getMsg("create.subsequent.action.in"));
        periodCombo = new PeriodTypeComboBox();
        freqSpinner = new JSpinner();
        freqSpinner.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        freqSpinner.setPreferredSize(new Dimension(50, 23));
        endLabel = new JLabel(getMsg("terminate"));
        endNeverRadio = new JRadioButton(getMsg("never"));
        endNbrRadio = new JRadioButton(getMsg("after"));
        endNbrSpinner = new JSpinner();
        endNbrSpinner.setModel(new SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(2), null, Integer.valueOf(1)));
        endDateRadio = new JRadioButton(getMsg("after"));
        endDateField = new DateField();
        buttonGroup = new ButtonGroup();
        buttonGroup.add(endNeverRadio);
        buttonGroup.add(endNbrRadio);
        buttonGroup.add(endDateRadio);

        JPanel panel = new JPanel(new MigLayout("", "12[]2[]0", "0[]2[]0"));
        panel.add(createLabel,   "align left, span, split 3");
        panel.add(freqSpinner,   "align left");
        panel.add(periodCombo,   "align left, wrap");
        panel.add(endLabel,      "align left, span, split 6");
        panel.add(endNeverRadio, "align left");
        panel.add(endNbrRadio,   "align left");
        panel.add(endNbrSpinner, "align left, w 54");
        panel.add(endDateRadio,  "align left");
        panel.add(endDateField,  "align left, wrap");
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private Recurrence recurrence;
    private ButtonGroup buttonGroup;
    private DateField endDateField;
    private JRadioButton endDateRadio;
    private JLabel endLabel;
    private JRadioButton endNbrRadio;
    private JSpinner endNbrSpinner;
    private JRadioButton endNeverRadio;
    private JLabel createLabel;
    private JSpinner freqSpinner;
    private PeriodTypeComboBox periodCombo;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

