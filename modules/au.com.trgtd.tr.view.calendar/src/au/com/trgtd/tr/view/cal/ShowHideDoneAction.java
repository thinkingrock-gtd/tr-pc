package au.com.trgtd.tr.view.cal;

import au.com.trgtd.tr.cal.ctlr.DateCtlr;
import au.com.trgtd.tr.cal.model.CalModel;
import au.com.trgtd.tr.resource.Icons;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToggleButton;
import org.openide.util.NbBundle;

/**
 * Action for show/hide done actions.
 *
 * Jeremy Moore
 */
public class ShowHideDoneAction extends AbstractAction {

    private final CalModel model;
    private final DateCtlr dateCtlr;

    public ShowHideDoneAction(CalModel model, DateCtlr dateCtlr) {
        this.model = model;
        this.dateCtlr = dateCtlr;
        initValues();
    }

    private void initValues() {
        putValue(Action.NAME, NbBundle.getMessage(getClass(), "show.hide.done"));
        putValue(SMALL_ICON, Icons.ShowDone);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JToggleButton) {
            JToggleButton tb = (JToggleButton) e.getSource();
            model.setShowDone(tb.isSelected());
        } else {
            model.setShowDone(!model.isShowDone());
        }
        dateCtlr.fireChange();
    }

}
