package au.com.trgtd.tr.view.process;

import org.openide.nodes.Node;

/**
 * Process add cookie.
 *
 * @author Jeremy Moore
 */
public interface ProcessAddCookie extends Node.Cookie {

    public boolean canProcessAdd();

    public void processAdd();
}
