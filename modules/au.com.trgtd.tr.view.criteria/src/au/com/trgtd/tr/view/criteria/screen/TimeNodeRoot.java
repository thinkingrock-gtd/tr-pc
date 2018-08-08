package au.com.trgtd.tr.view.criteria.screen;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

public class TimeNodeRoot extends AbstractNode {

    public TimeNodeRoot(TimeChildren children) {
        super(children);
    }

    @Override
    public <T extends Node.Cookie> T getCookie(Class<T> type) {
        if (type == Index.class) {
            return type.cast(((TimeChildren) getChildren()).getIndex());
        }
        Children children = getChildren();
        if (type.isInstance(children)) {
            return type.cast(children);
        }
        return super.getCookie(type);
    }

//    @Override
//    public Action[] getActions(boolean popup) {
//        return new Action[]{
//                    SystemAction.get(PasteAction.class)
//                };
//    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public boolean canCopy() {
        return false;
    }

    @Override
    public boolean canCut() {
        return false;
    }

}
