package au.com.trgtd.tr.export.references;

import au.com.trgtd.tr.export.ExportAction;
import au.com.trgtd.tr.resource.Icons;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export to a text file action.
 *
 * @author Jeremy Moore
 */
public final class ExportTextAction extends ExportAction {

    /**
     * Constructs a new instance.
     */
    public ExportTextAction() {
        super();
        setIcon(Icons.Text);
    }

    /**
     * Save the current data store as another file.
     */
    @Override
    public void performAction() {
        Data data = (Data) DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        try {
            new ExportText().process(data);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ExportTextAction");
    }

}
