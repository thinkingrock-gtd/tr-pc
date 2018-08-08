package au.com.trgtd.tr.view.reference.linker;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.NbBundle;
import tr.model.information.Information;

class RefViewer {

    private static RefViewer instance;

    public final static RefViewer getDefault() {
        if (instance == null) {
            instance = new RefViewer();
        }
        return instance;
    }

    private RefViewer() {
        panel = new RefViewerPanel();
        title = NbBundle.getMessage(CLASS, "ref.title");
        Object ok = DialogDescriptor.OK_OPTION;
        Object[] opts = new Object[] {ok};
        int align = DialogDescriptor.DEFAULT_ALIGN;
        
//      this.descriptor = new DialogDescriptor(panel, title);
        this.descriptor = new DialogDescriptor(panel, title, true, opts, ok, align, null, null);
    }

    public void view(Information ref) {
        panel.reset(ref);
        DialogDisplayer.getDefault().notify(descriptor);
    }

    private final static Class CLASS = RefViewer.class;
    private final DialogDescriptor descriptor;
    private final RefViewerPanel panel;
    private final String title;

}
