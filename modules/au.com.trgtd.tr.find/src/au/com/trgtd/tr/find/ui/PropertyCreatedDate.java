package au.com.trgtd.tr.find.ui;

import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import org.openide.nodes.PropertySupport;

public class PropertyCreatedDate extends PropertySupport.ReadOnly<Date> {

    private static final String LABEL = "Created Date";
    private final FoundItem item;

    public PropertyCreatedDate(FoundItem item) {
        super("created", Date.class, LABEL, "Created");
        this.item = item;
        setValue("suppressCustomEditor", Boolean.TRUE);
        
        PropertyEditorManager.registerEditor(Date.class, DatePropertyEditor.class);
    }

    @Override
    public Date getValue() throws IllegalAccessException, InvocationTargetException {
        return item == null ? null : item.getCreatedDate();
    }

}
