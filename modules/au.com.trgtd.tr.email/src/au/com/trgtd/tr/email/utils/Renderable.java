package au.com.trgtd.tr.email.utils;

/*
 * Renderable.java
 *
 * Created on 10 November 2005, 10:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Dj
 */
public interface Renderable {

    Attachment getAttachment(int i);

    int getAttachmentCount();

    String getBodytext();

    String getSubject();
    
}
