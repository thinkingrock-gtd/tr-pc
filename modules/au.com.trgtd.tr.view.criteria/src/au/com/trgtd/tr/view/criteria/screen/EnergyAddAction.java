package au.com.trgtd.tr.view.criteria.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.criteria.screen.dialog.EnergyDialog;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add energy criteria values.
 *
 * @author Jeremy Moore
 */
public class EnergyAddAction extends CallableSystemAction {

    private final Lookup.Result<Data> result;

    public EnergyAddAction() {
        super();
        setIcon(Icons.Add);
        result = DataLookup.instance().lookup(new Lookup.Template<Data>(Data.class));
        result.addLookupListener(new LookupListener() {
            @Override
            public void resultChanged(LookupEvent lookupEvent) {
                setEnabled(result.allInstances().size() > 0);
            }
        });
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(getClass(), "CTL_EnergyAddAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void performAction() {
        new EnergyDialog().showCreateDialog();
    }
    
}
