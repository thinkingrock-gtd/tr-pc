package au.com.trgtd.tr.email.utils;

/*
 * RenderablePlainText.java
 *
 * Created on 10 November 2005, 10:49
 */
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;

/**
 *
 * @author Dj
 */
public class RenderablePlainText implements Renderable {

    private final String bodytext;
    private final String subject;

    /** Creates a new instance of RenderablePlainText */
    public RenderablePlainText(Message message) throws MessagingException, IOException {
        subject = message.getSubject();
        bodytext = (String)message.getContent();
    }

    public Attachment getAttachment(int i) {
        return null;
    }

    public int getAttachmentCount() {
        return 0;
    }

    public String getBodytext() {
        return bodytext;
    }

    public String getSubject() {
        return subject;
    }

}
