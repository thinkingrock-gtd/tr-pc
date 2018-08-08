package au.com.trgtd.tr.find.ui;

import java.awt.BorderLayout;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.netbeans.swing.outline.Outline;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Property;

public class FoundOutlineView extends Outline implements ExplorerManager.Provider {

//  private final Property[] properties;
    private final OutlineView outlineView;
    private final ExplorerManager manager;
    private final TableColumnModel colModel;
    private TableColumn col;

    public FoundOutlineView(final Node rootNode, final Property[] properties) {
//      this.properties = properties;
        manager = new ExplorerManager();
        manager.setRootContext(rootNode);
        outlineView = new OutlineView();
        
        outlineView.setProperties(properties);
//        List<String> names = new ArrayList<String>();
//        for (Property prop : properties) {
//            names.add(prop.getName());
//            names.add(prop.getDisplayName());
//        }
//        outlineView.setPropertyColumns(names.toArray(new String[names.size()]));
        
        outlineView.getOutline().setRootVisible(true);
        colModel = outlineView.getOutline().getColumnModel();
        col = colModel.getColumn(0);
        col.setHeaderValue("");
        col.setPreferredWidth(250);
        col.setResizable(true);
        col = colModel.getColumn(1);
        col.setMaxWidth(200);
        col.setPreferredWidth(200);
        col.setResizable(true);
        col = colModel.getColumn(2);
        col.setMaxWidth(200);
        col.setPreferredWidth(200);
        col.setResizable(true);
        col = colModel.getColumn(3);
        col.setMinWidth(28);
        col.setMaxWidth(28);
        col.setPreferredWidth(28);
        col.setResizable(false);
        col = colModel.getColumn(4);
        col.setPreferredWidth(150);
        col.setResizable(true);
        setLayout(new BorderLayout());
        add(outlineView, "Center");
    }

    @Override
    public final ExplorerManager getExplorerManager() {
        return manager;
    }

}
