package au.com.trgtd.tr.view.goals.chooser;

import java.awt.Image;
import javax.swing.AbstractButton;
import org.openide.util.ImageUtilities;
import tr.model.goals.ctrl.GoalCtrl;

public class ChooserRootNode extends ChooserGoalNode {

    private final Image icon;

    public ChooserRootNode(GoalCtrl goalCtrl, AbstractButton selectButton, boolean showAll) {
        super(goalCtrl, selectButton, showAll);
        icon = ImageUtilities.loadImageIcon("au/com/trgtd/tr/view/goals/resource/Circle.png", false).getImage();
    }

    @Override
    public Image getIcon(int type) {
        return icon;
    }

    @Override
    public Image getOpenedIcon(int type) {
        return icon;
    }

    @Override
    public String getHtmlDisplayName() {
        return "<i>" + escapeHTML(getName()) + "</i>";
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

}
