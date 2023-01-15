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
package au.com.trgtd.tr.task.messages;

import au.com.trgtd.tr.task.messages.MessageParser.Message;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class MessageParserTest {

    // The message text stored in the sample messages.xml (pulled from the
    // https://trgtd.com.au/messages.xml on 2023-01-14) is too long and
    // cumbersome to validate verbatim.Simply verifying the length instead.
    private static final int LENGTH_OF_SAMPLE_MESSAGE_TEXT = 431;

    private final MessageParser parser = new MessageParser();

    private final LocalDate dateOfMessage1 = LocalDate.of(2014, Month.MARCH, 27);
    private final LocalDate dateOfMessage2 = LocalDate.of(2014, Month.MARCH, 30);
    private final LocalDate dateBeforeFirstMessage = LocalDate.of(2014, Month.MARCH, 26);

    // valid XML with two member-independent messages of different dates
    private final String sampleMessagesA = "<msgs>"
            + "<msg date='2014/03/27' type='A'>message1</msg>"
            + "<msg date='2014/03/30' type='B'>message2</msg>"
            + "</msgs>";

    // valid XML with two messages - each intended for either members or non-members.
    // the first for non-members (type='N'), the second for members (type = 'M')
    private final String sampleMessagesB = "<msgs>"
            + "<msg date='2014/03/27' type='N'>nonMemberMessage</msg>"
            + "<msg date='2014/03/30' type='M'>memberMessage</msg>"
            + "</msgs>";

    @Test
    public void parsingValidButEmptyXml_returnsNoMessages() throws Exception {
        assertTrue(parse("<msgs/>", dateBeforeFirstMessage).isEmpty());
    }

    @Test
    public void parsingSampleMessagesA_withDateBeforeBoth_returnsBothMessages() throws Exception {
        List<Message> messages = parse(sampleMessagesA, dateBeforeFirstMessage);
        assertTrue(messages.size() == 2);
        assertMessageA1(messages.get(0));
        assertMessageA2(messages.get(1));
    }

    @Test
    public void parsingSampleMessagesA_withDateAfterFirstButBeforeSecond_returnsNewerMessage() throws Exception {
        List<Message> messages = parse(sampleMessagesA, dateBeforeFirstMessage.plusDays(2));
        assertTrue(messages.size() == 1);
        assertMessageA2(messages.get(0));
    }

    @Test
    public void parsingSampleMessagesA_withDateAfterSecond_returnsNoMessages() throws Exception {
        List<Message> messages = parse(sampleMessagesA, dateBeforeFirstMessage.plusDays(20));
        assertTrue(messages.isEmpty());
    }

    @Test
    public void parsingSampleMessagesB_asMember_returnsMemberMessageOnly() throws Exception {
        Boolean isMember = true;
        List<Message> messages = parse(sampleMessagesB, dateBeforeFirstMessage, isMember);
        assertTrue(messages.size() == 1);
        assertMessageB2(messages.get(0));
    }

    @Test
    public void parsingSampleMessagesB_asNonMember_returnsNonMemberMessageOnly() throws Exception {
        Boolean isMember = false;
        List<Message> messages = parse(sampleMessagesB, dateBeforeFirstMessage, isMember);
        assertTrue(messages.size() == 1);
        assertMessageB1(messages.get(0));
    }

    @Test
    public void parseFile() throws Exception {
        String xml = readFromResourceAsString("messages.xml");
        List<Message> messages = parse(xml, dateBeforeFirstMessage);
        assertTrue(messages.size() == 1);
        assertMessage(messages.get(0), dateOfMessage1, "a", null);
        String text = messages.get(0).text;
        assertEquals(LENGTH_OF_SAMPLE_MESSAGE_TEXT, text.length());
        assertTrue(text.contains("ThinkingRock"));
    }

    private String readFromResourceAsString(String fileName) throws IOException {
        Path path = Paths.get(getClass().getResource(fileName).getPath());
        return new String(Files.readAllBytes(path));
    }

    @Test
    public void parsingNull_throws() throws Exception {
        assertThrows(null);
    }

    @Test
    public void parsingInvalidEmptyXml_throws() throws Exception {
        assertThrows("");
    }

    @Test
    public void parsingInvalidXml1_throws() throws Exception {
        assertThrows("<msgs>");
    }

    @Test
    public void parsingInvalidXml2_throws() throws Exception {
        assertThrows("<msgs><msg xx</msgs>");
    }

    private void assertThrows(String xml) {
        try {
            parse(xml, dateBeforeFirstMessage);
            fail("Should have thrown");
        } catch (Exception ex) {
            // Exception successfully thrown
        }
    }

    private void assertMessageA1(Message m) {
        assertMessage(m, dateOfMessage1, "a", "message1");
    }

    private void assertMessageA2(Message m) {
        assertMessage(m, dateOfMessage2, "b", "message2");
    }

    private void assertMessageB1(Message m) {
        assertMessage(m, dateOfMessage1, "n", "nonMemberMessage");
    }

    private void assertMessageB2(Message m) {
        assertMessage(m, dateOfMessage2, "m", "memberMessage");
    }

    private void assertMessage(Message m, LocalDate localDate, String type, String msg) {
        assertMessage(m, localDate, type, msg, "");
    }

    private void assertMessage(Message m, LocalDate localDate, String type, String msg, String href) {
        assertEquals(Date.valueOf(localDate), m.date);
        assertEquals(type, m.type);
        if (msg != null) {
            assertEquals(msg, m.text);
        }
        assertEquals(href, m.href);
    }

    private List<Message> parse(String xml, LocalDate localDate) throws Exception {
        return parse(xml, localDate, true);
    }

    private List<Message> parse(String xml, LocalDate localDate, Boolean isMember) throws Exception {
        return parser.parse(xml, Date.valueOf(localDate), isMember);
    }
}
