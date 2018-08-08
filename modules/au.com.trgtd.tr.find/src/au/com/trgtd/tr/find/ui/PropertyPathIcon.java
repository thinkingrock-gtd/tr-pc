package au.com.trgtd.tr.find.ui;

import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import javax.swing.ImageIcon;
import org.openide.nodes.PropertySupport;

public class PropertyPathIcon extends PropertySupport.ReadOnly<ImageIcon> {

    private static final String LABEL = "";
    private final FoundItem item;

    public PropertyPathIcon(FoundItem item) {
        super("path.icon", ImageIcon.class, LABEL, "Path Icon");
        this.item = item;
        setValue("suppressCustomEditor", Boolean.TRUE);

        PropertyEditorManager.registerEditor(ImageIcon.class, IconPropertyEditor.class);
    }

    @Override
    public ImageIcon getValue() throws IllegalAccessException, InvocationTargetException {
        return item == null ? null : item.getPathIcon();
    }

}
