/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.com.trgtd.tr.email.utils;

//import javax.mail.Message;

import java.io.IOException;
import javax.mail.Message;


//import javax.mail.Message;
import javax.mail.MessagingException;

//import javax.mail.MessagingException;

/**
 * RenderableFactory
 * 
 * @author Jeremy Moore
 */
public class RenderableFactory {

    public static Renderable create(Message message) {    
        if( message == null) {
            return null;
        }        
        try {
            if(message.getContentType().startsWith("text/plain")) {
                return new RenderablePlainText(message);
            } else {
                return new RenderableMessage(message);
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

}
