package au.com.trgtd.tr.view.goals.levels;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

class PropertyCanHaveStartDate extends PropertySupport.ReadOnly<Boolean> {

    private final static Class CLASS = PropertyCanHaveStartDate.class;
    private final static String NAME = NbBundle.getMessage(CLASS, "property.can.have.start");
    private final LevelCtrl level;

    public PropertyCanHaveStartDate(LevelCtrl level) {
        super("can.have.start", Boolean.class, NAME, NAME);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.level = level;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return level.isGoalsHaveStartDate();
    }

}
