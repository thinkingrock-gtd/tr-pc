package au.com.trgtd.tr.view.criteria.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.criteria.screen.dialog.PriorityDialog;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add priority criteria values.
 */
public class PriorityAddAction extends CallableSystemAction {

    private final Lookup.Result<Data> result;

    public PriorityAddAction() {
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
        return NbBundle.getMessage(getClass(), "CTL_PriorityAddAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void performAction() {
        new PriorityDialog().showCreateDialog();
    }
    
}
