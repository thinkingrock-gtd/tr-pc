package au.com.trgtd.tr.view.actn.recurrence.modify;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import org.openide.util.NbBundle;
import tr.model.action.Recurrence.Basis;

public final class ModifyRecurrenceVisualPanelModRecur1 extends JPanel {

    public ModifyRecurrenceVisualPanelModRecur1() {
        initView();
    }

    @Override
    public String getName() {
        return getMsg("choose.recurrence.type");
    }

    public Basis getBasis() {
        return basis;
    }
    
    public void setModel(Basis basis) {
        this.basis = basis;
        initPanel();
    }

    public void initPanel() {
        regRadio.setSelected(basis == Basis.START_DATE);
        subRadio.setSelected(basis == Basis.DONE_DATE);
    }

    private void initView() {
        regRadio = new JRadioButton(getMsg("regular"));
        regRadio.setFont(regRadio.getFont().deriveFont(Font.BOLD));
        regRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                basis = Basis.START_DATE;
            }
        });
        regHelp = new JTextArea(getMsg("regular.hint"));
        regHelp.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        regHelp.setEditable(false);
        regHelp.setFont(regHelp.getFont().deriveFont(Font.ITALIC));
        regHelp.setWrapStyleWord(true);
        regHelp.setLineWrap(true);

        subRadio = new JRadioButton(getMsg("subsequent"));
        subRadio.setFont(subRadio.getFont().deriveFont(Font.BOLD));
        subRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                basis = Basis.DONE_DATE;
            }
        });
        subHelp = new JTextArea(getMsg("subsequent.hint"));
        subHelp.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        subHelp.setEditable(false);
        subHelp.setFont(subHelp.getFont().deriveFont(Font.ITALIC));
        subHelp.setWrapStyleWord(true);
        subHelp.setLineWrap(true);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(regRadio);
        buttonGroup.add(subRadio);

        JPanel panel = new JPanel(new MigLayout("", "0[grow, fill]0", "2[]4[]4[]4[]2"));
        panel.add(regRadio, "growx, shrinkx, wrap");
//        panel.add(regHelp,  "gapleft 25, growx, shrinkx, wrap");
        panel.add(regHelp,  "gapleft 25, shrinkx, wrap");
        panel.add(subRadio, "growx, shrinkx, wrap");
//        panel.add(subHelp,  "gapleft 25, growx, shrinkx, wrap");
        panel.add(subHelp,  "gapleft 25, shrinkx, wrap");

//        JScrollPane scrollPane = new JScrollPane(panel);
//        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.setPreferredSize(new Dimension(800, 600));

        setLayout(new BorderLayout());
//        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);
    }

    public String getMsg(String key) {
        return NbBundle.getMessage(getClass(), key);
    }

    private Basis basis;
    private ButtonGroup buttonGroup;
    private JTextArea regHelp;
    private JRadioButton regRadio;
    private JTextArea subHelp;
    private JRadioButton subRadio;

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

