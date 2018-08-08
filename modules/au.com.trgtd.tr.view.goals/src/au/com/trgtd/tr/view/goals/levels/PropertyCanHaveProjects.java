package au.com.trgtd.tr.view.goals.levels;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

class PropertyCanHaveProjects extends PropertySupport.ReadOnly<Boolean> {

    private final static Class CLASS = PropertyCanHaveProjects.class;
    private final static String NAME = NbBundle.getMessage(CLASS, "property.can.have.projects");

    private final LevelCtrl level;

    public PropertyCanHaveProjects(LevelCtrl level) {
        super("can.have.projects", Boolean.class, NAME, NAME);
        this.level = level;
        this.setValue("SortableColumn", Boolean.FALSE);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    public Boolean getValue() throws IllegalAccessException, InvocationTargetException {
        return level.isGoalsHaveProjects();
    }

}
