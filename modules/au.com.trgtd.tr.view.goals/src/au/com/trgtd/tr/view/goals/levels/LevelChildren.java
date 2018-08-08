package au.com.trgtd.tr.view.goals.levels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import javax.swing.event.ChangeEvent;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import tr.model.goals.ctrl.LevelCtrl;
import tr.model.goals.ctrl.LevelsCtrl;
import tr.model.goals.ctrl.LevelsCtrlLookup;

public class LevelChildren extends Children.Keys<LevelCtrl> implements PropertyChangeListener {

    private final LevelsCtrl levelsCtrl;
    private final OutlineView view;

    public LevelChildren(OutlineView view) {
        this.view = view;
        this.levelsCtrl = LevelsCtrlLookup.getLevelsCtrl();
    }

    @Override
    protected void addNotify() {
        setKeys(levelsCtrl.getLevels());
        levelsCtrl.addPropertyChangeListener(levelsCtrl.PROP_LEVEL_INSERT, this);
        levelsCtrl.addPropertyChangeListener(levelsCtrl.PROP_LEVEL_DELETE, this);
        levelsCtrl.addPropertyChangeListener(levelsCtrl.PROP_LEVEL_MOVE_DOWN, this);
        levelsCtrl.addPropertyChangeListener(levelsCtrl.PROP_LEVEL_MOVE_UP, this);
        levelsCtrl.addPropertyChangeListener(levelsCtrl.PROP_LEVEL_REORDER, this);
        super.addNotify();
    }

    @Override
    protected void removeNotify() {
        levelsCtrl.removePropertyChangeListener(levelsCtrl.PROP_LEVEL_INSERT, this);
        levelsCtrl.removePropertyChangeListener(levelsCtrl.PROP_LEVEL_DELETE, this);
        levelsCtrl.removePropertyChangeListener(levelsCtrl.PROP_LEVEL_MOVE_DOWN, this);
        levelsCtrl.removePropertyChangeListener(levelsCtrl.PROP_LEVEL_MOVE_UP, this);
        levelsCtrl.removePropertyChangeListener(levelsCtrl.PROP_LEVEL_REORDER, this);
        setKeys(Collections.EMPTY_SET);
        super.removeNotify();
    }

    @Override
    protected Node[] createNodes(LevelCtrl levelCtrl) {
        return new Node[] { new LevelNode(levelCtrl) };
    }

    public void propertyChange(PropertyChangeEvent evt) {
        setKeys(levelsCtrl.getLevels());        
        super.refresh();
    }

    public Index getIndex() {
        return new IndexImpl();
    }

    private class IndexImpl extends Index.Support {

        public Node[] getNodes() {
            return LevelChildren.this.getNodes();
        }

        public int getNodesCount() {
            return getNodes().length;
        }

        @Override
        public void moveDown(int index) {
            int row = view.getOutline().getSelectedRow();

            LevelChildren.this.levelsCtrl.moveDown(index);
            fireChangeEvent(new ChangeEvent(IndexImpl.this));

            view.getOutline().setRowSelectionInterval(row + 1, row + 1);
        }

        @Override
        public void moveUp(int index) {
            int row = view.getOutline().getSelectedRow();

            LevelChildren.this.levelsCtrl.moveUp(index);
            fireChangeEvent(new ChangeEvent(IndexImpl.this));

            view.getOutline().setRowSelectionInterval(row - 1, row - 1);
        }

        public void reorder(int[] perm) {
            LevelChildren.this.levelsCtrl.reorder(perm);
            fireChangeEvent(new ChangeEvent(IndexImpl.this));
        }
    }

}
