package de.grinder.util.message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy for communication with GRINDER.
 *
 *
 */
public class Proxy {

    /** Class elvel logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Proxy.class);

    /** GRINDER TCP server host. */
    private final String host;

    /** GRINDER TCP server port. */
    private final Integer port;

    /** Socket for connecting to GRINDER's TCP server. */
    private Socket socket;

    /** Simple constructor. */
    public Proxy(final String host, final int port) {
        super();
        this.host = host;
        this.port = port;
    }

    protected OutputStream getSocketOutStream() throws IOException {
        return socket.getOutputStream();
    }

    /**
     * Establishes a connection to the server.
     *
     * @throws IOException
     *
     */
    public void connect() throws IOException {
        socket = new Socket(host, port);
        LOGGER.debug("Connected to {}:{}.", host, port);
    }

    /**
     * Closes the connection to the server
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        LOGGER.debug("Proxy disconnect");
        socket.close();
    }

    /**
     * Tells the current experiment configuration as String.
     *
     * @return String containing the current configuration
     * @throws IOException
     */
    public String getConfiguration() throws IOException {
        final Message request = new Message(MessageType.GET_CONFIGURATION);
        LOGGER.debug("Sending request: {}", request);

        socket.getOutputStream().write(request.getBytes());
        socket.getOutputStream().flush();

        LOGGER.debug("Processing Response.");
        final Message response = new Message();
        final byte[] header = new byte[Message.HEADER_SIZE];
        socket.getInputStream().read(header);
        response.setHeader(header);

        final byte[] config = new byte[response.getLength()];
        socket.getInputStream().read(config);

        final String strConfig = new String(config);
        LOGGER.debug("Received configuration: {}", strConfig);
        return strConfig;
    }

    public void sendExperimentFinished(final short result) throws IOException {
        final Message request = new Message(MessageType.FINISH_EXPERIMENT);
        LOGGER.debug("Sending request: {}", request);

        final byte[] body = new byte[2];
        body[0] = (byte) (result >>> 8);
        body[1] = (byte) result;

        request.body = body;
        request.length = 2;

        socket.getOutputStream().write(request.getBytes());
        socket.getOutputStream().flush();
    }

    public void sendExperimentFinished(final short result, final String activated)
    throws IOException {
        final Message request = new Message(MessageType.FINISH_EXPERIMENT);
        LOGGER.debug("Sending request: {}", request);

        final byte[] actByte = activated.getBytes();
        final byte[] body = new byte[actByte.length + 2];
        body[0] = (byte) (result >>> 8);
        body[1] = (byte) result;

        for (int i = 0; i < actByte.length; ++i) {
            body[i + 2] = actByte[i];
        }

        request.body = body;
        request.length = (short) body.length;

        socket.getOutputStream().write(request.getBytes());
        socket.getOutputStream().flush();
    }
}