package au.com.trgtd.tr.task.messages;

import au.com.trgtd.tr.task.messages.MessageParser.Message;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Only run ad-hoc. Once the messages have been fetched, the test will fail.
 */
@Ignore
public class MessageCheckerTest {

    final MessageChecker checker = new MessageChecker();

    @Test
    public void shouldCheckMessage() throws Exception {
        assertTrue(checker.shouldCheckMessages());
    }

    @Test
    public void canFetchMessagesFromSite() throws Exception {
        List<Message> messages = checker.getMessages();
        assertTrue(!messages.isEmpty());
    }
}
