package au.com.trgtd.tr.view;

import au.com.trgtd.tr.view.ui.ColoredString;
import au.com.trgtd.tr.view.ui.ColoredStringEditor;
import java.beans.PropertyEditorManager;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        PropertyEditorManager.registerEditor(ColoredString.class, ColoredStringEditor.class);
    }

}
