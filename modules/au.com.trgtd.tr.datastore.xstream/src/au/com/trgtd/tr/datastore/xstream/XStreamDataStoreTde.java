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

// Trial German
final class XStreamDataStoreTde extends XStreamDataStore {

    XStreamDataStoreTde() {
        super();
    }

    private static final String m_b = ">YDOB<>LMTH<";
    private static final String m_e = ">YDOB/<>LMTH/<";
    private static final String de_m11 = ">P/<.treitimil 05 fua nenoitkA dnu etkejorP red lhaznA eid tsi noisreV lairT kcoRgniknihT red nI>P<";
    private static final String de_m12 = ">P/<.tenfföeg rhem thcin eiS driw ,thcierre timiL seseid ietadnetaD eid dlaboS>P<";
    private static final String de_m13 = ">P/<.nedalretnureh dnu nebrewre >a/<ua.moc.dtgrt.www>'ua.moc.dtgrt.www//:ptth'=ferh a< retnu eiS nennök noisrevlloV kcoRgniknihT eiD>P<";    
    private static final String de_m21 = de_m11;
    private static final String de_m22 = ">P/<>b/<.tenfföeg rhem thcin driw dnu thcierre noisreV lairT red timiL sad tah ietadnetaD erhI>b<>P<";
    private static final String de_m23 = ">P/<>a/<ua.moc.dtgrt.www>'ua.moc.dtgrt.www//:ptth'=ferh a< retnu noisrevlloV eid tztej eiS nefuak redo ,nereiborpuzsua retiew kcoRgniknihT mu ietadnetaD euen enie eiS nelletsrE>P<";    
    
    private String ma() {
        return x(m_b) + x(de_m11) + x(de_m12) + x(de_m13) + x(m_e);
    }

    private String mb() {
        return x(m_b) + x(de_m21) + x(de_m22) + x(de_m23) + x(m_e);
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
        return ((int)Math.PI << 4) + 2 < c();
    }

    private String x(String x) {
        return new StringBuilder(x).reverse().toString();
    }

}
