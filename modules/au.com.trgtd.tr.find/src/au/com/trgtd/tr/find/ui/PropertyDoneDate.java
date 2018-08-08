package au.com.trgtd.tr.find.ui;

import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import org.openide.nodes.PropertySupport;

public class PropertyDoneDate extends PropertySupport.ReadOnly<Date> {

    private static final String LABEL = "Done Date";
    private final FoundItem item;

    public PropertyDoneDate(FoundItem item) {
        super("done.date", Date.class, LABEL, "Done");
        this.item = item;
        setValue("suppressCustomEditor", Boolean.TRUE);
        
        PropertyEditorManager.registerEditor(Date.class, DatePropertyEditor.class);
    }

    @Override
    public Date getValue() throws IllegalAccessException, InvocationTargetException {
        return item == null ? null : item.getDoneDate();
    }

}
