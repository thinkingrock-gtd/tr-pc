package au.com.trgtd.tr.view.goals.chooser;

import java.awt.Color;
import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.Action;
import org.apache.commons.lang.StringEscapeUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import tr.model.goals.ctrl.GoalCtrl;
import tr.model.goals.ctrl.LevelCtrl;

/**
 * Goal chooser goal node.
 * 
 * @author Jeremy Moore
 */
public class ChooserGoalNode extends AbstractNode implements SelectCookie {

    public final GoalCtrl goal;
    public final AbstractButton selectButton;
    private final boolean showAll;

    public ChooserGoalNode(GoalCtrl goal, AbstractButton selectButton, boolean showAll) {
        super(goal.hasSubgoals() ? new ChooserGoalChildren(goal, selectButton, showAll) : Children.LEAF);
        this.goal = goal;
        this.selectButton = selectButton;
        this.showAll = showAll;
    }

    @Override
    public Image getIcon(int type) {
        if (goal == null || goal.getLevel() == null) {
            return super.getIcon(type);
        }
        return goal.getLevel().getGoalsIcon().icon.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        if (goal == null || goal.getLevel() == null) {
            return super.getIcon(type);
        }
        return goal.getLevel().getGoalsIcon().icon.getImage();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return goal.getDescr();
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
    protected final static String HTML_COLOR_NOT_SELECTABLE = au.com.trgtd.tr.util.HTML.format(Color.GRAY);

    @Override
    public String getHtmlDisplayName() {
        if (goal.isAchieved()) {
            String name = escapeHTML(getName());
            return "<s>" + name + "</s>";
        }
        if (isSelectable()) {
            return getDisplayName();
        }
        String name = escapeHTML(getName());
        return "<font color='" + HTML_COLOR_NOT_SELECTABLE + "'>" + name + "</font>";
    }

    protected String escapeHTML(String str) {
        str = StringEscapeUtils.escapeXml(str);
        return str.replace("&apos;", "'");
    }

    @Override
    public Action[] getActions(boolean popup) {
        return new Action[]{};
    }

    @Override
    public Action getPreferredAction() {
        return isSelectable() ? SystemAction.get(SelectAction.class) : null;
    }

    @Override
    public Cookie getCookie(Class clazz) {        
        if (clazz == SelectCookie.class) {
            if (isSelectable()) {
                return this;
            }             
        }
        return super.getCookie(clazz);
    }

    public boolean isSelectable() {
        if (goal.isAchieved()) {
            return false;
        }
        if (showAll) {
            return true;
        }
        LevelCtrl level = goal.getLevel();
        if (level == null || !level.isGoalsHaveProjects()) {
            return false;
        }
        return true;
    }

    @Override
    public void select() {
        selectButton.doClick();
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
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canDestroy() {
        return false;
    }
}
