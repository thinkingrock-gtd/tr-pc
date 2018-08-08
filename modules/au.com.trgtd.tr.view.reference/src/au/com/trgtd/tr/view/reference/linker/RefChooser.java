package au.com.trgtd.tr.view.reference.linker;

import au.com.trgtd.tr.swing.mig.MigUtils;
import java.awt.event.ActionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import tr.model.information.Information;

class RefChooser {

    private final static Object OK = DialogDescriptor.OK_OPTION;
    private final static Object CANCEL = DialogDescriptor.CANCEL_OPTION;

    private static RefChooser instance;

    public final static RefChooser getDefault() {
        if (instance == null) {
            instance = new RefChooser();
        }
        return instance;
    }

    private RefChooser() {
        title = NbBundle.getMessage(CLASS, "ref.link.dialog");
        panel = new RefChooserPanel();
//      descriptor = new DialogDescriptor(panel, title);
//      panel.setDescriptor(descriptor);
        boolean modal = true;
        Object[] opts = MigUtils.isOkCancelOrder() ? new Object[]{OK, CANCEL} : new Object[]{CANCEL, OK};
        Object defOpt = OK;
        int align = DialogDescriptor.DEFAULT_ALIGN;
        HelpCtx help = null;
        ActionListener al = null;
        boolean leaf = false;
        descriptor = new DialogDescriptor(panel, title, modal, opts, defOpt, align, help, al, leaf);
        panel.setDescriptor(descriptor);
    }

    public Information getUserChoice() {
        panel.reset();
        Object option = DialogDisplayer.getDefault().notify(descriptor);
        if (option == DialogDescriptor.OK_OPTION) {
            return panel.getSelected();
        } else {
            return null;
        }
    }

    private final static Class CLASS = RefChooser.class;
    private final DialogDescriptor descriptor;
    private final RefChooserPanel panel;
    private final String title;

}
