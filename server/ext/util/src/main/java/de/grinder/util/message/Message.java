package de.grinder.util.message;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The format of GRINDER messages.
 *
 *
 */
public class Message {

    /**
     * Length of message headers in bytes.
     */
    public static final int HEADER_SIZE = 6;

    /**
     * Type of this message.
     */
    protected MessageType type;

    /**
     * Identifier of the message run.
     */
    protected short runId;

    /**
     * Length of the message body.
     */
    protected short length;

    /**
     * Message body with the message content.
     */
    protected byte[] body;

    public Message() {
        this(MessageType.NONE);
    }

    /**
     * Constructs a message with the given type.
     *
     * @param type
     */
    public Message(final MessageType type) {
        super();
        length = 0;
        runId = 0;
        this.type = type;
    }

    public void setType(final MessageType messageType) {
        this.type = messageType;
    }

    public MessageType getType() {
        return type;
    }

    public void setRunId(final short runId) {
        this.runId = runId;
    }

    public int getRunId() {
        return runId;
    }

    public int getLength() {
        return length;
    }

    /**
     * Sets the body of the message based on the given byte array.
     *
     * @param body
     *          Byte array containing the body content.
     */
    public void setBody(final byte[] body) {
        this.length = (short) body.length;
        this.body = body.clone();
    }

    /**
     * Get the message body as byte array.
     *
     * @return Byte array containing the message body.
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * Tells, if the message has a body.
     *
     * @return True, if the message has a body, otherwise false.
     */
    public boolean hasBody() {
        return length > 0;
    }

    /**
     * Sets the header of this message based on the given byte array.
     *
     * @param header
     *          Byte array containing the header information.
     */
    public void setHeader(final byte[] header) {
        final ByteBuffer bb = ByteBuffer.allocate(6).put(header);
        this.type = MessageType.getMessageType(bb.getShort(0));
        this.runId = bb.getShort(2);
        this.length = bb.getShort(4);
    }

    /**
     * Get the header of this message as byte array.
     *
     * @return Byte array containing the header information.
     */
    public byte[] getHeader() {
        final byte[] header = new byte[HEADER_SIZE];
        header[0] = (byte) (type.getId() >>> 8);
        header[1] = (byte) type.getId();
        header[2] = (byte) (runId >>> 8);
        header[3] = (byte) runId;
        header[4] = (byte) (length >>> 8);
        header[5] = (byte) length;
        return header;
    }

    /**
     * Get the byte wise representation of this message.
     *
     * @return Byte array containing the complete message.
     */
    public byte[] getBytes() {
        final byte[] buffer = new byte[getLength() + HEADER_SIZE];
        System.arraycopy(getHeader(), 0, buffer, 0, HEADER_SIZE);
        if (hasBody()) {
            System.arraycopy(getBody(), 0, buffer, HEADER_SIZE, getLength());
        }
        return buffer;
    }

    @Override
    public String toString() {
        return "Message [type=" + type + ", runId=" + runId + ", length=" + length
               + ", body=" + Arrays.toString(body) + "]";
    }
}
