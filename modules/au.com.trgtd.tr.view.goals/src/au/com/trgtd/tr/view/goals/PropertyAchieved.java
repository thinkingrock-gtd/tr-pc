package au.com.trgtd.tr.view.goals;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalCtrl;

public class PropertyAchieved extends PropertySupport.ReadOnly<Boolean> {

    public final static String NAME = "achieved";
    public final static String DISPLAY_NAME = NbBundle.getMessage(PropertyAchieved.class, "achieved");
    public final static String SHORT_DESCR = "";

    private final GoalCtrl goalCtrl;

    public PropertyAchieved(GoalCtrl goalCtrl) {
        super(NAME, Boolean.class, DISPLAY_NAME, SHORT_DESCR);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.goalCtrl = goalCtrl;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return goalCtrl.getAchievedDate() != null;
    }

}
