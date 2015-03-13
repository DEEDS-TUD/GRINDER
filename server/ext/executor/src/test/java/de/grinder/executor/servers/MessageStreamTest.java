package de.grinder.executor.servers;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import de.grinder.executor.MessageHandler;
import de.grinder.util.message.Message;
import de.grinder.util.message.MessageType;

public class MessageStreamTest {
  @Test
  public void testLogMessage() {
    final MessageStream stream = new MessageStream();

    final MessageHandler handler = mock(MessageHandler.class);
    stream.setHandler(handler);

    createMessage("Hello World!");
    // TODO Implement test

    // verify(handler).handle(any(Message.class));
  }

  private Message createMessage(final String content) {
    final Message message = new Message();
    message.setType(MessageType.LOG);
    message.setRunId((short) 0);
    message.setBody(content.getBytes());
    return message;
  }
}