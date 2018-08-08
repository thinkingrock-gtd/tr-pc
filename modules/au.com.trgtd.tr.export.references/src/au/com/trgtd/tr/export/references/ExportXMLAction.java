package au.com.trgtd.tr.export.references;

import au.com.trgtd.tr.export.ExportAction;
import au.com.trgtd.tr.resource.Icons;
import org.openide.util.NbBundle;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Export to an XML file action.
 *
 * @author Jeremy Moore
 */
public final class ExportXMLAction extends ExportAction {
    
    /** Constructs a new instance. */
    public ExportXMLAction() {
        super();
        setIcon(Icons.XML);
    }
    
    @Override
    public void performAction() {
        Data data = (Data)DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        try {
            new ExportXML().process(data);
        } 
        catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_ExportXMLAction");
    }
    
}
