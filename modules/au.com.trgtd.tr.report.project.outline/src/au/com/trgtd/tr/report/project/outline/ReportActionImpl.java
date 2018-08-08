package au.com.trgtd.tr.report.project.outline;

import au.com.trgtd.tr.resource.Icons;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import tr.extract.reports.ReportAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Report action implementation.
 *
 * @author Jeremy Moore
 */
public final class ReportActionImpl extends ReportAction {

    /**
     * Constructs a new instance.
     */
    public ReportActionImpl() {
        super();
        setIcon(Icons.PDF);
    }

    /**
     * Save the current data-store as another file.
     */
    @Override
    public void performAction() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) {
            return;
        }
        try {
            new ReportImpl().process(data);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Get the report action name.
     * @return The report action name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(this.getClass(), "CTL_ReportAction");
    }

    /**
     * Get the help context.
     * @return The help context.
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("tr.reports.project.outline");
    }
}
