package au.com.trgtd.tr.find.ui;

import au.com.trgtd.tr.find.FindPanel;
import au.com.trgtd.tr.util.Observable;
import au.com.trgtd.tr.util.Observer;
import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

public class FoundNodeRoot extends AbstractNode implements Observer {

    private static final String ICON_PATH = "au/com/trgtd/tr/find/Find.png";
    private static final Image icon = ImageUtilities.loadImage(ICON_PATH, true);

    /**
     * Constructs a new instance for the given found items.
     * @param items The found items.
     */
    public FoundNodeRoot(FoundItems items) {
        this(new FoundChildren(items));
        setName(items);
        items.addObserver(this);
    }

    /**
     * Constructs a new instance for a given children.
     * @param children The children.
     */
    private FoundNodeRoot(FoundChildren children) {
        super(children);
    }
    
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        sheet.put(Sheet.createPropertiesSet());
        return sheet;
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

    @Override
    public void update(Observable obs, Object arg) {
        if (obs instanceof FoundItems) {
            setName((FoundItems)obs);
        }
    }

    private void setName(FoundItems items) {
        String find = NbBundle.getMessage(FindPanel.class, "find");
        setName(find + ": " + items.getText() + " (" + items.size() + ")");
    }
    
}
