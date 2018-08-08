package au.com.trgtd.tr.view.criteria.screen;

import au.com.trgtd.tr.resource.Icons;
import au.com.trgtd.tr.view.criteria.screen.dialog.TimeDialog;
import org.openide.util.*;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;

/**
 * Action to add time criteria values.
 *
 * @author Jeremy Moore
 */
public class TimeAddAction extends CallableSystemAction {

    private final Lookup.Result<Data> result;

    public TimeAddAction() {
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
        return NbBundle.getMessage(getClass(), "CTL_TimeAddAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void performAction() {
        new TimeDialog().showCreateDialog();
    }
    
}
