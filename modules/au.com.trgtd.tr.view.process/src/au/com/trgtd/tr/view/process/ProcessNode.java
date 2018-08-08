package au.com.trgtd.tr.view.process;

import au.com.trgtd.tr.view.DeleteCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.lookup.Lookups;

/**
 * Node for processing.
 *
 * @author Jeremy Moore
 */
public class ProcessNode extends AbstractNode {
    
    public final ProcessCookie processCookie;
    public final ProcessAddCookie processAddCookie;
    public final DeleteCookie deleteCookie;

    /** Constructs a new instance.
     * @param deleteCookie
     * @param processCookie */
    public ProcessNode(DeleteCookie deleteCookie, ProcessCookie processCookie) {
        super(Children.LEAF, Lookups.fixed(deleteCookie, processCookie));
        this.processCookie = processCookie;
        this.processAddCookie = null;
        this.deleteCookie = deleteCookie;        
    }
    
    /** Constructs a new instance.
     * @param deleteCookie
     * @param processAddCookie
     * @param processCookie */
    public ProcessNode(DeleteCookie deleteCookie, ProcessAddCookie processAddCookie, ProcessCookie processCookie) {
        super(Children.LEAF, Lookups.fixed(deleteCookie, processAddCookie, processCookie));
        this.processCookie = processCookie;
        this.processAddCookie = processAddCookie;
        this.deleteCookie = deleteCookie;
    }
    
    @Override
    public Cookie getCookie(Class clazz) {
        if (clazz == ProcessCookie.class) {
            return processCookie;
        }
        if (clazz == ProcessAddCookie.class && processAddCookie != null) {
            return processAddCookie;
        }
        if (clazz == DeleteCookie.class) {
            return deleteCookie;
        }
        return super.getCookie(clazz);
    }

}
