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
