package au.com.trgtd.tr.email.utils;

/*
 * RenderableMessage.java
 */
import au.com.trgtd.tr.util.HTML;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Dj
 */
public class RenderableMessage implements Renderable {

//    private static final Logger LOG = Logger.getLogger("tr.email");
    private String subject;
    private String bodytext;
    private ArrayList<Attachment> attachments;

    /** 
     * Creates a new instance of RenderableMessage.
     */
    public RenderableMessage(Message m) throws MessagingException, IOException {
        attachments = new ArrayList<Attachment>();
        subject = m.getSubject();
        extractPart(m);
    }
    private static int n = 0;

    private boolean extractPart(final Part part) throws MessagingException, IOException {

        n++;

        if (part.getContent() instanceof Multipart) {

//            LOG.info("EXTRACT PART (" + n + ") " + "Multipart");

            return handleMultipart((Multipart) part.getContent());
        }

//        LOG.info("EXTRACT PART (" + n + ") " + "Content Type:" + part.getContentType());

        if (part.getFileName() != null) {

//            LOG.info("EXTRACT PART (" + n + ") " + "Filename: " + part.getFileName());

//            LOG.info("EXTRACT PART (" + n + ") " + "ATTACHMENT");

            Attachment attachment = new Attachment();
            attachment.setContenttype(part.getContentType());
            attachment.setFilename(MimeUtility.decodeText(part.getFileName()));

            InputStream in = part.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = in.read(buffer)) >= 0) {
                bos.write(buffer, 0, count);
            }
            in.close();
            attachment.setContent(bos.toByteArray());
            attachments.add(attachment);
            return true;
        }

        if (part.getContentType().startsWith("text/plain")) {

//            LOG.info("EXTRACT PART (" + n + ") " + "TEXT/PLAIN: " + part.getContent());

            if (bodytext == null) {
                bodytext = (String) part.getContent();
            } else {
                bodytext += "\n" + (String) part.getContent();
            }
            return true;
        }

        if (part.getContentType().startsWith("text/html")) {

//            LOG.info("EXTRACT PART (" + n + ") " + "TEXT/HTML: " + part.getContent());

            if (bodytext == null) {
                bodytext = HTML.html2text((String) part.getContent());
            } else {
                bodytext += HTML.html2text("<p>" + (String) part.getContent() + "</p>");
            }
            return true;
        }

////        LOG.info("ATTACHMENT");
//
//        Attachment attachment = new Attachment();
//        attachment.setContenttype(part.getContentType());
////      attachment.setFilename(part.getFileName());
//        attachment.setFilename(MimeUtility.decodeText(part.getFileName()));
//
//        InputStream in = part.getInputStream();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//        byte[] buffer = new byte[8192];
//        int count = 0;
//        while ((count = in.read(buffer)) >= 0) {
//            bos.write(buffer, 0, count);
//        }
//        in.close();
//        attachment.setContent(bos.toByteArray());
//        attachments.add(attachment);
//        return true;

//        LOG.warning("EXTRACT PART (" + n + ") " + "NOT HANDLED");

        return false;
    }

    private boolean handleMultipart(Multipart mp) throws MessagingException, IOException {
//      LOG.info("Multipart content type: " + mp.getContentType());

        if (mp.getContentType().startsWith("multipart/alternative")) {
            return handleMultipartAlternative(mp);
        } else if (mp.getContentType().startsWith("multipart/mixed")) {
            return handleMultipartMixed(mp);
        } else if (mp.getContentType().startsWith("multipart/related")) {
            return handleMultipartRelated(mp);
        } else if (mp.getContentType().startsWith("multipart/digest")) {
            return handleMultipartDigest(mp);
        } else if (mp.getContentType().startsWith("multipart/signed")) {
            return handleMultipartSigned(mp);
        }

//        LOG.warning("Multipart content could not be handled.");

        return false;
    }

    private boolean handleMultipartAlternative(Multipart mp) throws MessagingException, IOException {
        // first try to find a multipart
        for (int i = 0; i < mp.getCount(); i++) {
            Part part = mp.getBodyPart(i);
            if (part.getContentType().startsWith("multipart/")) {
                return handleMultipart((Multipart) part.getContent());
            }
        }
        // second try to find a plain text part
        for (int i = 0; i < mp.getCount(); i++) {
            Part part = mp.getBodyPart(i);
            if (part.getContentType().startsWith("text/plain")) {
                extractPart(part);
                return true;
            }
        }
        // third try to find a HTML part
        for (int i = 0; i < mp.getCount(); i++) {
            Part part = mp.getBodyPart(i);
            if (part.getContentType().startsWith("text/html")) {
                extractPart(part);
                return true;
            }
        }
        // give up    
        return false;
    }

    private boolean handleMultipartMixed(Multipart mp) throws MessagingException, IOException {
        // process all parts
        boolean handled = false;
        for (int i = 0; i < mp.getCount(); i++) {
            if (extractPart(mp.getBodyPart(i))) {
                handled = true;
            }
        }
        return handled;
    }

    private boolean handleMultipartDigest(Multipart mp) throws MessagingException, IOException {
        // try to process all parts
        boolean handled = false;
        for (int i = 0; i < mp.getCount(); i++) {
            if (extractPart(mp.getBodyPart(i))) {
                handled = true;
            }
        }
        return handled;
    }

    private boolean handleMultipartSigned(Multipart mp) throws MessagingException, IOException {
        // try to process all parts
        boolean handled = false;
        for (int i = 0; i < mp.getCount(); i++) {
            if (extractPart(mp.getBodyPart(i))) {
                handled = true;
            }
        }
        return handled;
    }

    private boolean handleMultipartRelated(Multipart mp) throws MessagingException, IOException {
        // try to process all parts
        boolean handled = false;
        for (int i = 0; i < mp.getCount(); i++) {
            if (extractPart(mp.getBodyPart(i))) {
                handled = true;
            }
        }
        return handled;
    }

    public String getSubject() {
        return subject;
    }

    public String getBodytext() {
        return bodytext;
    }

    public int getAttachmentCount() {
        return attachments == null ? 0 : attachments.size();
    }

    public Attachment getAttachment(int i) {
        return attachments.get(i);
    }
}
