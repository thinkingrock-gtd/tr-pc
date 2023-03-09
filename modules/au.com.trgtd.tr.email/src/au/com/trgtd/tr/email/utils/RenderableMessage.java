/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
        attachments = new ArrayList<>();
        subject = m.getSubject();
        extractPart(m);
    }
    private static int n = 0;

    private boolean extractPart(final Part part) throws MessagingException, IOException {

        n++;

        if (part.getContent() instanceof Multipart mp) {

//            LOG.log(Level.INFO, "EXTRACT PART ({0}) Multipart", n);

            return handleMultipart(mp);
        }

//        LOG.log(Level.INFO, "EXTRACT PART ({0}) Content Type:{1}", new Object[]{n, part.getContentType()});

        if (part.getFileName() != null) {

//            LOG.log(Level.INFO, "EXTRACT PART ({0}) Filename: {1}", new Object[]{n , part.getFileName()});

//            LOG.log(Level.INFO, "EXTRACT PART ({0}) ATTACHMENT", n);

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

//            LOG.log(Level.INFO, "EXTRACT PART ({0}) TEXT/PLAIN: {1}", new Object[]{n, part.getContent()});

            if (bodytext == null) {
                bodytext = (String) part.getContent();
            } else {
                bodytext += "\n" + part.getContent();
            }
            return true;
        }

        if (part.getContentType().startsWith("text/html")) {

//            LOG.log(Level.INFO, "EXTRACT PART ({0}) TEXT/HTML: {1}", new Object[]{n, part.getContent()});

            if (bodytext == null) {
                bodytext = HTML.html2text((String) part.getContent());
            } else {
                bodytext += HTML.html2text("<p>" + part.getContent() + "</p>");
            }
            return true;
        }

////        LOG.log(Level.INFO, "ATTACHMENT");
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

//        LOG.log(Level.WARNING, "EXTRACT PART ({0}) NOT HANDLED", n);

        return false;
    }

    private boolean handleMultipart(Multipart mp) throws MessagingException, IOException {
//      LOG.log(Level.INFO, "Multipart content type: {0}", mp.getContentType());

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

//        LOG.log(Level.WARNING, "Multipart content could not be handled.");

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
