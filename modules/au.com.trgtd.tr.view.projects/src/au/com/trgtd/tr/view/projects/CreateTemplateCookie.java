package au.com.trgtd.tr.view.projects;

import org.openide.nodes.Node;

/**
 * Cookie to create a template from a selected project
 * Mantis ID: 0001959
 *
 * @author Jeremy Moore
 */
public interface CreateTemplateCookie extends Node.Cookie {
    
    public void createTemplate();
    
}
