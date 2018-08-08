package au.com.trgtd.tr.view.delegates;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.appl.InitialAction;
import au.com.trgtd.tr.view.actors.ActorDialog;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add actors using the actors dialog.
 *
 * @author Jeremy Moore
 */
public class AddActorsAction extends CallableSystemAction implements InitialAction {
    
    private final Lookup.Result result;
    
    public AddActorsAction() {
        super();
        setIcon(Icons.Add);
        result = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        result.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                setEnabled(result.allInstances().size() > 0);
            }
        });
    }
    
    /** 
     * Gets the action identifier.
     * @return The ID.
     */
    @Override
    public String getID() {
        return "AddActorsAction";
    }
    
    @Override
    protected boolean asynchronous() {
        return false;
    }
    
    /** 
     * Gets the initial action name.
     * @return The name.
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_AddActorsAction");
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    public void performAction() {
        Data data = DataLookup.instance().lookup(Data.class);
        if (data == null) return;
        
        new ActorDialog(data).showCreateDialog();
    }
    
}
