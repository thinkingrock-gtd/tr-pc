package au.com.trgtd.tr.datastore.xstream;

import au.com.trgtd.tr.runtime.Open;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import tr.model.Data;
import tr.model.project.Project;

// Trial English
final class XStreamDataStoreTen extends XStreamDataStore {

    XStreamDataStoreTen() {
        super();
    }

    private static final String m_b = ">YDOB<>LMTH<";
    private static final String m_1 = ">P/<.ytfif ot snoitcA dna stcejorP fo rebmun eht stcirtser noisrev lairt kcoRgniknihT>P<";
    private static final String m2a = ">P/<.ti daol ton lliw kcoRgniknihT ,timil siht sehcaer elif atad ruoy nehW>P<";
    private static final String m2b = ">P/<.elif atad wen a etaerc ,noitaulave ruoy eunitnoc oT>/RB<>B/<.ti daol ton lliw kcoRgniknihT dna timil siht dehcaer sah elif atad ruoY>B<>P<";
    private static final String m_3 = ">P/<>A/<ua.moc.dtgrt.www>'ua.moc.dtgrt.www//:ptth'=ferh A< tisiv kcoRgniknihT esahcrup oT>P<";
    private static final String m_e = ">YDOB/<>LMTH/<";
    
    private String ma() {
        return x(m_b) + x(m_1) + x(m2a) + x(m_3) + x(m_e);
    }

    private String mb() {
        return x(m_b) + x(m_1) + x(m2b) + x(m_3) + x(m_e);
    }

    private void nu(String m) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText(m);
        editorPane.setEditable(false);
        editorPane.setOpaque(true);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent evt) {
                if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (evt.getURL() != null) {
                        Open.open(evt.getURL());
                    }
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        Object[] options = {scrollPane};
        JOptionPane.showOptionDialog(null, options, t(), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
    }

    private long c() {
        Data data = getData();
        return null == data ? 0 : c(data.getRootAll());
    }

    private long c(Project p) {
        long m = p.size();
        for (Project sp : p.getChildren(Project.class)) {
            m += c(sp);
        }
        return m;
    }

    private String t() {
        return x("noisreV lairT kcoRgniknihT");
    }

    private void f() {
        setData(null);
        nu(mb());
    }

    private void g() {
        nu(ma());
    }

    @Override
    public void load() throws Exception {
        super.load();
        if (z()) {
            f();
        } else {
            g();
        }
    }

    private boolean z() {
        return ((int)Math.PI << 3) + 2 < c();
    }

    private String x(String x) {
        return new StringBuilder(x).reverse().toString();
    }

}
