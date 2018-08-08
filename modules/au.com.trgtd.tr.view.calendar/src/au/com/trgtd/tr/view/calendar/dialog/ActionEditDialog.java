package au.com.trgtd.tr.view.calendar.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import org.jdesktop.swingx.util.WindowUtils;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import tr.model.action.Action;

/**
 * Dialog for viewing and modifying an action.
 *
 * @author Jeremy Moore
 */
public class ActionEditDialog extends JDialog {

    public static enum Result {OK, CANCEL};
    public static enum Mode {CREATE, MODIFY};
    
    private final Mode mode;
    private final ActionCtlr actionCtlr;
    private final ActionEditPanel panel;
    private JButton okButton;
    private JButton cancelButton;
    private Result result;

    /**
     * Constructs a new instance.
     */
    public ActionEditDialog(Action action) {
        super(WindowManager.getDefault().getMainWindow(), true);
        mode = Mode.MODIFY;
        actionCtlr = new ActionCtlr(action);
        panel = new ActionEditPanel(actionCtlr);
        construct();
    }

    private void construct() {
        this.setTitle(NbBundle.getMessage(getClass(), "LBL_EditAction"));
        
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        };
        cancelButton = new JButton(NbBundle.getMessage(getClass(), "cancel"));
        cancelButton.addActionListener(cancelListener);
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        };
        okButton = new JButton(NbBundle.getMessage(getClass(), "ok"));
        okButton.addActionListener(okListener);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        buttons.setPreferredSize(new Dimension(0, 36));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 36));
        buttons.add(new JLabel("   "));
        buttons.add(cancelButton);
        buttons.add(okButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800,600));
        pack();

        setLocation(WindowUtils.getPointForCentering(this));
    }

    /* Handle OK. */
    private void ok() {
        actionCtlr.commit();

        if (mode == Mode.MODIFY) {
            result = Result.OK;
            setVisible(false);
            dispose();
            return;
        }
    }

    /* Handle Cancel. */
    private void cancel() {
        result = Result.CANCEL;
        setVisible(false);
        dispose();
    }

    /* Shows the dialog. */
    private void showDialog() {
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
    }

    /** Shows the dialog in create mode. */
    public void showCreateDialog() {
    }

    /** Shows the dialog in edit mode. */
    public Result showModifyDialog() {
        if (actionCtlr == null) {
            return Result.CANCEL;
        }
        showDialog();
        return result;
    }

}
