package au.com.trgtd.tr.view.goals;

import au.com.trgtd.tr.view.ui.ColoredString;
import au.com.trgtd.tr.view.ui.ColoredStringImpl;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.GoalCtrl;

public class PropertyLevel extends PropertySupport.ReadOnly<ColoredString> {

    public final static String NAME = "level";
    public final static String DISPLAY_NAME = NbBundle.getMessage(PropertyLevel.class, "level");
    public final static String SHORT_DESCR = "";
    public final static Color TRANSPARENT = new Color(0, 0, 0, 0);

    private final GoalCtrl goalCtrl;

    public PropertyLevel(GoalCtrl goalCtrl) {
        super(NAME, ColoredString.class, DISPLAY_NAME, SHORT_DESCR);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.goalCtrl = goalCtrl;
    }

    @Override
    public ColoredString getValue() throws IllegalAccessException, InvocationTargetException {
        return new ColoredStringImpl(goalCtrl.getLevel().getDescr(), TRANSPARENT, Color.BLACK);
    }

}
