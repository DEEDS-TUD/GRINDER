package de.grinder.executor.servers;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for handling internal messages as streams.
 *
 * CURRENTLY NOT IN USE, MAY BE DEPRECATED.
 *
 *
 */
public class MessageServer extends AbstractListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageServer.class);

    /**
     * The writer to write messages to the server.
     */
    private transient PipedOutputStream toServer;

    private final void closePipe() {
        try {
            this.toServer.close();
        } catch (final IOException e) {
            // Too bad
        }
    }

    private final void openPipe() throws IOException {
        final MessageStream stream = new MessageStream();
        stream.setHandler(handler);

        // Create a pipe to send messages to the worker
        this.toServer = new PipedOutputStream();
        stream.setFromTarget(new PipedInputStream(toServer));

        final Thread thread = new Thread(stream);
        thread.start();
    }

    /**
     * Send a message to the server.
     *
     * The server cannot distinguish messages that are sent here from messages that are sent
     * by the targetInterface device.
     *
     * @param message
     *          The message to be sent.
     */
    protected final void sendMessage(final byte[] message) {
        try {
            if (null == toServer) {
                openPipe();
            }
            toServer.write(message);
            toServer.flush();
        } catch (final IOException e) {
            try {
                closePipe();
                openPipe();
                toServer.write(message);
                toServer.flush();
            } catch (final IOException e2) {
                LOGGER.error(e2.getMessage(), e2);
            }
        }
    }

    @Override
    public final void run() {
        // TODO Run a thread that accepts messages from the CUEAbstraction instance
        // (Producer/Consumer or something) and sends the messages into the pipe.
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
    }
}
