package au.com.trgtd.tr.view.goals.levels;

import au.com.trgtd.tr.view.ui.IconPropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import javax.swing.ImageIcon;
import org.openide.nodes.PropertySupport;
import org.openide.util.NbBundle;
import tr.model.goals.ctrl.LevelCtrl;

public class PropertyGoalsIcon extends PropertySupport.ReadOnly<ImageIcon> {

    private static final String LABEL = NbBundle.getMessage(PropertyGoalsIcon.class, "property.goals.icon");
    private final LevelCtrl levelCtrl;

    public PropertyGoalsIcon(LevelCtrl levelCtrl) {
        super("goals.icon", ImageIcon.class, LABEL, "");
        this.levelCtrl = levelCtrl;
        this.setValue("suppressCustomEditor", Boolean.TRUE);
        this.setValue("SortableColumn", Boolean.FALSE);
        PropertyEditorManager.registerEditor(ImageIcon.class, IconPropertyEditor.class);
    }

    @Override
    public ImageIcon getValue() throws IllegalAccessException, InvocationTargetException {
        return (levelCtrl == null || levelCtrl.getGoalsIcon() == null)
                ? null : levelCtrl.getGoalsIcon().icon;
    }
    
}
