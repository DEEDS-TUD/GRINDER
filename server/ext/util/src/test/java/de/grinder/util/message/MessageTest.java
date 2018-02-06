package de.grinder.util.message;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageTest {

    @Test
    public void testSetHeader() {
        final Message message = new Message();
        final byte[] header = { 0x00, 0x03, 0x0F, 0x00, 0x00, (byte) 0xFF };
        message.setHeader(header);

        assertEquals(MessageType.LOG, message.getType());
        assertEquals(0x0F00, message.getRunId());
        assertEquals(0x00FF, message.getLength());
    }
}
