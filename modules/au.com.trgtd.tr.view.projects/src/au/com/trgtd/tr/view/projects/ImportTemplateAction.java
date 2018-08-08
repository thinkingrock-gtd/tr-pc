/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.com.trgtd.tr.view.projects;

import java.awt.EventQueue;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import tr.model.Data;
import tr.model.DataLookup;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;

public final class ImportTemplateAction extends CallableSystemAction implements Observer {

    public ImportTemplateAction() {
        super();
        setEnabled(false);
        dataChanged();
        Lookup.Result r = DataLookup.instance().lookup(new Lookup.Template(Data.class));
        r.addLookupListener(new LookupListener() {
            public void resultChanged(LookupEvent lookupEvent) {
                dataChanged();
            }
        });        
    }
    
    public void performAction() {
        ImportTemplate importer = new ImportTemplate();
        importer.process();
    }

    public String getName() {
        return NbBundle.getMessage(ImportTemplateAction.class, "CTL_ImportTemplateAction");
    }

    @Override
    protected void initialize() {
        super.initialize();
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    private void dataChanged() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Data data = (Data)DataLookup.instance().lookup(Data.class);
                if (data == null) {
                    setEnabled(false);
                } else {
                    setEnabled(true);
                    data.addObserver(ImportTemplateAction.this);
                }
            }
        });
    }
    
    public void update(Observable observable, Object arguement) {
        dataChanged();        
    }
    
}
