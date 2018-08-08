package au.com.trgtd.tr.view.goals.levels;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

class PropertyCanHaveAccountability extends PropertySupport.ReadOnly<Boolean> {

    private final static Class CLASS = PropertyCanHaveAccountability.class;
    private final static String NAME = NbBundle.getMessage(CLASS, "property.accountability");
    private final LevelCtrl level;

    public PropertyCanHaveAccountability(LevelCtrl level) {
        super("accountability", Boolean.class, NAME, NAME);
        this.setValue("suppressCustomEditor", true);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.level = level;
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return level.isGoalsHaveAccountability();
    }

}
