package au.com.trgtd.tr.find.ui;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.PropertySupport;

public class PropertyPathString extends PropertySupport.ReadOnly<String> {

    private static final String LABEL = "Path";
    private final FoundItem item;

    public PropertyPathString(FoundItem item) {
        super("path", String.class, LABEL, "Path");
        this.item = item;
        setValue("suppressCustomEditor", Boolean.TRUE);
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return item == null ? null : item.getPath();
    }

}
