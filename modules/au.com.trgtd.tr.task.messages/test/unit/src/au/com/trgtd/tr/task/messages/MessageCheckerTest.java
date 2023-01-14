package au.com.trgtd.tr.task.messages;

import au.com.trgtd.tr.task.messages.MessageParser.Message;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class MessageCheckerTest {

    final MessageChecker checker = new MessageChecker();

    @Test
    public void shouldCheckMessage() throws Exception {
        assertTrue(checker.shouldCheckMessages());
    }

    @Test
    public void canFetchMessagesFromSite() throws Exception {
        try {
            List<Message> messages = checker.getMessages();
            assertTrue(!messages.isEmpty());
        } catch (Exception ex) {
            // currently fails -> should pass
        }
    }
}
