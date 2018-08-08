package au.com.trgtd.tr.export.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "au.com.trgtd.tr.export.data.ExportCSVFiles"
)
@ActionRegistration(
        displayName = "#CTL_ExportCSVFiles"
)
@ActionReferences({
    @ActionReference(path = "Menu/File/Export", position = 900, separatorBefore = 890),
    @ActionReference(path = "Shortcuts", name = "M-X M-D")
})
@Messages("CTL_ExportCSVFiles=Export CSV Files")
public final class ExportCSVFilesAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {        
        new ExportCSVFiles().export();        
    }
    
}
