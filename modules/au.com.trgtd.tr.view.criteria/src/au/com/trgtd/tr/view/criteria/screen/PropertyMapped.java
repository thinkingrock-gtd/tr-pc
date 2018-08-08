package au.com.trgtd.tr.view.criteria.screen;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.criteria.Value;

class PropertyMapped extends PropertySupport.ReadOnly<String> {

    private final static Class CLASS = PropertyMapped.class;
    private final static String NAME = NbBundle.getMessage(CLASS, "property.mapped.value");
    private final Value value;

    public PropertyMapped(Value value) {
        super("mapped.value", String.class, NAME, NAME);
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        this.value = value;
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        if (null == value) {
            return "";
        }
        Integer mv = value.getMappedValue();
        if (null == mv) {
            return "";
        }
        return mv.toString();
    }

}
